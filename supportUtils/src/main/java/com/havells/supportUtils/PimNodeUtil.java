package com.havells.supportUtils;

import com.day.cq.commons.jcr.JcrUtil;
import org.apache.commons.lang.StringUtils;


public class PimNodeUtil {

    private final static String incorrect_basic_feature1 = "basic feature";
    private final static String incorrect_basic_feature2 = "basic featrures";
    private final static String incorrect_technical_feature = "tchnical features";

    private final static String incorrect_color_png = "colour.png";

    public static String getCorrectString(String str){
        str = str.trim();
        if (str.equalsIgnoreCase(incorrect_basic_feature1) || str.equalsIgnoreCase(incorrect_basic_feature2)) {
            str = "Basic Features";
        } else if (str.equalsIgnoreCase(incorrect_technical_feature)) {
            str = "Technical Features";
        } else if (str.equalsIgnoreCase(incorrect_color_png)) {
            str = "color.png";
        }
        else if (str.equalsIgnoreCase("colour")) {
            str = "color";
        }
        return str;
    }
    public static synchronized String generateJcrFriendlyName(String str) {

        str = getCorrectString(str);
        str = str.replaceAll("\\s+", "-");
        str = JcrUtil.createValidName(str.toLowerCase());
        for (String chars : new String[]{"-_-", "-_","_-", "_", "\\--+", " - ", "-&-"}){
            str = str.replaceAll(chars, "-").intern();
        }
        StringUtils.removeEnd(str, "-");
        StringUtils.removeEnd(str, "_");

        return str.intern();
    }

}
