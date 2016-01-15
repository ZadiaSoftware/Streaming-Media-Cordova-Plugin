#import <Cordova/CDVPlugin.h>

@interface StreamingMedia : CDVPlugin

- (void)playVideo:(CDVInvokedUrlCommand*)command;
- (void)playAudio:(CDVInvokedUrlCommand*)command;

@end