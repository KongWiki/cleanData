package com.wkk.dataClean.rules;

/**
 * @Time: 19-10-2下午4:29
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class NumberRule  extends Rule{
    @Override
    public String process(String item) {
        if (isNumeric(item)) {
            return item;
        }
        return null;
    }

    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
