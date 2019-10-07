package com.wkk.dataClean.rules;

import java.util.Arrays;
import java.util.List;

/**
 * @Time: 19-10-2下午4:30
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class TranOperaRule extends EnumRule{
    @Override
    List<String> acceptStrings() {
        return Arrays.asList("VYBER KARTOU","VKLAD","PREVOD Z UCTU","VYBER","PREVOD NA UCET");
    }

    @Override
    List<String> replaceStrings() {
        return Arrays.asList("CREDIT_WITHDRAW","CREDIT_CASH","FROM_OTHER_BANK","CASH","TO_OTHER_BANK");
    }
}
