package quyennv.becamex.voteeoffice.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import quyennv.becamex.voteeoffice.BaseViewHolder;
import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.models.Poll;
import quyennv.becamex.voteeoffice.models.PollPlan;
import quyennv.becamex.voteeoffice.models.PollUserPlan;
import quyennv.becamex.voteeoffice.ui.CircularImageView;
import quyennv.becamex.voteeoffice.utils.Utils;

public class PlanDetailAdapter  extends RecyclerView.Adapter<BaseViewHolder> {
    private  Context context;
    private  List<PollPlan> pollPlans;
    private List<PollPlan> pollPlansCheck = new ArrayList<>();
    public PlanDetailAdapter(Context context, List<PollPlan> pollPlans){
        this.context  = context;
        this.pollPlans = pollPlans;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlanDetailAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_poll_plan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return this.pollPlans.size();
    }


    public List<PollPlan> getAllPlanCheck(){
        return  pollPlansCheck;
    }


    public class ViewHolder  extends  BaseViewHolder {
        @BindView(R.id.planName)
        public TextView planName;

        @BindView(R.id.checkbox)
        public CheckBox checkbox;

        @BindView(R.id.user_plan)
        public LinearLayout linearLayoutUserPlan;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

        public void onBind(final int position) {
            super.onBind(position);

            final PollPlan pollPlan = pollPlans.get(position);
            planName.setText(pollPlan.getPlanName());
            checkbox.setChecked(pollPlan.getCheck());
            if(pollPlan.getCheck()){
                planName.setBackground(ContextCompat.getDrawable(context, R.drawable.poll_rounded_border));
                pollPlansCheck.add(pollPlan);
            }
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkbox.isChecked()) {
                        pollPlansCheck.add(pollPlan);
                        planName.setBackground(ContextCompat.getDrawable(context, R.drawable.poll_rounded_border));
                    }else{
                        pollPlansCheck.remove(pollPlan);
                        planName.setBackground(ContextCompat.getDrawable(context, R.drawable.poll_rounded));
                    }
                }
            });

            linearLayoutUserPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Danh sách người chọn");

                    // set the custom layout
                    final View customLayout = ((Activity)context).getLayoutInflater().inflate(R.layout.row_poll_user,null);
                    builder.setView(customLayout);
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    negativeButton.setTextColor(Color.parseColor("#5E215D"));
                    negativeButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            });

            RenderAvatarPlan(pollPlan);
        }

        public  void RenderAvatarPlan(PollPlan pollPlan){

            if(pollPlan.getPollUserPlans().size()>2){
                CircularImageView imageView = new CircularImageView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dpToPx(30, context),Utils.dpToPx(30, context));
                imageView.setLayoutParams(layoutParams);
                Glide.with(context).load(pollPlan.getPollUserPlans().get(0).getAvatar()).override(Utils.dpToPx(30, context)).into(imageView);
                linearLayoutUserPlan.addView(imageView);

                TextView textView = new TextView(context);
                LinearLayout.LayoutParams layoutParamsPlus = new LinearLayout.LayoutParams(Utils.dpToPx(23, context),Utils.dpToPx(23, context));
                layoutParamsPlus.setMargins(0, Utils.dpToPx(3, context), 0, 0);
                textView.setLayoutParams(layoutParamsPlus);
                textView.setText((pollPlan.getPollUserPlans().size()-1+"+"));
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(ContextCompat.getDrawable(context, R.drawable.cicle_avatar));
                linearLayoutUserPlan.addView(textView);
            }
            else{
                for (PollUserPlan pollUserPlan: pollPlan.getPollUserPlans()){
                    CircularImageView imageView = new CircularImageView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dpToPx(30, context),Utils.dpToPx(30, context));
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(context).load(pollUserPlan.getAvatar()).override(Utils.dpToPx(30, context)).into(imageView);
                    linearLayoutUserPlan.addView(imageView);
                }
            }

            Boolean flag=  false;
            for (PollPlan itemPollPlan: pollPlans){
                if(itemPollPlan.getCountUserVote()>0){
                    flag=true;
                }
            }
            if(flag){
                linearLayoutUserPlan.setMinimumWidth(Utils.dpToPx(60, context));
            }
            else{
                linearLayoutUserPlan.setMinimumWidth(Utils.dpToPx(0, context));
            }

        }
    }
}
