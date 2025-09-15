#import <Cordova/CDV.h>

@interface DYGameCordovaPlugin : CDVPlugin

- (void)init:(CDVInvokedUrlCommand *_Nonnull)command;
- (void)onGameActive:(CDVInvokedUrlCommand *_Nonnull)command;
- (void)onAccountRegister:(CDVInvokedUrlCommand *_Nonnull)command;
- (void)onRoleRegister:(CDVInvokedUrlCommand *_Nonnull)command;
- (void)onAccountLogin:(CDVInvokedUrlCommand *_Nonnull)command;
- (void)onRoleLogin:(CDVInvokedUrlCommand *_Nonnull)command;
- (void)onPay:(CDVInvokedUrlCommand *_Nonnull)command;
@end
