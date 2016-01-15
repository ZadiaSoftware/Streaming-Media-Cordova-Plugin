#import "StreamingMedia.h"
#import "StreamingViewController.h"
#import <Cordova/CDV.h>

@interface StreamingMedia()
- (void)play:(CDVInvokedUrlCommand *) command type:(NSString *) type;
@end

@implementation StreamingMedia{ }

NSString * const TYPE_VIDEO = @"VIDEO";
NSString * const TYPE_AUDIO = @"AUDIO";
NSString * const DEFAULT_IMAGE_SCALE = @"center";


-(void)play:(CDVInvokedUrlCommand *) command type:(NSString *) type {
    StreamingViewController* streamController=[[StreamingViewController alloc]initWithNibName:@"StreamingView" bundle:nil];
    streamController.url=[command.arguments objectAtIndex:0];

    [self.viewController presentViewController:streamController animated:YES completion:nil];
}


-(void)playVideo:(CDVInvokedUrlCommand *) command {
    [self play:command type:[NSString stringWithString:TYPE_VIDEO]];
}

-(void)playAudio:(CDVInvokedUrlCommand *) command {
    [self play:command type:[NSString stringWithString:TYPE_AUDIO]];
}


@end