package com.example.hoang.fitness.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hoang.fitness.R;
import com.example.hoang.fitness.activities.DetailTargetActivity;
import com.example.hoang.fitness.activities.WorkoutDetailActivity;
import com.example.hoang.fitness.fragments.TargetFragment;
import com.example.hoang.fitness.models.Target;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.utils.FileUtil;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.ViewHolder> {
    private Context context;
    private List<Target> arrayList;
    public static TargetAdapter instance;

    public TargetAdapter(Context context, List<Target> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        instance = this;
    }

    @NonNull
    @Override
    public TargetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_target,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TargetAdapter.ViewHolder holder, int position) {
        holder.mName.setText(arrayList.get(position).getName());
        holder.mDetail1.setText(arrayList.get(position).getHour()+":"+ arrayList.get(position).getMinute()+
                arrayList.get(position).getAm_pm()+" • "+arrayList.get(position).getNumDay()+" days");
        holder.mDetail2.setText(arrayList.get(position).getWorkout().getTime()*arrayList.get(position).getNumDay()+" mins • "+
                arrayList.get(position).getWorkout().getCalorie()*arrayList.get(position).getNumDay()+" calories");
        if (arrayList.get(position).getState()==0){
            holder.mState.setText("Bắt đầu");
        } else if (arrayList.get(position).getState()==-1){
            holder.mState.setText("Không hoàn thành");
        } else if (arrayList.get(position).getState()==arrayList.get(position).getNumDay()){
            holder.mState.setText("Đã hoàn thành");
        } else {
            holder.mState.setText("Đang thực hiện: "+arrayList.get(position).getState()+"/"+arrayList.get(position).getNumDay());
        }
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailTargetActivity.class);
                intent.putExtra("TARGET_NAME",arrayList.get(position).getName());
                context.startActivity(intent);
            }
        });
        holder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                arrayList.remove(position);
                FileUtil.ghiFileTarget(context,arrayList);
                TargetFragment.alarmManager.cancel(
                        TargetFragment.pendingIntent[position]
                );
                notifyDataSetChanged();
                return false;
            }
        });
    }

    public void update(){
        arrayList = FileUtil.docFileTarget(context,"target.txt");
        instance.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name_target)
        TextView mName;
        @BindView(R.id.tv_detail1_target)
        TextView mDetail1;
        @BindView(R.id.tv_detail2_target)
        TextView mDetail2;
        @BindView(R.id.tv_state)
        TextView mState;
        @BindView(R.id.rl_item_custom_workout)
        RelativeLayout mLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}