package me.alisherafat.hooshang.app.configs;

import android.os.Environment;

/**
 * File structure configuration in SD CARD
 */
public class FileConfigs {
    public static final String DIR_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DIR_APP = DIR_SDCARD + "/Khatereha";
    public static final String DIR_IMAGES = DIR_APP + "/images";
    public static final String DIR_AUDIOS = DIR_APP + "/audios";
    public static final String DIR_VIDEOS = DIR_APP + "/videos";
    public static final String DIR_CONTACT_IMAGES = DIR_IMAGES + "/contacts";
    public static final String DIR_MEMORY_IMAGES = DIR_IMAGES + "/memories";
}
