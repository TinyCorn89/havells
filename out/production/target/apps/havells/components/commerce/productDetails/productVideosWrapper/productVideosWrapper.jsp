<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.util.ProductLikeHelper" %>
<%@ page import="com.adobe.cq.commerce.api.Product" %>
<%@ page import="com.day.cq.wcm.webservicesupport.ConfigurationManager" %>
<%@ page import="com.day.cq.wcm.webservicesupport.Configuration" %>
<%@ page import="com.day.cq.commons.jcr.JcrConstants" %>
<%
	Product actualProduct = ProductLikeHelper.getProduct() ;
 	String[] imgArray = null;
    try{
        if(actualProduct != null){
            imgArray = actualProduct.getProperty("youtube", String[].class);
        }
    }catch (Exception ex){}

	String result = properties.get("displayType", "A");
    Boolean arrows=false,dots=false;
    if(result.equals("A"))
        arrows=true;
    else
        dots=true;
%>
<input type="hidden" name="dot" value="<%=dots%>" />
<input type="hidden" name="arrow" value="<%=arrows%>" />

<%if(imgArray != null && imgArray.length >0){ %>

    <div class="videoWrapper" style="float:none;">
        <div class="divHeading" style="float:none;">${properties.heading}</div>
        <div class="videoSlider" style="float:none;">
            <div class="slick-slider">
                <%
                  for(String s : imgArray){
                    String videoId = "http://www.youtube.com/embed/"+ProductLikeHelper.getVideoId(s);
                    String videoThumbnail = "http://img.youtube.com/vi/"+ProductLikeHelper.getVideoId(s)+"/mqdefault.jpg";
                    %>
                <div>
                    <div class="videoSliderInner" style="width: 202.567px;float: none;" >
                        <a class="mediaVideo" href="<%=videoId%>"><img src="<%=videoThumbnail%>" style="width: 202.567px;"><em></em></a>
                        <div class="videoName videoName<%=ProductLikeHelper.getVideoId(s)%>"></div>
                        <div class="viewWrapper viewWrapper<%=ProductLikeHelper.getVideoId(s)%>"></div>
                    </div>
		        </div>
                <script>
                    $(".videoName<%=ProductLikeHelper.getVideoId(s)%>").ready(function(){
                        addToVideoIdArray("<%=ProductLikeHelper.getVideoId(s)%>");
                    });
                </script>
                <%
                    }
                %>
            </div>
            <%
                ConfigurationManager configurationManager = sling.getService(ConfigurationManager.class);
                String authKey = null;
                if(configurationManager != null){
                    String[] services = pageProperties.getInherited("cq:cloudserviceconfigs", new String[]{});
                    Configuration youtubeSearchConfiguration = configurationManager.getConfiguration("searchyoutube",services);
                    if(youtubeSearchConfiguration != null){
                        authKey = youtubeSearchConfiguration.getInherited("o.auth.key", "");
                    }
                }
            %>
            <script>
                jQuery(document).ready(function(){
                    ytVideoAjax("<%=authKey%>");
                });
            </script>
            <div style="clear:both;"></div>
        </div>
    </div>
<%}%>
