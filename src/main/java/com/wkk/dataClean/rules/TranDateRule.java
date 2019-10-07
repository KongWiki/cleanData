package com.wkk.dataClean.rules;

/**
 * @Time: 19-10-2下午4:29
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class TranDateRule extends  DateRule{

    @Override
    String getFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}
