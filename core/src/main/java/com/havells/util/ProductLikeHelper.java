package com.havells.util;

import com.adobe.cq.commerce.api.Product;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shashi
 */
public class ProductLikeHelper {
	private static String productData;
	private static Product product;

	public static Product getProduct() {
		return product;
	}

	public static void setProduct(Product productValue) {
		product = productValue;
	}

	public static String getProductData() {
		return productData;
	}

	public static void setProductData(String productDataValue) {
		productData = productDataValue;
	}

	public static String getVideoId(String url) {
		String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(url);

		if(matcher.find()){
			return matcher.group();
		}
		return null;
	}
}
