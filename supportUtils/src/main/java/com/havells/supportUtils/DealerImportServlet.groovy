package com.havells.servlet

import com.day.cq.commons.jcr.JcrUtil
import groovy.transform.CompileStatic
import org.apache.felix.scr.annotations.Component
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ResourceUtil
import org.apache.sling.api.servlets.SlingAllMethodsServlet
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.jcr.Node
import javax.jcr.Property
import javax.jcr.Session
import javax.jcr.Value

@groovy.util.logging.Slf4j
@org.apache.felix.scr.annotations.sling.SlingServlet(
        paths = ["/bin/importDealer"],
        generateComponent = false
)
@org.apache.felix.scr.annotations.Component(enabled = true, immediate = true, metatype = false)
class DealerImportServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DealerImportServlet.class);

    def basePath = "/etc/havells"
    final String DEALER_CHANNEL_TYPE_1 = "dealer"
    final String DEALER_CHANNEL_TYPE_2 = "galaxy"
    final String DEALER_CHANNEL_TYPE_3 = "distributor"

    def resourcePath = "havells/resource/dealerLocator"
    org.apache.poi.ss.usermodel.FormulaEvaluator evaluator

    private javax.jcr.Session jcrSession

    private org.apache.poi.xssf.usermodel.XSSFWorkbook workbook

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        ResourceResolver resolver = request.getResourceResolver()
        FileInputStream fileIO
        File file
        try {
            String filePath = request.getParameter("filePath")
            if (filePath) {
                jcrSession = resolver.adaptTo(Session)
                file = new File(filePath)
                fileIO = new FileInputStream(file);
                workbook = new XSSFWorkbook(fileIO)
                XSSFSheet sheet = workbook.getSheetAt(0)
                LOG.info("sheet accepted" + sheet)
                createDealerNode(sheet, resolver, "dealersData")
                response.getWriter().write("all dealers information imported")
            } else {
                response.getWriter().write("Provide product path with file name..")
            }
        }
        catch(FileNotFoundException ex){
            response.getWriter().write("provided file does not exist")
        }
        catch (Exception ex) {
            ex.printStackTrace()
            response.getWriter().write("Something went wrong. check the log file")
        } finally {
            if (resolver.isLive()) {
                resolver.close()
            }
            if (fileIO != null) {
                fileIO.close()
            }
            System.gc()
        }
    }
    /**
     *
     * @param galaxySheet
     * @param resolver
     * @param rootNode
     */
    private void createDealerNode(XSSFSheet xssfSheet, ResourceResolver resolver, String rootNodeName) throws Exception{

        Resource dealersProductResource = createNodeHierarchy("dealerProducts", resolver, basePath, "nt:unstructured")

        (xssfSheet.firstRowNum + 1..xssfSheet.lastRowNum).each {
            int xlsColumnCounter = 0
            XSSFRow topRow = xssfSheet.getRow(it)

            def state = fetchCellValue(topRow.getCell(9)), city = fetchCellValue(topRow.getCell(6)), dealerCode = fetchCellValue(topRow.getCell(2))

            def products = fetchCellValue(topRow.getCell(14)), productCategory = fetchCellValue(topRow.getCell(15))

            createProductData(products, resolver, dealersProductResource.path, products, productCategory, "nt:unstructured")

            LOG.info("data productCategory... " + productCategory)

            Resource galaxyResource = createNodeHierarchy(rootNodeName, resolver, basePath, "nt:unstructured")
            Resource stateResource = createNodeHierarchy(state, resolver, galaxyResource.path, "nt:unstructured")
            Resource cityResource = createNodeHierarchy(city, resolver, stateResource.path, "nt:unstructured")
            Resource dealerResource = createNodeHierarchy(dealerCode, resolver, cityResource.path, "nt:unstructured")

            javax.jcr.Node dealerNode = dealerResource.adaptTo(Node)

            dealerNode.setProperty("so", (long) (fetchCellValue(topRow.getCell(0)) as Double))
            dealerNode.setProperty("branch", fetchCellValue(topRow.getCell(1)))
            dealerNode.setProperty("dealerCode", dealerCode)
            dealerNode.setProperty("dealerName", fetchCellValue(topRow.getCell(3)))

            String tempPostalCode = fetchCellValue(topRow.getCell(7))
            dealerNode.setProperty("postalCode", tempPostalCode ? (long) (tempPostalCode as Double) : "")

            dealerNode.setProperty("address", fetchCellValue(topRow.getCell(4)) + fetchCellValue(topRow.getCell(5)) + "<br>District : " + fetchCellValue(topRow.getCell(8)) +
                    "<br> City : "+ city + "<br> Postal Code : "+ tempPostalCode)
            dealerNode.setProperty("city", city)

            dealerNode.setProperty("state", state)

            println("telephone-1 " + fetchCellValue(topRow.getCell(10)))
            println("telephone-2 " + fetchCellValue(topRow.getCell(11)))

            dealerNode.setProperty("telephone1", fetchCellValue(topRow.getCell(10)))
            dealerNode.setProperty("telephone2", fetchCellValue(topRow.getCell(11)))
            dealerNode.setProperty("email", fetchCellValue(topRow.getCell(12)))

            String channelType = fetchCellValue(topRow.getCell(13))

            dealerNode.setProperty("channelType", channelType)
            dealerNode.setProperty("sling:resourceType", "havells/dealer/" + (channelType.toLowerCase()).replaceAll(" ","_"))

            if (dealerNode.hasProperty("products")) {
                javax.jcr.Property property = dealerNode.getProperty("products")
                Map<String, String> productMap = [:]
                for (Value value : property.getValues()) {
                    productMap.put(value.getString(), value.getString())
                }
                if (products.contains(',')) {
                    products.split(',').each { String token ->
                        productMap.put(token, token)
                    }
                }
                List productList = new ArrayList()
                productMap.each { key, value ->
                    productList.add(key)
                }
                dealerNode.setProperty("products", (String[]) productList.toArray())
            } else {
                if (products.contains(',')) {
                    dealerNode.setProperty("products", products.split(','))
                } else {
                    String[] firstStr = new String[1]
                    firstStr[0] = products
                    dealerNode.setProperty("products", firstStr)
                }
            }

            jcrSession?.save()
        }
        jcrSession?.save()
    }

    private void createProductData(String nodeName, ResourceResolver resolver, String basePath, String products, String productCategory, type) {
        if(nodeName.contains(',')){
            nodeName.split(',').each { String token ->
                Resource productsResource = ResourceUtil.getOrCreateResource(resolver, "${basePath}/${encodeName(token.replaceAll(" ", ""))}", type, "", true)
                Node node = productsResource.adaptTo(Node)
                node.setProperty("productName", token)
                node.setProperty("productCategory", productCategory)
            }
        }else{
            Resource productsResource = ResourceUtil.getOrCreateResource(resolver, "${basePath}/${encodeName(nodeName)}", type, "", true)
            Node node = productsResource.adaptTo(Node)
            node.setProperty("productName", products)
            node.setProperty("productCategory", productCategory)
        }
    }

    private String encodeName(String name) {
        return name.toLowerCase().replace(":", "_").replace(".", " ").replace(" ", "_").replace("/", "_")
    }

    private Resource createNodeHierarchy(String nodeName, ResourceResolver resolver, String basePath, String type) {
        Resource resource = ResourceUtil.getOrCreateResource(resolver, "${basePath}/${encodeName(nodeName)}", type, "", true)
        Node node = resource.adaptTo(Node)
        node.setProperty("niceName", nodeName)
        return resource
    }

    private synchronized String fetchCellValue(XSSFCell cell) {
        if (cell != null) {
            LOG.info("Fetching cell records. cell Type = ${cell.cellType}")
        }

        if (!cell || XSSFCell.CELL_TYPE_BLANK == cell.cellType) {
            return ""
        } else if (cell.cellType == XSSFCell.CELL_TYPE_FORMULA) {
            org.apache.poi.ss.usermodel.FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            return evaluator.evaluate(cell).toString()
        } else if (cell.cellType == XSSFCell.CELL_TYPE_NUMERIC) {
            int absoluteValue = getLastDigit(cell.numericCellValue)
            if (absoluteValue > 0) {
                return (cell.numericCellValue)
            } else {
                return (int) (cell.numericCellValue)
            }

        } else {
            return cell?.richStringCellValue?.string?.replaceAll("\n", "<br/>") ?: ""
        }

        return ""
    }

    public int getLastDigit(double x) {
        return (int) ((x - (int) x) * Math.pow(10, 2));
    }
}
