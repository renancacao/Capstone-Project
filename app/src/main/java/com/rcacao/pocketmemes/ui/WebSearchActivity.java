package com.rcacao.pocketmemes.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.ResultAdapter;
import com.rcacao.pocketmemes.data.models.SearchResult;
import com.rcacao.pocketmemes.loaders.GoogleSearchAsyncLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebSearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<SearchResult> {

    private static final int ID_LOADER = 0;

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView_web)
    RecyclerView recyclerView;

    private ResultAdapter adapter;

    private LinearLayoutManager glmg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search);

        ButterKnife.bind(this);


        adapter = new ResultAdapter(this, null);
        recyclerView.setAdapter(adapter);

        glmg = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

    }

    @OnClick(R.id.menu_search_send)
    void clickSearchMenu() {

        if (!etSearch.getText().toString().trim().isEmpty()) {
            Bundle args = new Bundle();
            args.putString(GoogleSearchAsyncLoader.ARG_SEARCH, etSearch.getText().toString().trim());
            getSupportLoaderManager().restartLoader(ID_LOADER, args, this);
        }

    }

    @OnClick(R.id.menu_back)
    void clickBackMenu() {
        finish();
    }

    @OnClick(R.id.menu_clear)
    void clickClearMenu() {
        etSearch.setText("");
    }


    @NonNull
    @Override
    public Loader<SearchResult> onCreateLoader(int id, @Nullable Bundle args) {

        progressBar.setVisibility(View.VISIBLE);
        return new GoogleSearchAsyncLoader(this, args);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<SearchResult> loader, SearchResult data) {
        SearchResult searchResult = data;
        if (searchResult != null) {
            adapter.setItems(searchResult.getItems());
            adapter.notifyDataSetChanged();
            recyclerView.setLayoutManager(glmg);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<SearchResult> loader) {

    }
}
