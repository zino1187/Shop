/*
 * ��Ʈ�� ó�� �����Ͽ� ����� ��Ƴ��� Ŭ����!!
 * */
package com.javashop.util;
public class StringUtil {
	//Ȯ���� �����ϱ�!!
	public static String getExt(String path) {
		//���� ������ ���� index�� ���Ѵ�!!
		System.out.println(path.lastIndexOf("."));
		int last=path.lastIndexOf(".");
		String ext=path.substring(last+1, path.length());
		return ext;
	}
	/*
	 * public static void main(String[] args) { String
	 * ext=getExt("D:/java_developer/javascript/res/dart.png");
	 * System.out.println(ext); }
	 */	
}







