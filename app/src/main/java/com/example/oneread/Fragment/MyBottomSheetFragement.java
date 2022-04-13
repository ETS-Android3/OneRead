package com.example.oneread.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.Adapter.DetailBookAdapter;
import com.example.oneread.Listener.IRecylerClickListener;
import com.example.oneread.Model.Book;
import com.example.oneread.Model.Chapter;
import com.example.oneread.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@SuppressLint("NonConstantResourceId")
public class MyBottomSheetFragement extends BottomSheetDialogFragment implements IRecylerClickListener {
    Context context;
    List<Chapter> download_chapters;
    List<File> delete_chapters, list_file;
    Book book;
    File[] files;
    Boolean offline = false;


    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.btn_download)
    ImageView btn_download;

    public MyBottomSheetFragement() {
    }

    public MyBottomSheetFragement(Context context, Book book) {
        this.context = context;
        this.book = book;
        offline = false;
    }

    public MyBottomSheetFragement(Context context, File[] files) {
        this.context = context;
        this.files = files;
        Arrays.sort(this.files);
        list_file = new ArrayList<>(Arrays.asList(files));
        offline = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_chapter_list, null);
        ButterKnife.bind(this, contentView);
        contentView.setBackground(getResources().getDrawable(R.drawable.bottom_sheet));
        contentView.getBackground().setAlpha(200);
        dialog.setContentView(contentView);

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(!offline){
            recyclerView.setAdapter(new DetailBookAdapter(context, book, this));
            download_chapters = new ArrayList<>();
            btn_download.setImageResource(R.drawable.ic_download);
        }else{
            recyclerView.setAdapter(new DetailBookAdapter(context, list_file, this));
            delete_chapters = new ArrayList<>();
            btn_download.setImageResource(R.drawable.ic_delete);
        }

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(offline){
//                    for(File f : delete_chapters){
//                        deleteRecursive(f);
//                        list_file.remove(f);
//                    }
//                    delete_chapters.clear();
//                    recyclerView.getAdapter().notifyDataSetChanged();
//                }else{
//                    Intent intent = new Intent(context, DownloadActivity.class);
//                    intent.putExtra("download_chapters", (Serializable) download_chapters);
//                    intent.putExtra("thumb", detailComic.thumb);
//                    intent.putExtra("theme", detailComic.theme);
//                    intent.putExtra("title", detailComic.title);
//                    String genre;
//                    StringBuilder stringBuilder = new StringBuilder();
//                    for(int i=0; i<detailComic.genre_list.size(); i++){
//                        stringBuilder.append(", ").append(detailComic.genre_list.get(i).genre_name);
//                    }
//                    genre = String.valueOf(stringBuilder).substring(2);
//                    intent.putExtra("genre", genre);
//                    intent.putExtra("desc", detailComic.desc);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
            }
        });
        return root;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onCheckBoxClick(int postion, String state) {
//        if(offline){
//            if(state.equals("check")){
//                delete_chapters.add(list_file.get(postion));
//            }else{
//                delete_chapters.remove(list_file.get(postion));
//            }
//        }else{
//            if(state.equals("check")){
//                download_chapters.add(chapter_list.get(postion));
//            }else{
//                download_chapters.remove(chapter_list.get(postion));
//            }
//        }
    }


    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory() && fileOrDirectory != null)
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}
