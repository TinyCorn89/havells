<%@ page import="com.havells.servlet.catalogPageGenerator.CatalogTemplatesModal" %>
<%@include file="/apps/havells/global.jsp" %>
<%
     CatalogTemplatesModal modal = new CatalogTemplatesModal();
     String templatePath = properties.get("templatePath", "/content/catalogs/havells/templatePages/en");
     Resource tResource = resourceResolver.getResource(templatePath);

     String catalogPath = properties.get("catalogPath", "/content/catalogs/havells/havells");
     Resource cResource = resourceResolver.getResource(catalogPath);

     String bluePrintPath = properties.get("bluePrintPath", "/content/catalogs/havells/havells/bluePrintBaseCatalog");
     String msg = request.getParameter("msg")==null ? "" : request.getParameter("msg");

%>
<br/>
<h2 style="color:blue">Welcome to catalog pages generator tool. </h2>
<b>To create new Template Page..
    <a href="/content/catalogs/havells/templatePages/base-templates/pagecreator.templateform.html">
    Create New Template Page</a>
</b>
<div id="catalogPagesForm">
    <form name="catalogForm" action="/bin/createCatalogPage.json" method="POST">
        <b style="color:red"><%=msg%></b>
        <ul>
            <li>
                <div>
                    <b style="color:blue">Choose catalog page type :</b>
                    <select id="templatePageType" name="templatePageType">
                        <option selected> Select type of page</option>
                        <option>Category-Catalog</option>
                        <option>SubCategory-Catalog</option>
                        <option>CategoryListing-Catalog</option>
                    </select>
                </div><br/>
            </li>
            <li>
                <div>
                    <b style="color:blue">Catalog Page Name :</b>
                    <input type="text" name="pageName" width=100 height=30 multiple=true
                           required="Provide name"/> <br/><br/>
                    <b style="color:blue">Catalog Page Title :</b>
                    <input type="text" name="pageTitle" width=100 height=30 required="provide title"/>
                </div>
            </li>
            <br/>
            <li>
                <h3>Choose source templates path </h3><br/>
                <div>
                    <b style="color:blue">Range Templates:</b>
                    <select id="templateRangePath" name="templateRangePath">
                        <option value="selected" selected> Select type of page</option>
                        <c:forEach var="list" items="<%=modal.getTemplatesPath(tResource)%>">
                             <option value="${list.path}">${list.name}</option>
                        </c:forEach>
                    </select>
                    <b style="color:blue">Categories Templates:</b>
                    <select id="templateCategoryPath" name="templateCategoryPath">
                        <option value="selected"> Select type of page</option>
                    </select>
                 </div><br/>
                 <div>
                    <b style="color:blue">Sub categories Templates:</b>
                    <select id="templateSubCategoryPath" name="templateSubCategoryPath">
                        <option value="selected"> Select type of page</option>
                    </select>
                    <b style="color:blue">Product Listing Templates:</b>
                    <select id="templateListingPath" name="templateListingPath">
                        <option value="selected"> Select type of page</option>
                    </select>
                </div><br/>
            </li>
            <br/>
            <li>
                <h3>Choose destination base catalog pages path </h3><br/>
                <div>
                    <b style="color:blue">BaseCatalog Ranges:</b>
                    <select id="dCatalogRangePath" name="dCatalogRangePath">
                        <option value="selected"> Select type of page</option>
                        <c:forEach var="list"  items="<%=modal.getTemplatesPath(cResource)%>">
                            <option value="${list.path}">${list.name}</option>
                        </c:forEach>
                    </select>
                    <b style="color:blue">BaseCatalog Categories:</b>
                    <select id="dCatalogCategoryPath" name="dCatalogCategoryPath">
                        <option value="selected"> Select type of page</option>
                    </select>
                   <b style="color:blue">BaseCatalog Sub categories:</b>
                    <select id="dCatalogSubCategoryPath" name="dCatalogSubCategoryPath">
                        <option value="selected"> Select type of page</option>
                    </select>
                </div>
            </li>
            <br/>
            <li>
                <div>
                    <b style="color:blue">Catalog Category Listing Tags :</b>
                    <select id="rangTag" name="rangTag">
                        <option value="selected"> Select tags</option>
                        <option value="/etc/tags/havells/consumer">consumer</option>
                        <option value="/etc/tags/havells/industrial">industrial</option>
                    </select>

                    <select id="categoryTag" name="categoryTag">
                        <option value="selected"> Select tag</option>
                    </select>
                    <select id="subCategoryTag" name="subCategoryTag">
                        <option value="selected"> Select tag</option>
                    </select>
                    <select id="categoryListingTag" name="tags">
                        <option value="selected"> Select tag</option>
                    </select><br/>
                    <b style="color:blue">Tags are applicable only for category(product) Listing catalog pages..</b><br/>
                </div>
            </li>
                <input type="hidden" value="<%=currentPage.getPath()%>" name="currentPage" id="currentPage"/><br/>
                <input type="hidden" value="<%=bluePrintPath%>" name="bluePrintPath" id="bluePrintPath"/><br/>
                <input type="submit" name="submit" id="submit" text="Submit"/>
            <h3>
                <ul>
                    <li>IF:</li>
                    <li> category page to be created, choose Base Catalog Ranges.</li>
                    <li> SubCategory page to be created, choose base catalog category page.</li>
                    <li> CategoryListing page to be created, choose base catalog subcategory.</li>
                </ul>
            </h3>
        </ul>
    </form>
</div>
<script type="text/javascript">

    /*** source page of templates"**/
    $("#templateRangePath").on("change", function(){
        getTemplatePages("templateCategoryPath", $(this).val());
    });

    $("#templateCategoryPath").on("change",function(){
        getTemplatePages("templateSubCategoryPath", $(this).val());
    });
    $("#templateSubCategoryPath").on("change",function(){
        getTemplatePages("templateListingPath", $(this).val());
    });

    /** destination drowdowns****/

    $("#dCatalogRangePath").on("change", function(){
        getTemplatePages("dCatalogCategoryPath", $(this).val());
    });

    $("#dCatalogCategoryPath").on("change",function(){
        getTemplatePages("dCatalogSubCategoryPath", $(this).val());
    });

    /** Loading tags data ***/

    $("#rangTag").on("change", function(){
        getTemplatePages("categoryTag", $(this).val());
    });

    $("#categoryTag").on("change",function(){
        getTemplatePages("subCategoryTag", $(this).val());
    });

    $("#subCategoryTag").on("change",function(){
        getTemplatePages("categoryListingTag", $(this).val());
    });

    var flag = false;

    function getTemplatePages(htmlId, path) {
        if(path == 'selected'){
            console.log("do nothing");
        }else {
            $.ajax({
                url: '/bin/createCatalogPage.json',
                type: 'GET',
                data: { path: path},
                contentType: 'application/json; charset=utf-8',
                success: function (response) {
                    setResponseData(htmlId, response);
                },
                error: function () {
                    console.log("Error during the ajax call");
                }
            });
        }
    }
    function setResponseData(htmlId, response){

        var data = JSON.parse(response);
        var content = "<option value='selected' selected>Select any page </option>", len = 0;
        for (; len < data.length; len++){
            var item = data[len];
            content = content + "<option value='"+item.path+"'>"+item.name+"</option>";
        }
       // console.log(content);
        if(len > 0){
            $("#"+htmlId).html(content);
        }
    }


</script>
