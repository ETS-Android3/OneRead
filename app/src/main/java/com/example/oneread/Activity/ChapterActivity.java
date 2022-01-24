package com.example.oneread.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import com.example.oneread.Adapter.ChapterAdapter;
import com.example.oneread.Common.Common;
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
public class ChapterActivity extends AppCompatActivity{
    private CompositeDisposable compositeDisposable;

    @BindView(R.id.more)
    FloatingActionButton more;
    @BindView(R.id.next)
    FloatingActionButton next;
    @BindView(R.id.previous)
    FloatingActionButton prev;
    @BindView(R.id.beenhere)
    FloatingActionButton beenhere;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;

    private int position;
    private List<Chapter> chapter_list;
    private String book_endpoint;
    private String chapter_endpoint, endpoint;
    private Boolean isMoreClicked = true;
    private Boolean isMarkedChapterExisted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        ButterKnife.bind(this);

        position = getIntent().getIntExtra("position", 0);
        chapter_list = (List<Chapter>) getIntent().getSerializableExtra("chapter_list");
        book_endpoint = getIntent().getStringExtra("book_endpoint");
        chapter_endpoint = chapter_list.get(position).getChapterEndpoint();
        compositeDisposable = new CompositeDisposable();

        fetchChapter();


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibleButtons();
                setAnimateButtons();
                isMoreClicked = !isMoreClicked;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position - 1 < 0){
                    return;
                }
                Intent intent = new Intent(ChapterActivity.this, ChapterActivity.class);
                intent.putExtra("position", position - 1);
                intent.putExtra("chapter_list", (Serializable) chapter_list);
                intent.putExtra("book_endpoint", book_endpoint);
                startActivity(intent);
                finish();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position + 1 == chapter_list.size()){
                    return;
                }
                Intent intent = new Intent(ChapterActivity.this, ChapterActivity.class);
                intent.putExtra("position", position + 1);
                intent.putExtra("chapter_list", (Serializable) chapter_list);
                intent.putExtra("book_endpoint", book_endpoint);
                startActivity(intent);
                finish();
            }
        });

        beenhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isMarkedChapterExisted){
//                    markedChapterViewModel.update(new MarkedChapter(DetailComicActivity.endpoint, chapter_endpoint));
//                }else{
//                    markedChapterViewModel.insert(new MarkedChapter(DetailComicActivity.endpoint, chapter_endpoint));
//                }
//                Toast.makeText(ChapterActivity.this, "Đã đánh dấu", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchChapter() {
        compositeDisposable.add(Common.iServiceAPI.getChapter(book_endpoint, chapter_endpoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {
                    Chapter chapter = new Gson().fromJson(jsonObject.get("chapter"), Chapter.class);
                    viewPager.setAdapter(new ChapterAdapter(getApplication().getBaseContext(), chapter.getImages()));
                }));
    }

    private void setVisibleButtons(){
        if (isMoreClicked) {
            next.setVisibility(View.VISIBLE);
            prev.setVisibility(View.VISIBLE);
            beenhere.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
            beenhere.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimateButtons(){
        if (isMoreClicked) {
            Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_scale_out);
            next.startAnimation(anim);
            prev.startAnimation(anim);
            beenhere.startAnimation(anim);
        } else {
            Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_scale_in);
            next.startAnimation(anim);
            prev.startAnimation(anim);
            beenhere.startAnimation(anim);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}