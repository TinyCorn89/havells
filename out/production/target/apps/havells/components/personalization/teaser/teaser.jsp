<%@page session="false"%><%--
  ************************************************************************
  ADOBE CONFIDENTIAL
  ___________________

  Copyright 2011 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
  ************************************************************************

  ==============================================================================
  DEPRECATED since CQ 5.6.1.

  Use the target component (cq/personalization/components/target) instead, which
  provides better authoring of experiences/teaser variants and is integrated
  directly in the authoring UI for all compoentns ("Target" action).
  ==============================================================================

--%><%@include file="/libs/foundation/global.jsp"%><%!
%><%@page import="
       java.io.StringWriter,
       com.day.cq.wcm.api.WCMMode,
       com.day.cq.wcm.core.stats.PageViewStatistics,
       com.day.text.Text,
       java.util.ResourceBundle,
	   com.day.cq.i18n.I18n,
	   com.day.cq.personalization.TargetedContentManager,
	   com.day.cq.personalization.ClientContextUtil,
	   org.apache.sling.commons.json.JSONObject,
	   org.apache.sling.commons.json.JSONArray" %><%
%><cq:includeClientLib categories="personalization.kernel"/><%
	   
    final ResourceBundle resourceBundle = slingRequest.getResourceBundle(null);
    I18n i18n = new I18n(resourceBundle);

    final TargetedContentManager targetedContentManager = sling.getService(TargetedContentManager.class);

    final String campaignPath = properties.get("campaignpath", String.class);
    final String strategyPath = properties.get("strategyPath", "");
    final String location = properties.get("location", resource.getPath());

    String strategy = "";
    if (strategyPath.length() > 0) {
        strategy = Text.getName(strategyPath);
        strategy = strategy.replaceAll(".js", "");
    }

    String campaignClass = "";
    if (campaignPath != null) {
        Page campaignPage = pageManager.getPage(campaignPath);
        if (campaignPage != null) {
            campaignClass = "campaign-" + campaignPage.getName();
        }
    }

    //try to generate a "friendly" id for the div where teaser will be placed
    String targetDivId = ClientContextUtil.getId(resource.getPath());

    final PageViewStatistics pwSvc = sling.getService(PageViewStatistics.class);
    String trackingURLStr = null;
    if (pwSvc!=null && pwSvc.getTrackingURI() != null) {
        trackingURLStr = pwSvc.getTrackingURI().toString();
    }

    JSONObject teaserInfo = targetedContentManager.getTeaserInfo(resourceResolver, campaignPath, location);
    JSONArray allTeasers = teaserInfo.getJSONArray("allTeasers");
    String defaultTeaser = teaserInfo.optString("defaultTeaserPath");

    final String TEASER_SUFFIX = "/_jcr_content/par.html";

    if (allTeasers.length() > 0) {
        %>
        <script type="text/javascript">$CQ(function() {
            initializeTeaserLoader(<%=allTeasers%>, "<%=strategy%>", "<%=targetDivId%>", "<%=(WCMMode.fromRequest(request) == WCMMode.EDIT)%>", "<%=trackingURLStr%>", "<%=resource.getPath()%>");
        });</script>
        <div id="<%= xssAPI.encodeForHTMLAttr(targetDivId) %>" class="campaign <%= xssAPI.encodeForHTMLAttr(campaignClass) %>"><%
            if (defaultTeaser != null && defaultTeaser.length() > 0) {
                //include a default teaser into a noscript tag in case of no JS (SEO...)
                StringWriter defaultHtml = new StringWriter();
                pageContext.pushBody(defaultHtml);
                %><sling:include replaceSelectors="noscript" path="<%= defaultTeaser + TEASER_SUFFIX %>"/><%
                pageContext.popBody();

                %><noscript><%=defaultHtml%></noscript><%
            } %>
        </div> <%
    } else if (WCMMode.fromRequest(request) == WCMMode.EDIT) {
        %><style type="text/css">
            .cq-teaser-placeholder-off {
                display: none;
            }
        </style>
        <h3 class="cq-texthint-placeholder"><%=i18n.get("No active campaigns target this teaser") %></h3>
        <img src="/libs/cq/ui/resources/0.gif" class="cq-teaser-placeholder" alt=""><%
    }
%>
