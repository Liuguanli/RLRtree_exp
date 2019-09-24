package com.unimelb.cis.rlrtree;

import com.unimelb.cis.structures.IRtree;

public interface RtreeFinishCallback {

    void onFinish(IRtree rtree);

    void onError(IRtree rtree);

}
