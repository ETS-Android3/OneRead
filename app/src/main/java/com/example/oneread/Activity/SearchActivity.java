package com.example.oneread.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.oneread.Adapter.BookAdapter;
import com.example.oneread.Adapter.BookAdapter3;
import com.example.oneread.Adapter.GenreAdapter;
import com.example.oneread.Common.Common;
import com.example.oneread.Common.Message;
import com.example.oneread.Common.Utils;
import com.example.oneread.Model.Book;
import com.example.oneread.Model.Genre;
import com.example.oneread.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nex3z.flowlayout.FlowLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import java.io.Serializable;
import java.util.*;

@SuppressLint("NonConstantResourceId")
public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.flow_layout)
    FlowLayout flowLayout;
    @BindView(R.id.status)
    AutoCompleteTextView status;
    @BindView(R.id.type)
    AutoCompleteTextView type;
    @BindView(R.id.genre)
    TextView genre;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_top_search)
    RecyclerView recyclerViewTopSearch;
    @BindView(R.id.shimmer_layout)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.shimmer_layout_top_search)
    ShimmerFrameLayout shimmerFrameLayoutTopSearch;

    private CompositeDisposable compositeDisposable;
    private List<Book> books = new ArrayList<>();
    private List<Book> topSearch = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
    private HashMap<String, Boolean> isFollowed = new HashMap<>();
    private GenreAdapter genreAdapter;
    private AlertDialog.Builder alertDialog;
    private RecyclerView recyclerViewGenre;
    private View genreDialog;
    private JsonObject filter = new JsonObject();
    private String keyBooks = "books";
    private String keyGenres = "genres";
    private String keyBooksState = "booksState";
    private String keyTopSearch = "topSearch";
    private Parcelable state;

    @OnClick(R.id.go_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.btn_search)
    void search() {
        String title = edtSearch.getText().toString().trim();
        if (title.length() > 0) {
            filter.addProperty("title", title);
        } else {
            filter.remove("title");
        }
        books.clear();
        fetchBook();
    }

    @OnClick(R.id.genre)
    void getGenres() {
        if (!genres.isEmpty()) {
            if (genreDialog.getParent() != null) {
                ((ViewGroup) genreDialog.getParent()).removeView(genreDialog);
            }
            alertDialog.setView(genreDialog);
            alertDialog.show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState != null) {
            books = (List<Book>) savedInstanceState.getSerializable(keyBooks);
            state = savedInstanceState.getParcelable(keyBooksState);
            genres = (List<Genre>) savedInstanceState.getSerializable(keyGenres);
            topSearch = (List<Book>) savedInstanceState.getSerializable(keyTopSearch);
        }

        ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();


        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new BookAdapter3(this, books, isFollowed));
        if (books.size() == 0) {
            fetchBook();
        } else {
            setViewBook();
        }


        recyclerViewTopSearch.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewTopSearch.setAdapter(new BookAdapter(this, topSearch, isFollowed, 1));
        if (topSearch.size() == 0) {
            fetchTopSearch();
        } else {
            setViewTopSearch();
        }


        alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        alertDialog.setTitle(R.string.genre);
        genreDialog = LayoutInflater.from(this).inflate(R.layout.dialog_option, null);
        recyclerViewGenre = genreDialog.findViewById(R.id.recycler_option);
        recyclerViewGenre.setHasFixedSize(true);
        recyclerViewGenre.setLayoutManager(new GridLayoutManager(this, 2));
        genreAdapter = new GenreAdapter(this, genres);
        recyclerViewGenre.setAdapter(genreAdapter);
        alertDialog.setView(genreDialog);
        alertDialog.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        alertDialog.setPositiveButton("FILTER", (dialog, which) -> {
            dialog.dismiss();
            filter.add("genres", genreAdapter.getJsonArrayGenreEndpoint());
            search();
        });
        if (genres.size() == 0) {
            fetchGenre();
        }


        status.setAdapter(new ArrayAdapter(this, R.layout.dropdown_item, getResources().getStringArray(R.array.list_status)));
        type.setAdapter(new ArrayAdapter(this, R.layout.dropdown_item, getResources().getStringArray(R.array.list_type)));
        status.setOnItemClickListener((adapterView, view, i, l) -> {
            if (i != 0) {
                filter.addProperty("status", (Number) (i-1));
            } else {
                filter.remove("status");
            }
        });
        type.setOnItemClickListener(((adapterView, view, i, l) -> {
            if (i != 0) {
                filter.addProperty("type", String.valueOf(adapterView.getItemAtPosition(i)));
            } else {
                filter.remove("type");
            }
        }));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(keyBooks, (Serializable) books);
        outState.putParcelable(keyBooksState, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putSerializable(keyGenres, (Serializable) genres);
        outState.putSerializable(keyTopSearch, (Serializable) topSearch);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        recyclerView.getLayoutManager().onRestoreInstanceState(state);
        super.onResume();
    }

    private void fetchBook() {
        if (Utils.isNetworkAvailable(this)){
            try {
                shimmerFrameLayout.startShimmer();
                compositeDisposable.add(Common.iServiceAPI.getBooksFilter(filter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(jsonObject -> {
                            setViewBook();
                            JsonArray jsonArray = jsonObject.get("books").getAsJsonArray();
                            for (int i=0; i<jsonArray.size(); i++) {
                                String json = jsonArray.get(i).toString();
                                Book book = new Gson().fromJson(json, Book.class);
                                books.add(book);
                            }
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }, err -> {
                            setViewBook();
                            if (err instanceof HttpException) {
                                HttpException response = (HttpException) err;
                                String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                                Utils.showToast(this, Message.connectFail + "\n" + message, Toast.LENGTH_SHORT);
                            } else {
                                err.printStackTrace();
                                Utils.showToast(this, Message.connectFail + "\n" + err.getMessage(), Toast.LENGTH_SHORT);
                            }
                        }));
            } catch (Exception e) {
                setViewBook();
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void fetchTopSearch() {
        if (Utils.isNetworkAvailable(this)){
            try {
                shimmerFrameLayoutTopSearch.startShimmer();
                compositeDisposable.add(Common.iServiceAPI.getTopSearch()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(jsonObject -> {
                            setViewTopSearch();
                            JsonArray jsonArray = jsonObject.get("books").getAsJsonArray();
                            for (int i=0; i<jsonArray.size(); i++) {
                                String json = jsonArray.get(i).toString();
                                Book book = new Gson().fromJson(json, Book.class);
                                topSearch.add(book);
                            }
                            recyclerViewTopSearch.getAdapter().notifyDataSetChanged();
                        }, err -> {
                            setViewTopSearch();
                            if (err instanceof HttpException) {
                                HttpException response = (HttpException) err;
                                String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                                Utils.showToast(this, Message.connectFail + "\n" + message, Toast.LENGTH_SHORT);
                            } else {
                                err.printStackTrace();
                                Utils.showToast(this, Message.connectFail + "\n" + err.getMessage(), Toast.LENGTH_SHORT);
                            }
                        }));
            } catch (Exception e) {
                setViewTopSearch();
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void fetchGenre() {
        if (Utils.isNetworkAvailable(this)){
            try {
                compositeDisposable.add(Common.iServiceAPI.getGenres()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(jsonObject -> {
                            JsonArray jsonArray = jsonObject.get("genres").getAsJsonArray();
                            for (int i=0; i<jsonArray.size(); i++) {
                                String json = jsonArray.get(i).toString();
                                Genre genre = new Gson().fromJson(json, Genre.class);
                                genres.add(genre);
                            }
                            recyclerViewGenre.getAdapter().notifyDataSetChanged();
                        }, err -> {
                            if (err instanceof HttpException) {
                                HttpException response = (HttpException) err;
                                String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                                Utils.showToast(this, Message.connectFail + "\n" + message, Toast.LENGTH_SHORT);
                            } else {
                                err.printStackTrace();
                                Utils.showToast(this, Message.connectFail + "\n" + err.getMessage(), Toast.LENGTH_SHORT);
                            }
                        }));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    void setViewBook () {
        if (shimmerFrameLayout.isShimmerStarted()) shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    void setViewTopSearch () {
        if (shimmerFrameLayoutTopSearch.isShimmerStarted()) shimmerFrameLayoutTopSearch.stopShimmer();
        shimmerFrameLayoutTopSearch.setVisibility(View.GONE);
        recyclerViewTopSearch.setVisibility(View.VISIBLE);
    }
}