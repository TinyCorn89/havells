<%@page session="false" 
        contentType="text/html"
        pageEncoding="utf-8"%>
<%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %>
<%@include file="/apps/havells/global.jsp"%>
<cq:defineObjects/>

<div class="connect-content">
    <h3>Youtube Search Settings</h3> 
    <img src="<%=properties.get("thumbnailPath",String.class)%>"
         alt="Youtube Search"
         title="Youtube Search"
    class="fbconnect-thumbnail" style="float:left"/>

    <ul class="fbconnect-content">
        <li>
            <div class="li-bullet">
                <strong>App Key: </strong><%=xssAPI.encodeForHTML(properties.get("o.auth.key", ""))%>
            </div>
        </li>
        <li>
	    <div class="li-bullet">
                <strong>App Key Status: </strong><span class="statusCode"></span>
            </div>
        </li>
        <script>
            jQuery(".statusCode").ready(function(){
                jQuery.ajax({
                    url : "https://www.googleapis.com/youtube/v3/search?part=snippet%20&q=google%20&type=channel%20&key=<%=xssAPI.encodeForHTML(properties.get("o.auth.key", ""))%>",
                    statusCode: {
                        400: function() {
			    jQuery(".statusCode").html('<span style="color:#ff0000">Invalid Key. Please configure a valid key.</span>')
                        },
                            200: function() {
                            jQuery(".statusCode").html('<span style="color:#006400">Valid Key</span>')
                	}
                    }
                });
            });
        </script>
    </ul>
</div>
