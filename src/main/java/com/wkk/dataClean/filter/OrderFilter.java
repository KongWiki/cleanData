package com.wkk.dataClean.filter;

import com.wkk.dataClean.rules.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Time: 19-10-2下午4:25
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class OrderFilter extends CsvFilter{
    @Override
    String getCsvName() {
        return "order";
    }

    @Override
    List<Rule> getRules() {
        List<Rule> rules = new ArrayList<Rule>(6);
        rules.add(new IdRule());
        rules.add(new IdRule());
        rules.add(new BankRule());
        rules.add(new NumberRule());
        rules.add(new NumberRule());
        rules.add(new KSymbolEnumRule());
        return rules;
    }

    @Override
    String[] getHeaders() {
        return new String[]{"order_id","account_id","bank_to","account_to","amount","k_symbol"};
    }
}
