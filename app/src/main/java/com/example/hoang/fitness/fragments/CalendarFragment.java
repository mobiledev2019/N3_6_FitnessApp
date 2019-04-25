package com.example.hoang.fitness.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hoang.fitness.R;
import com.example.hoang.fitness.activities.FinishActivity;
import com.example.hoang.fitness.utils.SharedPrefsUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarFragment extends Fragment {
    @BindView(R.id.tv_workout_number)
    TextView mWorkoutNum;
    @BindView(R.id.tv_minute_number)
    TextView mMinutesNum;
    @BindView(R.id.tv_calories_number)
    TextView mCaloriesNum;
    @BindView(R.id.tv_curlink_number)
    TextView mCurStreakNum;
    @BindView(R.id.tv_bestlink_number)
    TextView mBestStreakNum;
    @BindView(R.id.calendarView)
    MaterialCalendarView mCalendar;
    ArrayList<CalendarDay> dates = new ArrayList<>();
    int WORKOUT_NUM;
    int MINUTES_NUM;
    int CALORIES_NUM;
    int CUR_STREAK_NUM;
    int BEST_STREAK_NUM;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container,false);
        ButterKnife.bind(this,view);
        WORKOUT_NUM = SharedPrefsUtils.getIntegerPreference(getActivity(),"WORKOUT_NUM",0);
        MINUTES_NUM = SharedPrefsUtils.getIntegerPreference(getActivity(),"MINUTES_NUM",0);
        CALORIES_NUM = SharedPrefsUtils.getIntegerPreference(getActivity(),"CALORIES_NUM",0);
        CUR_STREAK_NUM = SharedPrefsUtils.getIntegerPreference(getActivity(),"CUR_STREAK_NUM",0);
        BEST_STREAK_NUM = SharedPrefsUtils.getIntegerPreference(getActivity(),"BEST_STREAK_NUM",0);
        calCurStreakNum();
        mWorkoutNum.setText(WORKOUT_NUM+"");
        mMinutesNum.setText(MINUTES_NUM+"");
        mCaloriesNum.setText(CALORIES_NUM+"");
        mCurStreakNum.setText(CUR_STREAK_NUM+"");
        mBestStreakNum.setText(BEST_STREAK_NUM+"");
        dates = getListDates();
        mCalendar.setCurrentDate(CalendarDay.today());
        mCalendar.setDateSelected(CalendarDay.today(),true);
        mCalendar.addDecorator(new EventDecorator(Color.parseColor("#ff0eb664"),dates));
        return view;
    }

    private ArrayList<CalendarDay> getListDates(){
        ArrayList<CalendarDay> dates = new ArrayList<>();
        ArrayList<Integer> listDay = getListDay();
        ArrayList<Integer> listMonth = getListMonth();
        ArrayList<Integer> listYear = getListYear();
        for (int i=0;i<listDay.size();i++){
            CalendarDay calendarDay = CalendarDay.from(listYear.get(i),listMonth.get(i),listDay.get(i));
            dates.add(calendarDay);
        }
        return dates;
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
                calendarDay = CalendarDay.from(calendarDay.getDate().plusDays(1));
                if (!calendarDay.equals(calendarToday)){
                    CUR_STREAK_NUM = 0;
                }
            }
        }
    }

    public class EventDecorator implements DayViewDecorator {

        private int color;
        private HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }

    private ArrayList<Integer> getListDay(){
        ArrayList<Integer> list = new ArrayList<>();
        String day = SharedPrefsUtils.getStringPreference(getContext(),"LIST_DAY");
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
        String day = SharedPrefsUtils.getStringPreference(getContext(),"LIST_MONTH");
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
        String day = SharedPrefsUtils.getStringPreference(getContext(),"LIST_YEAR");
        try {
            String [] s = day.split(" ");
            for (String d: s){
                list.add(Integer.parseInt(d));
            }
        } catch (NullPointerException e){

        }
        return list;
    }
}
