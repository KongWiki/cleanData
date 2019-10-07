package com.wkk.dataClean.rules;

import java.util.Arrays;
import java.util.List;

/**
 * @Time: 19-10-2下午4:27
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class DispTypeEnumRule extends EnumRule{
    @Override
    List<String> acceptStrings() {
        return Arrays.asList("OWNER","DISPONENT");

    }

    @Override
    List<String> replaceStrings() {
        return Arrays.asList("OWNER","DISPONENT");

    }
}
