package com.tadazly.dygame;

import android.app.Application;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;

public class GDTActionCordovaPlugin extends CordovaPlugin {
    public final static String LOG_TAG = "plugin.DYGame";
    private static String GDT_USER_ACTION_SET_ID;
    private static String GDT_APP_SECRET_KEY;

    // 标记是否初始化过Sdk
    private static boolean hasInitSdk = false;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        GDT_USER_ACTION_SET_ID = webView.getPreferences().getString("GDT_USER_ACTION_SET_ID", "");
        GDT_APP_SECRET_KEY = webView.getPreferences().getString("GDT_APP_SECRET_KEY", "");
        Log.d(LOG_TAG, "GDTAction pluginInitialize");
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) {
        Log.d(LOG_TAG, "DYGame Action " + action);
        if (action.equals("init")) {
            return this.init(args, callbackContext);
        }

        if (!hasInitSdk) {
            Log.e(LOG_TAG, "DYGame not Init !");
            callbackContext.error("DYGame not Init ! Please Call init First !");
            return false;
        }

        if (action.equals("onGameActive")) {
            return this.onGameActive(args, callbackContext);
        }
        else if (action.equals("onAccountRegister")) {
            return this.onAccountRegister(args, callbackContext);
        }
        else if (action.equals("onRoleRegister")) {
            return this.onRoleRegister(args, callbackContext);
        }
        else if (action.equals("onAccountLogin")) {
            return this.onAccountLogin(args, callbackContext);
        }
        else if (action.equals("onRoleLogin")) {
            return this.onRoleLogin(args, callbackContext);
        }
        else if (action.equals("onPay")) {
            return this.onPay(args, callbackContext);
        }
        return false;
    }

    private boolean init(CordovaArgs args, CallbackContext callbackContext) {
        if (hasInitSdk) {
            callbackContext.success();
            return true;
        }

        try {
            Application app = cordova.getActivity().getApplication();
            callbackContext.success();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            Log.e(LOG_TAG, "DYGame init Error: Failed to init !");
            callbackContext.error(e.toString());
        }
        return true;
    }

    private boolean onGameActive(CordovaArgs args, CallbackContext callbackContext) {
        boolean result = DataLinkReportService.getInstance().onGameActive();
        if (result) {
            callbackContext.success();
        } else {
            callbackContext.error("onGameActive failed");
        }
        return true;
    }

    private boolean onAccountRegister(CordovaArgs args, CallbackContext callbackContext) {
        String gameUserId = args.getString(0);
        boolean result = GBCommonSDK.getService(IDataLinkService.class)
            .onAccountRegister(gameUserId);
        if (result) {
            callbackContext.success();
        } else {
            callbackContext.error("onAccountRegister failed");
        }
        return true;
    }

    private boolean onRoleRegister(CordovaArgs args, CallbackContext callbackContext) {
        String gameUserId = args.getString(0);
        String gameRoleId = args.getString(1);
        boolean result = GBCommonSDK.getService(IDataLinkService.class)
            .onRoleRegister(gameUserId, gameRoleId);
        if (result) {
            callbackContext.success();
        } else {
            callbackContext.error("onRoleRegister failed");
        }
        return true;
    }

    private boolean onAccountLogin(CordovaArgs args, CallbackContext callbackContext) {
        String gameUserId = args.getString(0);
        long gameUserLastLoginTime = args.getLong(1);
        boolean result = GBCommonSDK.getService(IDataLinkService.class)
            .onAccountLogin(gameUserId, gameUserLastLoginTime);
        if (result) {
            callbackContext.success();
        } else {
            callbackContext.error("onAccountLogin failed");
        }
        return true;
    }

    private boolean onRoleLogin(CordovaArgs args, CallbackContext callbackContext) {
        String gameUserId = args.getString(0);
        String gameRoleId = args.getString(1);
        long gameRoleLastLoginTime = args.getLong(2);
        boolean result = GBCommonSDK.getService(IDataLinkService.class)
            .onRoleLogin(gameUserId, gameRoleId, gameRoleLastLoginTime);
        if (result) {
            callbackContext.success();
        } else {
            callbackContext.error("onAccountLogin failed");
        }
        return true;
    }

    private boolean onPay(CordovaArgs args, CallbackContext callbackContext) {
        String gameUserId = args.getString(0);
        String gameRoleId = args.getString(1);
        String gameOrderId = args.getString(2);
        long totalAmount = args.getLong(3);
        String productId = args.getString(4);
        String productName = args.getString(5);
        String productDesc = args.getString(5);
        
        boolean result = GBCommonSDK.getService(IDataLinkService.class).onPay(
            gameUserId, gameRoleId, gameOrderId, totalAmount,
            productId, productName, productDesc
        );
        if (result) {
            callbackContext.success();
        } else {
            callbackContext.error("onAccountLogin failed");
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}