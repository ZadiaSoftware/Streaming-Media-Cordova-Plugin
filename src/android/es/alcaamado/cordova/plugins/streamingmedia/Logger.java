package es.alcaamado.cordova.plugins.streamingmedia;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

/**
 * Created by istar on 26/09/15.
 */
public class Logger {
    private CordovaInterface cordova;
    private CordovaWebView mWebView;

    public Logger(CordovaInterface cordovaInterface, CordovaWebView webView) {
        this.cordova = cordovaInterface;
        this.mWebView = webView;
    }

    public void log(final String msg) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:console.log('" + msg +"');");
            }
        });
    }
}
