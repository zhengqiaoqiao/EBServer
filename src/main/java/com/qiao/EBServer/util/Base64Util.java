/**
 * Copyright (C) 1998-2009 TENCENT Inc.All Rights Reserved.		
 * 																	
 * FileName：Base64Util.java					
 *			
 * Description：base64编码解码工具
 * History：
 *  1.0  2009-04-01        Create	
 */
package com.qiao.EBServer.util;

import java.io.IOException;

import sun.misc.BASE64Encoder;

/**
 * base64编码解码工具
 * 
 */
public class Base64Util
{
	public static void main(String[] args)
	{
		byte[] arrByte = new byte[1];
		arrByte[0] = 0;
		System.out.println("Dict:");
		for(byte i = 0; i < 64; i++)
		{
			System.out.print(String.format("%2d %c\t", i, Base64Util.encode(arrByte).charAt(0)));
			arrByte[0] += 4;
			if((i + 1) % 8 == 0)
			{
				System.out.println("");
			}
		}
		System.out.print(Base64Util.encode("12334jjgfddgh".getBytes()));
	}

	/**
	 * 将二进制内容编码为base64字符串
	 * @param srcContent 需要编码的数据
	 * @return String 编码结果。如果参数为null，则返回null。
	 */
	public static String encode(byte[] srcContent)
	{
		if(srcContent == null)
		{
			return null;
		}
		//sun的实现会每76个字符后面增加一个回车，需要删除。
		return new BASE64Encoder().encode(srcContent).replace("\r\n", "");
	}

	/**
	 * 将二进制内容编码为可以作URL参数的字符串行
	 * 由于默认base64字典中的+和=在URL参数中，都有特殊的含义，需要做转义
	 * 将+转义为-
	 * 将=转义为~
	 * @param srcContent 需要编码为URL参数的数据
	 * @return String 编码结果。如果参数为null，则返回null。
	 */
	public static String encodeURLParam(byte[] srcContent)
	{
		return encode(srcContent).replace('+', '-').replace('=', '~');
	}

	/**
	 * 将base64字符串解码为源数据内容
	 * 与encode互为相逆的过程
	 * @param base64Code base64编码字符串
	 * @return byte[] 解码结果。如果参数为null或解码失败，则返回null。
	 */
	public static byte[] decode(String base64Code)
	{
		if(base64Code == null)
		{
			return null;
		}
		
		try
		{
			return new sun.misc.BASE64Decoder().decodeBuffer(base64Code);
		}
		catch(IOException exception)
		{
			return null;
		}
	}

	/**
	 * 将base64字符串解码为源数据内容
	 * 与encodeURLParam互为相逆的过程
	 * @param base64Code base64编码字符串
	 * @return byte[] 解码结果。如果参数为null或解码失败，则返回null。
	 */
	public static byte[] decodeURLParam(String base64Code) throws IOException
	{
		return decode(base64Code.replace('-', '+').replace('~', '='));
	}
}
