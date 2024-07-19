/*
 * Copyright (C) 2024 ProjectEverest
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.everest.basecamp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Surface;
import android.widget.LinearLayout;
import android.widget.ImageView;

import com.android.internal.logging.nano.MetricsProto;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.everest.basecamp.fragments.*;

import android.content.Intent;
import android.widget.RelativeLayout;

import com.google.android.material.card.MaterialCardView;

public class BaseCamp extends SettingsPreferenceFragment implements View.OnClickListener {

    private LinearLayout[] settingCards;
    private MaterialCardView mLockScreenSettingsCard, wallpapercard;
    private RelativeLayout abouteverest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.basecamp, container, false);
        settingCards = new LinearLayout[]{
                view.findViewById(R.id.qscard),
                view.findViewById(R.id.statusbarcard),
                view.findViewById(R.id.themecard),
                view.findViewById(R.id.lscard),
                view.findViewById(R.id.gesturecard),
                view.findViewById(R.id.notificationcard),
                view.findViewById(R.id.systemcard),
                view.findViewById(R.id.miscscard),
                view.findViewById(R.id.buttonscard)
        };
        for (LinearLayout card : settingCards) {
            card.setOnClickListener(this);
        }
        mLockScreenSettingsCard = view.findViewById(R.id.lscard);
        mLockScreenSettingsCard.setOnClickListener(this);

        wallpapercard = view.findViewById(R.id.wallpapercard);
        wallpapercard.setOnClickListener(this);

        abouteverest = view.findViewById(R.id.abouteverest);
        abouteverest.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Fragment fragment = null;
        String title = null;
        if (id == R.id.qscard) {
            fragment = new QuickSettings();
            title = getString(R.string.quicksettings_title);
        } else if (id == R.id.statusbarcard) {
            fragment = new StatusBarSettings();
            title = getString(R.string.statusbar_title);
        } else if (id == R.id.lscard) {
            fragment = new LockScreenSettings();
            title = getString(R.string.lockscreen_title);
        } else if (id == R.id.buttonscard) {
            fragment = new ButtonSettings();
            title = getString(R.string.button_title);
        } else if (id == R.id.gesturecard) {
            fragment = new GestureSettings();
            title = getString(R.string.gestures_title);
        } else if (id == R.id.notificationcard) {
            fragment = new NotificationSettings();
            title = getString(R.string.notifications_title);
        } else if (id == R.id.themecard) {
            fragment = new ThemeSettings();
            title = getString(R.string.theme_title);
        } else if (id == R.id.systemcard) {
            fragment = new SystemSettings();
            title = getString(R.string.system_title);
        } else if (id == R.id.miscscard) {
            fragment = new MiscSettings();
            title = getString(R.string.misc_title);
        } else if (id == R.id.abouteverest) {
            fragment = new AboutSettings();
            title = getString(R.string.about_title);
        } else if (id == R.id.wallpapercard) {
            launchWallpaperPickerActivity();
            return;
        }

        if (fragment != null && title != null) {
            replaceFragment(fragment, title);
        }
    }

    private void replaceFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_slide_in, R.anim.fragment_slide_out);
            transaction.replace(this.getId(), fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            getActivity().setTitle(title != null ? title : "Everest Basecamp");
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.EVEREST;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Everest Basecamp");
    }

    private void launchWallpaperPickerActivity() {
        Intent intent = new Intent();
        intent.setClassName("com.google.android.apps.wallpaper", "com.google.android.apps.wallpaper.picker.CategoryPickerActivity");
        startActivity(intent);
    }

    public static void lockCurrentOrientation(Activity activity) {
        int currentRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation = activity.getResources().getConfiguration().orientation;
        int frozenRotation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        switch (currentRotation) {
            case Surface.ROTATION_0:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case Surface.ROTATION_90:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            case Surface.ROTATION_270:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }
        activity.setRequestedOrientation(frozenRotation);
    }
}
