package com.nutritech.Helpers;

import android.support.v7.widget.RecyclerView;

/**
 * Created by alexislefebvre on 01/05/2019.
 */

public interface RecyclerItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
