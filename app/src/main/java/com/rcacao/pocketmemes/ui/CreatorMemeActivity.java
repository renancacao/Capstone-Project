package com.rcacao.pocketmemes.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rcacao.pocketmemes.Constants;
import com.rcacao.pocketmemes.FileUtils;
import com.rcacao.pocketmemes.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatorMemeActivity extends BaseActivity {

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
        setBarColor(R.color.webSearchToolbarDark);
        ButterKnife.bind(this);

        createTargetImage();

        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (Intent.ACTION_SEND.equals(action) && intent.getType() != null) {
                loadImage((Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM));
            } else {
                loadImage(getIntent().getStringExtra(ARG_URL_IMAGE));
            }
        }

        setupButtons();
        setupTextWatcher();
    }

    private void loadImage(String imageUrl) {
        Picasso.get().load(imageUrl).into(target);
    }

    private void loadImage(Uri imageUri) {
        Picasso.get().load(imageUri).into(target);
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

    @OnClick(R.id.button_color_top)
    void clickButtonColorTop() {
        colorTopIndex = changeColor(buttonColorTop, colorTopIndex);
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

    private void setupColorButtom(Button button, int index) {
        button.setBackgroundResource(Constants.COLORS[index].getDrawable());
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

    private int changeSize(Button button, int index) {
        index += 1;
        if (index >= Constants.FONT_SIZE.length) {
            index = 0;
        }
        setupSizeButtom(button, index);
        return index;
    }

    private void setupSizeButtom(Button button, int index) {
        button.setText(String.valueOf(Constants.FONT_SIZE[index]));
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

    @OnClick(R.id.menu_back)
    void clickBack() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.menu_ok)
    void clickOk() {

        if (FileUtils.hasStoragePermission(this)) {
            saveMeme();
        }

    }

    private void saveMeme() {
        if (newBitmap != null) {
            String filename = FileUtils.getFileName();

            if (FileUtils.saveBitmap(filename, newBitmap, this)) {
                Log.i(this.getLocalClassName(), String.format(getString(R.string.file_saved), filename));
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(EditActivity.EXTRA_FILE_NAME, filename);
                startActivityForResult(intent, REQUEST_EDIT);
            } else {
                Toast.makeText(this, R.string.file_not_saved,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            if (isTaskRoot()) {
              Intent intent = new Intent(this, MainActivity.class);
              startActivity(intent);
            }
            finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveMeme();
        }
    }
}
