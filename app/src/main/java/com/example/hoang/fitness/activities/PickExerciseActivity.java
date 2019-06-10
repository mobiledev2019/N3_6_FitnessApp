package com.example.hoang.fitness.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.PickExerciseAdapter;
import com.example.hoang.fitness.adapters.PickExerciseAdapter2;
import com.example.hoang.fitness.listener.ItemOnClick;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.models.ExercisePick;
import com.example.hoang.fitness.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickExerciseActivity extends AppCompatActivity implements View.OnClickListener, ItemOnClick {
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
    PickExerciseAdapter2 adapter2;
    private int mStep = 1;
    private int count[] = new int[10];
    private ArrayList<Integer> dd[] = new ArrayList[10];
    TextView tv_title_toolbar;
    TextView tv_cancel_toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_exercise);
        ButterKnife.bind(this);
        mType.setText(getDescription(mStep));
        list = new ArrayList<>();
        for (int i=1;i<=6;i++) {
            count[i] = 0;
            dd[i] = new ArrayList<Integer>();
        }
        for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(mStep))){
            list.add(new ExercisePick(e,false));
        }
        adapter = new PickExerciseAdapter(this,list,this::clickItem,count[mStep]);
        mPickExercises.setAdapter(adapter);
        mPickExercises.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        tv_title_toolbar = mToolbar.findViewById(R.id.tv_title_toolbar);
        tv_cancel_toolbar = mToolbar.findViewById(R.id.tv_cancel_toolbar);
        mNextStep.setOnClickListener(this::onClick);
        mNextStep.setAlpha((float) 0.5);
        mNextStep.setClickable(false);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mStep>1) {
                    mStep--;
                    List<ExercisePick> list = new ArrayList<>();
                    for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(mStep))){
                        list.add(new ExercisePick(e,false));
                    }
                    for (int i=0;i<dd[mStep].size();i++)
                        list.get(dd[mStep].get(i)).setChoose(true);
                    updateView(list);
                } else {
                    onBackPressed();
                }
                break;
        }
        return true;
    }

    public void updateView(List<ExercisePick> list){
        this.list.clear();
        this.list.addAll(list);
        adapter = new PickExerciseAdapter(this,this.list,this::clickItem,count[mStep]);
        mPickExercises.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tv_title_toolbar.setText("STEP "+mStep+" OF 6");
        mType.setText(getDescription(mStep));
        if (count[mStep]==3){
            mNextStep.setAlpha(1);
            mNextStep.setClickable(true);
        } else {
            mNextStep.setAlpha((float) 0.5);
            mNextStep.setClickable(false);
        }
    }

    public void updateView2(List<ExercisePick> list){
        this.list.clear();
        this.list.addAll(list);
        adapter2 = new PickExerciseAdapter2(this,this.list,this::clickItem,count[mStep]);
        mPickExercises.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        tv_title_toolbar.setText("STEP "+mStep+" OF 6");
        mType.setText(getDescription(mStep));
        mNextStep.setAlpha(1);
        mNextStep.setClickable(true);
    }

    @Override
    public void onClick(View v) {
        if (v==mNextStep){
            mNextStep.setClickable(false);
            mNextStep.setAlpha((float) 0.5);
            mStep++;
            if (mStep==5){
                List<ExercisePick> list = new ArrayList<>();
                for (int i=1;i<=4;i++){
                    List<ExercisePick> lists = new ArrayList<>();
                    for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(i))){
                        lists.add(new ExercisePick(e,false));
                    }
                    for (int j=0;j<dd[i].size();j++) list.add(lists.get(dd[i].get(j)));
                }
                updateView2(list);
            } else {
                List<ExercisePick> list = new ArrayList<>();
                for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(mStep))){
                    list.add(new ExercisePick(e,false));
                }
                for (int i=0;i<dd[mStep].size();i++)
                    list.get(dd[mStep].get(i)).setChoose(true);
                updateView(list);
            }
        }
    }

    @Override
    public void clickItem(int pos, boolean isChoose) {
        if (isChoose) {
            count[mStep]++;
            dd[mStep].add(pos);
        }
        else {
            count[mStep]--;
            for (int i=0;i<dd[mStep].size();i++)
                if (dd[mStep].get(i)==pos){
                    dd[mStep].remove(i);
                    break;
                }

        }
        list.get(pos).setChoose(isChoose);
        if (count[mStep]==3){
            mNextStep.setAlpha(1);
            mNextStep.setClickable(true);
            adapter = new PickExerciseAdapter(this,this.list,this::clickItem,count[mStep]);
            mPickExercises.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Toast.makeText(PickExerciseActivity.this,"3",Toast.LENGTH_SHORT).show();
        } else {
            mNextStep.setAlpha((float) 0.5);
            mNextStep.setClickable(false);
            adapter = new PickExerciseAdapter(this,this.list,this::clickItem,count[mStep]);
            mPickExercises.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Toast.makeText(PickExerciseActivity.this,"<3",Toast.LENGTH_SHORT).show();
        }
    }
}
