package es.alcaamado.cordova.plugins.streamingmedia;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.Toast;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SimpleVideoStream extends Activity implements IVLCVout.Callback, LibVLC.HardwareAccelerationError {

	private static String TAG = "SimpleVideoStream";
	private SurfaceView mSurface;
	private SurfaceHolder holder;
	private ProgressBar mProgressBar = null;
	private String mVideoUrl;
	private Boolean mShouldAutoClose = true;

	private LibVLC libvlc;
	private MediaPlayer mMediaPlayer = null;
	private int mVideoWidth;
	private int mVideoHeight;
	private final static int VideoSizeChanged = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		StreamingMedia.logger.log("Loading SimpleVideoStream");

		Bundle b = getIntent().getExtras();
		mVideoUrl = b.getString("mediaUrl");
		mShouldAutoClose = b.getBoolean("shouldAutoClose");
		mShouldAutoClose = mShouldAutoClose == null ? true : mShouldAutoClose;

		RelativeLayout relLayout = new RelativeLayout(this);
		relLayout.setBackgroundColor(Color.BLACK);
		RelativeLayout.LayoutParams relLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		relLayoutParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

		mSurface = new SurfaceView(this);
		mSurface.setLayoutParams(relLayoutParam);

		holder = mSurface.getHolder();

		relLayout.addView(mSurface);

		// Create progress throbber
		mProgressBar = new ProgressBar(this);
		mProgressBar.setIndeterminate(true);
		// Center the progress bar
		RelativeLayout.LayoutParams pblp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		pblp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		mProgressBar.setLayoutParams(pblp);
		// Add progress throbber to view
		relLayout.addView(mProgressBar);
		mProgressBar.bringToFront();

		setContentView(relLayout, relLayoutParam);
	}

	@Override
	protected void onResume() {
		super.onResume();
		createPlayer(mVideoUrl);
	}

	@Override
	protected void onPause() {
		super.onPause();
		releasePlayer();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stop();
		releasePlayer();
	}

	private void createPlayer(String media) {
        mProgressBar.setVisibility(View.VISIBLE);
		releasePlayer();
		try {
			// Create LibVLC
			ArrayList<String> options = new ArrayList<String>();
			options.add("--aout=opensles");
			options.add("--audio-time-stretch"); // time stretching
			options.add("-vvv"); // verbosity
			libvlc = new LibVLC(options);
			libvlc.setOnHardwareAccelerationError(this);
			holder.setKeepScreenOn(true);

			// Create media player
			mMediaPlayer = new MediaPlayer(libvlc);
			mMediaPlayer.setEventListener(mPlayerListener);

			// Set up video output
			final IVLCVout vout = mMediaPlayer.getVLCVout();
			vout.setVideoView(mSurface);
			vout.addCallback(this);
			vout.attachViews();

			Media m = new Media(libvlc, Uri.parse(media));
			mMediaPlayer.setMedia(m);

            mProgressBar.setVisibility(View.GONE);

			mMediaPlayer.play();
			Log.d(TAG, "Starting video");
		} catch (Exception e) {
            wrapItUp(RESULT_CANCELED, "Error creating player");
		}
	}

	private void releasePlayer() {
		if (libvlc == null) return;
		mMediaPlayer.stop();
		final IVLCVout vout = mMediaPlayer.getVLCVout();
		vout.removeCallback(this);
		vout.detachViews();
		holder = null;
		libvlc.release();
		libvlc = null;

		mVideoWidth = 0;
		mVideoHeight = 0;
	}

	private void pause() {
		Log.d(TAG, "Pausing video.");
		mMediaPlayer.pause();
	}

	private void stop() {
		Log.d(TAG, "Stopping video.");
		mMediaPlayer.stop();
	}

	private void wrapItUp(int resultCode, String message) {
		Intent intent = new Intent();
		intent.putExtra("message", message);
		setResult(resultCode, intent);
		finish();
	}

    private void setSize(int width, int height) {
		mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1) return;

        if(holder == null || mSurface == null) return;

        // get screen size
        int w = getWindow().getDecorView().getWidth();
        int h = getWindow().getDecorView().getHeight();

        // force surface buffer size
        holder.setFixedSize(w, h);

        // set display size
        ViewGroup.LayoutParams lp = mSurface.getLayoutParams();
        lp.width = w;
        lp.height = h;
        mSurface.setLayoutParams(lp);
        mSurface.invalidate();
    }

	@Override
	public void onBackPressed() {
		wrapItUp(RESULT_OK, null);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// The screen size changed or the orientation changed... don't restart the activity
		super.onConfigurationChanged(newConfig);
	}

	private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);

	@Override
	public void onNewLayout(IVLCVout vout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
		if (width * height == 0) return;

		// store video size
		mVideoWidth = width;
		mVideoHeight = height;
		setSize(mVideoWidth, mVideoHeight);
	}

	@Override
	public void onSurfacesCreated(IVLCVout vout) { }

	@Override
	public void onSurfacesDestroyed(IVLCVout vout) { }

	private class MyPlayerListener implements MediaPlayer.EventListener {
		private WeakReference<SimpleVideoStream> mOwner;

		public MyPlayerListener(SimpleVideoStream owner) {
			mOwner = new WeakReference<SimpleVideoStream>(owner);
		}

		@Override
		public void onEvent(MediaPlayer.Event event) {
            SimpleVideoStream player = mOwner.get();

			switch(event.type) {
				case MediaPlayer.Event.EndReached:
					Log.d(TAG, "MediaPlayer EndReached");
                    if (mShouldAutoClose) {
                        wrapItUp(RESULT_OK, "End Reached");
                    }
					break;
				case MediaPlayer.Event.Playing:
				case MediaPlayer.Event.Paused:
				case MediaPlayer.Event.Stopped:
				default:
					break;
			}
		}
	}

	@Override
	public void eventHardwareAccelerationError() {
		// Handle errors with hardware acceleration
		Log.e(TAG, "Error with hardware acceleration");
        wrapItUp(RESULT_CANCELED, "Error with hardware acceleration");
	}
}
