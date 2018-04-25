package sy.UProject.talat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQlite extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;

    public SQlite(Context context) {
        super(context, "events", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE Events (id String,headline TEXT,des TEXT,host CHAR(50),"
                + "EX1 TEXT, EX2 TEXT, cost INTEGER, hangging INTEGER,"
                + "fe INTEGER, rank INTEGER, place TEXT,"
                + "s_date CHAR(20), e_date CHAR(20), s_time CHAR(5), e_time CHAR(5), section CHAR(2), isHangging CHAR(1))";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS Events");
        onCreate(db);
    }
    public void update(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Events");
        onCreate(db);
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (event != null && getEvent(event.getId()) != null) {
            updateEvent(event);
            return;
        }
        ContentValues values = new ContentValues();
        values.put("id", event.getId());
        values.put("headline", event.getHeadline());
        values.put("des", event.getDesc());
        values.put("host", event.getHost());
        values.put("cost", event.getCost());
        values.put("hangging", event.getHangging());
        values.put("fe", event.getFeatured());
        values.put("rank", event.getRank());
        values.put("place", event.getPlace());
        values.put("s_date", event.getS_date());
        values.put("e_date", event.getE_date());
        values.put("s_time", event.getS_time());
        values.put("e_time", event.getE_time());
        values.put("section", event.getSection());
        values.put("isHangging", event.getIsHangging());
        db.insert("Events", null, values);
    }

    public Event getEvent(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Events", new String[]{"id", "headline",
                        "des", "host", "cost", "hangging", "fe", "rank", "place",
                        "section", "s_date", "e_date", "s_time", "e_time", "isHangging"}, "id=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        //0,  1,       ,2   ,3    ,4   ,5   ,6    ,7        ,8  ,9    ,10   ,11     ,12     ,13     ,14     ,15      ,16
        //id ,headline ,des ,host ,EX1 ,EX2 ,cost ,hangging ,fe ,rank ,place,s_date ,e_date ,s_time ,e_time , section, isHangging
        Event event = new Event(cursor.getString(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3),
                Integer.parseInt(cursor.getString(9)), Integer.parseInt(cursor
                .getString(5)), Long.parseLong(cursor.getString(10)),
                Long.parseLong(cursor.getString(11)), Long.parseLong(cursor
                .getString(12)), Long.parseLong(cursor.getString(13)));
        event.setCost(Integer.parseInt(cursor.getString(4)));
        event.setFeatured(Integer.parseInt(cursor.getString(6)));
        event.setRank(Integer.parseInt(cursor.getString(7)));
        event.setPlace(cursor.getString(8));
        event.setIsHangging(Integer.parseInt(cursor.getString(14)));
        cursor.close();
        cursor = null;
        return event;
    }


    public int getEventsCount() {
        String countQuery = "SELECT  * FROM Events";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("headline", event.getHeadline());
        values.put("des", event.getDesc());
        values.put("host", event.getHost());
        values.put("hangging", event.getHangging());
        values.put("section", event.getSection());
        values.put("host", event.getHost());
        values.put("fe", event.getFeatured());
        values.put("cost", event.getCost());
        values.put("rank", event.getRank());
        values.put("place", event.getPlace());
        values.put("section", event.getSection());
        values.put("s_date", event.getS_date());
        values.put("e_date", event.getE_date());
        values.put("s_time", event.getS_time());
        values.put("e_time", event.getE_time());
        values.put("isHangging", event.getIsHangging());

        return db.update("Events", values, "id = ?",
                new String[]{String.valueOf(event.getId())});
    }

    public Event[] getUpcowming() {
        Event[] e = new Event[100];
//  get all hanggnig events and see if anyone of them is in the next hour, and not notified about it yet;
        return e;
    }

    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Events", "id = ?",
                new String[]{String.valueOf(event.getId())});
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Events");
        onCreate(db);
    }

    public void close() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }
}
