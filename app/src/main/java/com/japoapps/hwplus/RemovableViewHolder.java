package com.japoapps.hwplus;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dragos on 2/28/18.
 */

public class RemovableViewHolder extends RecyclerView.ViewHolder {

    View viewForeground;

    public RemovableViewHolder(View itemView) {
        super(itemView);
        viewForeground = itemView.findViewById(R.id.removable_foreground);
    }
}
