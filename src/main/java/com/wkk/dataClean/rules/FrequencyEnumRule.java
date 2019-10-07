package com.wkk.dataClean.rules;

import java.util.Arrays;
import java.util.List;

/**
 * @Time: 19-10-2下午4:28
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class FrequencyEnumRule extends EnumRule{
    @Override
    List<String> acceptStrings() {
        return Arrays.asList("POPLATEK MESICNE", "POPLATEK TYDNE", "POPLATEK PO OBRATU");
    }

    @Override
    List<String> replaceStrings() {
        return Arrays.asList("ONCE A MONTH", "ONCE A WEEK", "AFTER DEAL");
    }
}
