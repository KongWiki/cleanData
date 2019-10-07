package com.wkk.dataClean.rules;

import java.util.List;

/**
 * @Time: 19-10-2下午4:28
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public abstract class EnumRule extends Rule{

    @Override
    public String process(String item) {
        // 获取子类提供的定义字段
        List<String> acceptStrings = acceptStrings();
        // 获取子类提供的翻译字段
        List<String> replaceStrings = replaceStrings();
        // 相同返回翻译字段
        for (int i = 0; i < acceptStrings.size(); i++) {
            if (item.equals(acceptStrings.get(i))) return replaceStrings.get(i);
        }
        int maxIndex = 0;
        double maxValue = 0;
        // 不同计算编辑距离返回相似字段
        for (int i = 0; i < acceptStrings.size(); i++) {
            double simValue = sim(acceptStrings.get(i).toUpperCase(), item.toUpperCase());
            if (simValue > maxValue) maxIndex = i;
        }
        return replaceStrings.get(maxIndex);
    }

    // 计算距离
    private int ld(String str1, String str2) {
        int d[][];
        int n = str1.length();
        int m = str2.length();
        if (n == 0) return m;
        if (m == 0) return n;
        d = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        char ch1;
        char ch2;
        int temp;
        for (int i = 1; i <= n; i++) {
            ch1 = str1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                d[i][j] = Math.min(d[i - 1][j] + 1, Math.min(d[i][j - 1] + 1, d[i - 1][j - 1]+ temp));
            }
        }
        return d[n][m];
    }

    private double sim(String str1, String str2) {
        try {
            double ld = (double) ld(str1, str2);
            return (1-ld) / (double) Math.max(str1.length(), str2.length());
        } catch (Exception e) {
            return 0.1;
        }
    }

    abstract List<String> acceptStrings();

    abstract List<String> replaceStrings();
}
