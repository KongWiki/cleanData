package com.wkk.dataClean.rules;

import java.util.Arrays;
import java.util.List;

/**
 * @Time: 19-10-2下午4:27
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class CardTypeEnumRule extends EnumRule {
    @Override
    List<String> acceptStrings() {
        return Arrays.asList("junior", "classic", "gold");
    }

    @Override
    List<String> replaceStrings() {
        return Arrays.asList("JUNIOR", "CLASSIC", "GOLD");
    }
}
