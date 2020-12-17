package com.godimago.dima;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new DimaAdventure(), config);
	}

	public void privacyPolicy(View view) {
		Intent intent = new Intent(this, Privacy.class);
		intent.putExtra("privpo", "privpol");
		startActivity(intent);
	}
}
