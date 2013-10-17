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

public class MembersDB extends SQLiteOpenHelper  {
	private static final String DATABASE_NAME = "members.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "members";

	public static final String _ID = BaseColumns._ID;
	public static final String NAME = "name";
	public static final String NUMBER = "number";
	public static final String GROUP_TAG = "tag"; 

	public MembersDB(Context context) {
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
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + NUMBER + " TEXT NOT NULL, " + GROUP_TAG + " TEXT "+");";
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
	public void insert(String name, String number, String group_tag) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(NAME, name);
		values.put(NUMBER, number);
		values.put(GROUP_TAG, group_tag);
		String test_str = values.getAsString(NUMBER);
		System.out.println(values.getAsString(NUMBER));
		db.insertOrThrow(TABLE_NAME, null, values);
	}

    public void delete(long itemid){  
    	SQLiteDatabase db = getWritableDatabase();
//        db.delete(TABLE_NAME, "_id="+itemid, null);
    	String [] whereArgs = {String.valueOf(itemid)};
        db.delete(TABLE_NAME, "_id=?", whereArgs);
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
    
    public String check_number(String name_value){ 
    	SQLiteDatabase db = getReadableDatabase();
    	String check_number_str = "";
    	Cursor cursor = db.query(TABLE_NAME, new String[]{"number"}, "name=?", new String[]{name_value}, null, null, null);  
    	while(cursor.moveToNext()){  
            check_number_str = cursor.getString(cursor.getColumnIndex("number"));
    	}
    	return check_number_str;
    }
    
    public void update(String name, String number, String group_tag, long itemid){ 
    	SQLiteDatabase db = getWritableDatabase();
    	ContentValues values = new ContentValues();
		values.put(NAME, name);
		values.put(NUMBER, number);
		values.put(GROUP_TAG, group_tag);
//        db.update(TABLE_NAME, values, "_id="+itemid, null);
		String [] whereArgs = {String.valueOf(itemid)};
        db.update(TABLE_NAME, values, "_id=?", whereArgs);
    }
    
    public void ask(MemberData currentmember_value,long itemid){ 
    	SQLiteDatabase db = getReadableDatabase();
    	Cursor cursor = db.query(TABLE_NAME, new String[]{NAME,NUMBER,GROUP_TAG}, "_id=?", new String[]{String.valueOf(itemid)}, null, null, null);  
    	while(cursor.moveToNext()){  
            String askname = cursor.getString(cursor.getColumnIndex(NAME));  
            String asknumber = cursor.getString(cursor.getColumnIndex(NUMBER));
            String askgrouptag = cursor.getString(cursor.getColumnIndex(GROUP_TAG));
        	currentmember_value.set_member_name(askname);
        	currentmember_value.set_member_number(asknumber);
        	currentmember_value.set_member_grouptag(askgrouptag);
    	}
           
//    	currenttea_value.setTeaname(askname);
//    	currenttea_value.setBrewminutes(askminutes);
    }
    
    public MemberData [] checktagsout(String tag_value){ 
    	int i = 0;
    	MemberData [] member_in_group = new MemberData[10];
    	SQLiteDatabase db = getReadableDatabase();
    	Cursor cursor = db.query(TABLE_NAME, new String[]{NAME,NUMBER,GROUP_TAG}, "tag=?", new String[]{tag_value}, null, null, null, null);  
    	while(cursor.moveToNext()){  
            String askname = cursor.getString(cursor.getColumnIndex(NAME));  
            String asknumber = cursor.getString(cursor.getColumnIndex(NUMBER));
            String askgrouptag = cursor.getString(cursor.getColumnIndex(GROUP_TAG));
            
            member_in_group[i] = new MemberData();
            
            member_in_group[i].set_member_name(askname);
            member_in_group[i].set_member_number(asknumber);
            member_in_group[i].set_member_grouptag(askgrouptag);
//        	System.out.println("query------->" + String.valueOf(i)+"   "+
//                                                 "姓名："+member_in_group[i].get_member_name()+" "+
//        										 "号码："+member_in_group[i].get_member_number()+" "+
//        			                             "标签："+member_in_group[i].get_member_grouptag());
        	i++;
    	}
    	return member_in_group;

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
		String[] from = { _ID, NAME, NUMBER, GROUP_TAG };
		String order = NAME;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, from, null, null, null, null, order, null);
		activity.startManagingCursor(cursor);

		return cursor;
	}
}

