package com.wkk.dataClean.rules;

import java.util.Arrays;
import java.util.List;

/**
 * @Time: 19-10-2下午4:30
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class TranKSymbolRule extends EnumRule{
    @Override
    List<String> acceptStrings() {
        return Arrays.asList("POJISTNE","SLUZBY","UROK","SANKC.UROK","SIPO","DUCHOD","UVER","");
    }

    @Override
    List<String> replaceStrings() {
        return Arrays.asList("INSURANCE","BILL","INTEREST_INCOME","INTEREST_PENALTY","HOUSEHOLD","OLD_AGE_PENSION","LOAN","");
    }
}
