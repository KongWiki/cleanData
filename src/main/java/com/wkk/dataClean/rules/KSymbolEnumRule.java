package com.wkk.dataClean.rules;

import java.util.Arrays;
import java.util.List;

/**
 * @Time: 19-10-2下午4:28
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class KSymbolEnumRule extends EnumRule {
    @Override
    List<String> acceptStrings() {
        return Arrays.asList("POJISTNE","SIPO","LEASING","UVER","");
    }

    @Override
    List<String> replaceStrings() {
        return Arrays.asList("INSURANCE","MANAGEMENT","RENTAL","LOAN","");
    }

}
