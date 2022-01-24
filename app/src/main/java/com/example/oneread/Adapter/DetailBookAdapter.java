package com.example.oneread.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneread.Activity.ChapterActivity;
import com.example.oneread.Listener.IRecylerClickListener;
import com.example.oneread.Model.Book;
import com.example.oneread.R;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class DetailBookAdapter extends RecyclerView.Adapter<DetailBookAdapter.ViewHolder>{
    Context context;
    Book book;
    IRecylerClickListener recylerClickListener;
    List<File> files;
    Boolean offline = false;

    public DetailBookAdapter() {
    }

    public DetailBookAdapter(Context context, Book book, IRecylerClickListener recylerClickListener) {
        this.context = context;
        this.book = book;
        this.recylerClickListener = recylerClickListener;
        offline = false;
    }

    public DetailBookAdapter(Context context, List<File> files, IRecylerClickListener recylerClickListener) {
        this.context = context;
        this.files = files;
        this.recylerClickListener = recylerClickListener;
        offline = true;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.chapter_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(offline){
            holder.chapter_uploaded.setText("");
            holder.chapter_title.setText(files.get(position).getName());
        }else{
            holder.chapter_title.setText(book.getChapters().get(position).getTitle());
            holder.chapter_uploaded.setText(book.getChapters().get(position).getTime());
        }
        holder.btn_check.setImageResource(R.drawable.ic_uncheck);
        holder.btn_check.setTag(R.drawable.ic_uncheck);
    }

    @Override
    public int getItemCount() {
        if(offline) return files.size();
        else return book.getChapters().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chapter_title, chapter_uploaded;
        ImageView btn_check;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chapter_title = itemView.findViewById(R.id.chapter_title);
            chapter_uploaded = itemView.findViewById(R.id.chapter_uploaded);
            btn_check = itemView.findViewById(R.id.btn_check);
            btn_check.setTag(R.drawable.ic_uncheck);

            int uncheck_id = (int) btn_check.getTag();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(offline){
//                        Intent intent = new Intent(context, OfflineChapterActivity.class);
//                        intent.putExtra("position", getBindingAdapterPosition());
//                        intent.putExtra("list_file", (Serializable) files);
//                        context.startActivity(intent);
//                    }else{
                        Intent intent = new Intent(context, ChapterActivity.class);
                        intent.putExtra("position", getAdapterPosition());
                        intent.putExtra("book_endpoint", book.getEndpoint());
                        intent.putExtra("chapter_list", (Serializable) book.getChapters());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
//                    }
                }
            });

            btn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(((int) btn_check.getTag()) == uncheck_id){
//                        btn_check.setImageResource(R.drawable.ic_check);
//                        btn_check.setTag(R.drawable.ic_check);
//                        recylerClickListener.onCheckBoxClick(getBindingAdapterPosition(), "check");
//                    }else{
//                        btn_check.setImageResource(R.drawable.ic_uncheck);
//                        btn_check.setTag(R.drawable.ic_uncheck);
//                        recylerClickListener.onCheckBoxClick(getBindingAdapterPosition(), "uncheck");
//                    }

                }
            });
        }
    }
}
