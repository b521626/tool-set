package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * The type Md 5 util.
 */
@Slf4j
public class MD5Util {
	
	//用于加密的字符
	private static final char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Md 5 string.
     *
     * @param pwd the pwd
     * @return the string
     */
    public final static String md5(String pwd) {

    	try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();

            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }
            //返回经过加密后的字符串
            return new String(str);

        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Encrypt 16 string.
     *
     * @param encryptStr the encrypt str
     * @return the string
     */
    public static String encrypt16(String encryptStr) {

    	String md5Str = md5(encryptStr);

    	if(md5Str!=null) {
    		 return md5Str.substring(8, 24);
    	}

    	return null;
    }

    /**
     * SHA_256加密
     *
     * @param str the str
     * @return string
     */
    public static String getSHA256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("系统异常:", e);
        } catch (UnsupportedEncodingException e) {
            log.error("系统异常:", e);
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static String sortMap(Map<String,Object> map){
    	
        if (map == null || map.isEmpty()) {
            return null;
        }
        
        Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
        sortMap.putAll(map);
        
        final  StringBuilder sort = new StringBuilder();
        
        //声明为
        sortMap.forEach((k,v)->{
            if(!StringUtils.isEmpty(v+"")){
                sort.append(k+"="+v+"&");
            }
        });
        
        String sortStr = sort.deleteCharAt(sort.length() - 1).toString();
        return sortStr;
    }


    static class MapKeyComparator implements Comparator<String> {
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    public static String sign(String signStr) {
        return DigestUtils.md5DigestAsHex(signStr.getBytes()).toUpperCase();
    }

}