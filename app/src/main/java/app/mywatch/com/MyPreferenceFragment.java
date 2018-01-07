package app.mywatch.com;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by dragos on 1/7/18.
 */

public class MyPreferenceFragment extends PreferenceFragmentCompat {


    public static MyPreferenceFragment newInstance() {

        Bundle args = new Bundle();

        MyPreferenceFragment fragment = new MyPreferenceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
}
