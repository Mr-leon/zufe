package org.smartjq.plugin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolsUtil {
	private final static Map<String, String> IMGAE_TYPE_MAP = new HashMap<String, String>();
	private final static Map<String, String> VIDEO_TYPE_MAP = new HashMap<String, String>();
	private final static Map<String, String> DOCUMENT_TYPE_MAP = new HashMap<String, String>();
	private final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

	private ToolsUtil() {
	}

	static {
		getFileType();
		getImageType();
		getVideoType();
		getDocumentType();
	}

	/**
	 * 文件头信息，待补充
	 */
	private static void getFileType() {
		//压缩文件
		FILE_TYPE_MAP.put("zip", "504B0304");
		FILE_TYPE_MAP.put("rar", "52617221");
		
		//文件
		FILE_TYPE_MAP.put("dwg", "41433130"); // CAD (dwg)
		FILE_TYPE_MAP.put("html", "68746D6C3E"); // HTML (html)
		FILE_TYPE_MAP.put("rtf", "7B5C727466"); // Rich Text Format (rtf)
		FILE_TYPE_MAP.put("xml", "3C3F786D6C");
		FILE_TYPE_MAP.put("PSD", "38425053"); // photoshop (psd)
		FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A"); // Email [thorough only] (eml)
		FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F"); // Outlook Express (dbx)
		FILE_TYPE_MAP.put("pst", "2142444E"); // Outlook (pst)
		FILE_TYPE_MAP.put("mdb", "5374616E64617264204A"); // MS access (mdb)
		FILE_TYPE_MAP.put("wpd", "FF575043"); // WordPerfect (wpd)
		FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
		FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
		FILE_TYPE_MAP.put("qdf", "AC9EBD8F"); // Quicken (qdf)
		FILE_TYPE_MAP.put("pwl", "E3828596"); // Windows Password (pwl)
		FILE_TYPE_MAP.put("wav", "57415645"); // Wave (wav)
		FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); // Windows Media (asf)
		FILE_TYPE_MAP.put("mid", "4D546864"); // MIDI (mid)
	}
	

	/**
	 * 文件头信息，待补充
	 */
	private static void getImageType() {
		//图片文件
		IMGAE_TYPE_MAP.put("jpg", "FFD8FF"); // JPEG (jpg)
		IMGAE_TYPE_MAP.put("png", "89504E47"); // PNG (png)
		IMGAE_TYPE_MAP.put("gif", "47494638"); // GIF (gif)
		IMGAE_TYPE_MAP.put("tif", "49492A00"); // TIFF (tif)
		IMGAE_TYPE_MAP.put("bmp", "424D"); // Windows Bitmap (bmp)
		
	}


	/**
	 * 文件头信息，待补充
	 */
	private static void getVideoType() {
		//视频文件
		VIDEO_TYPE_MAP.put("avi", "41564920");
		VIDEO_TYPE_MAP.put("ram", "2E7261FD"); // Real Audio (ram)
		VIDEO_TYPE_MAP.put("rm", "2E524D46"); // Real Media (rm)
		VIDEO_TYPE_MAP.put("mpg", "000001BA"); //
		VIDEO_TYPE_MAP.put("mov", "6D6F6F76"); // Quicktime (mov)
		
	}


	/**
	 * 文件头信息，待补充
	 */
	private static void getDocumentType() {
		//文档
		DOCUMENT_TYPE_MAP.put("xlsx.docx", "504B030414000600080000002100");
		DOCUMENT_TYPE_MAP.put("xls.doc", "D0CF11E0"); // MS Word 注意：word 和 excel的文件头一样
		DOCUMENT_TYPE_MAP.put("pdf", "255044462D312E"); // Adobe Acrobat (pdf)
	}


	/**
	 * 获取文件类型,包括图片,若格式不是已配置的,则返回null
	 */
	public final static String getFileByFile(File file) {
		String filetype = null;
		byte[] b = new byte[50];
		try {
			InputStream is = new FileInputStream(file);
			is.read(b);
			filetype = getFileTypeByStream(b);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filetype;
	}

	public final static String getFileTypeByStream(byte[] b) {
		String filetypeHex = String.valueOf(getFileHexString(b));
		Iterator<Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
		while (entryiterator.hasNext()) {
			Entry<String, String> entry = entryiterator.next();
			String fileTypeHexValue = entry.getValue();
			if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
				return "file";
			}
		}
		entryiterator = IMGAE_TYPE_MAP.entrySet().iterator();
		while (entryiterator.hasNext()) {
			Entry<String, String> entry = entryiterator.next();
			String fileTypeHexValue = entry.getValue();
			if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
				return "image";
			}
		}
		entryiterator = VIDEO_TYPE_MAP.entrySet().iterator();
		while (entryiterator.hasNext()) {
			Entry<String, String> entry = entryiterator.next();
			String fileTypeHexValue = entry.getValue();
			if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
				return "video";
			}
		}
		entryiterator = DOCUMENT_TYPE_MAP.entrySet().iterator();
		while (entryiterator.hasNext()) {
			Entry<String, String> entry = entryiterator.next();
			String fileTypeHexValue = entry.getValue();
			if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
				return "document";
			}
		}
		return "file";
	}

	private final static String getFileHexString(byte[] b) {
		StringBuilder stringBuilder = new StringBuilder();
		if (b == null || b.length <= 0) {
			return null;
		}
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 返回byte的数据大小对应的文本
	 * 
	 * @param size
	 * @return
	 */
	public static String getDataSize(long size) {
		DecimalFormat formater = new DecimalFormat("####.00");
		if (size < 1024) {
			return size + "bytes";
		} else if (size < 1024 * 1024) {
			float kbsize = size / 1024f;
			return formater.format(kbsize) + "KB";
		} else if (size < 1024 * 1024 * 1024) {
			float mbsize = size / 1024f / 1024f;
			return formater.format(mbsize) + "MB";
		} else if (size < 1024 * 1024 * 1024 * 1024) {
			float gbsize = size / 1024f / 1024f / 1024f;
			return formater.format(gbsize) + "GB";
		} else {
			return "--";
		}
	}
	
	/**
	 * 判断大于0的整数或者小数（最多2位）
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str);
        return match.matches();
    }

	/**
	 * 判断正整数
	 * @param str
	 * @return
	 */
	public static boolean positiveInteger(String str) {
        Pattern pattern = Pattern.compile("^[1-9]\\d*$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str);
        return match.matches();
    }
}
