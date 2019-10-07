package com.wkk.dataClean.filter;

import com.wkk.dataClean.rules.BirthRule;
import com.wkk.dataClean.rules.IdRule;
import com.wkk.dataClean.rules.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * @Time: 19-10-2下午4:25
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class ClientFilter extends CsvFilter {
    @Override
    String getCsvName() {
        return "client";
    }

    @Override
    List<Rule> getRules() {
        List<Rule> rules = new ArrayList<Rule>(3);
        rules.add(new IdRule());
        rules.add(new BirthRule());
        rules.add(new IdRule());
        return rules;

    }

    @Override
    String[] getHeaders() {
        return new String[] {"cliend_id", "birth_number", "district_id", "gender", "age", "ageType"};

    }
}
