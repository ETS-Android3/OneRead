package com.example.oneread.Activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.oneread.Adapter.BookAdapter;
import com.example.oneread.Adapter.BookAdapter3;
import com.example.oneread.Common.Common;
import com.example.oneread.Common.Message;
import com.example.oneread.Common.Utils;
import com.example.oneread.Model.Book;
import com.example.oneread.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.nex3z.flowlayout.FlowLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NonConstantResourceId")
public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.flow_layout)
    FlowLayout flowLayout;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.genre)
    TextView genre;
    @BindView(R.id.btn_filter)
    ImageView btnFilter;
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
    private Map<String, String> genres = new HashMap<>();
    private HashMap<String, Boolean> isFollowed = new HashMap<>();

    @OnClick(R.id.go_back)
    void goBack() {
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new BookAdapter3(this, books, isFollowed));
        if (books.size() == 0) {
            fetchBook();
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManagerTopSearch = new LinearLayoutManager(this);
        linearLayoutManagerTopSearch.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewTopSearch.setLayoutManager(linearLayoutManagerTopSearch);
        recyclerViewTopSearch.setAdapter(new BookAdapter(this, topSearch, isFollowed));
        if (topSearch.size() == 0) {
            fetchTopSearch();
        } else {
            shimmerFrameLayoutTopSearch.stopShimmer();
            shimmerFrameLayoutTopSearch.setVisibility(View.GONE);
            recyclerViewTopSearch.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onPause() {
        compositeDisposable.dispose();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void fetchBook() {
        if (Utils.isNetworkAvailable(this)){
            try {
                shimmerFrameLayout.startShimmer();
                compositeDisposable.add(Common.iServiceAPI.getBooks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(jsonObject -> {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            JsonArray jsonArray = jsonObject.get("books").getAsJsonArray();
                            for (int i=0; i<jsonArray.size(); i++) {
                                String json = jsonArray.get(i).toString();
                                Book book = new Gson().fromJson(json, Book.class);
                                books.add(book);
                            }
                            recyclerView.getAdapter().notifyDataSetChanged();
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

    private void fetchTopSearch() {
        if (Utils.isNetworkAvailable(this)){
            try {
                shimmerFrameLayoutTopSearch.startShimmer();
                compositeDisposable.add(Common.iServiceAPI.getTopSearch()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(jsonObject -> {
                            shimmerFrameLayoutTopSearch.stopShimmer();
                            shimmerFrameLayoutTopSearch.setVisibility(View.GONE);
                            recyclerViewTopSearch.setVisibility(View.VISIBLE);
                            JsonArray jsonArray = jsonObject.get("books").getAsJsonArray();
                            for (int i=0; i<jsonArray.size(); i++) {
                                String json = jsonArray.get(i).toString();
                                Book book = new Gson().fromJson(json, Book.class);
                                topSearch.add(book);
                            }
                            recyclerViewTopSearch.getAdapter().notifyDataSetChanged();
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
}