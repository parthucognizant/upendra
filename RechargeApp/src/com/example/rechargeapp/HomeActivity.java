package com.example.rechargeapp;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class HomeActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	public void registerClick(View v){
		//Log.i(TAG,constants.logs.signupClick);
		Intent signupintent = new Intent(this, RegisterNumberActivity.class);
		startActivity(signupintent);
		//Toast.makeText(this, "Signup clicked", Toast.LENGTH_SHORT).show();
	}

	public void rechargeClick(View v){
		//Log.i(TAG,constants.logs.loginClick);
		Intent rechargeintent = new Intent(this, RechargeActivity.class);
		startActivity(rechargeintent);
		//Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show();
	}
}
