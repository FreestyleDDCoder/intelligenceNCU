package com.intelligencencu.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 读取流的工具
 * 
 * @author Administrator
 * 
 */
public class StreamUtils {
	// 将流读取成字符串后返回
	public static String readFromStream(InputStream in) throws IOException {
		int len = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] byt = new byte[1024];

		while ((len = in.read(byt)) != -1) {
			baos.write(byt, 0, len);
		}
		String result = baos.toString();
		in.close();
		baos.close();
		return result;

	}
}
