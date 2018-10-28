package com.poberwong.launcher;

import android.app.Activity;
import android.content.Intent;
import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.*;

import java.io.Console;
import java.util.Set;
import java.util.Iterator;

import android.provider.Settings;
/**
 * Created by poberwong on 16/6/30.
 */
public class IntentLauncherModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final int REQUEST_CODE = 12;
    private static final String ATTR_ACTION = "action";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_CATEGORY = "category";
    private static final String TAG_EXTRA = "extra";
    private static final String ATTR_DATA = "data";
    private static final String ATTR_FLAGS = "flags";
    private static final String ATTR_PACKAGE_NAME = "packageName";
    private static final String ATTR_CLASS_NAME = "className";
    Promise promise;

    public IntentLauncherModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "IntentLauncher";
    }

    /**
     * 选用方案
     * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     * getReactApplicationContext().startActivity(intent);
     */
    @ReactMethod
    public void startActivity(final Promise promise) {
        this.promise = promise;         
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                             Uri.parse("package:" + getReactApplicationContext().getPackageName()));

        getReactApplicationContext().startActivityForResult(intent, REQUEST_CODE, null); // 暂时使用当前应用的任务栈
    }

    @Override
    public void onNewIntent(Intent intent) {
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        if (requestCode != REQUEST_CODE) {
            return;
        }
        WritableMap params = Arguments.createMap();
        if (intent != null) {
            params.putInt("resultCode", resultCode);

            Uri data = intent.getData();
            if (data != null) {
                params.putString("data", data.toString());
            }

            Bundle extras = intent.getExtras();
            if (extras != null) {
                params.putMap("extra", Arguments.fromBundle(extras));
            }
        }

        this.promise.resolve(params);
    }
}
