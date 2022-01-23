package com.example.oneread.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.oneread.Common.Common;
import com.example.oneread.Common.Message;
import com.example.oneread.Common.Utils;
import com.example.oneread.Fragment.MyBottomSheetFragement;
import com.example.oneread.Model.Book;
import com.example.oneread.Model.Chapter;
import com.example.oneread.Model.User;
import com.example.oneread.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import java.io.Serializable;
import java.util.List;

public class DetailBookActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.follow)
    TextView follow;
    @BindView(R.id.genre)
    TextView genre;
    @BindView(R.id.view)
    TextView view;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.theme)
    ImageView theme;
    @BindView(R.id.thumb)
    ImageView thumb;


    private CompositeDisposable compositeDisposable;
    private MyBottomSheetFragement bottomSheetDialog;
    private Book book;

    @OnClick(R.id.read)
    void read() {
//        int position = detailComic.chapter_list.size()-1;
//        Intent intent = new Intent(this, ChapterActivity.class);
//        intent.putExtra("position", position);
//        intent.putExtra("chapter_list", (Serializable) detailComic.chapter_list);
//        startActivity(intent);
    }

    @OnClick(R.id.read_continue)
    void readContinue() {
//        int position=detailComic.chapter_list.size()-1;
//        if(chapter_endpoint == null) return;
//        for(int i=0 ;i<detailComic.chapter_list.size(); i++){
//            if(detailComic.chapter_list.get(i).chapter_endpoint.equals(chapter_endpoint)){
//                position = i;
//                break;
//            }
//        }
//        Intent intent = new Intent(DetailComicActivity.this, ChapterActivity.class);
//        intent.putExtra("position", position);
//        intent.putExtra("chapter_list", (Serializable) detailComic.chapter_list);
//        startActivity(intent);
    }

    @OnClick(R.id.chapter_list)
    void showChapterList() {
        bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
    }

    @OnClick(R.id.btn_follow)
    void followBook() {

    }

    @OnClick(R.id.btn_go_back)
    void goBack() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);

        book = new Book();
        book.setEndpoint(getIntent().getStringExtra("endpoint"));

        ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();

        fetchDetailComic();
    }

    private void fetchDetailComic() {
        try{
            compositeDisposable.addAll(Common.iServiceAPI.getBook(book.getEndpoint())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        book = new Gson().fromJson(jsonObject.get("book").toString(), Book.class);
                        title.setText(book.getTitle());
                        author.setText(book.getAuthor());
                        status.setText(book.getStatusString());
                        StringBuilder builder = new StringBuilder();
                        rating.setText(builder.append(book.getRating()).append(" - ").append(book.getRateCount()).toString());
                        follow.setText(book.getFollow());
                        builder = new StringBuilder();
                        for(int i=0; i<book.getGenres().size(); i++){
                            if(i==0)
                                builder.append(book.getGenres().get(i).getTitle());
                            else
                                builder.append(",").append(book.getGenres().get(i).getTitle());
                        }
                        genre.setText(builder.toString());
                        view.setText(book.getView());
                        desc.setText(book.getDescription());
                        Picasso.get().load(book.getTheme()).into(theme);
                        Picasso.get().load(book.getThumb()).into(thumb);
                    }, err -> {
                        if (err instanceof HttpException) {
                            HttpException response = (HttpException) err;
                            String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                            Utils.showToast(this, Message.connectFail + "\n" + message, Toast.LENGTH_SHORT);
                        } else {
                            err.printStackTrace();
                            Utils.showToast(this, Message.connectFail + "\n" + err.getMessage(), Toast.LENGTH_SHORT);
                        }
                    }), Common.iServiceAPI.getChapterList(book.getEndpoint())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        JsonArray jsonArray = jsonObject.get("chapters").getAsJsonArray();
                        for (int i=0; i<jsonArray.size(); i++) {
                            String json = jsonArray.get(i).toString();
                            Chapter chapter = new Gson().fromJson(json, Chapter.class);
                            book.getChapters().add(chapter);
                        }
                        bottomSheetDialog = new MyBottomSheetFragement(getBaseContext(), book);
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