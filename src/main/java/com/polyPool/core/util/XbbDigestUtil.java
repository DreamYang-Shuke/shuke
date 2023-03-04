package com.polyPool.core.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 签名工具类
 */
public class XbbDigestUtil {

	/**
	 *  parameter strSrc is a string will be encrypted,
	 *	parameter encName is the algorithm name will be used.
	 *	encName dafault to "MD5"
	 */
	public static String encrypt(String strSrc, String encName) {
		MessageDigest md = null;
		String strDes = null;
		
		try {
			byte[] bt = strSrc.getBytes(StandardCharsets.UTF_8);
			if (encName == null || encName.equals("")) {
				encName = "MD5";
			}
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return null;
		}
		return strDes;
	}

	public static String bytes2Hex(byte[] bts) {
		StringBuilder des = new StringBuilder();
		String tmp = null;
		for (byte bt : bts) {
			tmp = (Integer.toHexString(bt & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String strSrc = "可以加密汉字.Oh,and english";

		System.out.println("Source String:" + strSrc);
		System.out.println("Encrypted String:");
		System.out.println("Use Def:" + XbbDigestUtil.encrypt(strSrc, null));
		System.out.println("Use MD5:" + XbbDigestUtil.encrypt(strSrc, "MD5"));
		System.out.println("Use SHA-1:" + XbbDigestUtil.encrypt(strSrc, "SHA-1"));
		System.out.println("Use SHA-256:" + XbbDigestUtil.encrypt(strSrc, "SHA-256"));
	}

}
