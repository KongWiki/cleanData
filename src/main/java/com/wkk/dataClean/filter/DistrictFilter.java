package com.wkk.dataClean.filter;

import com.sun.xml.internal.bind.v2.model.core.ID;
import com.wkk.dataClean.rules.IdRule;
import com.wkk.dataClean.rules.NumberRule;
import com.wkk.dataClean.rules.Rule;
import com.wkk.dataClean.rules.StringRule;

import java.util.ArrayList;
import java.util.List;

/**
 * @Time: 19-10-2下午4:25
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class DistrictFilter extends CsvFilter {
    @Override
    String getCsvName() {
        return "district";
    }

    @Override
    List<Rule> getRules() {
        List<Rule> rules = new ArrayList<Rule>(8);
        rules.add(new IdRule());
        rules.add(new StringRule());
        rules.add(new StringRule());
        rules.add(new NumberRule());
        rules.add(new NumberRule());
        rules.add(new NumberRule());
        rules.add(new StringRule());
        rules.add(new NumberRule());
        return rules;
    }

    @Override
    String[] getHeaders() {
        return new String[]{"district_id","district_name","region","hab_number","city_number","ave_salary"
                ,"umemploy_rate","crime_number"};
    }
}