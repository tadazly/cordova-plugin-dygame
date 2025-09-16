package com.tadazly.dygame;

import android.util.Log;

import com.bytedance.ttgame.tob.common.host.api.GBCommonSDK;
import com.bytedance.ttgame.tob.common.host.api.callback.InitCallback;
import com.bytedance.ttgame.tob.common.host.api.config.InitConfig;
import com.bytedance.ttgame.tob.optional.datalink.api.DataLinkReportService;
import com.bytedance.ttgame.tob.optional.datalink.api.IDataLinkService;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

public class DYGameCordovaPlugin extends CordovaPlugin {
    public final static String LOG_TAG = "plugin.DYGame";

    // 标记是否初始化过Sdk
    private static boolean hasInitSdk = false;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        Log.d(LOG_TAG, "DYGame pluginInitialize");
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
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
            InitConfig initConfig = new InitConfig.Builder()
                    .packageChannel(InitConfig.PackageChannel.DOUYIN)
                    .build();

            GBCommonSDK.init(cordova.getActivity(), new InitCallback() {
                @Override
                public void onSuccess() {
                    hasInitSdk = true;
                    callbackContext.success();
                }

                @Override
                public void onFailed(int code, String msg) {
                    callbackContext.error("GBCommonSDK init Failed, code:" + code + ", msg:" + msg);
                }
            }, initConfig);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            Log.e(LOG_TAG, "DYGame init Error: Failed to init !");
            callbackContext.error("DYGame init Error: Failed to Init, " + e.toString());
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

    private boolean onAccountRegister(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
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

    private boolean onRoleRegister(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
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

    private boolean onAccountLogin(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
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

    private boolean onRoleLogin(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
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

    private boolean onPay(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        String gameUserId = args.getString(0);
        String gameRoleId = args.getString(1);
        String gameOrderId = args.getString(2);
        long totalAmount = args.getLong(3);
        String productId = args.getString(4);
        String productName = args.getString(5);
        String productDesc = args.getString(6);
        
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