package quyennv.becamex.voteeoffice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.models.ObjectSearch;

public class PollInviteAdapter extends RecyclerView.Adapter<PollInviteAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<ObjectSearch> contactList;
    private List<ObjectSearch> contactListFiltered;
    private PollInviteSearchAdapterListener listener;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final ObjectSearch contact = contactListFiltered.get(position);
        holder.name.setText(contact.getFullName());
        holder.department.setText(contact.getDepartment());
        holder.img_tick.setVisibility(contact.isChecked() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setChecked(!contact.isChecked());
                holder.img_tick.setVisibility(contact.isChecked() ? View.VISIBLE : View.GONE);
            }
        });
        Glide.with(context)
                .load(contact.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, department;
        public ImageView thumbnail, img_tick;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            department = view.findViewById(R.id.dept);
            thumbnail = view.findViewById(R.id.thumbnail);
            img_tick = view.findViewById(R.id.img_tick);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public PollInviteAdapter(Context context, List<ObjectSearch> contactList, PollInviteSearchAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<ObjectSearch> filteredList = new ArrayList<>();
                    for (ObjectSearch row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFullName().toLowerCase().contains(charString.toLowerCase()) || row.getDepartment().toLowerCase().contains(charString.toLowerCase()) || row.getEmail().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<ObjectSearch>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface PollInviteSearchAdapterListener {
        void onContactSelected(ObjectSearch contact);
    }

    public void ClearAllSelection() {
        for (ObjectSearch obj :
                contactList) {
            obj.setChecked(false);
        }
        notifyDataSetChanged();
    }

    public List<ObjectSearch> getSelected() {
        List<ObjectSearch> selected = new ArrayList<>();
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).isChecked()) {
                selected.add(contactList.get(i));
            }
        }
        return selected;
    }


}
