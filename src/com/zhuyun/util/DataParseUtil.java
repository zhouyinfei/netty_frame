package com.zhuyun.util;

public class DataParseUtil {
	
	public static String byteArrayToHexString(byte[] byteArray){
		StringBuffer sb = new StringBuffer();
    	for (byte b : byteArray) {
    		String hex = Integer.toHexString(b & 0xFF); 
    	     if (hex.length() == 1) { 
    	       hex = '0' + hex; 
    	     } 
    	    sb.append("0x" + hex.toUpperCase() + " ");
		}
		return sb.toString();
	}
}
