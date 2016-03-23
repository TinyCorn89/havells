<%@ include file="/libs/foundation/global.jsp" %>
<%@ page import="com.day.cq.i18n.I18n" %>
<%

    I18n i18n = new I18n(slingRequest);

    String title = properties.get("jcr:title", i18n.get("Recently Viewed"));
    int max = xssAPI.getValidInteger(properties.get("max", ""), 3);
    boolean excludeProductsInCart = properties.get("excludeProductsInCart", false);
    String containerId = "cq" + resource.getPath().replaceAll("/", "-").replaceAll(":", "-");
%>
<script type="text/javascript">
    $CQ(document).ready(function () {
        /**
         * ideally this call should have been somewhere in productTestimonials component
         * but this call would reload the component itself making it a recursive call there.
         */
        CommonUtil.showTestimonials();
        if (CQ_Analytics.ViewedProducts && CQ_Analytics.ViewedProducts.recent) {
            var container = $CQ("#<%= xssAPI.encodeForJSString(containerId) %>");
            var items = container.find(".applicationProducts ul li");
            var recent = CQ_Analytics.ViewedProducts.recent(<%= max %>, <%= excludeProductsInCart %>);
            for (var i = 0; i < <%= max %>; i++) {
                var item = $CQ(items[i]);
                if (recent.length > i) {
                    var a = item.find("a")[0];
                    if (a) {
                        a.setAttribute("href", CQ.shared.HTTP.externalize(recent[i].path));
                        a.setAttribute("title", recent[i].title);
                    }
                    var img = item.find("img")[0];
                    if (img) {
                        if (recent[i].image !== "undefined")
                            img.setAttribute("src", CQ.shared.HTTP.externalize(recent[i].image) + ".nav.jpg");
                        img.setAttribute("alt", recent[i].title);
                    }
                    var span = item.find("span")[0];
                    if (span) {
                        span.innerHTML = recent[i].title;
                    }
                    var mrpDiv = item.find(".mrpDiv")[0];
                    if (mrpDiv && (recent[i].price != "$0.00")) {
                        mrpDiv.innerHTML = "MRP " + recent[i].price.replace("$", "Rs. ");
                    }
                } else {
                    item.hide();
                }
            }
        } else {
            console.log("CQ Analytics view products not found");
        }
    });
</script>
<div id="<%= containerId %>" class="likeWrapper">
    <div class="divHeading"><%= xssAPI.encodeForHTML(title) %>
    </div>
    <div class="applicationProducts">
        <div class="youMayLikeWrap">
            <ul>
                <% for (int i = 0; i < max; i++) { %>
                <li>
                    <a>
                        <div class="productName"><span>&nbsp;</span></div>
                        <div class="mrpDiv">&nbsp;</div>
                        <div class="youMayLikeImg"><img src="" alt=""/></div>
                    </a>
                    <!-- <div class="addCartDiv">
                         <label>
                             <input type="checkbox">
                             ADD TO cart</label>
                         <label>
                             <input type="checkbox">
                             compare</label>
                     </div>-->
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</div>
