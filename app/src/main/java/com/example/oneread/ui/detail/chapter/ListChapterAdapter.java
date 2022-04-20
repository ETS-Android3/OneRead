package com.example.oneread.ui.detail.chapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.utils.MODE;

public class ListChapterAdapter extends RecyclerView.Adapter<ListChapterAdapter.ViewHolder>{
    Context context;
    Book book;

    private MODE mode;


    public ListChapterAdapter() {
    }

    public ListChapterAdapter(Context context, Book book, MODE mode) {
        this.context = context;
        this.book = book;
        this.mode = mode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.chapter_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.chapter_title.setText(book.getChapters().get(position).getTitle());
        holder.chapter_uploaded.setText(book.getChapters().get(position).getTime().split(" ")[0]);
    }

    @Override
    public int getItemCount() {
        return book.getChapters().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chapter_title, chapter_uploaded;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chapter_title = itemView.findViewById(R.id.chapter_title);
            chapter_uploaded = itemView.findViewById(R.id.chapter_uploaded);

            itemView.setOnClickListener(v -> {
                if (mode == MODE.ONLINE) {

                } else {

                }
            });
        }
    }

}
