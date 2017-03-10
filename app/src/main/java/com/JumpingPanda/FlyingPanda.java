package com.JumpingPanda;

import org.cocos2d.actions.CCActionManager;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class FlyingPanda extends Activity implements AdListener {
	/** Called when the activity is first created. */
	public static CCGLSurfaceView mGLSurfaceView;
	private boolean isCreated = false; 

	private static final String ADMOB_PUBLISH_ID = "xxxxxxxxxxxxxxx";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if( !isCreated ){
			isCreated = true;
		} else {
			return;
		}

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		RelativeLayout layout = new RelativeLayout(this);
		layout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		mGLSurfaceView = new CCGLSurfaceView(this);
		
		// Create the adView
		AdView adView = new AdView(this, AdSize.BANNER, ADMOB_PUBLISH_ID);
		
		// Initiate a generic request to load it with an ad
		adView.loadAd(new AdRequest());
		adView.setAdListener(this);

		// Add the adView to it
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		adView.setLayoutParams(params);
		
		layout.addView(mGLSurfaceView);
		layout.addView(adView);
		
		setContentView(layout);
		
		Common.game_initialize();
		getScaledCoordinate();
		CCDirector.sharedDirector().attachInView(mGLSurfaceView); 

		// attach the OpenGL view to a window
		Common.sound_engine = SoundEngine.sharedEngine();
		loadSound();

		CCScene scene = CCScene.node();
		scene.addChild(new HelloWorldLayer(), 1);
		CCDirector.sharedDirector().runWithScene(scene);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override    
	public void onPause() {
		super.onPause();

		MediaGlobal._shared().pauseMusic();

		if(GameLayer.sharedGameLayer() != null){
			GameLayer.sharedGameLayer().onPause(null);
		}
		CCDirector.sharedDirector().pause();
	}

	@Override
	public void onResume() {
		super.onResume();

		MediaGlobal._shared().resumeMusic();
	}

	@Override
	public void onDestroy() {
		isCreated = false;

		MediaGlobal._shared().stopMusic();
		Common.sound_engine.realesAllEffects();

		super.onDestroy();
		CCDirector.sharedDirector().end();       

		CCTextureCache.sharedTextureCache().removeAllTextures();
		CCTextureCache.sharedTextureCache().removeAllTextures();
		CCSpriteFrameCache.sharedSpriteFrameCache().removeAllSpriteFrames();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			exitGameDialog();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exitGameDialog() {
		Builder builder = new AlertDialog.Builder(FlyingPanda.this)
				.setIcon(R.drawable.icon)
				.setTitle("Exit the Game?")
				.setMessage("Are you sure?")
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								CCActionManager.sharedManager()
										.removeAllActions();
								CCDirector.sharedDirector().end();
								finish();
							}
						});
		builder.create().show();
	}

	private void loadSound() {
		SoundEngine.purgeSharedEngine();

		Common.sound_engine.preloadEffect(CCDirector.sharedDirector().getActivity().getApplication(), R.raw.bomb);
		Common.sound_engine.preloadEffect(CCDirector.sharedDirector().getActivity().getApplication(), R.raw.bounce);
		Common.sound_engine.preloadEffect(CCDirector.sharedDirector().getActivity().getApplication(), R.raw.death);
		Common.sound_engine.preloadEffect(CCDirector.sharedDirector().getActivity().getApplication(), R.raw.fly);
		Common.sound_engine.preloadEffect(CCDirector.sharedDirector().getActivity().getApplication(), R.raw.gamebg);
		Common.sound_engine.preloadEffect(CCDirector.sharedDirector().getActivity().getApplication(), R.raw.gameover);
		Common.sound_engine.preloadEffect(CCDirector.sharedDirector().getActivity().getApplication(), R.raw.jumppad);
	}

	private void getScaledCoordinate() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		Common.SCREEN_WIDTH = displayMetrics.widthPixels;
		Common.SCREEN_HEIGHT = displayMetrics.heightPixels;
		Common.kXForIPhone = Common.SCREEN_WIDTH / 480.0f;
		Common.kYForIPhone = Common.SCREEN_HEIGHT / 320.0f;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Admob Setting
	////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onDismissScreen(Ad ad) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailedToReceiveAd(Ad ad, ErrorCode errorCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeaveApplication(Ad ad) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPresentScreen(Ad ad) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveAd(Ad ad) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "+++++++++++++ New admob has received! ++++++++++++++");
	}

	
}
