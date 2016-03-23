<%@include file="/apps/havells/global.jsp"%><%
%><%@page session="false"
          import="com.day.cq.wcm.foundation.forms.FormsHelper,
                  com.day.cq.wcm.foundation.forms.LayoutHelper,
                  org.apache.commons.lang.StringUtils,
                  org.apache.jackrabbit.util.Base64" %>

<%
    final String name = FormsHelper.getParameterName(resource);
    final String id = FormsHelper.getFieldId(slingRequest, resource);
    final String title = FormsHelper.getTitle(resource, i18n.get("Password"));

    final boolean hideTitle = properties.get("hideTitle", false);
    final String confirmTitle = properties.get("confirmationTitle", i18n.get("Confirm Password"));
    final boolean hideConfirmTitle = properties.get("hideConfirmTitle", false);
    final String placeHolderTitle = properties.get("showLabelInside", false)? title : "";
    final String placeHolderConfirmTitle = properties.get("showLabelInside", false)? confirmTitle : "";
    final boolean required = FormsHelper.isRequired(resource);

    String width = properties.get("width", "300");
    width = width + "px;";

    final String cols = properties.get("cols", "35");
    String minlength = properties.get("minlength", "");
    String maxlength = properties.get("maxlength", "");

    LayoutHelper.printDescription(FormsHelper.getDescription(resource, ""), out);
    LayoutHelper.printErrors(slingRequest, name, out);
    String mail="";
    String token;
    if(request.getParameter("token")!=null){
        token= request.getParameter("token");
        String[] tempToken = StringUtils.split(Base64.decode(token), "$");
        if (tempToken.length == 2) {
            mail = tempToken[0];
        }
    }else{
        token="";
    }
%>
<div class="forgotPwdWrap" style="float: none;">
    <div class="resetPassword">
        <cq:include path="form_start" resourceType="havells/components/profile/form/start"/>
        <div class="form_row">
            <% LayoutHelper.printTitle(id, title, required, hideTitle, out); %>
            <div class="form_rightcol">
                <input class="geo textinput" placeholder = "<%=placeHolderTitle%>" name="password" id="password" value="" type="password" autocomplete="off" size="<%=xssAPI.encodeForHTMLAttr(cols)%>" <%= width.length() > 0 ? "style=\"width:" + xssAPI.encodeForHTMLAttr(width) + "\"" : "" %> required <%if(!minlength.isEmpty()){%>minlength="<%=minlength%>"<%}%> <%if(!maxlength.isEmpty()){%>maxlength="<%=maxlength%>"<%}%>>
            </div>
        </div>
        <div class="form_row">
            <% LayoutHelper.printTitle(id, confirmTitle, required, hideConfirmTitle, out); %>
            <div class="form_rightcol">
                <input class="geo textinput" placeholder = "<%=placeHolderConfirmTitle%>" name="confirmpassword" id="confirmpassword" value="" type="password" autocomplete="off" size="<%=xssAPI.encodeForHTMLAttr(cols)%>" <%= width.length() > 0 ? "style=\"width:" + xssAPI.encodeForHTMLAttr(width) + "\"" : "" %> required <%if(!minlength.isEmpty()){%>minlength="<%=minlength%>"<%}%> <%if(!maxlength.isEmpty()){%>maxlength="<%=maxlength%>"<%}%>>
            </div>
        </div>
        <input type="hidden" name="userid" value="<%=mail%>"/>
        <input type="hidden" id="mailid" value="<%=mail%>"/>
        <input type="hidden" id="token" value="<%=token%>"/>
        <input type="hidden" id="redirectTo404" value="${properties.redirectTo404}"/>
        <input type="hidden" id="redirectToAccessDenied" value="${properties.redirectToAccessDenied}"/>
        <input type="hidden" name="redirectTothank" value="${properties.redirectToThank}"/>
        <cq:include path="form_end" resourceType="havells/components/profile/form/end"/>
    </div>
    <div class="resetPasswordMessage" style="display: none">
        <label>${properties.tokenNotExistMessage}</label>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        var token=$("#token").val();
        resetPassword(token);
    });
</script>
