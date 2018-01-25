/*
 * $Id: DIYClientExample.java 1600 2009-06-23 02:38:46Z hyyang $
 * $Revision: 1600 $
 * $Date: 2009-06-23 10:38:46 +0800 (鏄熸湡浜? 23 鍏湀 2009) $
 */

package com.zhaihuilin.util;


import com.zhaihuilin.entity.SmsConstant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Mlink下行请求java示例 <br>
 * <Ul>
 * <Li>本示例定义几种下行请求消息的使用方法</Li>
 * <Li>本示例支持 jre1.4 或以上版本</Li>
 * <Li>本示例不依赖于除jre lib之外的jar包</Li>
 * </Ul>
 * @author hyyang
 * @since 1.4
 */
public class SmsSendUtils {

    private String mtUrl="http://esms.etonenet.com/sms/mt";

    /**
     * 启动测试
     * @param args
     */
    public static void main(String[] args) {
        SmsSendUtils test = new SmsSendUtils();
//        System.out.println(GenerateSmsUtils.generateSms());
        try {
            //测试单条下行
        	HashMap<String, String> pp = (HashMap<String, String>) singleMt("13162797161", SmsConstant.CONTANT);
            //测试相同内容群发
            //test.testMultiMt();
            //测试不同内容群发
            //test.testMultiXMt();
            //测试文件群发
            //test.testBatchMt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 单条下行示例
     */
    public static Map<String, String> singleMt(String phone, String contant) {
        //操作命令、SP编号、SP密码，必填参数
        String command = SmsConstant.COMMAND_REQUEST;
        String spid = SmsConstant.SPID;
        String sppassword = SmsConstant.SPPASSWORD;
        //sp服务代码，可选参数，默认为 00
        String spsc = "00";
        //源号码，可选参数
        String sa = SmsConstant.SA;
        //目标号码，必填参数
        String da = SmsConstant.DA_sufix.concat(phone);
        //下行内容以及编码格式，必填参数
        int dc = 15;
        String sm = encodeHexStr(dc, contant);//下行内容进行Hex编码，此处dc设为15，即使用GBK编码格式

        //组成url字符串
        String smsUrl = SmsConstant.MTURL + "?command=" + command + "&spid=" + spid + "&sppassword=" + sppassword + "&spsc=" + spsc + "&sa=" + sa + "&da=" + da + "&sm=" + sm + "&dc=" + dc;

        //发送http请求，并接收http响应
        String resStr = doGetRequest(smsUrl.toString());
//        System.out.println(resStr);

        //解析响应字符串
        HashMap<String, String> pp = parseResStr(resStr);
        System.out.println(pp.get("command"));
        System.out.println(pp.get("spid"));
        System.out.println(pp.get("mtmsgid"));
        System.out.println(pp.get("mtstat"));
        System.out.println(pp.get("mterrcode"));
        System.out.println("发送成功+手机号码"+da);
        
        return pp;
    }

    /**
     * 相同内容群发示例
     */
    public void testMultiMt() {
        //操作命令、SP编号、SP密码，必填参数
        String command = "MULTI_MT_REQUEST";
        String spid = "****";
        String sppassword = "****";
        //sp服务代码，可选参数，默认为 00
        String spsc = "00";
        //源号码，可选参数
        String sa = "10657109053657";
        //目标号码组，必填参数
        String das = "8613811111111,8613011111111";
        //下行内容以及编码格式，必填参数
        int dc = 15;
        String sm = encodeHexStr(dc, "你好,这是移通网络相同内容群发测试短信");//下行内容进行Hex编码，此处dc设为15，即使用GBK编码格式

        //组成url字符串
        String smsUrl = mtUrl + "?command=" + command + "&spid=" + spid + "&sppassword=" + sppassword + "&spsc=" + spsc + "&sa=" + sa + "&das=" + das + "&sm=" + sm + "&dc=" + dc;

        //发送http请求，并接收http响应
        String resStr = doGetRequest(smsUrl.toString());
        System.out.println(resStr);

        //解析响应字符串
        HashMap pp = parseResStr(resStr);
        System.out.println(pp.get("command"));
        System.out.println(pp.get("spid"));
        System.out.println(pp.get("mtmsgids"));
        System.out.println(pp.get("mtstat"));
        System.out.println(pp.get("mterrcode"));
    }

    /**
     * 不同内容群发示例
     */
    public void testMultiXMt() {
        //操作命令、SP编号、SP密码，必填参数
        String command = "MULTIX_MT_REQUEST";
        String spid = "****";
        String sppassword = "****";
        //sp服务代码，可选参数，默认为 00
        String spsc = "00";
        //源号码，可选参数
        String sa = "10657109053657";
        //编码格式，必填参数
        int dc = 15;
        //下行号码、内容列表
        StringBuffer dasms = new StringBuffer();
        dasms.append("8613811111111/");
        dasms.append(encodeHexStr(dc, "你好,这是移通网络不同内容群发测试短信1"));
        dasms.append(",8613011111111/");
        dasms.append(encodeHexStr(dc, "你好,这是移通网络不同内容群发测试短信2"));

        //组成url字符串
        String smsUrl = mtUrl + "?command=" + command + "&spid=" + spid + "&sppassword=" + sppassword + "&spsc=" + spsc + "&sa=" + sa + "&dc=" + dc;

        //发送http请求，并接收http响应
        String resStr = doPostRequest(smsUrl.toString(), dasms.toString());
        System.out.println(resStr);

        //解析响应字符串
        HashMap pp = parseResStr(resStr);
        System.out.println(pp.get("command"));
        System.out.println(pp.get("spid"));
        System.out.println(pp.get("mtmsgids"));
        System.out.println(pp.get("mtstat"));
        System.out.println(pp.get("mterrcode"));
    }

    /**
     * 文件群发示例
     * @throws UnsupportedEncodingException 
     */
    public void testBatchMt() throws UnsupportedEncodingException {
        //操作命令、SP编号、SP密码，必填参数
        String command = "BATCH_MT_REQUEST";
        String spid = "****";
        String sppassword = "****";
        //sp服务代码，可选参数，默认为 00
        String spsc = "00";
        //任务编号，可选参数
        int taskid = 10001;

        //*/相同内容文件群发
        int bmttype = 1;
        String title = java.net.URLEncoder.encode("abc,测试相同内容文件群发", "GBK");
        int dc = 15;
        String content = encodeHexStr(dc, "你好，这是移通网络相同内容文件群发测试短信");//下行内容进行Hex编码，此处dc设为15，即使用GBK编码格式
        String fileurl = "http://10.8.1.103:9001/testxiangtong.txt";
        //*/

        /*/不同内容文件群发
        int bmttype = 2;
        String title = java.net.URLEncoder.encode("abc,测试不同内容文件群发", "GBK");
        int dc = 15;
        String content = "";//此处content可置空
        String fileurl = "http://10.8.1.103:9001/testbutong.txt";
        //*/

        /*/动态模版文件群发，
        int bmttype = 3;
        String title = java.net.URLEncoder.encode("abc,测试动态模版文件群发", "GBK");
        int dc = 15;
        String content = encodeHexStr(dc, "你好，这是移通网络动态模版群发测试短信，占位测试参数#p_1#，#p_2#");//下行内容进行Hex编码，此处dc设为15，即使用GBK编码格式
        String fileurl = "http://10.8.1.103:9001/testdongtai2.txt";
        //*/

        //其他可选参数
        int priority = 1;//    优先级
        //String attime = "20081028163000";//  定时发送时间，格式yyyyMMddHHmmss
        //String validtime = "20081028173000";//   有效时间，格式yyyyMMddHHmmss

        //组成url字符串
        String smsUrl = mtUrl + "?command=" + command + "&spid=" + spid + "&sppassword=" + sppassword + "&spsc=" + spsc + "&taskid=" + taskid + "&bmttype=" + bmttype + "&title=" + title + "&dc=" + dc + "&content=" + content + "&url=" + fileurl + "&priority=" + priority;

        //发送http请求，并接收http响应
        String resStr = doGetRequest(smsUrl.toString());
        System.out.println(resStr);

        //解析响应字符串
        HashMap pp = parseResStr(resStr);
        System.out.println(pp.get("command"));
        System.out.println(pp.get("spid"));
        System.out.println(pp.get("bmtmsgid"));
        System.out.println(pp.get("mtstat"));
        System.out.println(pp.get("mterrcode"));
    }

    //以下代码为定义的工具方法或变量，可单独在一个工具类中进行定义

    /**
     * 发送http GET请求，并返回http响应字符串
     * 
     * @param urlstr 完整的请求url字符串
     * @return
     */
    public static String doGetRequest(String urlstr) {
        String res = null;
        try {
            URL url = new URL(urlstr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Content-Type", "text/html; charset=GB2312");
            System.setProperty("sun.net.client.defaultConnectTimeout", "5000");//jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", "10000"); //jdk1.4换成这个,读操作超时
            //httpConn.setConnectTimeout(5000);//jdk 1.5换成这个,连接超时
            //httpConn.setReadTimeout(10000);//jdk 1.5换成这个,读操作超时
            httpConn.setDoInput(true);
            int rescode = httpConn.getResponseCode();
            if (rescode == 200) {
                BufferedReader bfw = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                res = bfw.readLine();
            } else {
                res = "Http request error code :" + rescode;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return res;
    }

    /**
     * 发送http POST请求，并返回http响应字符串
     * 
     * @param urlstr 完整的请求url字符串
     * @return
     */
    public static String doPostRequest(String urlstr, String dasm) {
        String res = null;
        try {
            URL url = new URL(urlstr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
            OutputStream outputstream = httpConn.getOutputStream();
            outputstream.write(("dasm=" + dasm).getBytes());
            outputstream.flush();
            
            System.setProperty("sun.net.client.defaultConnectTimeout", "5000");//jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", "10000"); //jdk1.4换成这个,读操作超时
            //httpConn.setConnectTimeout(5000);//jdk 1.5换成这个,连接超时
            //httpConn.setReadTimeout(5000);//jdk 1.5换成这个,读操作超时
            
            int rescode = httpConn.getResponseCode();
            if (rescode == 200) {
                BufferedReader bfw = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                res = bfw.readLine();
            } else {
                res = "Http request error code :" + rescode;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return res;
    }
    
    /**
     * 将 短信下行 请求响应字符串解析到一个HashMap中
     * @param resStr
     * @return
     */
    public static HashMap<String, String> parseResStr(String resStr) {
        HashMap pp = new HashMap();
        try {
            String[] ps = resStr.split("&");
            for (int i = 0; i < ps.length; i++) {
                int ix = ps[i].indexOf("=");
                if (ix != -1) {
                    pp.put(ps[i].substring(0, ix), ps[i].substring(ix + 1));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return pp;
    }


    /** 
     * Hex编码字符组
     */
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * 将普通字符串转换成Hex编码字符串
     * 
     * @param dataCoding 编码格式，15表示GBK编码，8表示UnicodeBigUnmarked编码，0表示ISO8859-1编码
     * @param realStr 普通字符串
     * @return Hex编码字符串
     */
    public static String encodeHexStr(int dataCoding, String realStr) {
        String hexStr = null;

        if (realStr != null) {
            byte[] data = null;
            try {
                if (dataCoding == 15) {
                    data = realStr.getBytes("GBK");
                } else if ((dataCoding & 0x0C) == 0x08) {
                    data = realStr.getBytes("UnicodeBigUnmarked");
                } else {
                    data = realStr.getBytes("ISO8859-1");
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.toString());
            }

            if (data != null) {
                int len = data.length;
                char[] out = new char[len << 1];
                // two characters form the hex value.
                for (int i = 0, j = 0; i < len; i++) {
                    out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
                    out[j++] = DIGITS[0x0F & data[i]];
                }
                hexStr = new String(out);
            }
        }
        return hexStr;
    }
    
    /**
     * 将Hex编码字符串还原成普通字符串
     * 
     * @param dataCoding 反编码格式，15表示GBK编码，8表示UnicodeBigUnmarked编码，0表示ISO8859-1编码
     * @param hexStr Hex编码字符串
     * @return 普通字符串
     */
    public static String decodeHexStr(int dataCoding, String hexStr) {
        String realStr = null;

        if (hexStr != null) {
            char[] data = hexStr.toCharArray();

            int len = data.length;

            if ((len & 0x01) != 0) {
                throw new RuntimeException("Odd number of characters.");
            }

            byte[] out = new byte[len >> 1];

            // two characters form the hex value.
            for (int i = 0, j = 0; j < len; i++) {
                int f = Character.digit(data[j], 16) << 4;
                if(f==-1){
                    throw new RuntimeException("Illegal hexadecimal charcter " + data[j] + " at index " + j);
                }
                j++;
                f = f | Character.digit(data[j], 16);
                if(f==-1){
                    throw new RuntimeException("Illegal hexadecimal charcter " + data[j] + " at index " + j);
                }
                j++;
                out[i] = (byte) (f & 0xFF);
            }
            try {
                if (dataCoding == 15) {
                    realStr = new String(out, "GBK");
                } else if ((dataCoding & 0x0C) == 0x08) {
                    realStr = new String(out, "UnicodeBigUnmarked");
                } else {
                    realStr = new String(out, "ISO8859-1");
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.toString());
            }
        }

        return realStr;
    }

}
