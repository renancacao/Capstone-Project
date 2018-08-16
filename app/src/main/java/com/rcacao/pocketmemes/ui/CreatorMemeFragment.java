package com.rcacao.pocketmemes.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rcacao.pocketmemes.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreatorMemeFragment extends Fragment {
    public static final String ARG_URL_IMAGE = "param1";

    private String urlImage;


    public CreatorMemeFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.image_meme)
    ImageView imageMeme;

    public static CreatorMemeFragment newInstance(String urlImage) {
        CreatorMemeFragment fragment = new CreatorMemeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL_IMAGE, urlImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            urlImage = getArguments().getString(ARG_URL_IMAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_creator_meme, container, false);
        ButterKnife.bind(this, root);

        Picasso.get().load(urlImage).into(imageMeme);

        return root;
    }


   }
