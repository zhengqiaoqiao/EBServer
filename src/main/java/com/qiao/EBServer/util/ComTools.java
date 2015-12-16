package com.qiao.EBServer.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.nio.ByteBuffer;


public abstract class ComTools {

	public static byte[] recvByteFromStream(InputStream ins) {
		/* 接收从服务器端返回的信息 */
		byte[] rlt = null;
		DataInputStream clientIn = null;
		ByteBuffer paramOutBuf = ByteBuffer.allocate(8192);
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		try {
			int tempLen = 0;
			clientIn = new DataInputStream(ins);
			while (true) {
				paramOutBuf.clear();
				tempLen = clientIn.read(paramOutBuf.array());
				if (tempLen == -1) {
					break;
				}
				paramOutBuf.flip();
				byteOutStream.write(paramOutBuf.array(), 0, tempLen);

			}
			rlt = byteOutStream.toByteArray();
		} catch (IOException e) {
			System.out.println(e.toString());
		} finally {
			try {
				if (clientIn != null) {
					clientIn.close();
				}
				if (byteOutStream != null) {
					byteOutStream.close();
				}
				paramOutBuf = null;
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
		return rlt;
	}

	/**
	 * 从输入流中读取数据
	 * 
	 * @param inputStream
	 *            传递过来的数据流
	 * @return 字符串
	 */
	public static String recvStringFromStream(InputStream inputStream) {
		String rlt = "";
		ByteBuffer paramOutBuf = null;
		byte[] temp = null;
		int bytesRead = 0;
		try {
			while (true) {
				paramOutBuf = ByteBuffer.allocate(4096);
				temp = paramOutBuf.array();
				bytesRead = inputStream.read(temp, 0, 4096);
				if (bytesRead == -1) {
					break;
				}
				rlt += new String(temp, 0, bytesRead, "UTF-8");
			}
			paramOutBuf.clear();
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return rlt.trim();
	}

}
