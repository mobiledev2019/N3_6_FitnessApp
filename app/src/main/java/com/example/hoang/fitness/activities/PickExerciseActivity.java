package com.example.hoang.fitness.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.PickExerciseAdapter;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.models.ExercisePick;
import com.example.hoang.fitness.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickExerciseActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_pick_exercise_type)
    TextView mType;
    @BindView(R.id.rcv_pick_exercise)
    RecyclerView mPickExercises;
    @BindView(R.id.btn_nextstep)
    Button mNextStep;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    List<ExercisePick> list;
    PickExerciseAdapter adapter;
    private int mStep = 1;
    TextView tv_title_toolbar;
    TextView tv_cancel_toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_exercise);
        ButterKnife.bind(this);
        mType.setText(getDescription(mStep));
        list = new ArrayList<>();
        for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(mStep))){
            list.add(new ExercisePick(e,false));
        }
        adapter = new PickExerciseAdapter(this,list);
        mPickExercises.setAdapter(adapter);
        mPickExercises.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tv_title_toolbar = mToolbar.findViewById(R.id.tv_title_toolbar);
        tv_cancel_toolbar = mToolbar.findViewById(R.id.tv_cancel_toolbar);
    }
    private String getBodyPart(int i) {
        String str = "TOTAL BODY";
        switch (i) {
            case 1:
                return "TOTAL BODY";
            case 2:
                return "LOWER BODY";
            case 3:
                return "UPPER BODY";
            case 4:
                return "CORE";
            default:
                return str;
        }
    }

    private String getDescription(int i) {
        String str = "";
        switch (i) {
            case 1:
                return "pick 3 total body exercises";
            case 2:
                return "pick 3 lower body exercises";
            case 3:
                return "pick 3 upper body exercises";
            case 4:
                return "pick 3 core exercises";
            case 5:
                return "confirm workout order";
            default:
                return str;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
