package com.wkk.dataClean.filter;

import com.wkk.dataClean.rules.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Time: 19-10-2下午4:25
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class LoanFilter extends CsvFilter{
    @Override
    String getCsvName() {
        return "loan";
    }

    @Override
    List<Rule> getRules() {
        List<Rule> rules = new ArrayList<Rule>(8);
        rules.add(new IdRule());
        rules.add(new IdRule());
        rules.add(new AccountDateRule());
        rules.add(new NumberRule());
        rules.add(new DurationRule());
        rules.add(new NumberRule());
        rules.add(new LoanStatusRule());
        rules.add(new NumberRule());
        return rules;
    }

    @Override
    String[] getHeaders() {
        return new String[]{"loan_id","account_id","date","amount","duration","payments","status","payduration"};
    }
}
