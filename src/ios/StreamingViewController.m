//
//  StreamingViewController.m
//  EniMote
//
//  Created by Adrián Calviño Amado
//
//

#import "StreamingViewController.h"

@implementation StreamingViewController {
    VLCMediaPlayer *_mediaplayer;
}

- (id)initWithNibName:(NSString*)nibNameOrNil bundle:(NSBundle*)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    return self;
}

- (void)viewDidLoad {
     NSLog(@"viewDidLoad");
    [super viewDidLoad];
    
    /* setup the media player instance, give it a delegate and something to draw into */
    _mediaplayer = [[VLCMediaPlayer alloc] init];
    _mediaplayer.delegate = self;
    _mediaplayer.drawable = _viewStreaming;

    @try{
        _mediaplayer.media = [VLCMedia mediaWithURL:[NSURL URLWithString:_url]];
        [_mediaplayer play];
    }@catch(NSException *e){
        NSLog(@"Error playing stream:%@",e.description);
    }

    // Do any additional setup after loading the view from its nib.
}

- (void)viewDidDisappear:(BOOL)animated {
    [_mediaplayer stop];
    if (_mediaplayer)
        _mediaplayer = nil;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

- (IBAction)goBack:(id)sender {
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:true];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end




