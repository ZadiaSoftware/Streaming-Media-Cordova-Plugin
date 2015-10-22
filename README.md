# Cordova Streaming Media plugin 

for iOS and Android, by [Adrián Calviño Amado](https://github.com/alcaamado)

1. [Description](https://github.com/alcaamado/Streaming-Media-Cordova-Plugin#1-description)
2. [Usage](https://github.com/alcaamado/Streaming-Media-Cordova-Plugin#3-usage)

## 1. Description

This plugin allows you to stream audio and video in a fullscreen, VLC player on iOS and Android.

* Works with PhoneGap >= 3.0.

### iOS specifics
* Uses libVLC to play the videos
* Tested on iOS 7.

### Android specifics
* Uses libVLC to play the videos
* Creates two activities in your AndroidManifest.xml file.
* Tested on Android 4.0+.

## 2. Usage

```javascript
  var videoUrl = STREAMING_VIDEO_URL;

  // Just play a video
  window.plugins.streamingMedia.playVideo(videoUrl);


  var audioUrl = STREAMING_AUDIO_URL;
  
  // Play an audio file (not recommended, since the screen will be plain black)
  window.plugins.streamingMedia.playAudio(audioUrl);

```
