package com.adaptive.exoplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adaptive.exoplayer.database.Channel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class StraggeredRecyclerViewAdapter extends RecyclerView.Adapter<StraggeredRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "StraggeredRecyclerViewA";
    List<Channel> channel;


    Context context;

    public StraggeredRecyclerViewAdapter( Context context, List<Channel> channel) {

        this.channel = new ArrayList<>();
        this.channel = channel;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_item , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.error);
;        Glide.with(context).load(channel.get(position).getImage())
                .apply(requestOptions)
                .into(holder.image);
        holder.name.setText(channel.get(position).getName());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cliecked"+position);
                Intent intent;
                intent = new Intent(context, PlayerActivity.class);
//                intent.putExtra("movie" , mNames.get(position));
//                intent.putExtra("url", mImageUrls.get(position));
                intent.putExtra("movie_url", channel.get(position).getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return channel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }
    }
}
