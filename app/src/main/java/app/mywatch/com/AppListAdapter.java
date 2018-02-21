package app.mywatch.com;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by dragos on 2/19/18.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppViewHolder> {

    private List<AppModel> data;
    private AppCompatActivity activity;

    public AppListAdapter(AppCompatActivity activity, List<AppModel> strings) {
        data = strings;
        this.activity = activity;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        return new AppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AppViewHolder holder, int position) {
        final AppModel model = data.get(position);
        holder.bind(model);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AppDetailsActivity.class);
// Pass data object in the bundle and populate details activity.
                intent.putExtra(AppDetailsActivity.APP_MODEL, model);


                Pair<View, String> p1 = Pair.create((View)holder.appIcon, "appicon");
                Pair<View, String> p2 = Pair.create((View)holder.name, "apptitle");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(activity, p1, p2);

                activity.startActivity(intent, options.toBundle());
            }
        });
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

//        @OnClick(R.id.two_line_item)
//        void onItemClick(int position) {
//            Intent intent = new Intent(this, AppDetailsActivity.class);
////        intent.putExtra(AppDetailsActivity.APP_MODEL, apps.get(position));
////        ActivityOptionsCompat options = ActivityOptionsCompat.
////                makeSceneTransitionAnimation(this, (View)ivProfile, "profile");
//            startActivity(intent);//, options.toBundle());
//        }

    }
}
