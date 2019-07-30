package com.unimelb.cis;

import com.unimelb.cis.structures.zrtree.ZRtree;

public class Main {

    public static void main(String[] args) {
        ZRtree zRtree = new ZRtree();
        zRtree.buildRtree("datasets/normal_2000000_.csv");
        zRtree.output("datasets/Z_normal_2000000_.csv");
    }

}
