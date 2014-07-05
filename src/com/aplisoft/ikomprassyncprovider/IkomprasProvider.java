package com.aplisoft.ikomprassyncprovider;

import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import com.aplisoft.ikomprassync.IkomprasSync;
import com.aplisoft.ikomprassync.IkomprasSync.Cities;
import com.aplisoft.ikomprassyncdb.IkomprasSyncDB;

public class IkomprasProvider extends ContentProvider {

	
	private static final String TAG = "IkomprasProvider";

	private static HashMap<String, String> sIkomprasProjectionMap;

	private static final String[] READ_CITY_PROJECTION = new String[] {
			IkomprasSync.Cities._ID, IkomprasSync.Cities.COLUMN_NAME_NAME,

	};

	private static final int READ_CITY_ID_INDEX = 1;
	private static final int READ_CITY_NAME_INDEX = 2;

	private static final int CITY = 1;
	private static final int CITY_ID = 2;

	private static final UriMatcher sUriMatcher;

	private IkomprasSyncDB mIkomprasSyncDB;
	private Intent intent;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(IkomprasSync.AUTHORITY, "cities", CITY);
		sUriMatcher.addURI(IkomprasSync.AUTHORITY, "cities/#", CITY_ID);

		sIkomprasProjectionMap = new HashMap<String, String>();
		sIkomprasProjectionMap.put(Cities._ID, Cities._ID);
		sIkomprasProjectionMap.put(Cities.COLUMN_NAME_NAME,
				Cities.COLUMN_NAME_NAME);

	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		mIkomprasSyncDB.openWrite();
		String FinalWhere;
		int count;

		switch (sUriMatcher.match(uri)) {
		case CITY:
			count = mIkomprasSyncDB.mDB.delete(IkomprasSync.Cities.TABLE_NAME,
					where, whereArgs);
			break;
		case CITY_ID:
			FinalWhere = IkomprasSync.Cities._ID
					+ "="
					+ uri.getPathSegments().get(
							IkomprasSync.Cities.CITY_ID_PATH_POSITION);
			if (where != null) {
				FinalWhere = FinalWhere + " AND " + where;
			}
			count = mIkomprasSyncDB.mDB.delete(IkomprasSync.Cities.TABLE_NAME,
					FinalWhere, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {

		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sUriMatcher.match(uri) != CITY) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		ContentValues values;

		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		if (values.containsKey(IkomprasSync.Cities.COLUMN_NAME_NAME) == false) {
			Resources r = Resources.getSystem();
			values.put(IkomprasSync.Cities.COLUMN_NAME_NAME,
					r.getString(android.R.string.untitled));
		}

		mIkomprasSyncDB.openWrite();
		long RowId = mIkomprasSyncDB.mDB.insert(Cities.TABLE_NAME,
				Cities.COLUMN_NAME_NAME, values);

		if (RowId > 0) {
			Uri cityUri = ContentUris.withAppendedId(
					IkomprasSync.Cities.CONTENT_ID_URI_BASE, RowId);

			getContext().getContentResolver().notifyChange(cityUri, null);
			return cityUri;

		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		Log.i(TAG, "Creating Provider");
		mIkomprasSyncDB = new IkomprasSyncDB(getContext());
		return (mIkomprasSyncDB.mDBHelper == null)? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(Cities.TABLE_NAME);

		switch (sUriMatcher.match(uri)) {
		case CITY:
			qb.setProjectionMap(sIkomprasProjectionMap);
			break;
		case CITY_ID:
			qb.setProjectionMap(sIkomprasProjectionMap);
			qb.appendWhere(Cities._ID + "="
					+ uri.getPathSegments().get(Cities.CITY_ID_PATH_POSITION));
			break;
		default:
			// If the URI doesn't match any of the known patterns, throw an
			// exception.
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		mIkomprasSyncDB.openRead();

		Cursor c = qb.query(mIkomprasSyncDB.mDB, projection, selection,
				selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		mIkomprasSyncDB.openWrite();
		int count;
		String FinalWhere;
		switch (sUriMatcher.match(uri)) {
		case CITY:
			count = mIkomprasSyncDB.mDB.update(IkomprasSync.Cities.TABLE_NAME,
					values, where, whereArgs);
			break;
		case CITY_ID:
			FinalWhere = IkomprasSync.Cities._ID
					+ "="
					+ uri.getPathSegments().get(
							IkomprasSync.Cities.CITY_ID_PATH_POSITION);
			if (where != null) {
				FinalWhere = FinalWhere + " AND " + where;
			}
			count = mIkomprasSyncDB.mDB.update(IkomprasSync.Cities.TABLE_NAME,
					values, FinalWhere, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}
