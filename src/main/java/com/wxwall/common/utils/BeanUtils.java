package com.wxwall.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanUtils extends org.springframework.beans.BeanUtils {

	/**
	 * 复制属性值，将源对象非空的属性值复制到对应的目标对象属性中
	 * TODO 有BUG，boolean值属性，默认值拷贝
	 * @param target
	 *            目标对象
	 * @param source
	 *            源对象
	 */
	public static void copyNewPropertites(Object source, Object target) {

		Class<?> targetClazz = target.getClass();
		Class<?> sourceClazz = source.getClass();
		Field[] sourceFiles = sourceClazz.getDeclaredFields();
		for (int i = 0; i < sourceFiles.length; i++) {
			String fieldName = sourceFiles[i].getName();
			try {
				Field targetField = targetClazz.getDeclaredField(fieldName);
				// 判断目标对象和源对象同名属性的类型是否相同
				if (targetField.getType() == sourceFiles[i].getType()) {
					// 由属性名字得到对应get和set方法的名字
					String setMethodName = "set"
							+ fieldName.substring(0, 1).toUpperCase()
							+ fieldName.substring(1);
					String getMethodName = "get"
							+ fieldName.substring(0, 1).toUpperCase()
							+ fieldName.substring(1);
					// 调用source对象的getMethod方法
					Method getMethod = sourceClazz.getDeclaredMethod(
							getMethodName, new Class[] {});
					Object value = getMethod.invoke(source, new Object[] {});
					// 判断源对象的属性值是否非空
					if (null == value || "".equals(value.toString().trim())) {
						continue;
					}
					// 调用目标对象的setMethod方法
					Method setMethod = targetClazz.getDeclaredMethod(
							setMethodName, sourceFiles[i].getType());
					setMethod.invoke(target, value);
				} else {
					throw new NoSuchFieldException("同名属性类型不匹配");
				}
			} catch (Exception e) {
				continue;
			}
		}
	}
}
