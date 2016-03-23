<%@page session="false" %>
<%@ page import="  com.day.cq.personalization.UserPropertiesUtil,
                 com.day.cq.wcm.api.WCMMode,
                 com.day.cq.wcm.foundation.forms.FormsHelper,
                 com.day.text.Text" %>
<%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %>
<%@include file="/apps/havells/global.jsp" %>
<%
    String id = Text.getName(resource.getPath());
    String action = currentPage.getPath() + "/j_security_check";
    String alreadySignedInFunctionName = "cq5forms_already_signed_in_" + id;

    String loginLabelValue = properties.get("./loginLabel", String.class);
    String loginLabel = loginLabelValue == null ? "Sign In " : loginLabelValue;

    String redirectTo = currentPage.getPath();
    if (!redirectTo.endsWith(".html")) {
        redirectTo += ".html";
    }
    boolean isDisabled = WCMMode.fromRequest(request).equals(WCMMode.DISABLED);
    final boolean isAnonymous = UserPropertiesUtil.isAnonymous(slingRequest);
%>
<div class="signInWrapper">
    <% if (!isAnonymous) {%>
    <script type="text/javascript">
        function <%= xssAPI.encodeForHTML(alreadySignedInFunctionName) %>() {
            var url = CQ.shared.HTTP.noCaching("<%= xssAPI.encodeForJSString(redirectTo) %>");
            CQ.shared.Util.load(url);

        }
    </script>
    <% } else { %>
    <form id="<%= xssAPI.encodeForHTMLAttr(id) %>" name="<%= xssAPI.encodeForHTMLAttr(id) %>"
          method="POST" action="<%= xssAPI.getValidHref(action) %>" enctype="multipart/form-data">

        <input type="hidden" name="resource" value="<%= xssAPI.encodeForHTMLAttr(redirectTo) %>">
        <input type="hidden" name="j_validate" value="true">
        <input type="hidden" name="_charset_" value="UTF-8"/>
        <div class="login-form">
            <div class="tableRow" >
                <div class="tableData"><input id="<%= xssAPI.encodeForHTMLAttr(id + "_username")%>"
                           placeholder="Login Id" autocomplete="off"
                           class="<%= FormsHelper.getCss(properties, "form_field form_field_text form_" + id + "_username") %>"
                           name="j_username"/>
                </div>
            </div>
            <div class="tableRow">
                <div class="tableData"><input id="<%= xssAPI.encodeForHTMLAttr(id + "_password")%>"
                           placeholder="Password" autocomplete="off"
                           class="<%= FormsHelper.getCss(properties, "form_field form_field_text form_" + id + "_password") %>"
                           type="password"  name="j_password"/>
                </div>
            </div>
            <div class="tableRow">
                <div class="tableData"><input class="submit" type="submit" value="<%= xssAPI.encodeForHTMLAttr(loginLabel) %>"></div>
            </div>
        </div>
    </form>
    <% } %>
    <div class="errorMsg loginerror" id="<%= xssAPI.encodeForHTMLAttr(id) %>_errorMsg"></div>
    <div class="clearfix"></div>
    <a href="${properties.signUpUrl}.html">${properties.signUpLabel}</a><a
        href="${properties.forgotPasswordUrl}.html">${properties.forgotPasswordLabel}</a>
    <cq:include script="sociallogins.jsp"/>
</div>
<script>
    var formId = "<%= xssAPI.encodeForHTMLAttr(id) %>";
    var $form = $("#<%= xssAPI.encodeForHTMLAttr(id) %>");

    var loadProfile = function () {

        if (CQ_Analytics) {
            var u = document.forms['<%=id%>']['j_username'].value;

            <%  if ( !isDisabled ) { %>
            if (CQ_Analytics.ProfileDataMgr) {
                if (u) {
                    var loaded = CQ_Analytics.ProfileDataMgr.loadProfile(u);
                    if (loaded) {
                        var url = CQ.shared.HTTP.noCaching("<%= xssAPI.encodeForJSString(redirectTo) %>");
                        CQ.shared.Util.load(url);
                    } else {
                        alert("<%="The user could not be found."%>");
                    }
                    return false;
                }
            }
            return true;
            <%  } else { %>
            if (CQ_Analytics.ProfileDataMgr) {
                CQ_Analytics.ProfileDataMgr.clear();
            }
            return true;
            <%  } %>
        }
    }

    var validateForm = function (userId, password) {

        var result = false;
        if ((userId !== undefined) && (userId != '')) {
            if ((password !== undefined) && (password != '')) {
                result = true;
            } else {
                $(".signIn").parent().find("ul").show();
                $("#<%= xssAPI.encodeForHTMLAttr(id) %>_errorMsg").html("Password Required.");
            }
        } else {
            $(".signIn").parent().find("ul").show();
            $("#<%= xssAPI.encodeForHTMLAttr(id) %>_errorMsg").html("Username Required.");
        }
        return result;
    }

    $("#" + formId).submit(function (e) {

        var requestUrl = $form.attr("action");
        var userId = $form.find('input[name=j_username]').val();
        var password = $form.find('input[name=j_password]').val();
        var result = validateForm(userId, password);
        if (result) {
            $.ajax({
                type: 'POST',
                url: requestUrl,
                data: $("#" + formId).serialize(),
                success: function (response) {

                    loadProfile();
                    window.location.reload();
                },
                error: function (jqXHR, textStatus, errorThrown) {

                    if (jQuery(".signInWrapper .errorMsg .loginerror").length) {
                        $(".signIn").parent().find("ul").show();
                    }
                    $("#<%= xssAPI.encodeForHTMLAttr(id) %>_errorMsg").show();
                    $("#<%= xssAPI.encodeForHTMLAttr(id) %>_errorMsg").html("Invalid Credentials.Try again!");
                }
            });
        }
        return false;
    });
</script>