package com.wkk.dataClean.main;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.wkk.dataClean.filter.*;

/**
 * @Time: 19-10-2下午4:26
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class Main {
    public static void main(String[] args) {
        new AccountFilter().processCsv();
        new CardFilter().processCsv();
        new ClientFilter().processCsv();
        new DispFilter().processCsv();
        new DistrictFilter().processCsv();
        new LoanFilter().processCsv();
        new OrderFilter().processCsv();
        new TranFilter().processCsv();
    }
}
