package com.bearpluspie.letsdoit;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DataHelper {
	private static final String DATABASE_NAME = "drmurphy.db";
	private static final int DATABASE_VERSION = 1;
	private Context context;
	private SQLiteDatabase db;
	static DataHelper sInstance;
	
	 //declare your tables

	//Patients Table
	static String createHikesTable = "create table hikes ("+
			"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"name_of_hike TEXT,"+
			"avg_time TEXT,"+
			"GPS TEXT,"+
			"difficulty TEXT)";
	
	//Appointments Table
	static String createAppointmentsTable = "create table appointments ("+
			"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"hike_name TEXT,"+
			"date TEXT" +
			"start_stop TEXT," +
			"hikers TEXT)";
	
	//declare your columns

	
	//create your dbfunctions 
	
	public DataHelper(Context context) {
		this.context = context;
		openDatabase();
	}

	public static DataHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DataHelper(context);
		}
		sInstance.openDatabase();
		return sInstance;
	}

	private void openDatabase() {
		OpenHelper openHelper = new OpenHelper(this.context);
		db = openHelper.getWritableDatabase();
	}
	
	private static class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//Create your tables
			//Create patients table
			db.execSQL(createHikesTable);
			//Create Appointments table
			db.execSQL(createAppointmentsTable);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + "hikes");
			db.execSQL("DROP TABLE IF EXISTS " + "appointments");
			onCreate(db);
		}
	}

	public Cursor getHikes() {
		Cursor cursor = this.db.query("hikes", new String[] {"_id","name_of_hike","avg_time","GPS",
				"difficulty"}, null, null, null, null, null);
		return cursor;
	}
	
	public Cursor getAppointments(){
		Cursor cursor = this.db.query("hikes", new String[] {"_id", "hike_name","date","start_stop",
				"hikers"}, null, null, null, null, null);
		return cursor;
	}

	
