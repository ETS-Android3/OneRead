package com.example.oneread.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneread.Activity.DetailBookActivity;
import com.example.oneread.Helper.BookDiffCallBack;
import com.example.oneread.Model.Book;
import com.example.oneread.R;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookAdapter5 extends RecyclerView.Adapter<BookAdapter5.ViewHolder>{
    Context context;
    List<Book> books = new ArrayList<>();
    HashMap<String, Boolean> isFollowed = new HashMap<>();
    public BookAdapter5() {
    }

    public BookAdapter5(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.book_item_5, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads);
        else {
            Bundle bundle = (Bundle) payloads.get(0);
            for(String key : bundle.keySet()) {
                if (key.equals("changed")) {
                    Picasso.get().load(books.get(position).getThumb()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(holder.thumb);
                    holder.title.setText(books.get(position).getTitle());
                }
            }
        }
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Picasso.get().load(books.get(position).getThumb()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(holder.thumb);
        holder.title.setText(books.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumb;
        TextView title;
        public ViewHolder( View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title = itemView.findViewById(R.id.title);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailBookActivity.class);
                    intent.putExtra("endpoint", books.get(getAdapterPosition()).getEndpoint());
                    context.startActivity(intent);
                }
            } );
        }
    }

    public void updateListItem (List<Book> newBooks) {
        BookDiffCallBack bookRecyclerDiffCallback = new BookDiffCallBack(books, newBooks);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(bookRecyclerDiffCallback);
        books.clear();
        books.addAll(newBooks);
        diffResult.dispatchUpdatesTo(this);
    }
}
