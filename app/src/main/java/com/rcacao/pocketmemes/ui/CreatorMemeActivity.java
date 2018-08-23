package com.rcacao.pocketmemes.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rcacao.pocketmemes.Constants;
import com.rcacao.pocketmemes.MyUtils;
import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.data.database.DataBaseContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.nfc.NfcAdapter.EXTRA_ID;

public class CreatorMemeActivity extends AppCompatActivity  {

    public static final String ARG_URL_IMAGE = "url_image";
    private static final int REQUEST_EDIT = 10;

    @BindView(R.id.image_meme)
    ImageView imageMeme;

    @BindView(R.id.editText_top)
    EditText editTextTop;

    @BindView(R.id.editText_middle)
    EditText editTextMiddle;

    @BindView(R.id.editText_bottom)
    EditText editTextBottom;

    @BindView(R.id.button_color_top)
    Button buttonColorTop;

    @BindView(R.id.button_color_middle)
    Button buttonColorMiddle;

    @BindView(R.id.button_color_bottom)
    Button buttonColorBottom;

    @BindView(R.id.button_size_top)
    Button buttonSizeTop;

    @BindView(R.id.button_size_middle)
    Button buttonSizeMiddle;

    @BindView(R.id.button_size_bottom)
    Button buttonSizeBottom;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private String urlImage;
    private Target target;

    private int colorTopIndex = 0;
    private int colorMiddleIndex = 0;
    private int colorBottomIndex = 0;

    private int sizeTopIndex = 0;
    private int sizeMiddleIndex = 0;
    private int sizeBottomIndex = 0;
    private int xText;
    private Paint paintTop;
    private Paint paintMiddle;
    private Paint paintBottom;
    private int yMiddle;
    private Rect rectBottom = null;
    private Rect rectTop = null;

