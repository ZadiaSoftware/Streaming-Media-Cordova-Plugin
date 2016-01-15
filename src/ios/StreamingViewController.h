//
//  StreamingViewController.h
//  EniMote
//
//  Created by Adrián Calviño Amado
//
//

#import <UIKit/UIKit.h>
#import <MobileVLCKit/MobileVLCKit.h>
#import <Cordova/CDV.h>

@interface StreamingViewController: UIViewController  <VLCMediaPlayerDelegate>

- (IBAction)goBack:(id)sender;
@property (strong, nonatomic) IBOutlet UIButton *backButton;
@property (strong, nonatomic) IBOutlet UIView *viewStreaming;
@property (strong, nonatomic) NSString* url;
@property (strong, nonatomic) NSString* callbackId;
@property (strong, nonatomic) CDVInvokedUrlCommand* command;
@property (strong, nonatomic) id commandDelegate;

@end
