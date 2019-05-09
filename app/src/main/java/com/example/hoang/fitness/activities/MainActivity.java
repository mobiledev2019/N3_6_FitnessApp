package com.example.hoang.fitness.activities;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.fragments.CustomFragment;
import com.example.hoang.fitness.fragments.ProgressFragment;
import com.example.hoang.fitness.fragments.WorkoutsFragment;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int TAB_WORKOUTS = 0;
    public static final int TAB_CUSTOM = 1;
    public static final int TAB_PROGRESS = 2;
    private List<Fragment> listFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_workouts:
                openTab(TAB_WORKOUTS);
                return true;
            case R.id.navigation_custom:
                openTab(TAB_CUSTOM);
                return true;
            case R.id.navigation_progress:
                openTab(TAB_PROGRESS);
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(getApplicationContext());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragmet();
        openTab(TAB_WORKOUTS);
    }

    private void initFragmet(){
        listFragment = new ArrayList<>();
        listFragment.add(new WorkoutsFragment());
        listFragment.add(new CustomFragment());
        listFragment.add(new ProgressFragment());
    }
    private void openTab(int index){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fr_home, listFragment.get(index))
                .commit();
    }
}
