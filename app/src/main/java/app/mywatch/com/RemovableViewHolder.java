package app.mywatch.com;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

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
