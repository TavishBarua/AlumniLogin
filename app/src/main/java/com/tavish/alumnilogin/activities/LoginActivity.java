package com.tavish.alumnilogin.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.tavish.alumnilogin.R;
import com.tavish.alumnilogin.listeners.SocialConnectListener;
import com.tavish.alumnilogin.model.UserLoginDetails;
import com.tavish.alumnilogin.util.PreferenceManager;
import com.tavish.alumnilogin.util.Utility;
import com.tavish.alumnilogin.util.helpers.FacebookLoginHelper;
import com.tavish.alumnilogin.util.helpers.GooglePlusLoginHelper;
import com.tavish.alumnilogin.util.helpers.TwitterLoginHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity implements SocialConnectListener, View.OnClickListener {


	private static String TAG = "com.tavish.alumnilogin";

	@Bind(R.id.btn_goo)
	ImageButton btn_google;


	private static final int GPLUS_SIGN_IN = 1001;

	private GooglePlusLoginHelper gplusHelper;



	@Bind(R.id.btn_fb)
	ImageButton btn_facebook;




	// Facebook Login
	private FacebookLoginHelper fbHelper;

	private static final int FB_SIGN_IN = 1002;



	@Bind(R.id.btn_twitter)
	ImageButton btn_twitter;


	// Facebook Login
	private TwitterLoginHelper twitterHelper;

	private static final int TWITTER_SIGN_IN = 140;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);

		//getHashKey();




		initalizeView();
	}

	private void initalizeView()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {

				gplusHelper = new GooglePlusLoginHelper();
				gplusHelper.setUserCallbackListener(LoginActivity.this);


				fbHelper = new FacebookLoginHelper(LoginActivity.this);
				Log.d(TAG,"Helper Started");
				fbHelper.setUserCallbackListener(LoginActivity.this);
				Log.d(TAG,"Listener Started");


				twitterHelper = new TwitterLoginHelper();
				twitterHelper.setUserCallbackListener(LoginActivity.this);


				btn_google.setOnClickListener(LoginActivity.this);
				btn_facebook.setOnClickListener(LoginActivity.this);
				btn_twitter.setOnClickListener(LoginActivity.this);

			}
		}).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == GPLUS_SIGN_IN) {


			gplusHelper.onActivityResult(resultCode, data);
		}else if(requestCode == TWITTER_SIGN_IN)
		{
			twitterHelper.onActivityResult(requestCode,resultCode,data);
		}
		else {
			fbHelper.getCallbackManager().onActivityResult(requestCode, resultCode, data);
		}

	}

	@Override
	public void onUserConnected(int requestIdentifier,UserLoginDetails userData) {
		Log.d(TAG,"userConnected");
		if (requestIdentifier == GPLUS_SIGN_IN) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					PreferenceManager.preferencePutInteger(getResources().getString(R.string.pref_login_identifier),
							GPLUS_SIGN_IN);
				}
			});
		} else if (requestIdentifier == FB_SIGN_IN) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					PreferenceManager.preferencePutInteger(getResources().getString(R.string.pref_login_identifier),
							FB_SIGN_IN);
				}
			});
		}else if (requestIdentifier == TWITTER_SIGN_IN) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					PreferenceManager.preferencePutInteger(getResources().getString(R.string.pref_login_identifier),
							TWITTER_SIGN_IN);
				}
			});
		}

		Utility.showToast(LoginActivity.this, "connected");
		PreferenceManager.preferencePutBoolean(getResources().getString(R.string.pref_is_loggedin),true);

		Intent MainScreen = new Intent(LoginActivity.this,MainActivity.class);
		Log.d(TAG,"Activity Changed");

		Bundle b = new Bundle();
		b.putSerializable(getResources().getString(R.string.key_userModel),userData);

		MainScreen.putExtra(getResources().getString(R.string.key_userBundle), b);

		startActivity(MainScreen);
		overridePendingTransition(R.anim.right_slide_in, 0);
	}

	@Override
	public void onConnectionError(int requestIdentifier,String message) {
		Utility.showToast(LoginActivity.this, message);

	}


	@Override
	public void onCancelled(int requestIdentifier,String message) {
		Utility.showToast(LoginActivity.this, message);
	}



	@Override
	public void onClick(View view) {

		switch (view.getId())
		{

			case R.id.btn_fb:

				Log.d("onclick", "fbclicked");
				if (Utility.isConnectivityAvailable(LoginActivity.this)) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

						}
					});

					ArrayList<String> permissions = new ArrayList<>();
					permissions.add("public_profile");
					permissions.add("email");
					permissions.add("user_location");

					fbHelper.signIn(FB_SIGN_IN, permissions);

				} else {
					Utility.showToast(LoginActivity.this,"No internet connection available...");
				}

			break;

			case R.id.btn_goo:

				Log.d("onclick", "clicked");
				if (Utility.isConnectivityAvailable(LoginActivity.this)) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

						}
					});

					gplusHelper.createConnection(LoginActivity.this);
					gplusHelper.signIn(GPLUS_SIGN_IN);

				} else {
					Utility.showToast(LoginActivity.this,"No internet connection available...");
				}

			break;

			case R.id.btn_twitter:


				Log.d("onclick", "clicked");
				if (Utility.isConnectivityAvailable(LoginActivity.this)) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

						}
					});

					twitterHelper.createConnection(LoginActivity.this);
					twitterHelper.signIn(TWITTER_SIGN_IN);

				} else {
					Utility.showToast(LoginActivity.this,"No internet connection available...");
				}

			break;
		}

	}
}
