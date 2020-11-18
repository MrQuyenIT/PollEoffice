package quyennv.becamex.voteeoffice.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import quyennv.becamex.voteeoffice.BaseViewHolder;
import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.Settings;
import quyennv.becamex.voteeoffice.models.Poll;
import quyennv.becamex.voteeoffice.models.PollPlan;
import quyennv.becamex.voteeoffice.models.PollUserPlan;
import quyennv.becamex.voteeoffice.remote.IPollService;
import quyennv.becamex.voteeoffice.remote.NetworkClient;
import quyennv.becamex.voteeoffice.utils.Utils;
import quyennv.becamex.voteeoffice.views.AddPollActivity;
import quyennv.becamex.voteeoffice.views.PollDetailActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PollAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private  Context context;
    private  List<Poll> listPolls;
    private boolean isLoaderVisible = false;
    ProgressDialog progress;
    private IPollService apiService;
    Settings settings;
    public PollAdapter(Context context, List<Poll> listItem){
        this.context = context;
        this.listPolls = listItem;
        settings = new Settings(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(con)
//                .inflate(R.layout.row_poll,parent,false);
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new PollAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_poll_new, parent, false));
            case VIEW_TYPE_LOADING:
                return new PollAdapter.ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

//    @Override
//    public void onBindViewHolder(@NonNull VoteAdapter.ViewHolder holder, int position) {
//        Poll item = listPolls.get(position);
//        holder.title.setText(item.getQuestion());
//        holder.description.setText(item.getId());
//    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == listPolls.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return listPolls == null ? 0 : listPolls.size();
    }

    public void clear() {
        listPolls.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<Poll> polls) {
        Gson gson = new Gson();
        Log.d("listPolls",gson.toJson(listPolls));
        listPolls.addAll(polls);
        notifyDataSetChanged();
    }

    public void addLoading() {
        if(listPolls.size()>0) {
            isLoaderVisible = true;
            listPolls.add(new Poll());
            notifyItemInserted(listPolls.size() - 1);
        }
    }

    public void removeLoading() {
        isLoaderVisible = false;
        if(listPolls.size()>0) {
            int position = listPolls.size() - 1;
            Poll item = getItem(position);
            if (item != null) {
                listPolls.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    Poll getItem(int position) {
        return listPolls.get(position);
    }

    public class ViewHolder  extends  BaseViewHolder {
        @BindView(R.id.title)
        public TextView title;
        @BindView(R.id.user_created)
        public TextView user_created;
        @BindView(R.id.poll_created)
        public TextView poll_created;

        @BindView(R.id.img_poll_delete)
        public ImageView img_poll_delete;

        @BindView(R.id.cardView)
        public CardView cardView;

        @BindView(R.id.img_edit)
        public ImageView img_edit;

        @BindView(R.id.numberPoll)
        public TextView numberPoll;

        @BindView(R.id.list_plan_view)
        public LinearLayout linearLayoutViewPlan;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }



        public void onBind(final int position) {
            super.onBind(position);
            linearLayoutViewPlan.removeAllViews();


            final Poll poll = listPolls.get(position);
            title.setText(poll.getQuestion());
            user_created.setText(poll.getName());

            String dateCreated  =  Utils.ConvertStringDateToString(poll.getDateCreated(), "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy HH:mm", "UTC");
            poll_created.setText(dateCreated);
            String userCreated = poll.getUserCreated();
            String username =settings.getUserNameKey();

            if (!userCreated.equals(username)){
                img_poll_delete.setVisibility(View.INVISIBLE);
                img_edit.setVisibility(View.INVISIBLE);
            }
            else{
                img_poll_delete.setVisibility(View.VISIBLE);
                img_edit.setVisibility(View.VISIBLE);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PollDetailActivity.class);
                    intent.putExtra("id", poll.getId());
                    ((Activity) context).startActivityForResult(intent,0);
                }
            });

            img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddPollActivity.class);
                    intent.putExtra("pollId", poll.getId());
                    ((Activity) context).startActivityForResult(intent,0);
                }
            });

            //Add view vaof child
            int countAllUserpoll = 0;
            for (PollPlan plan : poll.getPollPlans()){
                countAllUserpoll += plan.getPollUserPlans().size();
                linearLayoutViewPlan.addView(RenderViewPlan(plan));
            }

            numberPoll.setText(countAllUserpoll +" người đã bình chọn" );

            img_poll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(R.string.delete);
                    builder.setMessage(R.string.delete_confirm);
                    builder.setPositiveButton(R.string.delete,new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int whichButton) {
                                    progress = new ProgressDialog(view.getContext());
                                    progress.setMessage(view.getContext().getString(R.string.loading));
                                    progress.setIndeterminate(false);
                                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progress.show();
                                    apiService = NetworkClient.getRetrofit(view.getContext()).create(IPollService.class);
                                  apiService.deletePoll(poll.getId()).enqueue(new Callback<Boolean>() {
                                      @Override
                                      public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                          if (response.isSuccessful()) {
                                              listPolls.remove(position);
                                              notifyDataSetChanged();
                                              dialog.dismiss();
                                              progress.dismiss();
                                              Toast toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.deleted), Toast.LENGTH_SHORT);
                                              toast.show();

                                          } else {
                                              progress.dismiss();
                                              dialog.dismiss();
                                              Toast toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.deleted_failed), Toast.LENGTH_SHORT);
                                              toast.show();
                                          }
                                      }

                                      @Override
                                      public void onFailure(Call<Boolean> call, Throwable t) {
                                          dialog.dismiss();
                                          progress.dismiss();
                                          Toast toast = Toast.makeText(view.getContext(), view.getContext().getString(R.string.deleted_failed), Toast.LENGTH_SHORT);
                                          toast.show();
                                      }
                                  });
                                }
                            });

                    // Set the negative button
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    // Change the alert dialog buttons text and background color
                    positiveButton.setTextColor(Color.parseColor("#5E215D"));
                    positiveButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    negativeButton.setTextColor(Color.parseColor("#5E215D"));
                    negativeButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

                }
            });
        }
    }
        public class ProgressHolder extends BaseViewHolder {
            public ProgressHolder(View itemView) {
                super(itemView);
                //ButterKnife.bind(this, itemView);
            }

            protected void clear() {

            }

            public void onBind(int position) {
                super.onBind(position);
            }
    }

    public RelativeLayout RenderViewPlan(PollPlan plan){
        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        int valPx =context.getResources().getDimensionPixelSize(R.dimen.card_margin);
        relativeLayoutParams.setMargins(0,valPx,0,valPx);
        relativeLayout.setLayoutParams(relativeLayoutParams);
        relativeLayout.setPadding(valPx,valPx,valPx,valPx);
        relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.poll_rounded));

        TextView textViewPlan = new TextView(context);
        textViewPlan.setText(plan.getPlanName());
        RelativeLayout.LayoutParams pramsPlan= new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pramsPlan.addRule(RelativeLayout.RIGHT_OF, R.id.countPoll);
        pramsPlan.setMargins(0,0,Utils.dpToPx(15, context),0);
        textViewPlan.setLayoutParams(pramsPlan);

        TextView textViewCountPlan = new TextView(context);
        textViewCountPlan.setText(String.valueOf(plan.getPollUserPlans().size()));

        RelativeLayout.LayoutParams pramsCountPlan= new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pramsCountPlan.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        textViewCountPlan.setLayoutParams(pramsCountPlan);

        relativeLayout.addView(textViewPlan);
        relativeLayout.addView(textViewCountPlan);

        return relativeLayout;
    }
}
