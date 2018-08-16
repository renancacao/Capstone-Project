package com.rcacao.pocketmemes.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rcacao.pocketmemes.R;

import butterknife.ButterKnife;

public class EditMemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meme);

        ButterKnife.bind(this);

        String url = "";

        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra(CreatorMemeFragment.ARG_URL_IMAGE)){
            url = intent.getStringExtra(CreatorMemeFragment.ARG_URL_IMAGE);
        }

        openCreator(url);
    }

    private void openCreator(String image) {

        CreatorMemeFragment creatorMemeFragment = CreatorMemeFragment.newInstance(image);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, creatorMemeFragment).commit();

    }
}
