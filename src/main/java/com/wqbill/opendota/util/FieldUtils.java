package com.wqbill.opendota.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class FieldUtils {
    public static String underlineToCamel(String name) {
        // 快速检查
        if (StringUtils.isEmpty(name)) {
            // 没必要转换
            return "";
        }
        StringBuilder result = new StringBuilder();
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        // 跳过原始字符串中开头、结尾的下换线或双重下划线
        // 处理真正的驼峰片段
        Arrays.stream(camels).filter(camel -> !StringUtils.isEmpty(camel)).forEach(camel -> {
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel);
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(capitalFirst(camel));
            }
        });
        return result.toString();
    }

    public static String capitalFirst(String name) {
        if (StringUtils.isNotEmpty(name)) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return "";
    }

    public static String singular(String name) {
        // tie ties
        // leaf leaves
        // country countries
        // 维护一个巨大的map
        if (name.endsWith("s")) return name.substring(0, name.length() - 1);
        return name;
    }

    public static String objectName(String name) {
        return capitalFirst(underlineToCamel(singular(name)));
    }

    public static String arrayName(String name) {
        return capitalFirst(underlineToCamel(singular(name)));
    }

    public static void main(String[] args) {
        System.out.println("cases".substring(0, "cases".length() - 1));
    }
}
