package com.example.oneread.Activity;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.oneread.Common.Common;
import com.example.oneread.Common.Message;
import com.example.oneread.Common.Utils;
import com.example.oneread.Fragment.MyBottomSheetFragement;
import com.example.oneread.Model.Book;
import com.example.oneread.Model.Chapter;
import com.example.oneread.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.nex3z.flowlayout.FlowLayout;
import com.squareup.picasso.Picasso;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class DetailBookActivity extends AppCompatActivity{

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
    @BindView(R.id.genre_layout)
    FlowLayout genre_layout;
    @BindView(R.id.view)
    TextView view;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.thumb)
    ImageView thumb;
    @BindView(R.id.read_more)
    TextView readMore;


    private CompositeDisposable compositeDisposable;
    private MyBottomSheetFragement bottomSheetDialog;
    private Book book;
    private boolean isGetBookFromSearch;

    @OnClick(R.id.read_more)
    void readMore() {
        desc.setEllipsize(null);
        desc.setMaxLines(Integer.MAX_VALUE);
        readMore.setVisibility(View.GONE);
    }

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

    @OnClick(R.id.chapters)
    void showChapterList() {
//        BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
//        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_chapter_list, (LinearLayout)findViewById(R.id.bottom_sheet));
//        view.setBackground(getResources().getDrawable(R.drawable.bottom_sheet));
//        view.getBackground().setAlpha(200);
//        bottomSheetDialog1.setContentView(view);
//        bottomSheetDialog1.show();
        bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
    }

    @OnClick(R.id.btn_follow)
    void followBook() {

    }

    @OnClick(R.id.comment)
    void comment() {

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
        isGetBookFromSearch = getIntent().getBooleanExtra("search", false);

        ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();

        fetchDetailComic();
    }

    private void fetchDetailComic() {
        try{
            compositeDisposable.addAll(Common.iServiceAPI.getBook(book.getEndpoint(), isGetBookFromSearch)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        book = new Gson().fromJson(jsonObject.get("book").toString(), Book.class);
                        title.setText(book.getTitle());
                        author.setText(book.getAuthor());
                        status.setText(book.getStatusString());
                        rating.setText(String.valueOf(book.getRating()));
                        follow.setText(book.getFollow());
                        for(int i=0; i<book.getGenres().size(); i++){
                            TextView genre = new TextView(this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(10,10,10,10);
                            genre.setLayoutParams(params);
                            genre.setBackground(getResources().getDrawable(R.drawable.genre_item));
                            genre.getBackground().setAlpha(200);
                            genre.setPadding(20,10,20,10);
                            genre.setText(book.getGenres().get(i).getTitle());
                            genre.setTextColor(Color.WHITE);
                            genre_layout.addView(genre);
                        }
                        view.setText(book.getView());
                        desc.setText(book.getDescription());
                        Picasso.get().load(book.getThumb()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(thumb);
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
                        bottomSheetDialog = new MyBottomSheetFragement(this, book);
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