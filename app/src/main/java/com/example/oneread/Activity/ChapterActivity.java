package com.example.oneread.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import butterknife.OnClick;
import com.example.oneread.Adapter.ChapterAdapter;
import com.example.oneread.Adapter.DetailBookAdapter;
import com.example.oneread.Adapter.GenreAdapter;
import com.example.oneread.Common.Common;
import com.example.oneread.Listener.IRecylerClickListener;
import com.example.oneread.Model.Book;
import com.example.oneread.Model.Chapter;
import com.example.oneread.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.io.Serializable;
import java.util.List;

@SuppressLint("NonConstantResourceId")
public class ChapterActivity extends AppCompatActivity implements IRecylerClickListener {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CompositeDisposable compositeDisposable;
    private int position;
    private Book book;
    private String chapter_endpoint;
    private RecyclerView recyclerViewChapters;
    private AlertDialog.Builder alertDialog;
    private View chaptersDialog;
    private DetailBookAdapter chapterAdapter;


    @OnClick(R.id.prev)
    void prev() {
        if(position + 1 == book.getChapters().size()){
            return;
        }
        Intent intent = new Intent(ChapterActivity.this, ChapterActivity.class);
        intent.putExtra("position", position + 1);
        intent.putExtra("book", book);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.next)
    void next() {
        if(position - 1 < 0){
            return;
        }
        Intent intent = new Intent(ChapterActivity.this, ChapterActivity.class);
        intent.putExtra("position", position - 1);
        intent.putExtra("book", book);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.chapters)
    void showChapters() {
        if (!book.getChapters().isEmpty()) {
            if (chaptersDialog.getParent() != null) {
                ((ViewGroup) chaptersDialog.getParent()).removeView(chaptersDialog);
            }
            alertDialog.setView(chaptersDialog);
            AlertDialog alert = alertDialog.create();
            alert.show();
            alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.primary_text));
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary_text));
        }
    }

    @OnClick(R.id.comment)
    void showComment() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        ButterKnife.bind(this);

        position = getIntent().getIntExtra("position", 0);
        book = (Book) getIntent().getSerializableExtra("book");
        chapter_endpoint = book.getChapters().get(position).getChapterEndpoint();
        compositeDisposable = new CompositeDisposable();

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        toolbar.getBackground().setAlpha(200);

        alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        alertDialog.setTitle(R.string.chapters);
        chaptersDialog = LayoutInflater.from(this).inflate(R.layout.dialog_option, null);
        recyclerViewChapters = chaptersDialog.findViewById(R.id.recycler_option);
        recyclerViewChapters.setHasFixedSize(true);
        recyclerViewChapters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chapterAdapter = new DetailBookAdapter(this, book, this);
        recyclerViewChapters.setAdapter(chapterAdapter);
        alertDialog.setView(chaptersDialog);
        alertDialog.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

        fetchChapter();

    }

    private void fetchChapter() {
        compositeDisposable.add(Common.iServiceAPI.getChapter(book.getEndpoint(), chapter_endpoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {
                    Chapter chapter = new Gson().fromJson(jsonObject.get("chapter"), Chapter.class);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                    recyclerView.setAdapter(new ChapterAdapter(getApplication().getBaseContext(), chapter.getImages()));
                }));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onItemClick(int position) {
        finish();
    }

    @Override
    public void onCheckBoxClick(int postion, String state) {

    }
}