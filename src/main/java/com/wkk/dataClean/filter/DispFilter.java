package com.wkk.dataClean.filter;

import com.wkk.dataClean.rules.DispTypeEnumRule;
import com.wkk.dataClean.rules.IdRule;
import com.wkk.dataClean.rules.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * @Time: 19-10-2下午4:25
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class DispFilter  extends CsvFilter{

    @Override
    String getCsvName() {
        return "disp";
    }

    @Override
    List<Rule> getRules() {
        List<Rule> rules = new ArrayList<Rule>(4);
        rules.add(new IdRule());
        rules.add(new IdRule());
        rules.add(new IdRule());
        rules.add(new DispTypeEnumRule());
        return rules;
    }

    @Override
    String[] getHeaders() {
        return new String[]{"disp_id", "cliend_id", "account_id", "type"};

    }
}
