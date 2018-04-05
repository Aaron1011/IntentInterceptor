package pw.aaron1011.intentinterceptor;

import android.app.ActivityOptions;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Interceptor implements IXposedHookLoadPackage {

    private static final String TAG = "Interceptor";

    @Override
    public  void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("android")) {
            return;
        }

        XposedBridge.log("We should be in ActivityStartInterceptor package from hook!");

        findAndHookMethod("com.android.server.am.ActivityStartInterceptor", lpparam.classLoader, "intercept",
                "android.content.Intent", "android.content.pm.ResolveInfo", "android.content.pm.ActivityInfo",
                String.class, "com.android.server.am.TaskRecord", int.class, int.class, "android.app.ActivityOptions",
                new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                Intent intent = (Intent) param.args[0];
                ResolveInfo resolveInfo = (ResolveInfo) param.args[1];
                ActivityInfo activityInfo = (ActivityInfo) param.args[2];
                String resolvedType = (String) param.args[3];
                Object taskRecord = param.args[4];
                int callingPid = (int) param.args[5];
                int callingUid = (int) param.args[6];
                ActivityOptions activityOptions = (ActivityOptions) param.args[7];

                CapturedIntent capturedIntent = new CapturedIntent(intent, resolveInfo, activityInfo, resolvedType, callingPid, callingUid);
                Log.d(TAG, "Starting activity: " + intent.toString());

                if (intent.getComponent().getClassName().equals("pw.aaron1011.intentinterceptor.InterceptHandlerService")) {
                    // Don't try to handle intents that we generate below
                    return;
                }

                Context context = AndroidAppHelper.currentApplication();
                Intent serviceIntent = new Intent();
                serviceIntent.setClassName("pw.aaron1011.intentinterceptor", "pw.aaron1011.intentinterceptor.InterceptHandlerService");
                serviceIntent.putExtra("capturedIntent", capturedIntent);

                context.startService(serviceIntent);
            }
        });
    }
}
