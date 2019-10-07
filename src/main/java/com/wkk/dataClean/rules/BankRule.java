package com.wkk.dataClean.rules;

/**
 * @Time: 19-10-2下午4:26
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class BankRule extends Rule{

    @Override
    public String process(String item) {
        if(item.length() == 2){
            return item;
        }
        return null;
    }
}
