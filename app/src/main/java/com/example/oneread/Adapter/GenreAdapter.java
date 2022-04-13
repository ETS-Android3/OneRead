package com.example.oneread.Adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.oneread.Model.Genre;
import com.example.oneread.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private Context context;
    private List<Genre> genres ,selected_genres;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    public GenreAdapter() {
    }

    public GenreAdapter(Context context, List<Genre> genres) {
        this.context = context;
        this.genres = genres;
        selected_genres = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.check_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.check_options.setText(genres.get(position).getTitle());
        holder.check_options.setChecked(itemStateArray.get(position));
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public JsonElement getJsonArrayGenreEndpoint(){
        List<String> genres_endpoint = new ArrayList<>();
        for(Genre genre: selected_genres){
            genres_endpoint.add(genre.getEndpoint());
        }
        return new Gson().toJsonTree(genres_endpoint.toArray(new String[0]));
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox check_options;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check_options = itemView.findViewById(R.id.check_options);
            check_options.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int adapterPosition = getAdapterPosition();
                itemStateArray.put(adapterPosition, isChecked);
                if(isChecked){
                    selected_genres.add(genres.get(adapterPosition));
                }else{
                    selected_genres.remove(genres.get(adapterPosition));
                }
            });
        }
    }
}
