package com.wxwall.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * @author： ganjunzx@gmail.com
 * @class: PropertiesTool
 * @package: com.kechuang.wifidog.horizon.utils
 * @description: 加载属性文件中属性的工具类 ，应该是线程安全的
 */
public class PropertiesTool {
	private Properties tool;

	public PropertiesTool() {
		tool = new Properties();
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: loadFile
	 * @description: 加载属性文件中的内容
	 * @param filename
	 *            属性文件路径
	 * @param encoding
	 *            文件的编码
	 * @return 是否加载成功，文件路径是否正确
	 */
	public boolean loadFile(String filename, String encoding) {
		try {
			String absolutePath = PropertiesTool.class.getResource("/" + filename).getPath();
			FileInputStream in = new FileInputStream(absolutePath);
			return loadStream(in, encoding);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: loadStream
	 * @description: 读取字节流加载内容
	 * @param stream
	 *            要读取的字节流
	 * @param encoding
	 *            字符流编码
	 * @return
	 */
	public boolean loadStream(InputStream stream, String encoding) {
		try {
			Reader reader = new InputStreamReader(stream, encoding);
			return loadReader(reader);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: loadReader
	 * @description: 读取字符流加载内容
	 * @param reader
	 *            要读取的字符流
	 * @return
	 */
	public synchronized boolean loadReader(Reader reader) {
		try {
			tool.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getTool
	 * @description: 获取Properties类型加载器
	 * @return
	 */
	public synchronized Properties getTool() {
		return tool;
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getString
	 * @description: 加载一个字符串。如果没有对应的key，就返回null
	 * @param key
	 *            键值对中的key
	 * @return 键值对中的value
	 */
	public String getString(String key) {
		return getTool().getProperty(key);
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getString
	 * @description: 加载一个字符串。如果没有对应的key，就返回默认值
	 * @param key
	 *            键值对中的key
	 * @param defaultValue
	 *            默认值
	 * @return 键值对中的value
	 */
	public String getString(String key, String defaultValue) {
		return getTool().getProperty(key, defaultValue);
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getStrings
	 * @description: 加载一个String数组，并且以regex正则表达式分割该字符串。如果没有对应的key则返回null
	 * @param key
	 *            键值对中的key
	 * @param regex
	 *            正则表达式
	 * @return
	 */
	public String[] getStrings(String key, String regex) {
		String str = getTool().getProperty(key);
		if (str == null) {
			return null;
		}
		return str.split(regex);
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getCharacter
	 * @description: 加载一个Character。如果没有对应的key或者value的长度不是1，则返回null
	 * @param key
	 * @return
	 */
	public Character getCharacter(String key) {
		String str = getTool().getProperty(key);
		if (str == null || str.length() != 1) {
			return null;
		}
		return str.charAt(0);
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getBoolean
	 * @description: 
	 *               加载一个Boolean。如果没有对应的key，则返回null；如果值是"true"或"1"，返回true，否则返回false
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key) {
		String str = getTool().getProperty(key);
		if (str == null) {
			return null;
		}
		// 这里比较"true"时，使用了不区分大小写的比较
		if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getInteger
	 * @description: 加载一个Integer。如果没有对应的key或者value无法转换成为Integer，则返回null
	 * @param key
	 * @return
	 */
	public Integer getInteger(String key) {
		String str = getTool().getProperty(key);
		if (str == null) {
			return null;
		}
		Integer value = null;
		try {
			value = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getLong
	 * @description: 加载一个Long。如果没有对应的key或者value无法转换成为Long，则返回null
	 * @param key
	 * @return
	 */
	public Long getLong(String key) {
		String str = getTool().getProperty(key);
		if (str == null) {
			return null;
		}
		Long value = null;
		try {
			value = Long.parseLong(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getShort
	 * @description: 加载一个Short。如果没有对应的key或者value无法转换成为Short，则返回null
	 * @param key
	 * @return
	 */
	public Short getShort(String key) {
		String str = getTool().getProperty(key);
		if (str == null) {
			return null;
		}
		Short value = null;
		try {
			value = Short.parseShort(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getByte
	 * @description: 加载一个Byte。如果没有对应的key或者value无法转换成为Byte，则返回null
	 * @param key
	 * @return
	 */
	public Byte getByte(String key) {
		String str = getTool().getProperty(key);
		if (str == null) {
			return null;
		}
		Byte value = null;
		try {
			value = Byte.parseByte(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getDouble
	 * @description: 加载一个Double。如果没有对应的key或者value无法转换成为Double，则返回null
	 * @param key
	 * @return
	 */
	public Double getDouble(String key) {
		String str = getTool().getProperty(key);
		if (str == null) {
			return null;
		}
		Double value = null;
		try {
			value = Double.parseDouble(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getFloat
	 * @description: 加载一个Float。如果没有对应的key或者value无法转换成为Float，则返回null
	 * @param key
	 * @return
	 */
	public Float getFloat(String key) {
		String str = getTool().getProperty(key);
		if (str == null) {
			return null;
		}
		Float value = null;
		try {
			value = Float.parseFloat(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: containsKey
	 * @description: 文件的键值对中是否包含该键
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		return getTool().containsKey(key);
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: containsValue
	 * @description: 文件的键值对中是否包含该值
	 * @param value
	 * @return
	 */
	public boolean containsValue(String value) {
		return getTool().containsValue(value);
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getKeySet
	 * @description: 获取所有的键
	 * @return
	 */
	public Set<Object> getKeySet() {
		return getTool().keySet();
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: getEntrySet
	 * @description: 获取键值对的集合
	 * @return
	 */
	public Set<Entry<Object, Object>> getEntrySet() {
		return getTool().entrySet();
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: setValue
	 * @description: 为key设置值value
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value) {
		getTool().setProperty(key, value);
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: writeToFile
	 * @description: 将已经加载的和改变的所有的东西写入文件中，如果该文件存在则覆盖，没有则创建，使用文件系统默认编码
	 * @param filename
	 *            要写入的文件的路径
	 * @return
	 */
	public boolean writeToFile(String filename) {
		return writeToFile(filename, System.getProperty("file.encoding"));
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: writeToFile
	 * @description: 将已经加载的和改变的所有的东西写入文件中，如果该文件存在则覆盖，没有则创建
	 * @param filename
	 *            要写入的文件的路径
	 * @param encoding
	 *            要写入的字符串的编码
	 * @return
	 */
	public boolean writeToFile(String filename, String encoding) {
		// 检查该文件是否存在，如果没有则创建
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		// 创建输出流
		try {
			FileOutputStream stream = new FileOutputStream(file);
			return writeToStream(stream, encoding);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: writeToStream
	 * @description: 将已经加载的和改变的所有的东西写入字节流中，使用文件指定的编码
	 * @param stream
	 *            要写入的字节流
	 * @param encoding
	 *            字符编码
	 * @return
	 */
	public boolean writeToStream(OutputStream stream, String encoding) {
		try {
			OutputStreamWriter writer = new OutputStreamWriter(stream, encoding);
			return writeToWriter(writer);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: writeToStream
	 * @description: 将已经加载的和改变的所有的东西写入字节流中，使用文件系统默认编码
	 * @param stream
	 *            要写入的字节流
	 * @return
	 */
	public boolean writeToStream(OutputStream stream) {
		return writeToStream(stream, System.getProperty("file.encoding"));
	}

	/**
	 * @author： ganjunzx@gmail.com
	 * @method: writeToWriter
	 * @description: 将已经加载的和改变的所有东西写入字符流中
	 * @param writer
	 *            要写入的字符流
	 * @return
	 */
	public boolean writeToWriter(Writer writer) {
		try {
			getTool().store(writer, null);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
