package com.example.oneread.ui.detail.chapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.data.network.model.Book;
import com.example.oneread.data.network.model.Chapter;
import com.example.oneread.ui.base.BaseFragment;
import com.example.oneread.utils.MODE;

import javax.inject.Inject;
import java.util.List;

@SuppressLint("NonConstantResourceId")
public class ListChapterFragment extends BaseFragment implements ListChapterContract.View  {

    private static final String TAG = "ListChapterFragment";

    @Inject
    ListChapterPresenter<ListChapterContract.View> presenter;

    @BindView(R.id.list_chapter)
    RecyclerView listChapter;

    private Book book;
    private ListChapterAdapter adapter;
    private MODE mode;

    public ListChapterFragment(Book book, MODE mode) {
        this.book = book;
        this.mode = mode;
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(this);
        return view;
    }

    @Override
    protected void setup(View v) {
        listChapter.setNestedScrollingEnabled(true);
        listChapter.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        adapter = new ListChapterAdapter(getBaseActivity(), book, mode);
        listChapter.setAdapter(adapter);
        if (book.getChapters() != null || book.getChapters().size() == 0) {
            if (mode == MODE.ONLINE) {
                presenter.getChaptersOnline(book.getEndpoint());
            } else {
                presenter.getChaptersOffline(book.getEndpoint());
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setChapters(List<Chapter> chapters) {
        book.getChapters().clear();
        book.getChapters().addAll(chapters);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void hideErrorView() {

    }
}
