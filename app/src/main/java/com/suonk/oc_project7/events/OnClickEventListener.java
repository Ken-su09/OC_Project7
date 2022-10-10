package com.suonk.oc_project7.events;

import android.view.View;

public interface OnClickEventListener {
    void onRestaurantClick(View view, String id);
    void onWorkmateClick(View view, String id);
}