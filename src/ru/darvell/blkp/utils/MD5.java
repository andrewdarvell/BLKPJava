package ru.darvell.blkp.utils;

import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 15.07.14
 * Time: 12:57
 * Return MD5
 */
public class MD5 {

	public static String getMd5(String source){
		try{

			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(source.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();

			//convert the byte to hex format method 1
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			//System.out.println("Digest(in hex format):: " + sb.toString());
			return sb.toString();
		}catch (Exception e){
			//log.error("Exception: "+e);
			System.out.println("Exception: "+e.getMessage());
			return null;
		}
	}

}