    private Bitmap original;
    private Bitmap newBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_meme);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            urlImage = getIntent().getStringExtra(ARG_URL_IMAGE);
        }

        createTargetImage();
        Picasso.get().load(urlImage).into(target);

        setupButtons();
        setupTextWatcher();
    }

    private void setupTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                drawText(original);
            }
        };

        editTextTop.addTextChangedListener(textWatcher);
        editTextMiddle.addTextChangedListener(textWatcher);
        editTextBottom.addTextChangedListener(textWatcher);

    }

    private void setupButtons() {

        setupColorButtom(buttonColorTop, colorTopIndex);
        setupColorButtom(buttonColorMiddle, colorMiddleIndex);
        setupColorButtom(buttonColorBottom, colorBottomIndex);

        setupSizeButtom(buttonSizeTop, sizeTopIndex);
        setupSizeButtom(buttonSizeMiddle, sizeMiddleIndex);
        setupSizeButtom(buttonSizeBottom, sizeBottomIndex);

    }

    private void setupColorButtom(Button button, int index) {
        button.setBackgroundResource(Constants.COLORS[index].getDrawable());
    }

    private void setupSizeButtom(Button button, int index) {
        button.setText(String.valueOf(Constants.FONT_SIZE[index]));
    }

    @OnClick(R.id.button_color_top)
    void clickButtonColorTop() {
        colorTopIndex = changeColor(buttonColorTop, colorTopIndex);
        drawText(original);
    }

    @OnClick(R.id.button_color_middle)
    void clickButtonColorMidle() {
        colorMiddleIndex = changeColor(buttonColorMiddle, colorMiddleIndex);
        drawText(original);
    }

    @OnClick(R.id.button_color_bottom)
    void clickButtonColorBottom() {
        colorBottomIndex = changeColor(buttonColorBottom, colorBottomIndex);
        drawText(original);
    }

    @OnClick(R.id.button_size_top)
    void clickButtonSizeTop() {
        sizeTopIndex = changeSize(buttonSizeTop, sizeTopIndex);
        drawText(original);
    }

    @OnClick(R.id.button_size_middle)
    void clickButtonSizeMiddle() {
        sizeMiddleIndex = changeSize(buttonSizeMiddle, sizeMiddleIndex);
        drawText(original);
    }

    @OnClick(R.id.button_size_bottom)
    void clickButtonSizeBottom() {
        sizeBottomIndex = changeSize(buttonSizeBottom, sizeBottomIndex);
        drawText(original);
    }

    private int changeColor(Button button, int index) {
        index += 1;
        if (index >= Constants.COLORS.length) {
            index = 0;
        }
        setupColorButtom(button, index);
        return index;
    }

    private int changeSize(Button button, int index) {
        index += 1;
        if (index >= Constants.FONT_SIZE.length) {
            index = 0;
        }
        setupSizeButtom(button, index);
        return index;
    }

    private void createTargetImage() {
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                if (bitmap != null) {
                    original = bitmap;
                    drawText(original);
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }

    private void setupDrawObjects(Bitmap bitmap) {

        paintTop = new Paint();
        paintMiddle = new Paint();
        paintBottom = new Paint();

        yMiddle = bitmap.getHeight() / 2;
        xText = bitmap.getWidth() / 2;

        rectBottom = new Rect();
        rectTop = new Rect();


    }

    private void drawText(Bitmap bitmap) {

        if (rectBottom == null) {
            setupDrawObjects(bitmap);
        }

        newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        canvas.drawBitmap(bitmap, 0, 0, null);

        paintTop.setColor(ContextCompat.getColor(this, Constants.COLORS[colorTopIndex].getColor()));
        paintTop.setTextSize(Constants.FONT_SIZE[sizeTopIndex]);
        paintTop.setTextAlign(Paint.Align.CENTER);
        paintTop.setStyle(Paint.Style.FILL);

        paintMiddle.setColor(ContextCompat.getColor(this, Constants.COLORS[colorMiddleIndex].getColor()));
        paintMiddle.setTextSize(Constants.FONT_SIZE[sizeMiddleIndex]);
        paintMiddle.setTextAlign(Paint.Align.CENTER);
        paintMiddle.setStyle(Paint.Style.FILL);

        paintBottom.setColor(ContextCompat.getColor(this, Constants.COLORS[colorBottomIndex].getColor()));
        paintBottom.setTextSize(Constants.FONT_SIZE[sizeBottomIndex]);
        paintBottom.setTextAlign(Paint.Align.CENTER);
        paintBottom.setStyle(Paint.Style.FILL);

        paintBottom.getTextBounds(editTextBottom.getText().toString(),
                0, editTextBottom.getText().toString().length(), rectBottom);

        paintTop.getTextBounds(editTextTop.getText().toString(),
                0, editTextTop.getText().toString().length(), rectTop);

        int yBottom = bitmap.getHeight() - rectBottom.bottom - getResources().getInteger(R.integer.margin_creator);
        int yTop = Math.abs(rectTop.top) + getResources().getInteger(R.integer.margin_creator);

        canvas.drawText(editTextTop.getText().toString(), xText, yTop, paintTop);
        canvas.drawText(editTextMiddle.getText().toString(), xText, yMiddle, paintMiddle);
        canvas.drawText(editTextBottom.getText().toString(), xText, yBottom, paintBottom);

        imageMeme.setImageBitmap(newBitmap);
    }

    @OnClick(R.id.menu_back)
    void clickBack() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.menu_ok)
    void clickOk() {

        if (hasStoragePermission()) {
            saveMeme();
        }

    }

    private void saveMeme() {
        if (newBitmap != null) {
            Cursor result = getContentResolver().query(DataBaseContract.MemeEntry.CONTENT_URI_LAST,
                    null, null, null, null);

            int id = 0;
            if (result != null) {
                if (result.moveToFirst()) {
                    id = result.getInt(0) + 1;
                }
                result.close();
            }

            String filename = MyUtils.getFileName(id);
            if (saveBitmap(filename)) {
                Toast.makeText(this, String.format(getString(R.string.file_saved), filename),
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(EXTRA_ID, id);
                startActivityForResult(intent, REQUEST_EDIT);

            } else {
                Toast.makeText(this, R.string.file_not_saved,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean saveBitmap(String filename) {
        boolean result = true;
        FileOutputStream out = null;
        try {
            File dir = new File(Constants.IMAGE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
            out = new FileOutputStream(filename);
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            updateImages(file);

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void updateImages(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            saveMeme();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }

    }
}
