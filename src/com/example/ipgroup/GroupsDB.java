package com.example.ipgroup;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class GroupsDB extends SQLiteOpenHelper  {
	private static final String DATABASE_NAME = "groups.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "groups";

	public static final String _ID = BaseColumns._ID;
	public static final String NAME = "name";
	public static final String NUMBER = "number";

	public GroupsDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	

	/** Overrides **/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// CREATE TABLE teas (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT
		// NULL, brew_time INTEGER);
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + NUMBER + " TEXT NOT NULL" + ");";
		db.execSQL(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	/**
	 * Insert a new tea record into the database.
	 * 
	 * @param name The name of the tea to add
	 * @param number The time (in minutes) the tea should be brewed.
	 * @throws SQLException
	 */
	public void insert(String name, String number) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(NAME, name);
		values.put(NUMBER, number);

		db.insertOrThrow(TABLE_NAME, null, values);
	}

    public void delete(long itemid){  
    	SQLiteDatabase db = getWritableDatabase();
//        db.delete(TABLE_NAME, "_id="+itemid, null);
    	String [] whereArgs = {String.valueOf(itemid)};
        db.delete(TABLE_NAME, "_id=?", whereArgs);
    }
    public void delete_by_name(String name_value){  
    	SQLiteDatabase db = getWritableDatabase();
//        db.delete(TABLE_NAME, "_id="+itemid, null);
    	String [] whereArgs = {name_value};
        db.delete(TABLE_NAME, "name=?", whereArgs);
    }
    
    public void update(String name, String number,long itemid){ 
    	SQLiteDatabase db = getWritableDatabase();
    	ContentValues values = new ContentValues();
		values.put(NAME, name);
		values.put(NUMBER, number);
//        db.update(TABLE_NAME, values, "_id="+itemid, null);
		String [] whereArgs = {String.valueOf(itemid)};
        db.update(TABLE_NAME, values, "_id=?", whereArgs);
        
    }
    
    public long check_id(String name_value){ 
    	SQLiteDatabase db = getReadableDatabase();
    	long group_id = 0;
    	Cursor cursor = db.query(TABLE_NAME, new String[]{"_id"}, "name=?", new String[]{name_value}, null, null, null);  
    	while(cursor.moveToNext()){  
            group_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
    	}
    	return group_id;
    }
    
    public void ask(GroupData currentgroup_value,long itemid){ 
    	SQLiteDatabase db = getReadableDatabase();
    	Cursor cursor = db.query(TABLE_NAME, new String[]{NAME,NUMBER}, "_id=?", new String[]{String.valueOf(itemid)}, null, null, null);  
    	while(cursor.moveToNext()){  
            String askname = cursor.getString(cursor.getColumnIndex(NAME));  
            String asknumber = cursor.getString(cursor.getColumnIndex(NUMBER));
        	currentgroup_value.set_group_name(askname);
        	currentgroup_value.set_group_number(asknumber);
    	}
    }
	/**
	 * Return a count of the number of teas in the database
	 * 
	 * @return long
	 */
	public long count() {
		SQLiteDatabase db = getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
	}

	/**
	 * Return all tea records in the database, ordered by name.
	 * 
	 * @param activity The Activity that will manage the cursor.
	 * @return {@link Cursor}
	 */
	public Cursor all(Activity activity) {
		String[] from = { _ID, NAME, NUMBER };
		String order = NAME;

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, from, null, null, null, null, order);
		activity.startManagingCursor(cursor);

		return cursor;
	}
}
