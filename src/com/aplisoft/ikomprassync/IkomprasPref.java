package com.aplisoft.ikomprassync;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class IkomprasPref extends PreferenceFragment {	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.mypreferences);
	}
}
