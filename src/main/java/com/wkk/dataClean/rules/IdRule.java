package com.wkk.dataClean.rules;

/**
 * @Time: 19-10-2下午4:28
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class IdRule extends Rule{

    public String process(String item) {
        if (item != null && !item.isEmpty()) {
            try {
                Integer.parseInt(item);
                return item;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
