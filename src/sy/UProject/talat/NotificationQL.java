package sy.UProject.talat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotificationQL extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    public NotificationQL(Context context) {
        super(context, "Notification", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE Notification (id String, txt TEXT, dat CHAR(50), type CHAR(2), read CHAR(1), extra CHAR(50))";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS Notification");
        onCreate(db);
    }

    public void update() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Notification");
        onCreate(db);
    }

    public void addNotification(NotificationAdapter NA) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (checkNotification(NA.getId())) {
            updateNotification(NA);
            return;
        }
        ContentValues values = new ContentValues();
        values.put("id", NA.getId());
        values.put("txt", NA.getText());
        values.put("dat", NA.getDate());
        values.put("type", NA.getType());
        values.put("extra", NA.getExtra());
        db.insert("Notification", null, values);
    }

    public boolean checkNotification(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Notification", new String[]{"id"}, "id=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst())
            return false;
        return true;
    }

    public int updateNotification(NotificationAdapter NA) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("txt", NA.getText());
        values.put("dat", NA.getDate());
        values.put("type", NA.getType());
        values.put("extra", NA.getExtra());

        return db.update("Notification", values, "id = ?",
                new String[]{NA.getId()});
    }

    public int updateRead(String id, String read) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("read", read);

        return db.update("Notification", values, "id = ?",
                new String[]{id});
    }

    public NotificationAdapter getNotification(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Notification", new String[]{"id", "txt","dat", "type", "extra"}, "id=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst())
            return null;
        NotificationAdapter NA = new NotificationAdapter(cursor.getString(0), cursor.getString(1), cursor.getString(2) , cursor.getString(3), cursor.getString(4));
        NA.setRead(cursor.getString(5));
        return NA;
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Notification");
        onCreate(db);
    }

    public void close() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }
}
