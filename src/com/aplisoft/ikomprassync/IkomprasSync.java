package com.aplisoft.ikomprassync;

import android.net.Uri;
import android.provider.BaseColumns;

public class IkomprasSync {
	
    public static final String AUTHORITY = "com.aplisoft.provider.ikomprassync";
    private IkomprasSync(){
    }
    public static final class Cities implements BaseColumns{
    	private Cities(){}
    	public static final String TABLE_NAME = "stocity";
    	private static final String SCHEME = "content://";
    	private static final String PATH_CITIES = "/cities";
    	private static final String PATH_CITY_ID ="/cities/";
    	public static final int CITY_ID_PATH_POSITION = 1;
    	public static final Uri CONTENT_URI = Uri.parse(SCHEME+AUTHORITY+PATH_CITIES);
    	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME+AUTHORITY+PATH_CITY_ID);
    	public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME+AUTHORITY+PATH_CITY_ID+"/#");
    	
    	/*
    	 * Columns Definitions
    	 */
    	 
    	  public static final String COLUMN_NAME_NAME = "name";
    	  
    	
    }

}
