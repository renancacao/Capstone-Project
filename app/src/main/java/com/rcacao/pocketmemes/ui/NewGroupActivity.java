package com.rcacao.pocketmemes.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.IconAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewGroupActivity extends AppCompatActivity {


    private static final int RESULT_NOTHING = 0;

    @BindView(R.id.recyclerView_icons)
    RecyclerView recyclerViewIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        ButterKnife.bind(this);

        IconAdapter adapter = new IconAdapter(this);
        adapter.setHasStableIds(true);
        recyclerViewIcons.setAdapter(adapter);
        recyclerViewIcons.setHasFixedSize(true);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this,3,LinearLayoutManager.HORIZONTAL,false);

        recyclerViewIcons.setLayoutManager(layoutManager);
    }

    @OnClick(R.id.menu_back)void clickMenuBack(){
        setResult(RESULT_NOTHING);
        finish();
    }

}
