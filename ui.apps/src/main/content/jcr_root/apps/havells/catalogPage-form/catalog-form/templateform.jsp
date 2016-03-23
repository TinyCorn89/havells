<%@ page import="com.havells.servlet.catalogPageGenerator.CatalogTemplatesModal" %>
<%@include file="/apps/havells/global.jsp" %>
<%
     CatalogTemplatesModal modal = new CatalogTemplatesModal();
     String templatePath = properties.get("templatePath", "/content/catalogs/havells/templatePages/en");
     Resource tResource = resourceResolver.getResource(templatePath);

     String msg = request.getParameter("msg")==null ? "" : request.getParameter("msg");
%>
<br/>
<h2 style="color:blue">Welcome to catalog template pages generator tool. </h2>
<b>To create new catalog Page..
    <a href="/content/catalogs/havells/templatePages/base-templates/pagecreator.html">
        Create New catalog Page</a>
</b>
<div id="templatePagesForm">
    <b style="color:red"><%=msg%></b>
     <form name="templateForm" action="/bin/createCatalogPage.json" method="POST">
         <ul>
             <li>
                 <div>
                     <b style="color:blue">Template Page Name & Page Title :</b>
                     <input type="textarea" name="pageName" width=100 height=30 multiple=true
                            required="Provide name"/> <br/>
                     <b style="color:blue">To create multiple pages of similar type, provide name & title like this.</b>
                     <div>pageName1=pageTitle1,pageName2=pageTitle2,pageName3=pageTitle3</div>
                     <br/>
                 </div>
             </li>
             <br/>
             <li>
                 <div>
                     <b style="color:blue">Choose template page type :</b>
                     <select id="templateType" name="templateType">
                         <option selected>Select type of page</option>
                         <option value="category">Category Page</option>
                         <option value="subcategory">SubCategory Page</option>
                         <option value="productListing">Product Listing</option>
                     </select>
                     <br/>
                    <label for="listingInCategory">
                        <b style="color:red">Do you want to create productListing under category?. if yes select it.</b>
                    </label>
                     <input type="checkbox" name="listingInCategory" id="listingInCategory"/>
                 </div><br/>
             </li>
             <br/>
             <li>
                 <h3>Choose Destination folder </h3><br/>
                 <div>
                     <b style="color:blue">Range Pages:</b>
                     <select id="templateRangesPath" name="templateRangesPath">
                         <option value="selected" selected> Select type of page</option>
                         <c:forEach var="list" items="<%=modal.getTemplatesPath(tResource)%>">
                             <option value="${list.path}">${list.name}</option>
                         </c:forEach>
                     </select>
                     <b style="color:blue">Category Pages:</b>
                     <select id="templateCategoriesPath" name="templateCategoriesPath">
                         <option value="selected"> Select type of page</option>
                     </select>
                 </div><br/>
                 <div>
                     <b style="color:blue">Sub categories Pages:</b>
                     <select id="templateSubCategoriesPath" name="templateSubCategoriesPath">
                         <option value="selected"> Select type of page</option>
                     </select>

                 </div><br/>
             </li>
          </ul>
         <input type="hidden" value="<%=properties.get("category","")%>" name="categoryTemplate" id="categoryTemplate"/>
         <input type="hidden" value="<%=properties.get("subcategory","")%>" name="subCategoryTemplate" id="subCategoryTemplate"/>
         <input type="hidden" value="<%=properties.get("productListing","")%>" name="listingTemplate" id="listingTemplate"/>

         <input type="hidden" value="true" name="templateCreation" id="templateCreation"/>
         <input type="hidden" value="<%=currentPage.getPath()%>" name="currentPage" id="currentPageForTemplate"/>
         <input type="submit" name="submit" id="submitTemplateForm" text="Submit"/>

     </form>
</div>

<script type="text/javascript">

    /***Destination pages for template page creation"**/
    $("#templateRangesPath").on("change", function(){
        getTemplatePages("templateCategoriesPath", $(this).val());
    });

    $("#templateCategoriesPath").on("change",function(){
        getTemplatePages("templateSubCategoriesPath", $(this).val());
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
