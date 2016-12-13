package com.fangzhurapp.technicianport.utils;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;



import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


public class WXPayUtil {
	/**
	 * app_id
	 */
	public static final String APP_ID = "wxe64eb893824cb31f";
	
	/**
	 * 商户号
	 */
	public static final String MCH_ID = "1369895402";
	/**
	 * 微信支付统一下单的url
	 */
	public static final String WXPAY_URL= "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	
	/**
	 * 生成随机字符串  位数
	 */
	public static String getRandomStringByLength(int length) {  
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";  
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < length; i++) {  
            int number = random.nextInt(base.length());  
            sb.append(base.charAt(number));  
        }  
        return sb.toString();  
    } 
	
	

	private static String Key = "23232819801008672X23232819801008";
	/**
	 * 生成签名算法
	 * @param
	 * @return
	 */
	 public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters){  
	        StringBuffer sb = new StringBuffer();  
	        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）  
	        Iterator it = es.iterator();  
	        while(it.hasNext()) {  
	            Map.Entry entry = (Map.Entry)it.next();  
	            String k = (String)entry.getKey();  
	            Object v = entry.getValue();  
	            if(null != v && !"".equals(v)   
	                    && !"sign".equals(k) && !"key".equals(k)) {  
	                sb.append(k + "=" + v + "&");  
	                Log.d("key:", k);
	                Log.d("value:", v.toString());
	            }  
	        }  
	        sb.append("key=" + Key); 
	        String sign = MD5.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
	        return sign;  
	    }
	 
	 
	 /**
	  * 生成订单号
	  * @return
	  */
	 public static String getOrderNumber(){
		 
		  String DATE_PATTERN="yyMMddHHmmssSSS";
		 
		 return "jzbphone"+new SimpleDateFormat(DATE_PATTERN).format(new Date());
		 
	 }
	 
	 /**
	  * 时间戳
	  * @return
	  */
	 public static String getTime(){
		 
		  String DATE_PATTERN="yyMMddHHmm";
		 
		 return new SimpleDateFormat(DATE_PATTERN).format(new Date());
		 
	 }
	 
	 
	 /**
	  * 将微信返回的xml文件转换成map集合
	  * @param xml
	  * @return
	  */
	/* public static Map<String, String> readStringXmlOut(String xml) {
	        Map<String, String> map = new HashMap<String, String>();
	        Document doc = null;
	        try {
	            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
	            Element rootElt = doc.getRootElement(); // 获取根节点
	            @SuppressWarnings("unchecked")
	            List<Element> list = rootElt.elements();// 获取根节点下所有节点
	            for (Element element : list) { // 遍历节点
	                map.put(element.getName(), element.getText()); // 节点的name为map的key，text为map的value
	            }
	        } catch (DocumentException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return map;
	    }*/
	 
	 /**
	  * 获取预支付订单返回的信息
	  * @param xml
	  * @return
	  */
	public static Map<String,String> readXml(String xml){
		
		XmlPullParser newPullParser = Xml.newPullParser();
		Map<String,String> xmlMap = null;
		try {
			newPullParser.setInput(new StringReader(xml));
			
			int event = newPullParser.getEventType(); 
			
			
			while (event != XmlPullParser.END_DOCUMENT) {  
			switch (event) {  
			    case XmlPullParser.START_DOCUMENT: 
			    	xmlMap = new HashMap<String, String>();
			        break;  
			    case XmlPullParser.START_TAG: 
			    	String name = newPullParser.getName();
			        if ("prepay_id".equals(name)) {  
			          
			        	
			        	xmlMap.put("prepay_id", newPullParser.nextText());
			        }else if ("sign".equals(name)) {
			        	
			        	xmlMap.put("sign", newPullParser.nextText());	
					}else if ("nonce_str".equals(name)) {
						
						xmlMap.put("nonce_str", newPullParser.nextText());
					}
			        
			        
			        
			        break;  
			    case XmlPullParser.END_TAG:  
			        break;  
			    }  
			    event = newPullParser.next();  
			} 
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		} 
	 
		return xmlMap;
	}
	
	
	
	/**
	 * 支付成功的信息
	 * @param xml
	 * @return
	 */
	public static Map<String,String> paySucessToXml(String xml){
		
		XmlPullParser newPullParser = Xml.newPullParser();
		Map<String,String> map = null;
		try {
			newPullParser.setInput(new StringReader(xml));
			
			int event = newPullParser.getEventType(); 
			
			
			while (event != XmlPullParser.END_DOCUMENT) {  
			switch (event) {  
			    case XmlPullParser.START_DOCUMENT: 
			    	map = new HashMap<String, String>();
			        break;  
			    case XmlPullParser.START_TAG: 
			    	String name = newPullParser.getName();
			        if ("out_trade_no".equals(name)) {  
			          
			        	
			        	map.put("out_trade_no", newPullParser.nextText());
			        }
			        
			        
			        
			        break;  
			    case XmlPullParser.END_TAG:  
			        break;  
			    }  
			    event = newPullParser.next();  
			} 
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} 
	 
		return map;
	}


}
