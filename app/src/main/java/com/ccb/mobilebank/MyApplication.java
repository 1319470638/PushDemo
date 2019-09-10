package com.ccb.mobilebank;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.huawei.android.hms.agent.HMSAgent;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.lang.reflect.Method;
import java.util.List;

public class MyApplication extends Application {

    // user your appid the key.
    private static final String APP_ID = "2882303761518125196";
    // user your appid the key.
    private static final String APP_KEY = "5821812543196";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo
    public static final String TAG = "com.ccb.mobilebank";
    private static PushHandler pushHandler;
    public static PushActivity pushActivity;


    @Override
    public void onCreate() {
        super.onCreate();

        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {

            if(canHuaWeiPush()){
                HMSAgent.init(this);
            }else {


                MiPushClient.registerPush(this, APP_ID, APP_KEY);
                LoggerInterface newLogger = new LoggerInterface() {

                    @Override
                    public void setTag(String tag) {
                        // ignore
                    }

                    @Override
                    public void log(String content, Throwable t) {
                        Log.d(TAG, content, t);
                    }

                    @Override
                    public void log(String content) {
                        Log.d(TAG, content);
                    }
                };
                Logger.setLogger(this, newLogger);

                if (pushHandler == null) {
                    pushHandler = new PushHandler(getApplicationContext());
                }
            }

        }

    }
    public static void setPushActivity(PushActivity activity) {
        pushActivity = activity;
    }
    /**
     * 判断是否可以使用华为推送
     *
     * @return
     */
    public static Boolean canHuaWeiPush() {

        int emuiApiLevel = 0;
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return emuiApiLevel > 5.0;

    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static void reInitPush(Context ctx) {
        MiPushClient.registerPush(ctx.getApplicationContext(), APP_ID, APP_KEY);
    }
    public static PushHandler getHandler() {
        return pushHandler;
    }
    public static class PushHandler extends Handler {

        private Context context;

        public PushHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            Log.d("handleMessage","msg:===="+s);
            if (!TextUtils.isEmpty(s)) {
                pushActivity.showLog(s);
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        }
    }
}
