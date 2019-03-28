package com.pemila.util;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 参数校验
 * @author 月在未央
 * @date 2019/3/28 10:39
 */
public class ValidParamUtil {

    public static boolean isAllFieldNull(Object obj, List<String> excludeNames) throws Exception{
        // 取到obj的class, 并取到所有属性
        Field[] fs = obj.getClass().getDeclaredFields();
        // 定义一个flag, 标记是否所有属性值为空
        boolean flag = true;
        // 遍历所有属性
        for (Field f : fs) {
            // 设置私有属性也是可以访问的
            f.setAccessible(true);
            // 1.排除不包括的属性名, 2.属性值不为空, 3.属性值转换成String不为""
            if(!excludeNames.contains(f.getName()) && f.get(obj) != null && !"".equals(f.get(obj).toString())) {
                // 有属性满足3个条件的话, 那么说明对象属性不全为空
                flag = false;
                break;
            }
        }
        return flag;
    }
}
