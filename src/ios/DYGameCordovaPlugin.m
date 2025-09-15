#import "DYGameCordovaPlugin.h"
#import "UnionOpenPlatformCore/UOPManager.h"

@implementation DYGameCordovaPlugin

// SDK配置接口，可在隐私协议前调用，不采集个人信息
- (void)setupOSDK:(CDVInvokedUrlCommand *_Nonnull)command
{
    NSString *configFilePath = [[NSBundle mainBundle] pathForResource:@"UOPSDKConfig"
                                                               ofType:@"json"
                                                          inDirectory:@"www"];
    if (!configFilePath) {
        CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                 messageAsString:@"UOPSDKConfig.json not found in www/"];
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        return;
    }

    [[UOPManager sharedManager] setupWithConfigFilePath:configFilePath];
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

// 用户同意隐私协议后调用SDK启动接口
- (void)startOSDK:(CDVInvokedUrlCommand *_Nonnull)command
{
    [[UOPManager sharedManager] startOSDKWithCompletion:^(NSError *err) {
        CDVPluginResult *result;
        if (err) {
            NSLog(@"SDK初始化失败：%@", err);
            NSString *errorMessage = [NSString stringWithFormat:@"SDK初始化失败：%@", err];
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                     messageAsString:errorMessage];
        } else {
            NSLog(@"SDK初始化成功");
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        }
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    }];
}

- (void)onGameActive:(CDVInvokedUrlCommand *_Nonnull)command
{
    BOOL success = [[UOPDataLinkManager sharedInstance] onGameActive];
    CDVPluginResult *result;
    if (success) {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

@end
