package com.unimelb.cis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
//        ZRtree zRtree = new ZRtree();
////        zRtree.buildRtree("datasets/normal_2000000_.csv");
////        zRtree.output("datasets/Z_normal_2000000_.csv");
//        zRtree.buildRtreeAfterTuning("datasets/Z_normal_2000000_new.csv", 2,3);
//        zRtree.getRoot();
        ExpExecuter executer = new ExpExecuter.PythonCommandBuilder()
                .buildPythonFile("/Users/guanli/Documents/codes/DL_Rtree/structure/rtree.py")
                .buildInputFile("/Users/guanli/Documents/codes/DL_Rtree/dataset/Z_normal_2000000_.csv")
                .buildOutputFile("/Users/guanli/Documents/codes/DL_Rtree/dataset/Z_normal_2000000_new.csv")
                .buildDim(2)
                .buildLevel(3)
                .buildPagesize(102)
                .build();

        executer.executePythonCommand(null, new Callback() {
            @Override
            public void onFinish() {
                System.out.println("RL Finish");
            }

            @Override
            public void onError() {
                System.out.println("RL error");
            }
        });

    }

}
