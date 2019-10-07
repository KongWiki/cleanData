package com.wkk.dataClean.filter;

import com.wkk.dataClean.rules.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Time: 19-10-2下午4:26
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class TranFilter extends CsvFilter{
    @Override
    String getCsvName() {
        return "trans";
    }

    @Override
    List<Rule> getRules() {
        List<Rule> rules = new ArrayList<Rule>(10);
        rules.add(new IdRule());
        rules.add(new IdRule());
        rules.add(new TranDateRule());
        rules.add(new TranTypeRule());
        rules.add(new TranOperaRule());
        rules.add(new IdRule());
        rules.add(new IdRule());
        rules.add(new TranKSymbolRule());
        rules.add(new StringRule());
        rules.add(new StringRule());
        return rules;
    }

    @Override
    String[] getHeaders() {
        return new String[]{"trans_id","account_id","date","type","operation","amount","balance","k_symbol","bank","account"};
    }
}
