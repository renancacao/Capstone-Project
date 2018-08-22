package com.rcacao.pocketmemes.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.rcacao.pocketmemes.MyUtils;
import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.adapters.IconAdapter;
import com.rcacao.pocketmemes.data.database.DataBaseContract;
import com.rcacao.pocketmemes.data.models.GroupIcon;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.nfc.NfcAdapter.EXTRA_ID;

public class EditActivity extends AppCompatActivity implements IconAdapter.IconClickListener {

    @BindView(R.id.image_meme)
    ImageView imageMeme;

    @BindView(R.id.recyclerView_groups)
    RecyclerView recyclerViewGroups;

    private int id_image = -1;

    private IconAdapter adapter;
    private List<GroupIcon> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ButterKnife.bind(this);

        if (getIntent() != null) {
            id_image = getIntent().getIntExtra(EXTRA_ID, -1);
        }
        if (id_image == -1) {
            finish();
        }

        openImage(id_image);

        groups = getGroupsIcons();
        adapter = new IconAdapter(this, groups);
        adapter.setHasStableIds(true);
        recyclerViewGroups.setAdapter(adapter);
        recyclerViewGroups.setHasFixedSize(true);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewGroups.setLayoutManager(layoutManager);
    }

    private void openImage(int id) {
        String filename = MyUtils.getFileName(id);
        Uri uri = Uri.fromFile(new File(filename));
        Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageMeme);
    }

    public List<GroupIcon> getGroupsIcons() {

        List<GroupIcon> list = new ArrayList<>();

        Cursor result = getContentResolver().query(DataBaseContract.GroupEntry.CONTENT_URI,
                null, null, null, DataBaseContract.GroupEntry._ID);

        if (result != null && result.moveToFirst()) {
            do {
                GroupIcon icon = new GroupIcon(
                        result.getInt(result.getColumnIndex(DataBaseContract.GroupEntry.COLUMN_IMAGE)));
                icon.setId(result.getInt(result.getColumnIndex(DataBaseContract.GroupEntry._ID)));
                list.add(icon);
            }
            while (result.moveToNext());
            result.close();
        }

        return list;

    }

    @Override
    public void onClick(int id) {
        groups.get(id).setChecked(!groups.get(id).isChecked());
        adapter.setIcons(groups);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.menu_back) void clickBack(){
        this.setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.menu_ok) void clickOK(){
        //TODO:salvar no bd

        this.setResult(RESULT_OK);
        finish();
    }
}
