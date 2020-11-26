package quyennv.becamex.voteeoffice.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import quyennv.becamex.voteeoffice.BaseViewHolder;
import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.models.PollPlan;
import quyennv.becamex.voteeoffice.ui.CircularImageView;
import quyennv.becamex.voteeoffice.views.AddPollActivity;

public class PollPlanAddAdapter  extends RecyclerView.Adapter<BaseViewHolder>{
    private  Context context;
    private  ArrayList<PollPlan> pollPlans;
    public PollPlanAddAdapter(Context context, ArrayList<PollPlan> pollPlans){
        this.context  = context;
        this.pollPlans = pollPlans;
    }
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PollPlanAddAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_poll_plan_add, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return this.pollPlans.size();
    }

    public void  addPlan(ArrayList<PollPlan> plans){
        this.pollPlans = plans;
        notifyDataSetChanged();
    }
    public ArrayList<PollPlan> getPlans(){
        return this.pollPlans;
    }

    public class ViewHolder  extends  BaseViewHolder {


        @BindView(R.id.cardView)
        public CardView cardView;

        @BindView(R.id.planName)
        public TextInputEditText planName;
        @BindView(R.id.deletePlan)
        public CircularImageView deletePlan;

        @BindView(R.id.input_layout)
        public TextInputLayout input_layout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

        public void onBind(final int position) {
            super.onBind(position);

            PollPlan pollPlan = pollPlans.get(position);
            cardView.setId(pollPlan.getId());
            planName.setText(pollPlan.getPlanName());
            input_layout.setHint("Phương án "+String.valueOf((position+1)));
            deletePlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(pollPlans.size()<=2){
                        Toast.makeText(context,"Ít nhất 2 phương án !",Toast.LENGTH_LONG).show();
                        return;
                    }

                    pollPlans =   ((AddPollActivity)context).getPollPlansInRecycleView();

                    pollPlans.remove(position);

                   notifyDataSetChanged();

                }
            });
        }
    }
}
