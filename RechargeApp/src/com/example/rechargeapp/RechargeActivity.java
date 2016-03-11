package com.example.rechargeapp;

import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;

import com.example.rechargeapp.adapter.TabsPagerAdapter;

public class RechargeActivity extends FragmentActivity implements ActionBar.TabListener, TabListener, OnPageChangeListener  {


	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private android.app.ActionBar actionBar;
	String username;
	// Tab titles
	private final String[] tabs = { "mobile", "dth", "data_card" };

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
			viewPager.setOnPageChangeListener(this);
		}
	}



	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		actionBar.setSelectedNavigationItem(position);
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public void onTabReselected(android.support.v7.app.ActionBar.Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	public void onTabSelected(android.support.v7.app.ActionBar.Tab tab,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());

	}

	public void onTabUnselected(android.support.v7.app.ActionBar.Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}


}
