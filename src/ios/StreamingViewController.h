//
//  StreamingViewController.h
//  EniMote
//
//  Created by Adrián Calviño Amado
//
//

#import <UIKit/UIKit.h>
#import <MobileVLCKit/MobileVLCKit.h>

@interface StreamingViewController: UIViewController  <VLCMediaPlayerDelegate>

- (IBAction)goBack:(id)sender;
@property (strong, nonatomic) IBOutlet UIButton *backButton;
@property (strong, nonatomic) IBOutlet UIView *viewStreaming;
@property (strong, nonatomic)   NSString * url;

@end
