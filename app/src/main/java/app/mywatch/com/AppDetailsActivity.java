package app.mywatch.com;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dragos on 2/21/18.
 */

public class AppDetailsActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    public static final String APP_MODEL = "app_model_tag";

    @BindView(R.id.details_app_icon)
    ImageView imageView;
    @BindView(R.id.details_title)
    TextView titleText;

    @BindView(R.id.details_ignore_list)
    RecyclerView ignoreRecycler;

    AppModel appModel;
    private IgnoreNotifTextAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        appModel = getIntent().getParcelableExtra(APP_MODEL);
        setupViews();
    }


    private void setupViews() {
        titleText.setText(appModel.getName());
        try {
            Drawable icon = getPackageManager().getApplicationIcon(appModel.getPackageName());
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ignoreRecycler.setLayoutManager(mLayoutManager);
        ignoreRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter = new IgnoreNotifTextAdapter(appModel);
        ignoreRecycler.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(ignoreRecycler);

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

    private void addToIgnoreList(String text) {
        if (text != null)
            text = text.trim();
        if (TextUtils.isEmpty(text))
            return;
        if (appModel != null) {
            appModel.addToIgnoreList(text);
            AppRepository.getInstance().updateAppModel(appModel);
            adapter.notifyItemInserted(appModel.getIgnoreList().size());
        }
    }

    @OnClick(R.id.details_fab)
    public void fabClick(View view) {
        FrameLayout frameLayout = new FrameLayout(AppDetailsActivity.this);
        final EditText edittext = new EditText(AppDetailsActivity.this);
        frameLayout.addView(edittext);
        ViewHelper.setMargins(edittext, 24, 8, 24, 8);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_text_title)
                .setMessage(R.string.add_text_warning)
                .setView(frameLayout)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addToIgnoreList(edittext.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        appModel.getIgnoreList().remove(position - 1);
        adapter.notifyItemRemoved(position);
    }
}
