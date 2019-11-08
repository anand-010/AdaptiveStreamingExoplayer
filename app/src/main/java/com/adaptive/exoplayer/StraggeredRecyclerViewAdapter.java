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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class StraggeredRecyclerViewAdapter extends RecyclerView.Adapter<StraggeredRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "StraggeredRecyclerViewA";
    ArrayList<String> mNames = new ArrayList<String>();
    ArrayList<String> mImageUrls = new ArrayList<String>();
    ArrayList<String> mMovieurl = new ArrayList<String>();

    Context context;

    public StraggeredRecyclerViewAdapter( Context context, ArrayList<String> mNames, ArrayList<String> mImageUrls ,ArrayList<String> mMovieurl) {
        this.mNames = mNames;
        this.mImageUrls = mImageUrls;
        this.mMovieurl = mMovieurl;
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
                .placeholder(R.drawable.ic_launcher_background);
;        Glide.with(context).load(mImageUrls.get(position))
                .apply(requestOptions)
                .into(holder.image);
        holder.name.setText(mNames.get(position));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cliecked"+position);
                Intent intent;
                intent = new Intent(context, Moive_activity.class);
                intent.putExtra("movie" , mNames.get(position));
                intent.putExtra("url", mImageUrls.get(position));
                intent.putExtra("movie_url", mMovieurl.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
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
