package com.japoapps.hwplus;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.List;

import com.japoapps.hwplus.models.AppModel;
import com.japoapps.hwplus.notificationService.NotificationSenderFactory;
import com.japoapps.hwplus.repos.AppRepository;
import bolts.Continuation;
import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.apps_list)
    RecyclerView recyclerView;

    private static final int PERMISSION_REQUESTCODE = 142;
    AppListAdapter adapter;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_CALENDAR};

    private static final String SHARED_PREFFERENCE_NAME = "MyWatchPref";
    private static final String SHARED_PREFFERENCE_VERSION_CODE_KEY = "version_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUESTCODE);
        }
        checkFirstRun();
        AppRepository.getInstance().getAllAppsAsync(this).continueWith(new Continuation<List<AppModel>, Void>() {
            @Override
            public Void then(final Task<List<AppModel>> task) throws Exception {
                if (!task.isFaulted())
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setRecyclerView(task.getResult());
                        }
                    });
                return null;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkSubscriptionToNotifications())
            ViewHelper.getSnackbar(recyclerView, getString(R.string.permission_read_notifications), ViewHelper.SNACKBAR_TYPE.ERROR, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.fix, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                            startActivity(intent);
                        }
                    })
                    .show();
    }

    private void setRecyclerView(List<AppModel> apps) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AppListAdapter(MainActivity.this, apps);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, MainActivity.this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    showExplanationDialog();
                    break;
                }
            }
        }
    }

    private void showExplanationDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.permission_explanation)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUESTCODE);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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

    private void checkFirstRun() {
        final int notAvailable = -1;
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFFERENCE_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(SHARED_PREFFERENCE_VERSION_CODE_KEY, notAvailable);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            // normal run
            return;
        } else if (savedVersionCode == notAvailable) {
            //first run
            AppRepository.getInstance().addDefaultApps(this)
                    .continueWith(new Continuation<Void, Void>() {
                        @Override
                        public Void then(Task<Void> task) throws Exception {
                            adapter.notifyDataSetChanged();
                            return null;
                        }
                    });
        } else if (currentVersionCode > savedVersionCode) {
            // upgrade
        }

        prefs.edit().putInt(SHARED_PREFFERENCE_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    private boolean checkSubscriptionToNotifications() {
        boolean hasPermission = false;
        for (String service : NotificationManagerCompat.getEnabledListenerPackages(this)) {
            if (service.equals(getPackageName()))
                hasPermission = true;
        }
        return hasPermission;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        String pckName = AppRepository.getInstance().getAllApps().get(position).getPackageName();
        AppRepository.getInstance().removeAppAsync(this, pckName);
        adapter.notifyItemRemoved(position);
    }
}
