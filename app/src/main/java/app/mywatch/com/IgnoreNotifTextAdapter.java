package app.mywatch.com;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dragos on 2/26/18.
 */

public class IgnoreNotifTextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private AppModel appModel;

    public IgnoreNotifTextAdapter(AppModel appModel) {
        this.appModel = appModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_ignore, parent, false);
            return new IgnoreVH(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.header_ignore_list, parent, false);
            return new HeaderVH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof IgnoreVH)
            ((IgnoreVH) holder).textView.setText(appModel.getIgnoreList().get(position - 1));

        else if (holder instanceof HeaderVH) {
            //cast holder to VHHeader and set data for header.
            HeaderVH header = (HeaderVH) holder;
            header.allowNotifications.setChecked(appModel.getAllowNotifications());
            header.packageName.setText(appModel.getPackageName());
        }

    }

    @Override
    public int getItemCount() {
        return appModel.getIgnoreList() != null ? appModel.getIgnoreList().size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    class HeaderVH extends RecyclerView.ViewHolder {
        @BindView(R.id.header_package)
        TextView packageName;

        @BindView(R.id.header_switch)
        Switch allowNotifications;

        public HeaderVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class IgnoreVH extends RemovableViewHolder {
        @BindView(R.id.ignore_text)
        TextView textView;

        public IgnoreVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
