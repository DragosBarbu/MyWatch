package app.mywatch.com;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dragos on 2/21/18.
 */

public class AppDetailsActivity extends AppCompatActivity {

    @BindView(R.id.details_app_icon)
    ImageView imageView;
    @BindView(R.id.details_title)
    TextView titleText;

    @BindView(R.id.details_ignore_list)
    RecyclerView ignoreRecycler;

    @BindView(R.id.details_fab)
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        AppModel appModel = getIntent().getParcelableExtra(APP_MODEL);
        setupViews(appModel);
    }


    private void setupViews(AppModel appModel) {
        AppModel model = getIntent().getParcelableExtra(APP_MODEL);

        titleText.setText(model.getName());
        try {
            Drawable icon = getPackageManager().getApplicationIcon(model.getPackageName());
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ignoreRecycler.setLayoutManager(mLayoutManager);
        ignoreRecycler.setItemAnimator(new DefaultItemAnimator());
        ignoreRecycler.setAdapter(new IgnoreNotifTextAdapter(appModel));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final String APP_MODEL = "app_model_tag";
}
