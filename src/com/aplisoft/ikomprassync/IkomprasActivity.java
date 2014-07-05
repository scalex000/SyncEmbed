package com.aplisoft.ikomprassync;

import java.util.Properties;

import org.jumpmind.symmetric.android.SQLiteOpenHelperRegistry;
import org.jumpmind.symmetric.android.SymmetricService;
import org.jumpmind.symmetric.common.ParameterConstants;

import com.aplisoft.ikomprassyncdb.IkomprasSyncDB;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class IkomprasActivity extends Activity implements OnClickListener{

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainLayout();
		
		View button_start = findViewById(R.id.button1);
		button_start.setOnClickListener(this);
		
		View button_stop = findViewById(R.id.button2);
		button_stop.setOnClickListener(this);
		
		View button_setup = findViewById(R.id.button3);
		button_setup.setOnClickListener(this);
		
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.start:
			
				myStart();
			return true;
		case R.id.stop:
				myStop();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void SetSymmetricDS() {
		SharedPreferences mysettings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String rReg = "";
		String rSync = "";
		String rNode = "";
		String rExternal = "";
		Boolean rBackground = true;

		if (mysettings.contains("urlregister")) {
			rReg = mysettings.getString("urlregister", "");
		}
		if (mysettings.contains("urlsync")) {
			rSync = mysettings.getString("urlsync", "");
		}
		if (mysettings.contains("nodegroupid")) {
			rNode = mysettings.getString("nodegroupid", "");
		}
		if (mysettings.contains("externalid")) {
			rExternal = mysettings.getString("externalid", "");
		}
		if (mysettings.contains("httpbackground")) {
			rBackground = mysettings.getBoolean("httpbackground", true);
		}

		final String HELPER_KEY = "IkomprasSyncHelperKey";
		IkomprasSyncDB mIkomprasSyncDB = new IkomprasSyncDB(
				getApplicationContext());
		SQLiteOpenHelperRegistry
				.register(HELPER_KEY, mIkomprasSyncDB.mDBHelper);
		intent = new Intent(getApplicationContext(), SymmetricService.class);

		intent.putExtra(
				SymmetricService.INTENTKEY_SQLITEOPENHELPER_REGISTRY_KEY,
				HELPER_KEY);
		intent.putExtra(SymmetricService.INTENTKEY_REGISTRATION_URL, rReg);
		intent.putExtra(SymmetricService.INTENTKEY_EXTERNAL_ID, rExternal);
		intent.putExtra(SymmetricService.INTENTKEY_NODE_GROUP_ID, rNode);
		intent.putExtra(SymmetricService.INTENTKEY_START_IN_BACKGROUND,
				rBackground);

		Properties properties = new Properties();
		properties.setProperty(ParameterConstants.AUTO_RELOAD_REVERSE_ENABLED,
				"true");
		properties.setProperty(ParameterConstants.SYNC_URL, rSync);
		intent.putExtra(SymmetricService.INTENTKEY_PROPERTIES, properties);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.button1:
			myStart();
			break;
		case R.id.button2:
			myStop();
			break;
		case R.id.button3:
			findViewById(R.id.mainbuttons).setVisibility(View.GONE);
			getFragmentManager().beginTransaction()
			.replace(R.id.fragment_container, new IkomprasPref()).commit();
			break;
		case R.id.button4:
			mainLayout();
		}
		
	}
	
	private void myStart(){
		if (isNetworkAvailable()) {
			SetSymmetricDS();
			getApplicationContext().startService(intent);
		}else{
		}
			
	}

	private void myStop(){
		getApplicationContext().stopService(intent);
	}
	
	private void mainLayout(){
		RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.fragment_container);
		View view = getLayoutInflater().inflate(R.layout.main_buttons, mainLayout,false);
		mainLayout.addView(view);
	}
}