package com.sillebille.earthquakeviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sillebille.earthquakeviewer.ui.main.EarthQuakeListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, EarthQuakeListFragment.newInstance())
                    .commitNow();
        }
    }
}