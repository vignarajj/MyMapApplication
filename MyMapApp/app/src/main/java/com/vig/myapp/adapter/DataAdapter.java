package com.vig.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.vig.myapp.R;
import com.vig.myapp.custom.TextSansBold;
import com.vig.myapp.interfaces.UserSelectedListener;
import com.vig.myapp.models.UserData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.RowHolder> {
    Context context;
    List<UserData> list = new ArrayList<>();
    UserSelectedListener listener;

    public class RowHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.layout_userdata)
        LinearLayout layoutUserData;
        @Bind(R.id.img_user)
        ImageView imgUser;
        @Bind(R.id.txt_user)
        TextSansBold txtUser;

        public RowHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.layout_userdata)
        void onClick() {
            listener.onItemClick(getAdapterPosition());
        }
    }

    public DataAdapter(Context context, List<UserData> list, UserSelectedListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_users, parent, false);
        return new RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        UserData data = list.get(position);
        String imgUrl = data.getPicture();
        String userName = data.getName();
        Picasso.with(context).load(imgUrl).into(holder.imgUser);
        holder.txtUser.setText(userName);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
