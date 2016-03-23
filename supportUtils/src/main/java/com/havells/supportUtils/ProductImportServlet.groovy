package com.havells.supportUtils
import com.day.cq.commons.jcr.JcrUtil
import com.day.cq.replication.PathNotFoundException
import com.day.cq.tagging.Tag
import com.day.cq.tagging.TagManager
import com.havells.supportUtils.PimNodeUtil
import org.apache.commons.lang.StringUtils
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.sling.SlingServlet
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse

import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ResourceUtil
import org.apache.sling.api.servlets.SlingAllMethodsServlet

import javax.jcr.Node
import javax.jcr.PathNotFoundException
import javax.jcr.Session

@groovy.util.logging.Slf4j
@SlingServlet(
resourceTypes = "havells/upload-product-data-form",
selectors = "submit",
extensions = "html",
generateComponent = false,
methods = "POST"
)
@Component(enabled = true, immediate = true, metatype = false)
public class ProductImportServlet extends SlingAllMethodsServlet {

	final String DEFAULT_IMG_PATH = "/content/dam/havells/default_img.png"

    String basePath = "/etc/commerce/products/havells"

    String contentDamBasePath = "/content/dam/havells"

    private static String TITLE_JCR_PROPERTY = "title"

    private static String TITLE_JCR_TITLE_PROPERTY = "jcr:title"

    private static String SUBTITLE_JCR_PROPERTY = "subTitle"

    private TagManager tagManager

    String section = ""

    String range = ""

    String category = ""

    String subCategory = ""

    String title = ""

    int quickFeatureCount = 3

    int detailFeatureCount = 25

    int optionalFeatureCount = 25

    int techHeadingCount = 50

    int accessoryHeadingCount = 20

    int startingRow = 5

    /**
     *  XLSX FILE CELL NO.
     */
    final int SECTION_CELL_NO = 0
    final int RANGE_CELL_NO = 1
    final int CATEGORY_CELL_NO = 2
    final int SUBCATEGORY_CELL_NO = 3
    final int TITLE_CELL_NO = 4
    final int SUB_TITLE_CELL_NO = 5

    final int MAIN_SKU_ID_CELL_NO = 6
    //final int MAIN_SKU_PRICE_CELL_NO = 7

    final int VARIANT_SKU_CELL_NO = 7
    final int COLOR_SKU_CELL_NO = 8

    final int ENABLE_ECOMM = 9

    final int PRODUCT_IMAGES = 11
    final int COLOR_IMAGE = 14
    final int GIFT_WRAP = 16
    final int PRODUCT_RECOMMENDATION = 19

    int FEATURES_START_CELL_NO = 20


    XSSFWorkbook workbook

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        tagManager = request.resourceResolver.adaptTo(TagManager)
        RequestParameter attach = request.getRequestParameter("fileData");
        def techSpecsColumnNos = request.getParameter("techSpecsColumnNos")?: "50"
        String currentPage = request.getParameter("currentPage")

        ResourceResolver resolver = request.resourceResolver
        Session jcrSession = resolver.adaptTo(Session)
        String messageToDisplay = "Product xlsx file not imported!!!"

