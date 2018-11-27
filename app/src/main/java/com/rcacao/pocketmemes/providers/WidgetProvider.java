package com.rcacao.pocketmemes.providers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.RemoteViews;

import com.rcacao.pocketmemes.FileUtils;
import com.rcacao.pocketmemes.R;
import com.rcacao.pocketmemes.data.database.DataBaseContract;
import com.rcacao.pocketmemes.data.database.DataBaseContract.RandomEntry;
import com.rcacao.pocketmemes.ui.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.io.File;

public class WidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            int idMeme;
            views.setViewVisibility(R.id.widget_empty_view,View.VISIBLE);

            Cursor cursor;
            cursor = context.getContentResolver().query(RandomEntry.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToNext()) {

                    views.setViewVisibility(R.id.widget_empty_view,View.GONE);

                    idMeme = cursor.getInt(cursor.getColumnIndex(DataBaseContract.MemeEntry._ID));
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.EXTRA_MEME_ID, idMeme);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    PendingIntent appPendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, 0);
                    views.setOnClickPendingIntent(R.id.image_widget, appPendingIntent);

                    File file = new File(
                            FileUtils.getFileNameWithPath(cursor.getString(
                                    cursor.getColumnIndex(DataBaseContract.MemeEntry.COLUMN_IMAGE))));
                    if (file.exists()) {
                        Picasso.get().load(file).into(views,R.id.image_widget,new int[]{appWidgetId});
                    } else {
                        views.setImageViewResource(R.id.image_widget, R.drawable.notfound);

                    }

                }
                cursor.close();
            }


            appWidgetManager.updateAppWidget(appWidgetId, views);

        }

    }


}
