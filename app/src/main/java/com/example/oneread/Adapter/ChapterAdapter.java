package com.example.oneread.Adapter;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneread.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder>{

    Context context;
    List<String> list_iamge = new ArrayList<>();

    public ChapterAdapter() {
    }

    public ChapterAdapter(Context context, List<String> list_iamge) {
        this.context = context;
        this.list_iamge = list_iamge;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(new ContextThemeWrapper(context, R.style.Theme_AppCompat)).inflate(R.layout.chapter_image_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Picasso.get().load(list_iamge.get(position)).into(holder.chapter_image);
    }

    @Override
    public int getItemCount() {
        return list_iamge.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       ImageView chapter_image;
        public ViewHolder( View itemView) {
            super(itemView);
            chapter_image = itemView.findViewById(R.id.chapter_image);
        }
    }
}
