#import "DYGameCordovaPlugin.h"
#import "UnionOpenPlatformCore/UOPManager.h"
#import "UnionOpenPlatformDataLink/UOPDataLinkManager.h"

@implementation DYGameCordovaPlugin

// Call after user agree privacy
- (void)init:(CDVInvokedUrlCommand *_Nonnull)command
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

- (void)onAccountRegister:(CDVInvokedUrlCommand *_Nonnull)command
{
    NSString *userId = [[command.arguments objectAtIndex:0] stringValue];
    BOOL success = [[UOPDataLinkManager sharedInstance] onAccountRegister:userId];
    CDVPluginResult *result;
    if (success) {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)onRoleRegister:(CDVInvokedUrlCommand *_Nonnull)command
{
    NSString *userId = [[command.arguments objectAtIndex:0] stringValue];
    NSString *roleId = [[command.arguments objectAtIndex:1] stringValue];
    BOOL success = [[UOPDataLinkManager sharedInstance] onRoleRegister:userId 
                                                        gameRoleID:roleId];
    CDVPluginResult *result;
    if (success) {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)onAccountLogin:(CDVInvokedUrlCommand *_Nonnull)command
{
    NSString *userId = [[command.arguments objectAtIndex:0] stringValue];
    int64_t lastLoginTime = [[command.arguments objectAtIndex:1] longLongValue];
    BOOL success = [[UOPDataLinkManager sharedInstance] onAccountLogin:userId
                                                        lastLoginTime:lastLoginTime];
    CDVPluginResult *result;
    if (success) {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)onRoleLogin:(CDVInvokedUrlCommand *_Nonnull)command
{
    NSString *userId = [[command.arguments objectAtIndex:0] stringValue];
    NSString *roleId = [[command.arguments objectAtIndex:1] stringValue];
    int64_t lastLoginTime = [[command.arguments objectAtIndex:2] longLongValue];
    BOOL success = [[UOPDataLinkManager sharedInstance] onRoleLogin:userId
                                                        gameRoleID:roleId 
                                                        lastRoleLoginTime:lastLoginTime];
    CDVPluginResult *result;
    if (success) {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)onPay:(CDVInvokedUrlCommand *_Nonnull)command
{
    NSString *userId = [[command.arguments objectAtIndex:0] stringValue];
    NSString *roleId = [[command.arguments objectAtIndex:1] stringValue];
    NSString *orderId = [[command.arguments objectAtIndex:2] stringValue];
    int64_t amount = [[command.arguments objectAtIndex:3] longLongValue];
    NSString *productId = [[command.arguments objectAtIndex:4] stringValue];
    NSString *productName = [[command.arguments objectAtIndex:5] stringValue];
    NSString *productDesc = [[command.arguments objectAtIndex:6] stringValue];
    BOOL success = [[UOPDataLinkManager sharedInstance] onPay:userId 
                                                        gameRoleID:roleId 
                                                        gameOrderID:orderId 
                                                        totalAmount:amount
                                                        productID:productId 
                                                        productName:productName 
                                                        productDesc:productDesc];
    CDVPluginResult *result;
    if (success) {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

@end
