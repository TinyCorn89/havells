package com.havells.core.model.product;

 public interface ProductConstant {

    String DEFAULT_TAGS_PATH = "/etc/tags/havells";

    String OTHER_INFO = "otherInfo/";

    String IMAGE = "image";
    String VARIANT = "variant";

    String QUICK_FEATURES = "quickFeatures";
    String DETAIL_FEATURES = "detailFeatures";
    String ACCESSORY_FEATURES = "accessory";
    String TECHNICAL_DRAWING = "technicalDrawing";
    String TECHNICAL_SPECIFICATION = "technicalSpec";

    String OPTIONAL_FEATURES = "optionalFeatures";

    String PRODUCT_PATH = "/jcr:content/par/product";

    int MAX_QUICK_FEATURE_PROPERTIES = 5;
    int MAX_DETAIL_FEATURE_PROPERTIES = 25;
    int MAX_ACCESSARY_PROPERTIES = 20;

    String PRODUCT_IMAGE_RENDITION = "/jcr:content/renditions/";

    String DEFAULT_IMAGE_PATH = "/etc/clientlibs/havells/image/default_img.png";

     String HAVELLS_PRODUCTS_PATH = "/etc/commerce/products/havells";
     String HAVELLS_PRODUCTS_RESOURCE_TYPE = "commerce/components/product";

     /**
      * Custom Product properties.
      */
     String SKU_ID = "identifier";
     String FAQS_PROP = "faq";
     String PRODUCT_PRICE_PROP = "price";
     String PRODUCT_SELLING_PRICE_PROP = "sellingPrice";
     String HEADING_PROP = "heading";
     String DESCRIPTION_PROP = "description";
     String IMAGE_PROP = "image";
     String WARRANTY_PROP = "description4";

     String PRODUCT_SUB_TITLE = "subTitle";

     String PRODUCT_PAGE_PATH = "pagePath";

     String JCR_TITLE_PROP = "jcr:title";

     String PRODUCT_RECOMMEND_PROP = "productRecommendation";

     String PRODUCT_DATA = "productData";

     String PRODUCT_QTY_PROP = "productQty";
     String MIN_PRODUCT_QTY_PROP ="minProductQty";

     String PRODUCT_MANUAL_DOC_PROP = "productManual";
     String PRODUCT_BROCHURE_PROP = "productBrochure";

     String TECH_SPECS_IMAGES_PROP = "images" ;
     String TECH_DRAWING_IMAGES_PROP = "drawingImages";
     String COVER_IMAGE_PROP = "fileReference";
     String  COLOR_IMAGE_PROP = "colorImg";
     String PRODUCT_IMAGES_PROP = "productImages";

     /** DAM Images renditions ***/
     String THUMBNAIL_RENDITION_342X325 = "cq5dam.web.342.325.png";
     String THUMBNAIL_RENDITION_100X95 = "cq5dam.web.100.95.png";
     String THUMBNAIL_RENDITION_150X143 = "cq5dam.web.150.143.png";
     String THUMBNAIL_RENDITION_75X71 = "cq5dam.web.75.71.png";
     String TECHSPEC_UNWANTED_TEXT_1 = "NA";
     String TECHSPEC_UNWANTED_TEXT_2 = "N/A";

     interface CurrencyConstant{
         String MRP = "MRP ";
         String RUPEE = "Rs. ";
         String DOLLAR = "$ ";
         String MONEY_DELIMETER = "/-";
     }


 }
