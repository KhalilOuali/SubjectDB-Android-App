package my.subjectdb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Subjects.db";

	public DBHelper(@Nullable Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		String SQL_CREATE_ENTRIES =
				"CREATE TABLE " + Subject.DBEntry.TABLE_NAME + " (" +
						Subject.DBEntry._ID + " INTEGER PRIMARY KEY, " +
						Subject.DBEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
						Subject.DBEntry.COLUMN_PROF + " TEXT, " +
						Subject.DBEntry.COLUMN_CLASSCODE + " TEXT, " +
						Subject.DBEntry.COLUMN_TYPE + " NUMBER NOT NULL, " +
						Subject.DBEntry.COLUMN_HASFINAL + " NUMBER NOT NULL);";
		sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		String SQL_DELETE_ENTRIES =
				"DROP TABLE IF EXISTS " + Subject.DBEntry.TABLE_NAME;
		sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
		onCreate(sqLiteDatabase);
	}

	public Subject addSubject(String name, String prof, String classCode, int type, boolean hasFinal) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Subject.DBEntry.COLUMN_NAME, name);
		values.put(Subject.DBEntry.COLUMN_PROF, prof);
		values.put(Subject.DBEntry.COLUMN_CLASSCODE, classCode);
		values.put(Subject.DBEntry.COLUMN_TYPE, type);
		values.put(Subject.DBEntry.COLUMN_HASFINAL, hasFinal ? 1 : 0);
		long newID = db.insert(Subject.DBEntry.TABLE_NAME, null, values);
		return newID == -1 ? null : new Subject(newID, name, prof, classCode, type, hasFinal);
	}

	public Subject getSubject(long id) {
		SQLiteDatabase db = getReadableDatabase();
		String[] projection = {
				Subject.DBEntry.COLUMN_NAME,
				Subject.DBEntry.COLUMN_PROF,
				Subject.DBEntry.COLUMN_CLASSCODE,
				Subject.DBEntry.COLUMN_TYPE,
				Subject.DBEntry.COLUMN_HASFINAL};

		String selection = BaseColumns._ID + " = ?";
		String[] args = {String.valueOf(id)};
		Cursor cursor = db.query(Subject.DBEntry.TABLE_NAME, projection, selection, args, null, null, null);
		if (cursor.moveToNext()) {
			Subject subject = new Subject(id,
					cursor.getString(0),
					cursor.getString(1),
					cursor.getString(2),
					cursor.getInt(3),
					cursor.getInt(4) == 1);
			cursor.close();
			return subject;
		} else {
			cursor.close();
			return null;
		}
	}

	/**
	 * <b color="red">Important:</b> subjects returned <b>only</b> have <b>ID</b> and <b>NAME</b>.
	 */
	public List<Subject> getSubjectList(String searchFilter, boolean finalFilter) {
		SQLiteDatabase db = getReadableDatabase();
		List<Subject> subjectList = new ArrayList<>();
		String[] projection = {BaseColumns._ID, Subject.DBEntry.COLUMN_NAME};
		String selection = Subject.DBEntry.COLUMN_NAME + " LIKE ?";
		String[] args = {"%" + searchFilter + "%"};
		selection += finalFilter ? ( " AND " + Subject.DBEntry.COLUMN_HASFINAL + " = 1") : "";
		String orderBy = Subject.DBEntry.COLUMN_NAME;

		Cursor cursor = db.query(Subject.DBEntry.TABLE_NAME, projection, selection, args, null, null, orderBy);
		while (cursor.moveToNext()) {
			Subject subject = new Subject(cursor.getLong(0), cursor.getString(1));
			subjectList.add(subject);
		}
		cursor.close();
		return subjectList;
	}

	public boolean updateSubject(long id, String name, String prof, String classCode, int type, boolean hasFinal) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Subject.DBEntry.COLUMN_NAME, name);
		values.put(Subject.DBEntry.COLUMN_PROF, prof);
		values.put(Subject.DBEntry.COLUMN_CLASSCODE, classCode);
		values.put(Subject.DBEntry.COLUMN_TYPE, type);
		values.put(Subject.DBEntry.COLUMN_HASFINAL, hasFinal ? 1 : 0);
		String where = BaseColumns._ID + " = ?";
		String[] args = {String.valueOf(id)};

		try {
			db.update(Subject.DBEntry.TABLE_NAME, values, where, args);
			return true;
		} catch (SQLiteConstraintException e) {
			return false;
		}
	}

	public void removeSubject(Subject subject) {
		SQLiteDatabase db = getWritableDatabase();
		String where = BaseColumns._ID + " = ?";
		String[] args = {String.valueOf(subject.getId())};
		db.delete(Subject.DBEntry.TABLE_NAME, where, args);
	}
}
