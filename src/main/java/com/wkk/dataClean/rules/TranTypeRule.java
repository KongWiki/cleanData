package com.wkk.dataClean.rules;

import java.util.Arrays;
import java.util.List;

/**
 * @Time: 19-10-2下午4:30
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class TranTypeRule extends EnumRule{
    @Override
    List<String> acceptStrings() {
        return Arrays.asList("PRIJEM","VYDAJ");
    }

    @Override
    List<String> replaceStrings() {
        return Arrays.asList("DEPOSIT","WITHDRAW");
    }
}
