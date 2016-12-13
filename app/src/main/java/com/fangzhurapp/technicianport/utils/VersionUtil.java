package com.fangzhurapp.technicianport.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by android on 2016/8/30.
 */
public class VersionUtil {



    public  static int  getVersion(Context context){


        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            int versionCode = packageInfo.versionCode;
            return versionCode;


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
