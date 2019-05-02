package com.nutritech.Helpers;

import android.view.View;

/**
 * Created by alexislefebvre on 01/05/2019.
 */

public interface ItemClickListener {
    void onClick(View v, int adapterPosition, boolean isLongClick);
}
