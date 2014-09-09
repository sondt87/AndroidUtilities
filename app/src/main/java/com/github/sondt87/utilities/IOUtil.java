package com.github.sondt87.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;

public class IOUtil {
	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	static public String getFileContenntAsString(File file, String encoding) throws Exception {
		FileInputStream is = new FileInputStream(file);
		return new String(getStreamContentAsBytes(is), encoding);
	}

	static public String getFileContenntAsString(File file) throws Exception {
		FileInputStream is = new FileInputStream(file);
		return new String(getStreamContentAsBytes(is));
	}

	static public String getFileContenntAsString(String fileName, String encoding) throws Exception {
		FileInputStream is = new FileInputStream(fileName);
		return new String(getStreamContentAsBytes(is), encoding);
	}

	static public String getFileContenntAsString(String fileName) throws Exception {
		FileInputStream is = new FileInputStream(fileName);
		return new String(getStreamContentAsBytes(is));
	}

	static public byte[] getFileContentAsBytes(String fileName) throws Exception {
		FileInputStream is = new FileInputStream(fileName);
		return getStreamContentAsBytes(is);
	}

	static public String getStreamContentAsString(InputStream is, String encoding) throws Exception {
		char buf[] = new char[is.available()];
		BufferedReader in = new BufferedReader(new InputStreamReader(is, encoding));
		in.read(buf);
		return new String(buf);
	}

	static public String getStreamContentAsUTF8String(InputStream is) throws Exception {
		char buf[] = new char[is.available()];
		BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		in.read(buf);
		return new String(buf);
	}

	static public byte[] getStreamContentAsBytes(InputStream is) throws Exception {
		BufferedInputStream buffer = new BufferedInputStream(is);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] data = new byte[4912];
		int available = -1;
		while ((available = buffer.read(data)) > -1) {
			output.write(data, 0, available);
		}
		return output.toByteArray();
	}

	static public String getResourceAsString(String resource, String encoding) throws Exception {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource(resource);
		InputStream is = url.openStream();
		return getStreamContentAsString(is, encoding);
	}

	static public byte[] getResourceAsBytes(String resource) throws Exception {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource(resource);
		InputStream is = url.openStream();
		return getStreamContentAsBytes(is);
	}

	static public byte[] serialize(Object obj) throws Exception {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bytes);
		out.writeObject(obj);
		out.close();
		byte[] ret = bytes.toByteArray();
		return ret;
	}

	static public Object deserialize(byte[] bytes) throws Exception {
		if (bytes == null)
			return null;
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		ObjectInputStream in = new ObjectInputStream(is);
		Object obj = in.readObject();
		in.close();
		return obj;
	}

	public static void stringToFile(byte[] data, String path) throws Exception {
		FileOutputStream outputStream = new FileOutputStream(path);
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
	}

	public static void stringToFile(String data, String path) throws Exception {
		File f = new File(path);
		File parentFile = f.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		if (!f.exists()) {
			f.createNewFile();
		}
		FileOutputStream outputStream = new FileOutputStream(path);
		outputStream.write(data.getBytes("UTF-8"));
		outputStream.flush();
		outputStream.close();
	}

	public static void stringToFile(String data, String path, String fileName) throws Exception {
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		if (!path.endsWith("/"))
			path = path + "/";
		path = path + fileName;
		FileOutputStream outputStream = new FileOutputStream(path);
		outputStream.write(data.getBytes("UTF-8"));
		outputStream.flush();
		outputStream.close();
	}

	public static String fileToString(String path) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		String text = IOUtil.getStreamContentAsString(fis, "UTF-8");
		fis.close();
		return text;
	}

	public static String fileToString(String path, String encoding) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		String text = IOUtil.getStreamContentAsString(fis, encoding);
		fis.close();
		return text;
	}

	public static String getPrintString(Object[] contacts, String string, String append) {
		// if((contacts == null ||contacts.length < 1)) return "";
		StringBuilder b = new StringBuilder();
		b.append(append + "-------------- +> " + string + "-" + contacts.length + " <+ ----------------");
		int i = 0;
		for (Object s : contacts) {
			b.append(append + i++ + " - lenght: " + s.toString().length() + ":" + s.toString());
		}
		b.append(append + "----------------------------------------------------");
		return b.toString();
	}

	public static String getPrintString(Collection<?> contacts, String string, String append) {
		return getPrintString(contacts.toArray(), string, append);
	}

	public static void println(Object[] contacts, String string, boolean inNull) {
		System.out.println(getPrintString(contacts, string, "\n"));
	}

	public static void println(Collection<?> contacts, String string, boolean inNull) {
		println(contacts.toArray(), string, inNull);
	}

	public static void println(Collection<?> contacts) {
		println(contacts.toArray(), "", true);
	}

	public static void println(Collection<?> contacts, String string) {
		println(contacts.toArray(), string, true);
	}

	public static void println(Object[] contacts) {
		println(contacts, "", true);
	}

	public static void println(Object[] contacts, String string) {
		println(contacts, string, true);
	}

	public static void appendStringToFile(String s, String file) throws Exception {
		String old = fileToString(file);
		old = old + " " + s;
		stringToFile(old, file);
	}

	public static String readFile(File file) throws IOException {
		StringBuilder contents = new StringBuilder();
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return contents.toString();
	}

	public static void writeFile(File file, String data) throws IOException {
		BufferedWriter bufferedWriter = null;
		File folder = file.getParentFile();
		if (!folder.exists())
			folder.mkdir();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file, true));
			bufferedWriter.write(data);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			bufferedWriter.close();
		}

	}
}