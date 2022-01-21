package com.example.oneread.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneread.Model.Book;
import com.example.oneread.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookAdapter3 extends RecyclerView.Adapter<BookAdapter3.ViewHolder>{
    Context context;
    List<Book> books = new ArrayList<>();
    HashMap<String, Boolean> isFollowed = new HashMap<>();

    public BookAdapter3() {
    }

    public BookAdapter3(Context context, List<Book> books, HashMap<String, Boolean> isFollowed) {
        this.context = context;
        this.books = books;
        this.isFollowed = isFollowed;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.book_item_3, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Picasso.get().load(books.get(position).getThumb()).into(holder.thumb);
        if(isFollowed.containsKey(books.get(position).getEndpoint()))
            holder.btn_follow.setImageResource(R.drawable.ic_marked);
        else
            holder.btn_follow.setImageResource(R.drawable.ic_mark);
        holder.btn_follow.setTag(position);
        holder.title_comic.setText(books.get(position).getTitle());
        holder.rating.setText(String.valueOf(books.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumb;
        TextView title_comic, chapter, rating, genres;
        ImageView btn_follow;
        public ViewHolder( View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title_comic = itemView.findViewById(R.id.title_comic);
            chapter = itemView.findViewById(R.id.chapter);
            genres = itemView.findViewById(R.id.genres);
            rating = itemView.findViewById(R.id.rating);
            btn_follow = itemView.findViewById(R.id.btn_follow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, DetailComicActivity.class);
//                    intent.putExtra("endpoint", books.get(getBindingAdapterPosition()).endpoint);
//                    context.startActivity(intent);
                }
            } );

            btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(toast != null) toast.cancel();
//                    if(btn_follow.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.ic_mark).getConstantState())){
//                        btn_follow.setImageResource(R.drawable.ic_marked);
//                        toast = Toast.makeText(context, "Followed", Toast.LENGTH_SHORT);
//                        toast.show();
//                        MainActivity.followedComicViewModel.insert(new FollowedComic(books.get(getBindingAdapterPosition()).endpoint));
//                        if(!isFollowed.containsKey(books.get((Integer) btn_follow.getTag()).endpoint)){
//                            isFollowed.put(books.get((Integer) btn_follow.getTag()).endpoint, true);
////                            FollowedFragment.listFollowedComic.add(books.get((Integer) btn_follow.getTag()));
//                        }
//                    }else{
//                        btn_follow.setImageResource(R.drawable.ic_mark);
//                        toast = Toast.makeText(context, "Unollowed", Toast.LENGTH_SHORT);
//                        toast.show();
//                        MainActivity.followedComicViewModel.delete(new FollowedComic(books.get(getBindingAdapterPosition()).endpoint));
//                        if(isFollowed.containsKey(books.get((Integer) btn_follow.getTag()).endpoint)){
//                            isFollowed.remove(books.get((Integer) btn_follow.getTag()).endpoint);
////                            FollowedFragment.listFollowedComic.remove(books.get((Integer) btn_follow.getTag()));
//                        }
//                    }
                }
            });
        }
    }
}
