<%@page session="false"%><%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

--%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page import="
       org.apache.sling.api.resource.Resource,
       org.apache.sling.api.resource.ResourceResolver,
       org.apache.sling.api.resource.ResourceUtil,
       org.apache.sling.api.resource.ValueMap,
       java.util.Map,
       java.util.HashMap,
       java.util.Iterator" %><%
    final ResourceResolver resolver = resource.getResourceResolver();
    final String paths[] = resolver.getSearchPath();
    final String libPath = "cq/personalization/clientlib/kernel/source/strategies/list";
    final String legacyLibPath = "cq/personalization/clientlib/source/strategies/list";
    final Map<String, String> entries = new HashMap<String, String>();
    for (final String path : paths) {
        Resource dir = resolver.getResource(path + libPath);
        if (dir != null) {
            processScripts(path, dir, entries);
        }
        dir = resolver.getResource(path + legacyLibPath);
        if (dir != null) {
            processScripts(path, dir, entries);
        }
    }
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    boolean first = true;
%>[<%
    for(final Map.Entry<String, String> entry : entries.entrySet()) {
        if ( !first ) {%>,<%} first = false;
        %>{"value": "<%=xssAPI.encodeForJSString(entry.getKey()) %>","text":"<%=xssAPI.encodeForJSString(entry.getValue()) %>"}<%
    }
%>]<%!
    private static void processScripts(String basePath, Resource dir, Map<String, String> entries) {
        final Iterator<Resource> i = dir.listChildren();
        while (i.hasNext()) {
            final Resource script = i.next();
            final ValueMap vm = ResourceUtil.getValueMap(script);

            // First, strip the path back to just the libPath:
            String key = script.getPath().substring(basePath.length());
            // Then convert to a pre-5.6-path (before clientlibs were separated into kernel and ui):
            key = key.replace("clientlib/kernel/source", "clientlib/source");

            final boolean enabled = vm.get("enabled", true);
            if (entries.containsKey(key)) {
                if (!enabled) {
                    entries.remove(key);
                }
            } else {
                if (enabled) {
                    entries.put(key, vm.get("jcr:title", script.getName()));
                }
            }
        }
    }
%>
