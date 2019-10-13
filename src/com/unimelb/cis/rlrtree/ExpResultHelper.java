package com.unimelb.cis.rlrtree;

import com.unimelb.cis.utils.ExpReturn;

import java.util.ArrayList;
import java.util.List;

public class ExpResultHelper {

    List<ExpReturn> results;

    ExpResultHelper() {
        results = new ArrayList<>();
    }

    public void addReturn(ExpReturn expReturn) {
        results.add(expReturn);
    }

    public ExpReturn getResult() {
        long time = 0;
        int pageaccess = 0;
        double accuracy = 0;
        for (int i = 0; i < results.size(); i++) {
            time += results.get(i).time;
            pageaccess += results.get(i).pageaccess;
            accuracy += results.get(i).accuracy;
        }
        ExpReturn result = new ExpReturn();
        result.time = time / results.size();
        result.pageaccess = pageaccess / results.size();
        result.accuracy = accuracy / results.size();
        return result;
    }

}