        try {
            techHeadingCount = Integer.parseInt(techSpecsColumnNos)

            workbook = new XSSFWorkbook(attach.getInputStream())

            XSSFSheet sheet = workbook.getSheetAt(0); // Get iterator to all the rows in current sheet

            println ("sheet accepted" + sheet)

            for (int count=(sheet.firstRowNum + startingRow - 1); count < sheet.lastRowNum; count++) {

                try {
                    XSSFRow topRow = sheet.getRow(count)
                    section = fetchCellValue(topRow.getCell(SECTION_CELL_NO))
                    range = fetchCellValue(topRow.getCell(RANGE_CELL_NO))
                    category = fetchCellValue(topRow.getCell(CATEGORY_CELL_NO))
                    subCategory = fetchCellValue(topRow.getCell(SUBCATEGORY_CELL_NO))
                    title = fetchCellValue(topRow.getCell(TITLE_CELL_NO))

                    Resource subCatResource = createHierarchyTillSubCategory(resolver, section, range, category, subCategory)

                    /*********************------------ D O N E --------------****************************/
                    if ((section == null || section.equals("")) || (range == null || range.equals("")) || (category == null || category.equals(""))) {
                        log.warn "One of section, range OR category is blank. See the source file."
                        response.sendRedirect(currentPage + ".html")
                        break

                    }

                    synchronized (this) {
                        Node leafNode = createLeafNode(resolver, topRow, subCatResource)
                        //log.info("Created leafNode = ${leafNode.path}")

                        saveLeafNodeProps(resolver, leafNode, topRow)
                        int currentExcelCounter = addQuickFeatures(resolver,leafNode, topRow)
                        currentExcelCounter = addDetailFeatures(resolver,leafNode, topRow, currentExcelCounter)
                        currentExcelCounter = addOptionalFeatures(resolver,leafNode, topRow, currentExcelCounter)
                        leafNode.setProperty("faq", fetchCellValue(topRow.getCell(currentExcelCounter++)))
                        currentExcelCounter = addTechnicalDescription(resolver,leafNode, topRow, currentExcelCounter)

                        if (accessoryHeadingCount > 0) {
                            currentExcelCounter = addAccessoriesFeatures(resolver,leafNode, topRow, currentExcelCounter)
                        }

                        Node techDrawingNode = JcrUtil.createPath("${leafNode.path}/otherInfo/technicalDrawing", "nt:unstructured", resolver.adaptTo(Session))
                        String productBasePath = getProductBasePath(resolver, leafNode)
                        String[] techDrawingArray = fetchCellValue(topRow.getCell(currentExcelCounter++)).tokenize(";").collect {
                            "${productBasePath}/${it}"
                        }
                        techDrawingNode.setProperty("drawingImages", techDrawingArray)
                        String[] productVideos = fetchCellValue(topRow.getCell(currentExcelCounter)).tokenize(";").collect {
                            "${productBasePath}/${it}"
                        }

                        leafNode.setProperty("youtube", productVideos)
                    }
                    jcrSession?.save()
                }
                catch(IOException ex){
                    log.error "Error in inside loop reading file "
                    ex.printStackTrace(System.out)
                }
            }
            jcrSession?.save()
            messageToDisplay = "Product xlsx file imported!!!"
            response.getWriter().write(messageToDisplay)
            response.sendRedirect(currentPage + ".success.html")
        }
         catch (Exception ex) {
             log.error "Error in outside catch "
             ex.printStackTrace(System.out)
        }
        finally {
            resolver?.close()
        }
        response.getWriter().write(messageToDisplay);
        response.sendRedirect(currentPage + ".apply.html")
    }

    private synchronized String fetchCellValue(XSSFCell cell) {
    	if (cell != null) {
            log.info ("Fetching cell records. cell Type = ${cell.cellType}")
    	}

        if (!cell || XSSFCell.CELL_TYPE_BLANK == cell.cellType) {
            return ""
        } else if (cell.cellType== XSSFCell.CELL_TYPE_FORMULA) {
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            return evaluator.evaluate(cell).toString()
        } else if (cell.cellType == XSSFCell.CELL_TYPE_NUMERIC) {

        	int absoluteValue = getLastDigit(cell.numericCellValue)
        	if (absoluteValue > 0) {
        		// There are valid digits after decimal. Return as is.
        		return (cell.numericCellValue)
        	} else {
        		// Zeros after decimal point. Remove zeros.
        		//Double double1 = Double.valueOf(cell.numericCellValue)
        		return (int)(cell.numericCellValue)
        	}

        } else {
        	//String str = new String(cell?.richStringCellValue?.getBytes("UTF-8"))
           // println("Fetching cell records. cell Type = ${cell?.richStringCellValue}")
        	return cell?.richStringCellValue?.string?.replaceAll("\n","<br/>")?: ""
        }
        
        return ""
    }

    public static int getLastDigit(double x) {
        return (int) ( (x - (int) x) * Math.pow(10, 2) );
    }

    private synchronized Resource createHierarchyTillSubCategory(ResourceResolver resourceResolver, String section, String range, String category, String subCategory) {
    	String parentHierarchy = "${basePath}/${PimNodeUtil.generateJcrFriendlyName(section)}/${PimNodeUtil.generateJcrFriendlyName(range)}/${PimNodeUtil.generateJcrFriendlyName(category)}"
    	
    	if (subCategory) {
    		parentHierarchy = "${parentHierarchy}/${PimNodeUtil.generateJcrFriendlyName(subCategory)}"
    	}
    	
        return ResourceUtil.getOrCreateResource(resourceResolver, parentHierarchy, "", "", true)
    }

    /**
     * @param str
     * @return
     */

    private synchronized Node createLeafNode(ResourceResolver resourceResolver, XSSFRow currentRow, Resource subCatResource) {

        String leadProductSKU = fetchCellValue(currentRow.getCell(MAIN_SKU_ID_CELL_NO))
        String identifier = leadProductSKU
        String variantSKU = fetchCellValue(currentRow.getCell(VARIANT_SKU_CELL_NO))
        String colorSKU = fetchCellValue(currentRow.getCell(COLOR_SKU_CELL_NO))
        String commerceType = "product"
        boolean isTaggable = false
        
        /* Create PIM data node for a product under '/etc/commerce/products' hierarchy. */
        Resource leafResource = ResourceUtil.getOrCreateResource(resourceResolver, "${subCatResource.path}/${PimNodeUtil.generateJcrFriendlyName(leadProductSKU)}", ["jcr:primaryType":"nt:unstructured", "sling:resourceType":"commerce/components/product"], "", true)

        println "sku detail "+ leadProductSKU +" -- "+ variantSKU +"--"+ colorSKU

        boolean isVariantAColor = false
        if(!colorSKU && !variantSKU){
             println "color & variant both not found."
             isTaggable = true
        }else {

            if (leadProductSKU.equals(variantSKU) && leadProductSKU.equals(colorSKU)) {
                // Item is main Product. Product
                println "item is main product"
                isTaggable = true
            } else if (leadProductSKU.equals(variantSKU) && !leadProductSKU.equals(colorSKU)) {
                // Item is color variant for the main product. Product -> color
                println "item is color variant of main product"
                leafResource = ResourceUtil.getOrCreateResource(resourceResolver, "${leafResource.path}/${PimNodeUtil.generateJcrFriendlyName(colorSKU)}", "commerce/components/product", "", true)
                identifier = colorSKU
                commerceType = "variant"
                isVariantAColor = true
            } else if (!leadProductSKU.equals(variantSKU) && variantSKU.equals(colorSKU)) {
                // Item is a variant of main Product. Product -> variant
                println "item is variant of main product"
                leafResource = ResourceUtil.getOrCreateResource(resourceResolver, "${leafResource.path}/${PimNodeUtil.generateJcrFriendlyName(variantSKU)}", "commerce/components/product", "", true)
                identifier = variantSKU
                commerceType = "variant"
            } else if (!leadProductSKU.equals(variantSKU) && !variantSKU.equals(colorSKU)) {
                // Item is color variant of variant. Product -> variant -> color
                println "item is variant of color variant"
                leafResource = ResourceUtil.getOrCreateResource(resourceResolver, "${leafResource.path}/${PimNodeUtil.generateJcrFriendlyName(variantSKU)}/${PimNodeUtil.generateJcrFriendlyName(colorSKU)}", "commerce/components/product", "", true)
                identifier = colorSKU
                commerceType = "variant"
                isVariantAColor = true
            }
        }

        Node node = leafResource.adaptTo(Node)
        node.setProperty("identifier", identifier.trim())
        
        if (isTaggable) {
        	node.addMixin("cq:Taggable")
        }
        saveCurrentPathTags(resourceResolver, node)
        node.setProperty("cq:commerceType", commerceType)
        node.setProperty("sling:resourceType", "commerce/components/product")
        if(isVariantAColor) {
            // add variantType property and value (colorVariant) to identify
            // color variants for product & variant
            node.setProperty("variantType", "colorVariant")
        }

        return node
    }
    /**
     *
     * @param resourceResolver
     * @param node
     * @param currentRow
     */
    private synchronized void saveLeafNodeProps(ResourceResolver resourceResolver, Node node, XSSFRow currentRow) {
        Node imageNode = null;
        try{
            if(node.hasNode("image")){
                imageNode = node.getNode("image")
            }else{
                imageNode = JcrUtil.createUniqueNode(node, "image", "nt:unstructured", resourceResolver.adaptTo(Session))
            }
        }catch(PathNotFoundException ex){

        }catch(Exception ex){
            ex.printStackTrace()
        }

        if(imageNode != null) {

            title = fetchCellValue(currentRow.getCell(TITLE_CELL_NO))

            node.setProperty(TITLE_JCR_PROPERTY, title)
            node.setProperty(TITLE_JCR_TITLE_PROPERTY, title)
            node.setProperty(SUBTITLE_JCR_PROPERTY, fetchCellValue(currentRow.getCell(SUB_TITLE_CELL_NO)))
            node.setProperty("enableEcomm", fetchCellValue(currentRow.getCell(ENABLE_ECOMM)))

            String productBasePath = getProductBasePath(resourceResolver, node)
            imageNode.setProperty("fileReference", "${productBasePath}/cover.png")

            imageNode.setProperty("sling:resourceType", "commerce/components/product/image")

            String[] productImages = fetchCellValue(currentRow.getCell(PRODUCT_IMAGES)).tokenize(";").collect {
                "${productBasePath}/${it}"
            }

            node.setProperty("productImages", productImages)

            /* saveCurrentPathTags(node) */
            //fetchCellValue(currentRow.getCell(MAIN_SKU_PRICE_CELL_NO))
            /*String priceInfo = "0.0"

            Double price = new Double(Double.parseDouble(priceInfo))
            node.setProperty("price", price)
            println "price is "+price*/

            node.setProperty("color", fetchCellValue(currentRow.getCell(COLOR_IMAGE)))
            node.setProperty("colorImg", "${productBasePath}/color.png")

            node.setProperty("giftWrap", fetchCellValue(currentRow.getCell(GIFT_WRAP)))
            node.setProperty("productManual", "${productBasePath}/product-manual.pdf")
            node.setProperty("productBrochure", "${productBasePath}/product-brochure.pdf")
            String[] recommendedProducts = fetchCellValue(currentRow.getCell(PRODUCT_RECOMMENDATION)).tokenize(";")
            node.setProperty("productRecommendation", recommendedProducts)
        }
    }
    
    /**
    * This method returns the equivalent image path for a product. The logic simply prepends "/content/dam/havells"
    * followed by <section>/<range>/<category>/<subcategory>/<title>/<Product-SKUId>
    */
    private synchronized String getProductBasePath(ResourceResolver resourceResolver, Node node) {
    	String productBasePath = "${generateContentDAMBasePath(resourceResolver, node)}/${PimNodeUtil.generateJcrFriendlyName(title)}"
        StringUtils.removeEnd(productBasePath, "-")
    	if (node.hasProperty('identifier')) {
    		productBasePath = "${productBasePath}/${node.getProperty('identifier').getString()}"
    	} else {
    		throw new IllegalArgumentException("Product Identifier property missing for ${node.path}")
    	}
    	return productBasePath.replaceAll(" ", "")
    }
    
    
    private String generateContentDAMBasePath(ResourceResolver resourceResolver, Node node) {
        
        Resource subCatResource = getSubCategoryResource(resourceResolver.getResource(node.path))
        String topLevelPath = subCatResource.path.substring(basePath.size() + 1)
        
        return "${contentDamBasePath}/${topLevelPath}"
    }
    
    private Resource getSubCategoryResource(Resource resource) {
    	Resource subCategoryresource = resource
    	if (resource.isResourceType("commerce/components/product")) {
        	subCategoryresource = getSubCategoryResource(resource.parent)
        }
        return subCategoryresource
    }
    
    /**
    * Creates Product sub-category tag and applies to each PIM product node.
    */
    /**
     *
     * @param resolver
     * @param node
     */
    private void saveCurrentPathTags(ResourceResolver resolver, Node node) {
    	if (section != null && !section.equals("")) {
    		Tag productTag = tagManager.createTag("havells:" + PimNodeUtil.generateJcrFriendlyName(section), section, "Section Tag")
    		
    		if (range != null && !range.equals("")) {
    			productTag = tagManager.createTag(productTag.path + "/" + PimNodeUtil.generateJcrFriendlyName(range), range, "Range Tag")
    			
    			if (category != null && !category.equals("")) {
    				productTag = tagManager.createTag(productTag.path + "/" + PimNodeUtil.generateJcrFriendlyName(category), category, "Category Tag")
    				
    				if (subCategory != null && !subCategory.equals("")) {
    					productTag = tagManager.createTag(productTag.path + "/" + PimNodeUtil.generateJcrFriendlyName(subCategory), subCategory, "Sub Category Tag")
    				}
    			}
    		}
	
	        //Tag[] tagList = [sectionTag, rangeTag, categoryTag, subCatTag]
	        tagManager.setTags(resolver.getResource(node.path), productTag)
    	}        
    }

    /**
     *
     * @param resolver
     * @param node
     * @param row
     * @return
     */
    private synchronized int addQuickFeatures(ResourceResolver resolver , Node node, XSSFRow row) {
        int excelCounter = FEATURES_START_CELL_NO
        Node quickFeatureNode = JcrUtil.createPath("${node.path}/otherInfo/quickFeatures", "nt:unstructured", resolver.adaptTo(Session))

        (0..quickFeatureCount - 1).each { currentCount ->
            String heading = fetchCellValue(row.getCell(excelCounter++))
            String cellDescription = fetchCellValue(row.getCell(excelCounter++));
            if(heading != null && !StringUtils.isEmpty(heading.trim()) ||
                    cellDescription != null && !StringUtils.isEmpty(cellDescription.trim())) {
                quickFeatureNode.setProperty("heading${currentCount + 1}", heading)
                quickFeatureNode.setProperty("description${currentCount + 1}", cellDescription)
            }
        }
        quickFeatureNode.setProperty("description${quickFeatureCount + 1}", fetchCellValue(row.getCell(excelCounter++)))
        return excelCounter
    }

    /**
     *
     * @param resolver
     * @param node
     * @param row
     * @param excelCounter
     * @return
     */
    private synchronized int addDetailFeatures(ResourceResolver resolver, Node node, XSSFRow row, int excelCounter) {
        Node detailFeatureNode = JcrUtil.createPath("${node.path}/otherInfo/detailFeatures", "nt:unstructured", resolver.adaptTo(Session))
        String productBasePath = getProductBasePath(resolver, node)

        (0..detailFeatureCount - 1).each { currentCount ->
            String heading = fetchCellValue(row.getCell(excelCounter++))
            String cellDescription = fetchCellValue(row.getCell(excelCounter++));
            if(heading != null && !StringUtils.isEmpty(heading.trim()) ||
                    cellDescription != null && !StringUtils.isEmpty(cellDescription.trim())) {
                detailFeatureNode.setProperty("heading${currentCount + 1}", heading)
                detailFeatureNode.setProperty("description${currentCount + 1}", cellDescription)
            }
            String imagePath = PimNodeUtil.generateJcrFriendlyName(fetchCellValue(row.getCell(excelCounter++)))

            if(imagePath != null && !StringUtils.isEmpty(imagePath.trim())) {
                imagePath = imagePath.lastIndexOf(".png") > 0 ? imagePath : "${imagePath}.png";
                imagePath = PimNodeUtil.getCorrectString(imagePath)
                detailFeatureNode.setProperty("image${currentCount + 1}", imagePath.equals("") ? imagePath : "${productBasePath}/${imagePath}")
            }
        }
        return excelCounter
    }

    /**
     *
     * @param resolver
     * @param node
     * @param row
     * @param excelCounter
     * @return
     */
    private synchronized int addOptionalFeatures(ResourceResolver resolver, Node node, XSSFRow row, int excelCounter) {
        Node optionalFeatureNode = JcrUtil.createPath("${node.path}/otherInfo/optionalFeatures", "nt:unstructured", resolver.adaptTo(Session))
        String productBasePath = getProductBasePath(resolver, node)
        (0..optionalFeatureCount - 1).each { currentCount ->
            String heading = fetchCellValue(row.getCell(excelCounter++))
            String cellDescription = fetchCellValue(row.getCell(excelCounter++));
            if(heading != null && !StringUtils.isEmpty(heading.trim()) ||
                    cellDescription != null && !StringUtils.isEmpty(cellDescription.trim())) {
                optionalFeatureNode.setProperty("heading${currentCount + 1}", heading)
                optionalFeatureNode.setProperty("description${currentCount + 1}", cellDescription)
            }
            String imagePath = PimNodeUtil.generateJcrFriendlyName(fetchCellValue(row.getCell(excelCounter++)))
            if(imagePath != null && !StringUtils.isEmpty(imagePath)) {
                imagePath = imagePath.lastIndexOf(".png") > 0 ? imagePath : "${imagePath}.png";
                imagePath = PimNodeUtil.getCorrectString(imagePath)
                optionalFeatureNode.setProperty("image${currentCount + 1}", imagePath.equals("") ? imagePath : "${productBasePath}/${imagePath}")
            }
        }
        return excelCounter
    }

    /**
     *
     * @param resolver
     * @param node
     * @param row
     * @param excelCounter
     * @return
     */
    private synchronized int addTechnicalDescription(ResourceResolver resolver, Node node, XSSFRow row, int excelCounter) {

        Session session = resolver.adaptTo(Session)

        Node techFeaturesNode = JcrUtil.createPath("${node.path}/otherInfo/technicalSpec", "nt:unstructured", session)
        Tag techSpecMainTag = tagManager.createTag("havells:product_tech_specification", "Technical Specification", "Technical Specification")

        (0..techHeadingCount - 1).each { currentCount ->

            synchronized(this) {
                String techSpecTitle = fetchCellValue(row.getCell(excelCounter++))
                String techSpecHeading = fetchCellValue(row.getCell(excelCounter++))

                if (techSpecTitle) {
                    println ("techSpecTitle  " + techSpecTitle)

                    Tag techSpecTitleTag = tagManager.createTag("${techSpecMainTag.path}/" +
                            PimNodeUtil.generateJcrFriendlyName(techSpecTitle),
                            PimNodeUtil.getCorrectString(techSpecTitle),
                            PimNodeUtil.getCorrectString(techSpecTitle) + " Tag")

                    Tag techSpecHeadingTag = tagManager.createTag("${techSpecTitleTag.path}/" + PimNodeUtil.generateJcrFriendlyName(techSpecHeading), techSpecHeading, "")
                    session.save()
                    try {
                        Node headingNode;
                        if (techFeaturesNode.hasNode("heading${currentCount + 1}")) {
                            headingNode = techFeaturesNode.getNode("heading${currentCount + 1}")
                        } else {
                            headingNode = JcrUtil.createUniqueNode(techFeaturesNode, "heading${currentCount + 1}", "nt:unstructured", session)
                        }
                        session.save()
                        if (headingNode != null) {
                            tagManager.setTags(resolver.getResource(headingNode.path), techSpecHeadingTag)
                            headingNode.setProperty("key", "havells:product_tech_specification/" + PimNodeUtil.generateJcrFriendlyName(techSpecTitle) + "/" + PimNodeUtil.generateJcrFriendlyName(techSpecHeading))
                            headingNode.setProperty("value", fetchCellValue(row.getCell(excelCounter++)) ?: "NA")
                        }
                    } catch (Exception ex) {
                        log.error ("issue occurs in created technical specification ")
                        ex.printStackTrace(System.out)
                    }
                } else {
                    excelCounter++
                }
                session.save()
            }
        }
        String productBasePath = getProductBasePath(resolver, node)
        String techSpecsImages = fetchCellValue(row.getCell(excelCounter++))

        String[] productImages = techSpecsImages.tokenize(";").collect { "${productBasePath}/${it}" }
        techFeaturesNode.setProperty("images", productImages)
        session.save()
        return excelCounter
    }

    /**
     *
     * @param resolver
     * @param node
     * @param row
     * @param excelCounter
     * @return
     */
    private synchronized int addAccessoriesFeatures(ResourceResolver resolver,Node node, XSSFRow row, int excelCounter) {

        Node accessoryHeadingNode = JcrUtil.createPath("${node.path}/otherInfo/accessory", "nt:unstructured", resolver.adaptTo(Session))
        String productBasePath = getProductBasePath(resolver, node)

        (0..accessoryHeadingCount - 1).each { currentCount ->
            String heading = fetchCellValue(row.getCell(excelCounter++))
            String cellDescription = fetchCellValue(row.getCell(excelCounter++));
            if(heading != null && !StringUtils.isEmpty(heading.trim()) ||
                    cellDescription != null && !StringUtils.isEmpty(cellDescription.trim())) {
                accessoryHeadingNode.setProperty("heading${currentCount + 1}", heading)
                accessoryHeadingNode.setProperty("description${currentCount + 1}", cellDescription)
            }
            String imagePath = fetchCellValue(row.getCell(excelCounter++))
            if(imagePath != null && !StringUtils.isEmpty(imagePath)) {
                imagePath = imagePath.lastIndexOf(".png") > 0 ? imagePath : "${imagePath}.png";
                accessoryHeadingNode.setProperty("image${currentCount + 1}", imagePath.equals("") ? imagePath : "${productBasePath}/${imagePath}")
            }
        }
        return excelCounter
    }

}
