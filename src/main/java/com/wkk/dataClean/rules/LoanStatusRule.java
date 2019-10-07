package com.wkk.dataClean.rules;

import java.util.Arrays;
import java.util.List;

/**
 * @Time: 19-10-2下午4:29
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class LoanStatusRule  extends EnumRule{
    @Override
    List<String> acceptStrings() {
        return Arrays.asList("A","B","C","D");
    }

    @Override
    List<String> replaceStrings() {
        return Arrays.asList("A","B","C","D");
    }
}
