package com.zhaihuilin.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.*;

/**
 * MD5 签名校验
 * Created by zhaihuilin on 2017/11/16  15:52.
 */
public class MD5Encrypt {

    /**
     * 对字符串进行MD5签名
     * @param text 明文
     * @param charset 编码
     * @return 密文
     */
    public static String md5(String text, String charset) {
        return DigestUtils.md5Hex(getContentBytes(text,charset));
    }

    /**
     * 生成签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * 不排序把数组所有元素按照“参数1={参数1}&参数2={参数2}&……&参数n={参数n}”的模式用“&”字符拼接成字符串
     * @param params 参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        return createLinkString(params, false);
    }

    /**
     * 把数组所有元素排序，并按照“参数1={参数1}&参数2={参数2}&……&参数n={参数n}”的模式用“&”字符拼接成字符串
     *
     * @param params 参与字符拼接的参数组
     * @param isSort 是否排序
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean isSort) {
        List<String> keys = new ArrayList<String>(params.keySet());
        if (isSort) {
            Collections.sort(keys);
        }
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 把数组所有元素参数值相加
     * @param params 参与字符拼接的参数组
     * @param isSort 是否排序
     * @return 拼接后字符串
     */
    public static String createLinkStringSum(Map<String, String> params,boolean isSort) {
        List<String> keys = new ArrayList<String>(params.keySet());
        if(isSort){
            Collections.sort(keys);
        }
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            prestr = prestr + value;
        }
        return prestr;
    }

    /**
     * 除去数组中的空值
     * @param sArray 签名参数组
     * @return 去掉空值参数后的新签名参数组
     */
    public static Map<String, String> paraFilterNone(Map<String, String> sArray) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }
    /**
     * equalsIgnoreCase  : 比较的时候 忽略大小写
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")
                    || key.equalsIgnoreCase("signMsg")|| key.equalsIgnoreCase("signType")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @param key 要签名的key
     * @return 签名结果字符串
     */
    public static String buildRequestMysign(Map<String, String> sPara,String key, String input_charset) {
        sPara = paraFilter(sPara);   //除去数组中的空值和签名参数
        String prestr = createLinkString(sPara,true); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        System.out.println("签名字符串：" + prestr);
        String mysign = "";
        mysign = MD5Encrypt.sign(prestr, key, input_charset);
        System.out.println("签名：" + mysign);
        return mysign;
    }

    /**
     * 微信签名
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public String createSign(SortedMap<String, String> params,String key, String input_charset) {
        StringBuffer sb = new StringBuffer();
        Set es = params.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        String sign = MD5Encrypt.sign(sb.toString(), key, input_charset)
                .toUpperCase();
        return sign;

    }

    /**
     * 验证签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if(mysign.equals(sign)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}
