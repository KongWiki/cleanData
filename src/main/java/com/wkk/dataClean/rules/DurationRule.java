package com.wkk.dataClean.rules;

/**
 * @Time: 19-10-2下午4:27
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class DurationRule extends Rule{

    @Override
    public String process(String item) {
        try {
            int num = Integer.parseInt(item);
            if(num%12 == 0) return num/12+"";
            return null;
        }catch (Exception e){
            return null;
        }
    }
}
