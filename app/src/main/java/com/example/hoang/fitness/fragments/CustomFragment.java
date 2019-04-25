package com.example.hoang.fitness.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.activities.PickExerciseActivity;
import com.example.hoang.fitness.adapters.CustomWorkoutAdapter;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rcv_custom_workout)
    RecyclerView mCustomWorkouts;
    @BindView(R.id.btn_floating_custom_workout)
    ImageView mAddCustomWorkouts;
    @BindView(R.id.tv_tap_to_start)
    TextView mTapToStart;
    private CustomWorkoutAdapter adapter;
    private List<Workout> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);
        ButterKnife.bind(this,view);
        getActivity().findViewById(R.id.tv_title_main).setVisibility(View.GONE);
        getActivity().findViewById(R.id.btn_setting).setVisibility(View.GONE);
        //list = new ArrayList<>();
        list = JsonUtil.getInstance().getListWorkout(getContext());
        adapter = new CustomWorkoutAdapter(getContext(),list.subList(0,2));
        mCustomWorkouts.setAdapter(adapter);
        mCustomWorkouts.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mAddCustomWorkouts.setOnClickListener(this::onClick);
        if (list.size()==0){
            mTapToStart.setVisibility(View.VISIBLE);
        } else {
            mTapToStart.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), PickExerciseActivity.class);
        startActivity(intent);
        if (list.size()==0){
            mTapToStart.setVisibility(View.VISIBLE);
        } else {
            mTapToStart.setVisibility(View.GONE);
        }

    }
}
