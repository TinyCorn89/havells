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
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ResourceUtil
import org.apache.sling.api.servlets.SlingAllMethodsServlet

import javax.jcr.Session


@groovy.util.logging.Slf4j
@SlingServlet(
        paths = ["/bin/importProductFilters"],
        generateComponent = false
)
@Component(enabled = true, immediate = true, metatype = false)
public class ProductFilterImportServlet extends SlingAllMethodsServlet {

    String basePath = "/etc/tags/havells/filters"

    private TagManager tagManager

    String section = ""

    String range = ""

    int startingRow = 3

    /**
     *  XLSX FILE CELL NO.
     */
    final int SECTION_CELL_NO = 0
    final int RANGE_CELL_NO = 1

    final String FILTER_VALUES_SEPARATOR = ";"

    XSSFWorkbook workbook

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        tagManager = request.resourceResolver.adaptTo(TagManager)
        ResourceResolver resolver = request.resourceResolver
        Session jcrSession = resolver.adaptTo(Session)
        String filePath = request.getParameter("filepath")
        boolean successFlag = true
        if (filePath == null) {
            response.getWriter().write("provide xlsx file path");
        }
        try {
            File file1 = new File(filePath)
            if (file1 == null) {
                throw new FileNotFoundException()
            }
            FileInputStream file = new FileInputStream(file1);
            workbook = new XSSFWorkbook(file)
            XSSFSheet sheet = workbook.getSheetAt(0); // Get iterator to all the rows in current sheet

            Tag techSpecMainTag = tagManager.createTag("havells:filters", "Product Filters", "Product Filters")
            jcrSession?.save()
            for (int count = (sheet.firstRowNum + startingRow - 1); count < sheet.lastRowNum; count++) {
                try {

                    XSSFRow topRow = sheet.getRow(count)
                    if(topRow) {
                        section = fetchCellValue(topRow.getCell(SECTION_CELL_NO))
                        range = fetchCellValue(topRow.getCell(RANGE_CELL_NO))
                        /*********************------------ D O N E --------------**/
                        if ((section == null || section.equals("")) || (range == null || range.equals(""))) {
                            println "One of section, range OR category is blank. See the source file."
                            break
                        } else {
                            Tag sectionTag = tagManager.createTag("${techSpecMainTag.path}/" + PimNodeUtil.generateJcrFriendlyName(section), section, "")
                            Tag rangeTag = tagManager.createTag("${sectionTag.path}/" + PimNodeUtil.generateJcrFriendlyName(range), range, "")
                            //print "rangeTag  is " + rangeTag
                            synchronized (this) {
                                jcrSession?.save()
                                int cellCount = 6
                                (0..5).each { counter ->
                                    String filter = fetchCellValue(topRow.getCell(cellCount))
                                    print "filter  is " + filter
                                    if (filter) {
                                        Tag filterTag = tagManager.createTag("${rangeTag.path}/" + PimNodeUtil.generateJcrFriendlyName(filter), filter, "", true)
                                        String filterValues = fetchCellValue(topRow.getCell(cellCount + 1))
                                        //println "filter value is " + filter + " \nfilterValues are " + filterValues
                                        String[] values = filterValues.split(FILTER_VALUES_SEPARATOR)
                                        values.each { value ->
                                            tagManager.createTag("${filterTag.path}/" + PimNodeUtil.generateJcrFriendlyName(value), value, "", true)
                                        }
                                        jcrSession?.save()
                                    }
                                    cellCount = cellCount + 2
                                    jcrSession?.save()
                                }
                                jcrSession?.save()
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace()
                    successFlag = false
                }
            }

        }catch (Exception ex){
            ex.printStackTrace()
            successFlag = false
        }
        if(successFlag) {
            response.getWriter().write("Product filter xlsx file imported!!!");
        }else {
            response.getWriter().write("Product importing is failed.");
        }
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
            return cell?.richStringCellValue?.string?.replaceAll("\n","<br/>")?: ""
        }

        return ""
    }
}
