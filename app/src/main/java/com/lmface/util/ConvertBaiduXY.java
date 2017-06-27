package com.lmface.util;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 
 * @author 王广春
 * @createdate 2017年3月13日
 * @desp 转换成百度的gps坐标
 */
public class ConvertBaiduXY {
	public static ConvertBaiduXY convertBaiduXY;
	public  BaiduLocation bl; //坐标点
	public  Location location;
	
	private ConvertBaiduXY(){}
	
	
	public static ConvertBaiduXY getConvertBaiduXY(){
		if(convertBaiduXY==null){
			
			convertBaiduXY=new ConvertBaiduXY();
			
			return convertBaiduXY;
		}
		return convertBaiduXY;
		
	}
	
	
	public   BaiduLocation getBaiduLocation(float gpsx,float gpsy){
		 
	    try {
	    	if(bl==null){
	    	bl= new BaiduLocation();
	    	}
	            bl.gpsx = gpsx;//经度
	            bl.gpsy = gpsy;//纬度
	            GetBaiduLocation(bl);
	            if(bl.ok) {
	                // 转换成功，这个坐标是百度专用的
	                     return bl;
	            }
	            else {
	                /// 转换失败
	            	  System.out.println("转换失败");
	            }
	    }
	    catch(Exception ex) {
	    	  System.out.println("异常");
	    }
	    
	    return null;    
	    		
	}
	 
	public class BaiduLocation {
	    public float gpsx, gpsy;
	    public float baidux, baiduy;
	    public boolean ok = false;
	}
	
	public class Location {
	    public float gpsx, gpsy;
	}
	 
	public  String GetBaiduLocation(float x, float y) throws MalformedURLException, IOException {
	    String url = String.format("http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=%f&y=%f&ak=WnZuX68aycdnmnxACMi0fgVIcnDI4uoC", x, y);
	    HttpURLConnection urlConnection = (HttpURLConnection)(new URL(url).openConnection());
	    urlConnection.connect();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	    String lines = reader.readLine();
	    reader.close(); 
	    urlConnection.disconnect();
	    return lines;
	}   
	 
	public  boolean GetBaiduLocation(BaiduLocation bl) {
	    try {
	        bl.ok = false;
	        String res = GetBaiduLocation(bl.gpsx, bl.gpsy);
	        if(res.startsWith("{") && res.endsWith("}")) {
	            res = res.substring(1, res.length() - 2).replace("\"", "");
	            String[] lines = res.split(",");
	            for(String line : lines) {
	                String[] items = line.split(":");
	                if(items.length == 2) {
	                    if("error".equals(items[0])) {
	                        bl.ok = "0".equals(items[1]);
	                    }
	                    if("x".equals(items[0])) {
	                        bl.baidux = ConvertBase64(items[1]);
	                    }
	                    if("y".equals(items[0])) {
	                        bl.baiduy = ConvertBase64(items[1]);
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	        bl.ok = false;
	    } 
	    return bl.ok;   
	}
	private  float ConvertBase64(String str) {
	    byte[] bs = Base64.decode(str,0);
	    return Float.valueOf(new String(bs));
	}
	
	
	private static final Double PI = Math.PI;  
	  
    private static final Double PK = 180 / PI;  
      
    /** 
     * @Description: 获取两点经纬之间的距离
     * @param lat_a 
     * @param lng_a 
     * @param lat_b 
     * @param lng_b 
     * @param @return    
     * @return double 
     * @author 
     * @date 
     */  
    public  double getDistanceFromTwoPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
		double t1 = Math.cos(lat_a / PK) * Math.cos(lng_a / PK) * Math.cos(lat_b / PK) * Math.cos(lng_b / PK);
		double t2 = Math.cos(lat_a / PK) * Math.sin(lng_a / PK) * Math.cos(lat_b / PK) * Math.sin(lng_b / PK);
		double t3 = Math.sin(lat_a / PK) * Math.sin(lat_b / PK);

		double tt = Math.acos(t1 + t2 + t3);

		//System.out.println("两点间的距离：" + 6366000 * tt + " 米");
		return 6366000 * tt;
	}
	/**
     * 经纬度度分秒标示方法转换为经纬度
     * @param x    经度
     * @param y    纬度
     * @return
     */
    public  Location ConvertGps(String x,String y){
    	
    	
		//度
		float dddx=Float.valueOf(x.substring(1, 4));
		float dddy=Float.valueOf(y.substring(2, 4));
		//分
		float mmx=Float.valueOf(x.substring(4, 6));
		float mmy=Float.valueOf(y.substring(4, 6));
		//秒
		float ssx=Float.valueOf(x.substring(6))*15/2500;
		float ssy=Float.valueOf(y.substring(6))*15/2500;
		
		float gpsx=dddx+mmx/60+ssx/3600;
		float gpsy=dddy+mmy/60+ssy/3600;
		if(location==null){
			location=new Location();
		}
		location.gpsx=gpsx;
		location.gpsy=gpsy;
		return location;
    	
    	
    	
    }
    
    
   
}
