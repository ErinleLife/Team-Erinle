package com.arnav.pocdoc.utils;

import android.os.Environment;

public class Constants {
    public static final String uID = "uid";
    public static final String user_gson = "user_gson";
    public static final String user_id = "user_id";
    public static final String auth_token = "auth_token";
    public static final String position = "position";
    public static final String data = "data";
    public static final String pharmacy_id = "pharmacy_id";
    public static final String description = "description";
    public static final String image_back = "image_back";
    public static final String image_front = "image_front";
    public static final String pharmacy_locatore = "pharmacy_locatore";
    public static final String have_pharmacy_insurance = "have_pharmacy_insurance";
    public static final String type = "type";
    public static final String info = "info[]";

    public static final int REQUEST_CODE_STORAGE_PERMISSION = 111;
    public static final int REQUEST_CODE_CAMERA_PERMISSION = 110;
    public static final int REQUEST_CODE_PERMISSION_RESULT = 112;
    public static final int FLAG_CROP = 1314;
    public static final int REQUEST_CODE_CAMERA = 504;

    /**
     * CROP
     */
    public static String image = "image";
    public static String images = "images";
    public static String video = "video";
    public static String video_url = "video_url";
    public static String FLAG_IS_SQUARE = "flag_is_square";

    /**
     * IMAGE/VIDEO
     */
    public static String APP_ROOT_FOLDER = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath() + "/" + "AK" + "/";
    public static final String IMAGE_ROOT_FOLDER = APP_ROOT_FOLDER + "IMAGES";
    public static final String APP_DOWNLOADS_FOLDER = APP_ROOT_FOLDER + "MEDIA";
    public static final String AUDIO_ROOT_FOLDER = APP_ROOT_FOLDER + "AUDIO";
    public static final String IMAGE_FILE_NAME_PREFIX = "IMG_CAM" + "_X" + ".png";
    public static final String VIDEO_FILE_NAME_PREFIX = "VID_CAM" + "_X" + ".jpg";
    public static final String AUDIO_NAME_PREFIX = "AUDIO" + "_X" + ".mp3";

}
