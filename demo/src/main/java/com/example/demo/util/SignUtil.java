package com.example.demo.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SignUtil {

	public static final String POST_METHOD = "POST";

	public static final String GET_METHOD = "GET";

	public static final String NEW_POST_METHOD = "NEW_POST";
	
	public static final String CHAR_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	private static Random random = null;
	
	static {
		try {
			random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
//			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public static String getNonceStr(int length) {
		String str = CHAR_STR;
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(62);
			buf.append(str.charAt(num));
		} 
		return buf.toString();
	}

	/**
	 * 请求参数签名验证
	 *
	 * @param secret
	 * @param request
	 * @return localSign
	 * @throws Exception
	 */
	public static String getLocalSignByGet(String secret, HttpServletRequest request) throws Exception {
		TreeMap<String, String> params = new TreeMap<String, String>();

		Enumeration<String> enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paramName = enu.nextElement().trim();
			params.put(paramName, request.getParameter(paramName));
		}
		String localSign = MD5Util.md5(getSortedStr(params) + "&key=" + secret).toUpperCase();
		return localSign;
	}


	/**
	 * 获取签名
	 *
	 * @param requestParams 请求参数，不包含sign
	 * @param key           签名秘钥
	 * @return sign str
	 */
	public static String getSignStrV2(String requestParams, String key) {

		String queryStr = getSortedStr(JSONObject.parseObject(requestParams, new TypeReference<Map<String, String>>() {
		},new Feature[0]));
		//加上key
		String unsignStr = queryStr + "&key=" + key;
		log.debug("加密前字符串:" + unsignStr);
		return MD5Util.md5(unsignStr).toUpperCase();
	}


	/**
	 * Map排序去除空值并拼接为query string
	 *
	 * @param unSortedMap
	 * @return
	 */
	public static String getSortedStr(Map<String, String> unSortedMap) {
		String sortedStr = unSortedMap
				.entrySet()
				.stream()
				.filter(entry -> !StringUtils.isEmpty(entry.getValue()))
				.sorted(Map.Entry.comparingByKey())
				.map(entry -> {
					return entry.getKey() + "=" + entry.getValue();
				})
				.collect(Collectors.joining("&"));
		return sortedStr;
	}

	public static boolean verifyTimestamp(String timestamp){
		try{
			long time = Long.parseLong(timestamp) * 1000;
			Calendar starTime = Calendar.getInstance();
			starTime.setTimeInMillis(time);
			starTime.add(Calendar.MINUTE, 10);
			Calendar nowTime = Calendar.getInstance();
			if (starTime.after(nowTime)){
				return true;
			}else {
				return false;
			}
		}catch (Exception e){
			return false;
		}

	}
	/**
	 * 获取签名
	 *
	 * @param unSortedMap   参数map
	 * @param key           签名秘钥
	 * @return sign str
	 */
	public static String createSign(Map<String, Object> unSortedMap, String key) {
		String sign = getSortedStr2(unSortedMap);
		//加上key
		String unsignStr = sign + "&key=" + key;
		//log.info("加密前字符串:" + unsignStr);
		return MD5Util.md5(unsignStr).toUpperCase();
	}

	/**
	 * Map排序去除空值并拼接为query string
	 *
	 * @param unSortedMap
	 * @return
	 */
	public static String getSortedStr2(Map<String, Object> unSortedMap) {
		String sortedStr = unSortedMap
				.entrySet()
				.stream()
				.filter(entry -> !StringUtils.isEmpty(entry.getValue()))
				.sorted(Map.Entry.comparingByKey())
				.map(entry -> {
					return entry.getKey() + "=" + entry.getValue();
				})
				.collect(Collectors.joining("&"));
		return sortedStr;
	}
	
}
