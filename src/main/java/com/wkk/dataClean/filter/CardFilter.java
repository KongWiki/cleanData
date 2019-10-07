package com.wkk.dataClean.filter;

import com.wkk.dataClean.rules.AccountDateRule;
import com.wkk.dataClean.rules.CardTypeEnumRule;
import com.wkk.dataClean.rules.IdRule;
import com.wkk.dataClean.rules.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * @Time: 19-10-2下午4:24
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class CardFilter extends CsvFilter{
    @Override
    String getCsvName() {
        return "card";
    }

    @Override
    List<Rule> getRules() {
        List<Rule> rules = new ArrayList<Rule>(4);
        rules.add(new IdRule());
        rules.add(new IdRule());
        rules.add(new CardTypeEnumRule());
        rules.add(new AccountDateRule());
        return rules;
    }

    @Override
    String[] getHeaders() {
        return new String[]{"card_id", "disp_id", "type", "issued"};

    }
}