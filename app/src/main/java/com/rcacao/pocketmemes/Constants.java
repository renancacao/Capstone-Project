package com.rcacao.pocketmemes;

import android.os.Environment;

import com.rcacao.pocketmemes.data.models.ColorItem;
import com.rcacao.pocketmemes.data.models.GroupIcon;

import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;

public class Constants {

    private Constants() {
    }

    public static final ColorItem[] COLORS = new ColorItem[]{
            new ColorItem(R.drawable.color_white, R.color.font_creator_white),
            new ColorItem(R.drawable.color_gray, R.color.font_creator_gray),
            new ColorItem(R.drawable.color_black, R.color.font_creator_black),
            new ColorItem(R.drawable.color_blue, R.color.font_creator_blue),
            new ColorItem(R.drawable.color_green, R.color.font_creator_green),
            new ColorItem(R.drawable.color_orange, R.color.font_creator_orange),
            new ColorItem(R.drawable.color_purple, R.color.font_creator_purple),
            new ColorItem(R.drawable.color_red, R.color.font_creator_red),
            new ColorItem(R.drawable.color_yellow, R.color.font_creator_yellow)
    };

    public static final int[] FONT_SIZE = new int[]{25, 50, 75, 100, 125, 150};

    static final String IMAGE_PATH
            = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).getPath() + "/pocketMemes/";

    public static List<GroupIcon> getIcons() {

        List<GroupIcon> icons = new ArrayList<>();

        icons.add(new GroupIcon(R.drawable.ic_whatshot));
        icons.add(new GroupIcon(R.drawable.ic_accessibility));
        icons.add(new GroupIcon(R.drawable.ic_airline_seat_individual_suite));
        icons.add(new GroupIcon(R.drawable.ic_account_balance));
        icons.add(new GroupIcon(R.drawable.ic_account_box));
        icons.add(new GroupIcon(R.drawable.ic_airplanemode_active));
        icons.add(new GroupIcon(R.drawable.ic_android));
        icons.add(new GroupIcon(R.drawable.ic_assessment));
        icons.add(new GroupIcon(R.drawable.ic_attach_file));
        icons.add(new GroupIcon(R.drawable.ic_attach_money));
        icons.add(new GroupIcon(R.drawable.ic_beach_access));
        icons.add(new GroupIcon(R.drawable.ic_bookmark));
        icons.add(new GroupIcon(R.drawable.ic_brightness_low));
        icons.add(new GroupIcon(R.drawable.ic_build));
        icons.add(new GroupIcon(R.drawable.ic_business));
        icons.add(new GroupIcon(R.drawable.ic_card_giftcard));
        icons.add(new GroupIcon(R.drawable.ic_card_travel));
        icons.add(new GroupIcon(R.drawable.ic_casino));
        icons.add(new GroupIcon(R.drawable.ic_child_friendly));
        icons.add(new GroupIcon(R.drawable.ic_directions_car));
        icons.add(new GroupIcon(R.drawable.ic_directions_railway));
        icons.add(new GroupIcon(R.drawable.ic_directions_run));
        icons.add(new GroupIcon(R.drawable.ic_event_seat));
        icons.add(new GroupIcon(R.drawable.ic_explore));
        icons.add(new GroupIcon(R.drawable.ic_extension));
        icons.add(new GroupIcon(R.drawable.ic_face));
        icons.add(new GroupIcon(R.drawable.ic_favorite));
        icons.add(new GroupIcon(R.drawable.ic_fiber_new));
        icons.add(new GroupIcon(R.drawable.ic_fitness_center));
        icons.add(new GroupIcon(R.drawable.ic_flag));
        icons.add(new GroupIcon(R.drawable.ic_free_breakfast));
        icons.add(new GroupIcon(R.drawable.ic_games));
        icons.add(new GroupIcon(R.drawable.ic_gavel));
        icons.add(new GroupIcon(R.drawable.ic_grade));
        icons.add(new GroupIcon(R.drawable.ic_group_work));
        icons.add(new GroupIcon(R.drawable.ic_headset));
        icons.add(new GroupIcon(R.drawable.ic_headset_mic));
        icons.add(new GroupIcon(R.drawable.ic_help));
        icons.add(new GroupIcon(R.drawable.ic_high_quality));
        icons.add(new GroupIcon(R.drawable.ic_home));
        icons.add(new GroupIcon(R.drawable.ic_https));
        icons.add(new GroupIcon(R.drawable.ic_insert_emoticon));
        icons.add(new GroupIcon(R.drawable.ic_kitchen));
        icons.add(new GroupIcon(R.drawable.ic_language));
        icons.add(new GroupIcon(R.drawable.ic_library_music));
        icons.add(new GroupIcon(R.drawable.ic_lightbulb_outline));
        icons.add(new GroupIcon(R.drawable.ic_local_taxi));
        icons.add(new GroupIcon(R.drawable.ic_looks));
        icons.add(new GroupIcon(R.drawable.ic_mood_bad));
        icons.add(new GroupIcon(R.drawable.ic_motorcycle));
        icons.add(new GroupIcon(R.drawable.ic_movie));
        icons.add(new GroupIcon(R.drawable.ic_music_video));
        icons.add(new GroupIcon(R.drawable.ic_new_releases));
        icons.add(new GroupIcon(R.drawable.ic_pan_tool));
        icons.add(new GroupIcon(R.drawable.ic_perm_identity));
        icons.add(new GroupIcon(R.drawable.ic_pets));
        icons.add(new GroupIcon(R.drawable.ic_radio));
        icons.add(new GroupIcon(R.drawable.ic_room));
        icons.add(new GroupIcon(R.drawable.ic_school));
        icons.add(new GroupIcon(R.drawable.ic_shopping_cart));
        icons.add(new GroupIcon(R.drawable.ic_smoking_rooms));
        icons.add(new GroupIcon(R.drawable.ic_store));
        icons.add(new GroupIcon(R.drawable.ic_supervisor_account));
        icons.add(new GroupIcon(R.drawable.ic_theaters));
        icons.add(new GroupIcon(R.drawable.ic_thumb_down));
        icons.add(new GroupIcon(R.drawable.ic_thumb_up));
        icons.add(new GroupIcon(R.drawable.ic_timeline));
        icons.add(new GroupIcon(R.drawable.ic_videogame_asset));
        icons.add(new GroupIcon(R.drawable.ic_wc));
        icons.add(new GroupIcon(R.drawable.ic_weekend));

        return icons;
    }
}
