package app.mywatch.com;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.List;

import app.mywatch.com.models.AppModel;
import app.mywatch.com.repos.AppRepository;
import bolts.Continuation;
import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.apps_list)
    RecyclerView recyclerView;

    private static final int PERMISSION_REQUESTCODE = 142;
    AppListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            requestPermission(Manifest.permission.WRITE_CALENDAR);
            requestPermission(Manifest.permission.READ_CALENDAR);
        }
        AppRepository.getInstance().getAllAppsAsync(this).continueWith(new Continuation<List<AppModel>, Void>() {
            @Override
            public Void then(Task<List<AppModel>> task) throws Exception {
                if (!task.isFaulted())
                    setRecyclerView(task.getResult());
                return null;
            }
        });


    }

    private void setRecyclerView(List<AppModel> apps) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AppListAdapter(this, apps);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send_test) {
            sendTestNotification();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUESTCODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (permission.equals(Manifest.permission.WRITE_CALENDAR) && grantResult != PackageManager.PERMISSION_GRANTED)
                    requestPermission(Manifest.permission.WRITE_CALENDAR);
                if (permission.equals(Manifest.permission.READ_CALENDAR) && grantResult != PackageManager.PERMISSION_GRANTED)
                    requestPermission(Manifest.permission.READ_CALENDAR);
            }
        }
    }

    private void requestPermission(final String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(R.string.permission_explanation)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{permission},
                                        PERMISSION_REQUESTCODE);
                            }
                        })
                        .setNegativeButton(R.string.cancel, null).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        PERMISSION_REQUESTCODE);
            }
        }
    }

    private void sendTestNotification() {
        NotificationSenderFactory.createInstance(NotificationSenderFactory.NotificationType.CALENDAR)
                .send(getString(R.string.test), MainActivity.this);
        Snackbar.make(recyclerView, R.string.test_notification, Snackbar.LENGTH_LONG).show();
    }

    private void addAppModel(String packageName) {
        if (packageName != null)
            packageName = packageName.trim();
        if (TextUtils.isEmpty(packageName))
            return;

        String appName = getAppName(packageName);
        if (appName == null) {
            // show error?
            appName = packageName;
        }
        AppRepository.getInstance()
                .addAppAsync(this, appName, packageName)
                .continueWith(
                        new Continuation<Void, Void>() {
                            @Override
                            public Void then(Task<Void> task) throws Exception {
                                adapter.notifyItemChanged(AppRepository.getInstance().getAllApps().size());
                                return null;
                            }
                        }
                );
    }


    @OnClick(R.id.fab)
    public void fabClick(View view) {
        FrameLayout frame = new FrameLayout(MainActivity.this);
        final EditText edittext = new EditText(MainActivity.this);
        frame.addView(edittext);
        ViewHelper.setMargins(edittext, 24, 8, 24, 8);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_app)
                .setMessage(R.string.add_app_text)
                .setView(frame)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addAppModel(edittext.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public String getAppName(String packageName) {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            String appName = (String) packageManager.getApplicationLabel(info);
            return appName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
