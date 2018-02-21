package app.mywatch.com;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setupViews();
    }

    private void setupViews() {
        AppModel model = getIntent().getParcelableExtra(APP_MODEL);

        titleText.setText(model.getName());
        try {
            Drawable icon = getPackageManager().getApplicationIcon(model.getPackageName());
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

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
