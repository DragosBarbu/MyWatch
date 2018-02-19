package app.mywatch.com;

import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dragos on 2/19/18.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppViewHolder> {

    private List<AppModel> data;

    public AppListAdapter(List<AppModel> strings) {
        data = strings;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        return new AppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        holder.bind(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class AppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.app_name)
        TextView name;

        @BindView(R.id.app_package_name)
        TextView packageName;

        @BindView(R.id.app_icon)
        ImageView appIcon;

        AppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        private void bind(AppModel appModel) {
            name.setText(appModel.getName());
            packageName.setText(appModel.getPackageName());

            try {
                Drawable icon = name.getContext().getPackageManager().getApplicationIcon(appModel.getPackageName());
                appIcon.setImageDrawable(icon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }

    }
}
