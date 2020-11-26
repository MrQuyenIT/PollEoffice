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
import android.widget.ArrayAdapter;
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
import quyennv.becamex.voteeoffice.models.PollPlan;
import quyennv.becamex.voteeoffice.models.PollUserPlan;
import quyennv.becamex.voteeoffice.models.UserDialog;
import quyennv.becamex.voteeoffice.ui.CircularImageView;
import quyennv.becamex.voteeoffice.utils.Utils;

public class PollUserDialogAdapter extends ArrayAdapter<UserDialog> {
    public PollUserDialogAdapter(Context context, ArrayList<UserDialog> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserDialog user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_poll_user, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.nameUser);
        name.setText(user.getName());

        CircularImageView avatar = (CircularImageView) convertView.findViewById(R.id.avatar);
        Glide.with(getContext()).load(user.getAvatar()).override(Utils.dpToPx(60, getContext())).into(avatar);
        // Return the completed view to render on screen
        return convertView;
    }
}
