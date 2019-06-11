package com.example.hoang.fitness.activities;

import android.content.Intent;
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
import com.example.hoang.fitness.adapters.ExerciseAdapter;
import com.example.hoang.fitness.models.CustomWorkout;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.utils.FileUtil;
import com.example.hoang.fitness.utils.JsonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutDetailActivity2 extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_workout_description)
    TextView mDes;
    @BindView(R.id.rcv_exercise)
    RecyclerView mExercises;
    @BindView(R.id.btn_start_workout)
    Button mStart;
    private ExerciseAdapter adapter;
    private List<Exercise> list;
    private String WORKOUT_NAME;
    TextView tv_title_toolbar;
    TextView tv_cancel_toolbar;
    List<CustomWorkout> customWorkouts;
    CustomWorkout customWorkout;
    int vt = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail2);
        ButterKnife.bind(this);
        WORKOUT_NAME = getIntent().getStringExtra("WORKOUT_NAME");
        customWorkouts = FileUtil.docFileCustomWorkout(this,"customworkout.txt");
        for (int i=0; i<customWorkouts.size();i++)
            if (WORKOUT_NAME.equals(customWorkouts.get(i).getName())) {
                customWorkout = customWorkouts.get(i);
                vt = i;
                break;
            }
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        tv_title_toolbar = mToolbar.findViewById(R.id.tv_title_toolbar);
        tv_cancel_toolbar = mToolbar.findViewById(R.id.tv_cancel_toolbar);
        tv_title_toolbar.setText(customWorkout.getName());
        tv_cancel_toolbar.setText("Delete");
        mDes.setText(customWorkout.getTime()+" mins | "+customWorkout.getCircuit()+" cycle | "+
                customWorkout.getWorkoutRestTime()+"s rest | "+customWorkout.getCalorie()+" calories");
        list = JsonUtil.getInstance().getListExercise(this,customWorkout);
        adapter = new ExerciseAdapter(this,list);
        mExercises.setAdapter(adapter);
        mExercises.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mExercises.setNestedScrollingEnabled(false);
        mExercises.setHasFixedSize(false);
        mStart.setOnClickListener(this::onClick);
        tv_cancel_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customWorkouts.remove(vt);
                FileUtil.ghiFileCustomWorkout(WorkoutDetailActivity2.this,customWorkouts);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(WorkoutDetailActivity2.this,PlayingActivity.class);
        intent.putExtra("WORKOUT_NAME",WORKOUT_NAME);
        startActivity(intent);
    }

}
