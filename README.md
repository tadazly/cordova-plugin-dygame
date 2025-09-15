# cordova-plugin-dygame

抖音全官服促活分账接入 “数据深度合作”

## 获取 App ID 和 Client Key

请前往 [**厂商合作平台 -> 开发者服务 -> 进阶服务**](https://game.open.douyin.com/platform/product_list?) 查看。

## 安装

### 参数

参数名 | 必填 | 说明
-- | -- | --
APP_ID | 是 | 抖音 App ID
CLIENT_KEY | 是 | 抖音 Client Key
OSDK_TYPE | 否 | `Omnichannel_DataLink` 为“回传游戏用户的活跃数据和付费数据”，`Omnichannel_ActivityLink` 为“回传游戏用户的活跃数据”，默认值为 `Omnichannel_DataLink`
SCREEN_ORIENTATION | 否 | 屏幕方向，`sensorPortrait` 为竖屏，`sensorLandscape` 为横屏，默认值为 `sensorLandscape`
DEBUG_MODE | 否 | 是否开启调试模式，可选值为 `true` 或 `false`，默认值为 `false`

### 命令行安装

- npm

``` shell
cordova plugin add cordova-plugin-dygame \
  --variable APP_ID=123456 \
  --variable CLIENT_KEY=abcdefghijklmnop

cordova plugin rm cordova-plugin-dygame \
  --variable APP_ID=123456 \
  --variable CLIENT_KEY=abcdefghijklmnop
```

- git

``` shell
cordova plugin add https://github.com/tadazly/cordova-plugin-dygame.git \
  --variable APP_ID=123456 \
  --variable CLIENT_KEY=abcdefghijklmnop

cordova plugin rm cordova-plugin-dygame \
  --variable APP_ID=123456 \
  --variable CLIENT_KEY=abcdefghijklmnop
```

- 本地调试

```shell
cordova plugin add /path/to/cordova-plugin-dygame \
  --variable APP_ID=123456 \
  --variable CLIENT_KEY=abcdefghijklmnop \
  --link

cordova plugin rm /path/to/cordova-plugin-dygame \
  --variable APP_ID=123456 \
  --variable CLIENT_KEY=abcdefghijklmnop
```

## 手动配置参数文件

插件会根据传入的参数自动生成配置文件，如果失败，可以手动放入配置文件。

请前往 [厂商合作平台 -> 开发者服务 -> 进阶服务 -> **下载参数文件**](https://game.open.douyin.com/platform/product_list?)

在下载到的json文件中添加 `"osdk_type"`，示例：

``` json
{
    "app_id": "180***",
    "screen_orientation": "sensorLandscape",
    "osdk_type": "Omnichannel_DataLink",
    "client_key": {
        "douyin_key": "awt*********9enj"
    },
    "debug_mode": false,
}
```

- android

    放到 `platforms/android/app/src/main/assets/config.json`

- ios

    放到 `www/UOPSDKConfig.json` 和 `platforms/ios/www/UOPSDKConfig.json`

## 安卓需要继承 Application（直接或间接继承 Application）

- 游戏的 Application 直接继承 com.bytedance.ttgame.tob.common.host.api.GBApplication，如下：

``` java
import com.bytedance.ttgame.tob.common.host.api.GBApplication;

public class XXApplication extends GBApplication {
    //...
}
```

- 如果不能继承 GBApplication，则需要在 Application 的 onCreate 函数中实现以下方法

    - 如果直接继承 Application，则不需要再间接调用 Application，避免重复。

    ```java
    import android.app.Application;

    public class XXApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            // 请对齐游戏的application的onCreate时机，不要延后调用
            // 确保GBCommonSDK.onCreate(...)调用在GBCommonSDK.init(...) 调用之前
            GBCommonSDK.onCreate(this);
        }
        
    }
    ```