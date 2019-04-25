package com.example.hoang.fitness.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.TargetAdapter;
import com.example.hoang.fitness.fragments.TargetFragment;
import com.example.hoang.fitness.models.Target;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.utils.FileUtil;
import com.example.hoang.fitness.utils.JsonUtil;
import com.example.hoang.fitness.utils.SharedPrefsUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinishActivity extends AppCompatActivity {
    @BindView(R.id.tv_workout_name)
    TextView mName;
    @BindView(R.id.tv_exercise_number)
    TextView mNum;
    @BindView(R.id.tv_minute_number)
    TextView mMin;
    @BindView(R.id.tv_calories_number)
    TextView mCalo;
    @BindView(R.id.rl_save_layout)
    RelativeLayout mSave;
    Workout workout;
    private int WORKOUT_ID;
    private String TARGET_NAME;
    private List<Target> list = new ArrayList<>();
    private Target target;
    private int vt = -1;
    int WORKOUT_NUM;
    int MINUTES_NUM;
    int CALORIES_NUM;
    int CUR_STREAK_NUM;
    int BEST_STREAK_NUM;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        ButterKnife.bind(this);
        WORKOUT_ID = getIntent().getIntExtra("WORKOUT_ID", 0);
        TARGET_NAME = getIntent().getStringExtra("TARGET_NAME");
        workout = JsonUtil.getInstance().getWorkout(this, WORKOUT_ID);
        mName.setText(workout.getName());
        mNum.setText(workout.getExercises().size()+"");
        mMin.setText(workout.getTime()+"");
        mCalo.setText(workout.getCalorie()+"");
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WORKOUT_NUM = SharedPrefsUtils.getIntegerPreference(FinishActivity.this,"WORKOUT_NUM",0);
                MINUTES_NUM = SharedPrefsUtils.getIntegerPreference(FinishActivity.this,"MINUTES_NUM",0);
                CALORIES_NUM = SharedPrefsUtils.getIntegerPreference(FinishActivity.this,"CALORIES_NUM",0);
                CUR_STREAK_NUM = SharedPrefsUtils.getIntegerPreference(FinishActivity.this,"CUR_STREAK_NUM",0);
                BEST_STREAK_NUM = SharedPrefsUtils.getIntegerPreference(FinishActivity.this,"BEST_STREAK_NUM",0);
                calCurStreakNum();
                calBestStreakNum();
                SharedPrefsUtils.setIntegerPreference(FinishActivity.this,"WORKOUT_NUM",WORKOUT_NUM+1);
                SharedPrefsUtils.setIntegerPreference(FinishActivity.this,"MINUTES_NUM",MINUTES_NUM+workout.getTime());
                SharedPrefsUtils.setIntegerPreference(FinishActivity.this,"CALORIES_NUM",CALORIES_NUM+workout.getCalorie());
                SharedPrefsUtils.setIntegerPreference(FinishActivity.this,"CUR_STREAK_NUM",CUR_STREAK_NUM);
                SharedPrefsUtils.setIntegerPreference(FinishActivity.this,"BEST_STREAK_NUM",BEST_STREAK_NUM);
                if (TARGET_NAME!=null){
                    list = FileUtil.docFileTarget(FinishActivity.this,"target.txt");
                    TARGET_NAME = getIntent().getStringExtra("TARGET_NAME");
                    for (int i=0;i<list.size();i++){
                        if (list.get(i).getName().equals(TARGET_NAME)) {
                            target = list.get(i);
                            vt = i;
                            break;
                        }
                    }
                    target.setState(target.getState()+1);
                    list.set(vt,target);
                    FileUtil.ghiFileTarget(FinishActivity.this,list);
                    try {
                        TargetAdapter.instance.update();
                    } catch (Exception e){

                    }
                    if (target.getState()==target.getNumDay()){
                        TargetFragment.alarmManager.cancel(
                                TargetFragment.pendingIntent[vt]
                        );
                        congrateDialog();
                    } else {
                        startActivity(new Intent(FinishActivity.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                } else {
                    startActivity(new Intent(FinishActivity.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
            }
        });
    }
    private void calCurStreakNum(){
        ArrayList<Integer> listDay = getListDay();
        ArrayList<Integer> listMonth = getListMonth();
        ArrayList<Integer> listYear = getListYear();
        if (listDay.size()!=0){
            CalendarDay calendarDay = CalendarDay.from(listYear.get(listYear.size()-1),
                    listMonth.get(listMonth.size()-1),listDay.get(listDay.size()-1));
            CalendarDay calendarToday = CalendarDay.today();
            if (!calendarDay.equals(calendarToday)) {
                addDate();
                calendarDay = CalendarDay.from(calendarDay.getDate().plusDays(1));
                if (calendarDay.equals(calendarToday)){
                    CUR_STREAK_NUM++;
                } else {
                    CUR_STREAK_NUM = 1;
                }
            }
        } else {
            CUR_STREAK_NUM++;
            addDate();
        }

    }

    private void calBestStreakNum(){
        if (CUR_STREAK_NUM>BEST_STREAK_NUM) BEST_STREAK_NUM = CUR_STREAK_NUM;
    }

    //danh dau ngay tap
    private void addDate(){
        CalendarDay calendarToday = CalendarDay.today();
        addDay(calendarToday.getDay());
        addMonth(calendarToday.getMonth());
        addYear(calendarToday.getYear());
    }

    private ArrayList<Integer> getListDay(){
        ArrayList<Integer> list = new ArrayList<>();
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_DAY");
        try {
            String [] s = day.split(" ");
            for (String d: s){
                list.add(Integer.parseInt(d));
            }
        } catch (NullPointerException e){

        }

        return list;
    }

    private ArrayList<Integer> getListMonth(){
        ArrayList<Integer> list = new ArrayList<>();
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_MONTH");
        try {
            String [] s = day.split(" ");
            for (String d: s){
                list.add(Integer.parseInt(d));
            }
        } catch (NullPointerException e){

        }
        return list;
    }

    private ArrayList<Integer> getListYear(){
        ArrayList<Integer> list = new ArrayList<>();
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_YEAR");
        try {
            String [] s = day.split(" ");
            for (String d: s){
                list.add(Integer.parseInt(d));
            }
        } catch (NullPointerException e){

        }
        return list;
    }

    private void addDay(int d){
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_DAY");
        String day_new = (day==null?"":day)+d+" ";
        SharedPrefsUtils.setStringPreference(this,"LIST_DAY",day_new);
    }

    private void addMonth(int d){
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_MONTH");
        String day_new = (day==null?"":day)+d+" ";
        SharedPrefsUtils.setStringPreference(this,"LIST_MONTH",day_new);
    }

    private void addYear(int d){
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_YEAR");
        String day_new = (day==null?"":day)+d+" ";
        SharedPrefsUtils.setStringPreference(this,"LIST_YEAR",day_new);
    }

    @Override
    public void onBackPressed() {
    }

    public void congrateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Mục tiêu: "+TARGET_NAME)
                .setMessage("Chúc mừng bạn đã hoàn thành mục tiêu "+TARGET_NAME)
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(FinishActivity.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
