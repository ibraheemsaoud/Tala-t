package sy.UProject.talat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileQL extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    public ProfileQL(Context context) {
        super(context, "Profile", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE Profile (id String, abot TEXT, pic CHAR(50), follow CHAR(2), EX CHAR(10))";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS Profile");
        onCreate(db);
    }

    public void update() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Profile");
        onCreate(db);
    }

    public void addProfile(String username, String abot, String pic, String follow) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (checkProfile(username)) {
            updateProfile(username, abot, pic);
            return;
        }
        ContentValues values = new ContentValues();
        values.put("id", username);
        values.put("abot", abot);
        values.put("pic", pic);
        values.put("follow", follow);
        values.put("EX", "0");
        db.insert("Profile", null, values);
    }

    public void addProfile(String username, String abot, String pic) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (checkProfile(username)) {
            updateProfile(username, abot, pic);
            return;
        }
        ContentValues values = new ContentValues();
        values.put("id", username);
        values.put("abot", abot);
        values.put("pic", pic);
        values.put("follow", "0");
        values.put("EX", "0");
        db.insert("Profile", null, values);
    }

    public boolean checkProfile(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Profile", new String[]{"id", "abot", "pic", "follow", "EX"}, "id=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst())
            return false;
        return true;
    }

    public int updateProfile(String username, String abot, String pic) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("abot", abot);
        values.put("pic", pic);

        return db.update("Profile", values, "id = ?",
                new String[]{username});
    }

    public int updateProfile(String username, String abot, String pic, String follow) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("abot", abot);
        values.put("pic", pic);
        values.put("follow", follow);

        return db.update("Profile", values, "id = ?",
                new String[]{username});
    }

    public int updateFollow(String username, String follow) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("follow", follow);

        return db.update("Profile", values, "id = ?",
                new String[]{username});
    }

    public String getPic(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Profile", new String[]{"id", "abot", "pic"}, "id=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst())
            return null;
        return cursor.getString(2);
    }

    public String getAbot(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Profile", new String[]{"id", "abot", "pic"}, "id=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst())
            return "";
        return cursor.getString(1);
    }

    public String getFollow(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Profile", new String[]{"id", "abot", "pic", "follow"}, "id=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst())
            return "";
        return cursor.getString(3);
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Profile");
        onCreate(db);
    }

    public void close() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }
}