//	public void insertPatient(Patient newPatient){
//		
//		ContentValues cv = new ContentValues();
//		cv.put("name", newPatient.getName());
//		cv.put("phone", newPatient.getPhone());
//		cv.put("email", newPatient.getEmail());
//		db.insert("patients", null, cv);
//		
//	}
//	
//
//	public void insertAppointment(Appointment newAppointment) {
//		
//		ContentValues cv = new ContentValues();
//		cv.put("patientName", newAppointment.getPatientName());
//		cv.put("date", newAppointment.getDate());
//		cv.put("time", newAppointment.getTime());
//		db.insert("appointments", null, cv);
//		
//	}
	
	public List<String> selectAllHikes() {
		List<String> list = new ArrayList<String>();
		Cursor cursor = this.db.query("hikes", new String[] {"_id","name_of_hike","avg_time",
		"difficulty"}, null, null, null, null, null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				do {
					list.add(cursor.getString(1));		//1 is the index for name_of_hike
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed())
				cursor.close();			
		}
		return list;
	}

	public void deleteHike(int id_key) {	//This is the id_key for the name_of_hike. Use getID(String name_of_hike) to get
		String id = ""+id_key;
		String prevName = "undefined";
		
		Cursor cursor_patientDB = db.query("hikes", null, "_id =?", new String[]{id}, null, null, null, null);
		if (cursor_patientDB != null){
			cursor_patientDB.moveToFirst();
			prevName = cursor_patientDB.getString(1);			//get prevname to find what to delete in appointments
		}
		
		db.delete("patients", "_id=?", new String[] { id });
		
		Cursor cursor_apptsDB = db.query("appointments", null, "patientName =?", new String[]{prevName.toString()}, null, null, null, null);
		if (cursor_apptsDB != null){
			db.delete("appointments", "patientName=?", new String[]{prevName});
		}
	}

	public String get_avg_time(String name_of_hike) {
		String avg_time = "undefined";
		Cursor cursor = db.query("patients", null, "name =?", new String[]{name_of_hike.toString()}, null, null, null, null);
		if (cursor != null){
			cursor.moveToFirst();
			avg_time = cursor.getString(2);
		}
		return avg_time;
	}
	
	public String get_difficulty(String name_of_hike){
		String difficulty = "undefined";
		Cursor cursor = db.query("hikes", null, "name =?", new String[]{name_of_hike.toString()}, null, null, null, null);
		if (cursor != null){
			cursor.moveToFirst();
			difficulty = cursor.getString(3);
		}
		return difficulty;
	}
	
	public String get_GPS(String name_of_hike){
		String GPS = "undefined";
		Cursor cursor = db.query("hikes", null, "name =?", new String[]{name_of_hike.toString()}, null, null, null, null);
		if (cursor != null){
			cursor.moveToFirst();
			GPS = cursor.getString(4);
		}
		return GPS;
	}
	
	public int getID(String name_of_hike) {
		int id_key=-99;	//If you get this, something went wrong
		Cursor cursor = db.query("hikes", null, "name =?", new String[]{name_of_hike.toString()}, null, null, null, null);
		if (cursor != null){
			cursor.moveToFirst();
			id_key = cursor.getInt(0);
		}
		return id_key;
	}

	
	public void updateHikeName(int id_key, String name_of_hike) {
		String id = "" +id_key;
		String prevName = "undefined";
		
		//This bit is used to get the name at the id in table hikes in order to find it in table appointments
		Cursor cursor_hikes_table = db.query("hikes", null, "_id =?", new String[]{id}, null, null, null, null);
		if (cursor_hikes_table != null){
			cursor_hikes_table.moveToFirst();
			prevName = cursor_hikes_table.getString(1);
		}
		
		//This updates the name in table hikes using the _id
		ContentValues values_hikes_table = new ContentValues();
		values_hikes_table.put("name", name_of_hike);
		db.update("patients", values_hikes_table, "_id=?", new String[]{id});
		
		//This updates the hike_name in table appointments
		Cursor cursor_appts_table = db.query("appointments", null, "patientName =?", new String[]{prevName.toString()}, null, null, null, null);
		if (cursor_appts_table != null){
			ContentValues values_apptsDB = new ContentValues();
			values_apptsDB.put("hike_name", name_of_hike);
			db.update("appointments", values_apptsDB, "patientName=?", new String[]{prevName});
		}
		
	}

	public void update_avg_time(int id_key, String avg_time) {
		String id = "" +id_key;
		ContentValues values = new ContentValues();
		values.put("avg_time", avg_time);
		db.update("hikes", values, "_id=?", new String[]{id});
	}

	public void updateDifficulty(int id_key, String difficulty) {
		String id = "" +id_key;
		ContentValues values = new ContentValues();
		values.put("difficulty", difficulty);
		db.update("difficulty", values, "_id=?", new String[]{id});
	}
	
	public void updateGPS(int id_key, String GPS){
		String id = "" +id_key;
		ContentValues values = new ContentValues();
		values.put("GPS", GPS);
		db.update(GPS, values, "_id=?", new String[]{id});
	}
	
	public void add_appt_hikers(String hike_name, String date, String start_stop, String newUser){
		ContentValues values = new ContentValues();
		String users = "undefined";
		
		Cursor cursor = db.query("hikes", null,
				"name =?", new String[]{hike_name.toString()}, null, null, null, null);
		if (cursor != null){
			cursor.moveToFirst();
			users = cursor.getString(4);
		}
	}
	

	public List<String> selectAppointments() {
		List<String> list = new ArrayList<String>();
		Cursor cursor = this.db.query("appointments", new String[] {"_id","patientName","date",
		"time"}, null, null, null, null, null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				do {
					list.add(cursor.getString(2)+" : " +cursor.getString(1) + " @ " + cursor.getString(3));
					// Date: name @ time
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed())
				cursor.close();			
		}
		return list;
	}
}