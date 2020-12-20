package com.mark.tiktok20;


public class AppConfig {

    public static final String API_URL = "https://apis.argear.io";
    public static final String API_KEY = "35a5d5fe47d8ebfdc681805f";
    public static final String SECRET_KEY = "672d19764c59d7665c39d911def11c059aef67599681ac59cb7279b15e35169b";
    public static final String AUTH_KEY = "U2FsdGVkX1+ZjvNERmgUDdfaAAj+6nTg1+FKvfVLjtDG3K5PuYdGLpknnNkAmEuD738Wc0+NRA4Ee1+77QtFhQ==";

    // preference
    public static final String USER_PREF_NAME = BuildConfig.APPLICATION_ID + ".Preference";
    public static final String USER_PREF_NAME_FILTER = BuildConfig.APPLICATION_ID + ".ARGearFilter.Preference";
    public static final String USER_PREF_NAME_STICKER = BuildConfig.APPLICATION_ID + ".ARGearItem.Preference";

    // camera
    // 1: CAMERA_API_1, 2: CAMERA_API_2
    public static final int USE_CAMERA_API = 2;

    // region - beauty sample
    public static final float[] BEAUTY_TYPE_INIT_VALUE = {
            10,     //VLINE
            90,     //ACE_SLIM
            55,     //JAW
            -50,    //CHIN
            5,      //EYE
            -10,    //EYE_GAP
            0,      //NOSE_LINE
            35,     //NOSE_SIDE
            30,     //NOSE_LENGTH
            -35,    //MOUTH_SIZE
            0,      //EYE_BACK
            0,      //EYE_CORNER
            0,      //LIP_SIZE
            50,     //SKIN
            0,      //DARK_CIRCLE
            0,      //MOUTH_WRINKLE
    };

    public static final float[] BASIC_BEAUTY_1 = {
            20, 10, 45, 45, 5, -10, 40, 20, 15, 0, 0, 0, 0, 50, 0, 0
    };

    public static final float[] BASIC_BEAUTY_2 = {
            10, 90, 55, -50, 5, -10, 0, 35, 30, -35, 0, 0, 0, 50, 0, 0
    };

    public static final float[] BASIC_BEAUTY_3 = {
            25, 20, 50, -25, 25, -10, 30, 40, 90, 0, 0, 0, 0, 50, 0, 0
    };
    // endregion
}
