package com.havells.servlet.catalogPageGenerator
import com.day.cq.commons.jcr.JcrConstants
import com.day.cq.commons.jcr.JcrUtil
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.sling.SlingServlet
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.servlets.SlingAllMethodsServlet
import org.apache.sling.commons.json.JSONArray
import org.apache.sling.commons.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.havells.supportUtils.ScriptConstants
import javax.jcr.Session

@SlingServlet(
        paths = ["/bin/createCatalogPage.json"],
        generateComponent = false
)
@Component(enabled = true, immediate = true, metatype = false)
class CreateCatalogPage extends SlingAllMethodsServlet {


    private static final Logger LOG = LoggerFactory.getLogger(CreateCatalogPage.class);

    private static final String CATEGORY_CATALOG = "Category-Catalog",
                                SUBCATEGORY_CATALOG = "SubCategory-Catalog", CATEGORY_LISTING_CATALOG = "CategoryListing-Catalog"

    private static final String TEMPLATE_CATEGORY_TYPES = "category", TEMPLATE_SUBCATEGORY_TYPES = "subcategory",
                                TEMPLATE_LISTING_TYPES = "productListing"

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
            IOException {
        CatalogTemplatesModal catalogTemplatesModal = new CatalogTemplatesModal();
        String path = request.getParameter("path")
        Resource resource = request.getResourceResolver().getResource(path)
        List<ChildModal> list = catalogTemplatesModal.getTemplatesPath(resource)

        JSONArray jsonArray = new JSONArray()
        list.each { ChildModal childModal ->
            JSONObject jsonObject = new JSONObject()
            jsonObject.put("path", childModal.path)
            jsonObject.put("name", childModal.name)
            jsonArray.put(jsonObject)
        }
        response.getWriter().write(jsonArray.toString())
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
            IOException {

        ResourceResolver resourceResolver = request.getResourceResolver()
        String currentPage = request.getParameter("currentPage")
        String pageName = request.getParameter("pageName")
        String pageTitle = request.getParameter("pageTitle") == null ?: pageName;

        try {
            String templateCreation = request.getParameter("templateCreation");

            if ("true".equals(templateCreation)) {
                try {
                    LOG.info(" init template creation ")
                    createTemplatePages(resourceResolver, request, pageName)
                    response.sendRedirect(currentPage + ".templateform.html?msg=Congratulations!!!, Catalog template page is created!!!!")
                } catch (Exception ex) {
                    ex.printStackTrace()
                    response.sendRedirect(currentPage + ".templateform.html?msg=Expectation is failed. Check the logs.")
                }
            } else {

                String templatePageType = request.getParameter("templatePageType");
                String bluePrintPath = request.getParameter("bluePrintPath");

                /**Reading source templates category */
                String catalogCategoryPath = request.getParameter("templateCategoryPath");
                String catalogSubCategoryPath = request.getParameter("templateSubCategoryPath");
                String catalogListingPath = request.getParameter("templateListingPath");

                /** Reading target templates category ***/
                String dCatalogRangePath = request.getParameter("dCatalogRangePath");
                String dCatalogCategoryPath = request.getParameter("dCatalogCategoryPath");
                String dCatalogSubCategoryPath = request.getParameter("dCatalogSubCategoryPath");

                String destinationPath = ""
                String catalogTemplatePath = ""

                /**
                 * Destination path will be chosen with logic that if
                 * 1. category to be created, choose Base Catalog Ranges.
                 * 2. SubCategory page to be created, choose base catalog category page
                 * 3. CategoryListing page to be created, choose base catalog subcategory
                 *
                 **/
                switch (templatePageType) {
                    case CATEGORY_CATALOG:
                        catalogTemplatePath = catalogCategoryPath
                        destinationPath = dCatalogRangePath
                        break
                    case SUBCATEGORY_CATALOG:
                        catalogTemplatePath = catalogSubCategoryPath
                        destinationPath = dCatalogCategoryPath
                        break
                    case CATEGORY_LISTING_CATALOG:
                        catalogTemplatePath = catalogListingPath
                        destinationPath = dCatalogSubCategoryPath
                        break
                }
                String tags = request.getParameter("tags");

                Resource sResource = resourceResolver.getResource(bluePrintPath)
                Resource dResource = resourceResolver.getResource(destinationPath)

                LOG.info("\ndestinationPath=" + destinationPath + "\npageNames=" + pageName + "\ntemplatePageType=" + templatePageType +
                        "\ncatalogTemplatePath=" + catalogTemplatePath + "\ncurrentPage="
                        + currentPage + "\ntags=" + tags)

                if (CATEGORY_CATALOG.equals(templatePageType) || SUBCATEGORY_CATALOG.equals(templatePageType) || CATEGORY_LISTING_CATALOG.equals(templatePageType)) {
                    LOG.info("catalog pages to be created ")
                    if (catalogTemplatePath != "" && destinationPath != "") {
                        if(isCatalogPathExist(resourceResolver, catalogTemplatePath)) {
                            createCatalogPages(resourceResolver, sResource, dResource, catalogTemplatePath, tags, pageName, pageTitle, templatePageType)
                        }else{
                            response.sendRedirect(currentPage + ".html?msg= you have chosen wrong template path!!!!!")
                        }
                        response.sendRedirect(currentPage + ".html?msg= Congratulations!!!, Catalog page is created!!!!")
                    } else {
                        LOG.info("catalog template must not empty ", catalogTemplatePath)
                        response.sendRedirect(currentPage + ".html?msg=Expectation is failed. Check the logs..destinationPath is " + destinationPath)
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("error occurs : ")
            ex.printStackTrace()
            response.sendRedirect(currentPage + ".html?msg=Expectation is failed. Check the logs..")
        } finally {
            if (resourceResolver.isLive()) {
                resourceResolver.close()
            }
        }
    }


    /**
     *
     * @param resourceResolver
     * @param sResource
     * @param dResource
     * @param tags
     * @param pageNames
     * @throws Exception
     */
    private void createCatalogPages(ResourceResolver resourceResolver, Resource sResource, Resource dResource,
                                    final String catalogTemplatePath,
                                    final String tags,
                                    final String pageName,
                                    final String pageTitle, final String templatePageType) throws Exception {

        Session session = resourceResolver.adaptTo(Session.class)
        if (sResource != null && dResource != null) {
            javax.jcr.Node sNode = sResource.adaptTo(javax.jcr.Node.class)
            javax.jcr.Node dNode = dResource.adaptTo(javax.jcr.Node.class)
            LOG.info(" source node & destination node ", sNode, dNode)

            JcrUtil.copy(sNode, dNode, pageName)
            LOG.info(" destination node is created ")
            session?.save()

            dNode = dNode.getNode(pageName)

            javax.jcr.Node jcrContent = dNode.getNode(JcrConstants.JCR_CONTENT)
            if (jcrContent != null) {
                jcrContent.setProperty(JcrConstants.JCR_TITLE, pageTitle)
                LOG.info("catalog jcr title updated")
            }

            javax.jcr.Node targetNode = dNode.getNode(JcrConstants.JCR_CONTENT + "/target")
            javax.jcr.Node filterNode = dNode.getNode(JcrConstants.JCR_CONTENT + "/filter")
            javax.jcr.Node templatesNode = dNode.getNode(JcrConstants.JCR_CONTENT + "/templates")

            if (targetNode != null && filterNode != null && templatesNode != null) {
                LOG.info("section path is "+ catalogTemplatePath)
                templatesNode.setProperty("section", catalogTemplatePath)
                LOG.info("section node updated")
                targetNode.setProperty("jcr_title", pageTitle)
                targetNode.setProperty("pageTitle", pageTitle)
                LOG.info("generated title updated")
                if (CATEGORY_LISTING_CATALOG.equals(templatePageType)) {
                    String[] strings = new String[1]
                    strings[0] = tags.replaceAll(ScriptConstants.DEFAULT_TAGS_PATH+"/", "havells:");
                    filterNode.setProperty("matchTags", strings)
                    LOG.info("match tags updated")
                }
            }
        }
        session?.save()
    }
    private boolean isCatalogPathExist(ResourceResolver resourceResolver, String catalogTemplatePath){
        return resourceResolver.getResource(catalogTemplatePath) != null
    }
    /**
     *
     * @param resourceResolver
     * @param sResource
     * @param dResource
     * @param tags
     * @param pagesName
     * @throws Exception
     */
    private void createTemplatePages(ResourceResolver resourceResolver, SlingHttpServletRequest request
                                     , final String pageInfo) throws Exception {

        String templateRangesPath = request.getParameter("templateRangesPath")
        String templateCategoriesPath = request.getParameter("templateCategoriesPath")
        String templateSubCategoriesPath = request.getParameter("templateSubCategoriesPath")
        String templateListingPath = request.getParameter("templateListingPath")
        String listingInCategory = request.getParameter("listingInCategory")

        LOG.info("templateRangesPath " + templateRangesPath)
        LOG.info("templateCategoriesPath " + templateCategoriesPath)
        LOG.info("templateSubCategoriesPath " + templateSubCategoriesPath)
        LOG.info("templateListingPath " + templateListingPath)

        String templateType = request.getParameter("templateType")
        /*** template source **/

        String categoryTemplate = request.getParameter("categoryTemplate")
        String subCategoryTemplate = request.getParameter("subCategoryTemplate")
        String listingTemplate = request.getParameter("listingTemplate")

        LOG.info("categoryTemplate " + categoryTemplate)
        LOG.info("subCategoryTemplate " + subCategoryTemplate)
        LOG.info("listingTemplate " + listingTemplate)


        LOG.info("Display all received parameters : ", request.getParameterNames())
        String destinationPath = "", pageTemplate = "";
        LOG.info("template type is " + templateType)

        switch (templateType) {
            case TEMPLATE_CATEGORY_TYPES:
                destinationPath = templateRangesPath
                pageTemplate = categoryTemplate
                break
            case TEMPLATE_SUBCATEGORY_TYPES:
                destinationPath = templateCategoriesPath
                pageTemplate = subCategoryTemplate
                break
            case TEMPLATE_LISTING_TYPES:
                destinationPath = ("on".equalsIgnoreCase(listingInCategory) ? templateCategoriesPath : templateSubCategoriesPath)
                pageTemplate = listingTemplate
                break
        }

        LOG.info("destinationPath " + destinationPath)

        if (pageTemplate != null && destinationPath != null && !("selected".equals(destinationPath))) {
            Resource sResource = resourceResolver.getResource(pageTemplate)
            Resource dResource = resourceResolver.getResource(destinationPath)
            if (sResource != null && dResource != null) {
                StringTokenizer tokenizer = new StringTokenizer(pageInfo, ',')
                LOG.info("pageDetails " + tokenizer)
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken()
                    List<String> pageNameTitle = token.tokenize('=')
                    LOG.info("pageNameTitle " + pageNameTitle)

                    JcrUtil.copy(sResource.adaptTo(javax.jcr.Node.class), dResource.adaptTo(javax.jcr.Node.class), pageNameTitle.get(0))
                    resourceResolver.adaptTo(Session.class)?.save()
                    Resource jcrResource = resourceResolver.getResource(destinationPath + "/" + pageNameTitle.get(0) + "/" + JcrConstants.JCR_CONTENT)
                    JcrUtil.setProperty(jcrResource.adaptTo(javax.jcr.Node.class),
                            JcrConstants.JCR_TITLE,
                            pageNameTitle.size() > 0 ? pageNameTitle.get(1) : pageNameTitle.get(0))
                }
            }
        }else{
            LOG.info("pageTemplate "+pageTemplate+", "+destinationPath)
        }
        resourceResolver.adaptTo(Session.class)?.save()
    }
}
