package com.wkk.dataClean.rules;

/**
 * @Time: 19-10-2下午4:26
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class AccountDateRule extends DateRule{

    @Override
    String getFormat() {
        return "yyyy/MM/dd H:mm";
    }
}
