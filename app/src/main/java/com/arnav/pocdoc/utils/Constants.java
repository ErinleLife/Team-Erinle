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
    public static final String info = "info";
    public static final String isOTC = "ISOTC";
    public static final String pharmacyPosition = "pharmacyPosition";
    public static final String page = "page";
    public static final String per_page = "per_page";
    public static final String to_id = "to_id";
    public static final String from_id = "from_id";
    public static final String file = "file";
    public static final String message = "message";
    public static final String fcm_token = "fcm_token";
    public static final String fcm_registration_id = "fcm_registration_id";

    public static final int REQUEST_CODE_STORAGE_PERMISSION = 111;
    public static final int REQUEST_CODE_CAMERA_PERMISSION = 110;
    public static final int REQUEST_CODE_PERMISSION_RESULT = 112;
    public static final int FLAG_CROP = 1314;
    public static final int REQUEST_CODE_CAMERA = 504;

    public static final int ITEM_CLICK = 1;

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

    public static final String DATE_YYYY_MM_DD_HH_MM_AA_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PUSH_NOTIFICATION_FORMAT = "HHmmssSSS";
    public static final String DATE_EEE_DD_MM_FORMAT = "EEE dd/MM";
    public static final String DATE_HH_MM_AA_FORMAT = "hh:mm aa";


    /**
     * LIST PAGINATION
     */
    public static int pagination_start_offset = 1;
    public static int pagination_last_offset = -1;
    public static int PAGE_LIMIT = 50;

    public static final String HEADER = "HEADER";
    public static final String TEXT = "TEXT";
    public static final String IMAGE = "IMAGE";

}
