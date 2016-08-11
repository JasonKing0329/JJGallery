package com.jing.app.jjgallery.viewsystem.main.settings;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.model.main.login.FingerPrintController;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            }  else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.back);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_safety);
        if (!new FingerPrintController(this).isSupported()) {
            findPreference(PreferenceKey.PREF_SAFETY_FP).setEnabled(false);
        }

        // Add 'general' preferences, and a corresponding header.
        PreferenceCategory fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.pref_header_general);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_general);

        // Add 'file manager' preferences, and a corresponding header.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.tab_filemanager);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_filemanager);

        // Add 'sorder' preferences, and a corresponding header.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.tab_sorder);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_sorder);

        // Add 'surf' preferences, and a corresponding header.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.setting_title_surf);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_surf);

        // Add 'waterfall' preferences, and a corresponding header.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.setting_waterfall);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_waterfall);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.

        //general
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_HOME_VIEW));
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_CASUAL_NUMBER));
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_TIMELINE_VIEW));
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_BOOK_SWITCH));

        //file manager
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_FM_VIEW));
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_FM_INDEX_ITEM_OPEN));

        //sorder
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SORDER_VIEW));
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SORDER_PAGE_NUM));
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SORDER_CASCADE_NUM));
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SORDER_GRID_ITEM_OPEN));
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SORDER_INDEX_ITEM_OPEN));
        EditTextPreference preference = (EditTextPreference) findPreference(PreferenceKey.PREF_SORDER_CARD_TOP_NUMBER);
        bindPreferenceSummaryToValue(preference);
        limitEditRange(preference.getEditText(), 5, 20);

        //surf
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SURF_PLAY_MODE));
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SURF_PLAY_SPEED));

        //waterfall
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_WATERFALL_COL));
        bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_WATERFALL_COL_LAND));
    }

    public static void limitEditRange(final EditText editText, final int min, final int max) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    int num = Integer.parseInt(s.toString());
                    if (num > max) {
                        editText.setText("" + max);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || SafetyPreferenceFragment.class.getName().equals(fragmentName)
                || FileMnagerPreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || SOrderPreferenceFragment.class.getName().equals(fragmentName)
                || SurfPreferenceFragment.class.getName().equals(fragmentName)
                || WaterfallPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SafetyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_safety);
            setHasOptionsMenu(true);

            if (!new FingerPrintController(getActivity()).isSupported()) {
                findPreference(PreferenceKey.PREF_SAFETY_FP).setEnabled(false);
            }

        }

    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_HOME_VIEW));
            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_CASUAL_NUMBER));
            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_TIMELINE_VIEW));
            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_BOOK_SWITCH));
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class FileMnagerPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_filemanager);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_FM_VIEW));
            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_FM_INDEX_ITEM_OPEN));
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SOrderPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_sorder);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SORDER_VIEW));
            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SORDER_PAGE_NUM));
            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SORDER_CASCADE_NUM));
            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SORDER_GRID_ITEM_OPEN));
            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SORDER_INDEX_ITEM_OPEN));
            EditTextPreference preference = (EditTextPreference) findPreference(PreferenceKey.PREF_SORDER_CARD_TOP_NUMBER);
            bindPreferenceSummaryToValue(preference);
            limitEditRange(preference.getEditText(), 5, 20);
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SurfPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_surf);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SURF_PLAY_MODE));
            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_SURF_PLAY_SPEED));
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class WaterfallPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_waterfall);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_WATERFALL_COL));
            bindPreferenceSummaryToValue(findPreference(PreferenceKey.PREF_WATERFALL_COL_LAND));
        }

    }
}
