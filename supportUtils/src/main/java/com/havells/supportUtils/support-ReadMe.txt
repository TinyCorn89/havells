
CORRECT DAM IMAGES BEFORE UPLOADING IN LOCALHOST & CREATING RENDITIONS :
------------------------------------------------------------------------
To correct image paths and directories in your hard disk before uploading in DAM and creating renditions.
This will remove all special chars & extra spaces. Every spacial chars will be replaced with "-"

http://localhost:4502/bin/search/correctImagePaths?imageDirPath=/Users/jitendra/productImages

--------------------------------------------------------------------------------------------------------------
UPLOADING PRODUCT DATA & TAGS THROUGH XLSX FILE:
-----------------------------------------------
To upload PIM XLSX file and create product data & tags on the server.
Technical specs columns are inconsistent across the product files so open the file and pass
the column numbers according to excelsheet.

http://localhost:4502/bin/importProducts?filepath=/Users/jitendra/projectCode/Havells/Havells-PIM-data-entry-Pump.xlsx&techSpecsColumnNos=50
--------------------------------------------------------------------------------------------------------------

MAPPING PRODUCT IMAGES WITH THEIR DOM :
--------------------------------------
Mapping product images (cover.png, color.png, productImages, techspecs etc) to actual respective DOM.
We search cover.png file and get SKU of that image and then search this SKU in product data. If sku exists in
PIM Data, replace all product images with searched cover.png DAM path.

http://localhost:4502/bin/map/productImages?searchFileName=cover.png&jcrDamPath=/content/dam/havells/consumer/appliances
--------------------------------------------------------------------------------------------------------------
PRODUCTS REPORT :
-------------------------------
To validate Product properties (cover.png, color.png, productImages, techspecs etc), i.e. to check if the Images,Pdfs
etc exists in the repository, ProductPropertiesValidatorServlet is to be used.
It required an attribute 'directory', which specifies where to save the generated Excel Report.
The following example will generate the excel under /home/shashi directory.

http://localhost:4502/bin/productReports.html?directory=/home/shashi
--------------------------------------------------------------------------------------------------------------
DEALER LOCATOR DATA IMPORTER
----------------------------------------------------------------
below url help you to imported dealer locator data. This API stores two information.
1. Dealers product information
2. Dealers data with their state, city, contact no and address.

http://localhost:4502/bin/importDealer?filePath=<FILE_PATH INCLUDING FILE NAME and must be .XLSX>
--------------------------------------------------------------------------------------------------------------
PRODUCT FILTER DATA IMPORTER
----------------------------------------------------------------
Product filter is for importing all the filters and create tags(/etc/tags/havells/filters) accordingly.
http://hostname:port/bin/importProductFilters?filepath=/Users/jitendra/Downloads/Geyser_Filters.xlsx
--------------------------------------------------------------------------------------------------------------



