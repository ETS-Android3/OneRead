package com.example.oneread.Fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.oneread.Adapter.BookAdapter;
import com.example.oneread.Common.Common;
import com.example.oneread.Common.Message;
import com.example.oneread.Common.Utils;
import com.example.oneread.Model.Book;
import com.example.oneread.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuggestForYouFragment extends Fragment{

    private CompositeDisposable compositeDisposable;
    private Unbinder unbinder;
    private static SuggestForYouFragment instance;
    private List<Book> books = new ArrayList<>();
    private HashMap<String, Boolean> isFollowed = new HashMap<>();

    Parcelable state;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.shimmer_layout)
    ShimmerFrameLayout shimmerFrameLayout;

    public SuggestForYouFragment() {
    }


    public static SuggestForYouFragment getInstance() {
        if (instance == null) {
            return instance = new SuggestForYouFragment();
        } else return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            books = savedInstanceState.getParcelableArrayList("books");
            state = savedInstanceState.getParcelable("state");
        }
    }

    @Override
    public void onPause() {
        compositeDisposable.dispose();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        compositeDisposable.dispose();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        recyclerView.getLayoutManager().onRestoreInstanceState(state);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggest_for_you, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("books", new ArrayList<Book>(books));
        outState.putParcelable("state", recyclerView.getLayoutManager().onSaveInstanceState());
    }

    private void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        compositeDisposable = new CompositeDisposable();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new BookAdapter(view.getContext(),books, isFollowed));
        if (books.size() == 0 && Common.currentUser != null) {
            fetchBook();
        } else {
            if (shimmerFrameLayout.isShimmerStarted()) shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void fetchBook() {
        if (Utils.isNetworkAvailable(getContext())){
            try {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                shimmerFrameLayout.startShimmer();
                compositeDisposable.add(Common.iServiceAPI.getSuggestBook(Common.currentUser.getAccessToken(), Common.currentUser.getUsername())
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
                            System.out.println(((HttpException) err).code());
                            String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                            Utils.showToast(getContext(), Message.connectFail + "\n" + message, Toast.LENGTH_SHORT);
                        } else {
                            err.printStackTrace();
                            Utils.showToast(getContext(), Message.connectFail + "\n" + err.getMessage(), Toast.LENGTH_SHORT);
                        }
                    }));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void onLogin() {
        books.clear();
        fetchBook();
    }

    public void onLogout() {
        if (shimmerFrameLayout.isShimmerStarted()) shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}