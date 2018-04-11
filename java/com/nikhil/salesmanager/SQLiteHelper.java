package com.nikhil.salesmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SQLiteHelper extends SQLiteOpenHelper {
  /*We are creating a java file called SQLiteHelper and extending SQLiteOpenHelper class and It is used to create a bridge between android and SQLite.
    To perform basic SQL operations we need to extend SQLiteOpenHelper class.
*/

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ItemDetails.db";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static final String TABLE_NAME1 = "ITEMS";
    public static final String COLUMN_ITEM_ID = "ITEM_ID";
    public static final String COLUMN_ITEM_NAME = "ITEM_NAME";
    public static final String COLUMN_ITEM_CATEGORY = "ITEM_CATEGORY";
    public static final String COLUMN_ITEM_PRICE = "ITEM_PRICE";
    public static final String COLUMN_ITEM_QUANTITY = "ITEM_QUANTITY";
    public static final String COLUMN_ITEM_SALES = "ITEM_SALES";

    public static final String TABLE_NAME2 = "LOG";
    public static final String COLUMN_DATE = "DATE";

    public static final String TABLE_NAME3 = "CATEGORY";
    public static final String COLUMN_CATEGORY = "ITEM_CATEGORY";

    private SQLiteDatabase database;

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME1 + " ( " + COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ITEM_NAME +
                " VARCHAR, " + COLUMN_ITEM_CATEGORY + " VARCHAR, " + COLUMN_ITEM_PRICE + " FLOAT(4,2), " + COLUMN_ITEM_QUANTITY + " INTEGER, " + COLUMN_ITEM_SALES + " FLOAT(6,2));");

        db.execSQL("CREATE TABLE " + TABLE_NAME2 + " ( " + COLUMN_ITEM_ID + " INTEGER ," + COLUMN_DATE + " DATE, " + COLUMN_ITEM_QUANTITY + " INTEGER, " + COLUMN_ITEM_SALES + " FLOAT(6,2)," +
                " FOREIGN KEY (" + COLUMN_ITEM_ID + ") REFERENCES " + TABLE_NAME1 + "(" + COLUMN_ITEM_ID + "));");

        db.execSQL("CREATE TABLE " + TABLE_NAME3 + " ( " + COLUMN_CATEGORY + " VARCHAR" + ");");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

/*
    public void insertItem(ItemModel item) {

        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ITEM_ID, item.getId());
        contentValues.put(COLUMN_ITEM_NAME, item.getItemName());
        contentValues.put(COLUMN_ITEM_CATEGORY, item.getCatgeory());
        contentValues.put(COLUMN_ITEM_PRICE, item.getPrice());
        contentValues.put(COLUMN_ITEM_QUANTITY, item.getQuantity());
        contentValues.put(COLUMN_ITEM_SALES, item.getSales());

        database.insert(TABLE_NAME1, null, contentValues);
        database.close();
    }
*/

    public boolean insertItem(ItemModel item) {

        database = this.getReadableDatabase();
        boolean flag = true;
        String itemName = "";
        String[] projection = {
                COLUMN_ITEM_NAME
        };

        String[] selection = {
                item.getItemName()
        };
        Cursor cursor = database.query(TABLE_NAME1,
                projection,
                COLUMN_ITEM_NAME + " = ?",
                selection,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            itemName = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME));
        }
        cursor.close();
        if (itemName.equalsIgnoreCase(item.getItemName())) {
            flag = false;
        } else {
            database.execSQL("INSERT INTO " + TABLE_NAME1 + "(" + COLUMN_ITEM_NAME + "," + COLUMN_ITEM_CATEGORY + "," + COLUMN_ITEM_PRICE + "," + COLUMN_ITEM_QUANTITY + "," + COLUMN_ITEM_SALES +
                    ") VALUES('" + item.getItemName() + "','" + item.getCatgeory() + "','" + item.getPrice() + "','" + item.getQuantity() + "','" + item.getSales() + "')");
        }
        database.close();
        return flag;
    }

    public void removeItem(String itemName) {
        database = this.getReadableDatabase();

        database.execSQL("DELETE FROM " + TABLE_NAME1 + " WHERE " + COLUMN_ITEM_NAME + " = '" + itemName + "';");


        database.close();
    }


    public ArrayList<String> getItemNames() {
        database = this.getReadableDatabase();
        ArrayList<String> items = new ArrayList<String>();
        String item = " ";

        String[] logProjection = {
                COLUMN_ITEM_NAME
        };

        Cursor cursor = database.query(TABLE_NAME1, logProjection, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            item = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME));
            items.add(item);
        }
        cursor.close();
        database.close();
        return items;
    }


    //  TODO: CHECK AFTER DONE
    public void insertLog(String itemName, int quantity) {
        //This will update ITEM table and update LOG table

        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        Calendar date = Calendar.getInstance();
        int yy = date.get(Calendar.YEAR);
        String mm = Integer.toString(date.get(Calendar.MONTH) + 1);
        String dd = Integer.toString(date.get(Calendar.DAY_OF_MONTH));


        if (Integer.parseInt(mm) < 10) {
            mm = "0" + mm;
        }

        if (Integer.parseInt(dd) < 10) {
            dd = "0" + dd;
        }


        String strDate = Integer.toString(yy) + "-" + mm + "-" + dd;

        //Retrieval of data required
        int id = 0;
        float price = 0;
        int quantityOld = 0;
        float salesOld = 0;
        String compareDate = "";
        String[] projection = {
                COLUMN_ITEM_ID,
                COLUMN_ITEM_PRICE,
                COLUMN_ITEM_QUANTITY,
                COLUMN_ITEM_SALES
        };

        String[] selection = {
                itemName
        };
        Cursor cursor = database.query(TABLE_NAME1,
                projection,
                COLUMN_ITEM_NAME + " = ?",
                selection,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_ID));
            price = cursor.getFloat(cursor.getColumnIndex(COLUMN_ITEM_PRICE));
            quantityOld = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY));
            salesOld = cursor.getFloat(cursor.getColumnIndex(COLUMN_ITEM_SALES));
        }
        cursor.close();
        float sales = price * quantity;

        //UPDATE ITEM TABLE

        database.execSQL("UPDATE " + TABLE_NAME1 + " SET " + COLUMN_ITEM_QUANTITY + " = " + (quantityOld + quantity) + ", " +
                COLUMN_ITEM_SALES + " = " + (salesOld + sales) + " WHERE " + COLUMN_ITEM_ID + " = " + id + ";");


        //UPDATE / INSERT IN LOG TABLE
        //TODO: Condition to be set.
        long count = DatabaseUtils.queryNumEntries(database,
                TABLE_NAME2,
                COLUMN_ITEM_ID + " = ? AND " + COLUMN_DATE + " = ?",
                new String[]{Integer.toString(id), strDate});
        if (count == 0) {
            contentValues.put(COLUMN_ITEM_ID, id);
            contentValues.put(COLUMN_DATE, strDate);
            contentValues.put(COLUMN_ITEM_QUANTITY, quantityOld + quantity);
            contentValues.put(COLUMN_ITEM_SALES, salesOld + sales);


            database.insert(TABLE_NAME2, null, contentValues);
        } else {
            int logQuantityOld = 0;
            float logSalesOld = 0;
            String[] logProjection = {
                    COLUMN_ITEM_QUANTITY,
                    COLUMN_ITEM_SALES,
                    COLUMN_DATE
            };

            String[] logSelection = {
                    Integer.toString(id)
            };

            Cursor logCursor = database.query(TABLE_NAME2,
                    logProjection,
                    COLUMN_ITEM_ID + " = ?",
                    logSelection,
                    null,
                    null,
                    null,
                    null);

            while (logCursor.moveToNext()) {
                compareDate = logCursor.getString(logCursor.getColumnIndex(COLUMN_DATE));
                if (compareDate.equals(strDate)) {
                    logQuantityOld = logCursor.getInt(logCursor.getColumnIndex(COLUMN_ITEM_QUANTITY));
                    logSalesOld = logCursor.getFloat(logCursor.getColumnIndex(COLUMN_ITEM_SALES));
                }
            }
            logCursor.close();
            int finalQuantity = logQuantityOld + quantity;
            float finalSales = logSalesOld + sales;
            database.execSQL("UPDATE " + TABLE_NAME2 + " SET " + COLUMN_ITEM_QUANTITY + " = " + finalQuantity + ", " +
                    COLUMN_ITEM_SALES + " = " + finalSales + " WHERE " + COLUMN_ITEM_ID + " = " + id + " AND " + COLUMN_DATE + " = '" + strDate + "';");
        }
        database.close();
    }

    public String getQuantity(String itemName) {
        database = this.getReadableDatabase();
        int quantity = 0;
        String[] projection = {
                COLUMN_ITEM_QUANTITY
        };

        String[] selection = {
                itemName
        };
        Cursor cursor = database.query(TABLE_NAME1,
                projection,
                COLUMN_ITEM_NAME + " = ?",
                selection,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY));
        }
        cursor.close();
        database.close();
        return Integer.toString(quantity);
    }

    public float getMonthlyEarning(String date) {

        database = this.getReadableDatabase();
        float earnings = 0f;
/*
        String[] projection = {
                COLUMN_ITEM_SALES
        };
        String[] selection = {
                date
        };

        Cursor cursor = database.query(TABLE_NAME2,
                projection,
                COLUMN_DATE + " = ?",
                selection, null, null, null, null);

        while (cursor.moveToNext  MadHouseGrill  ()){
            earnings = cursor.getFloat(cursor.getColumnIndex(COLUMN_ITEM_SALES));
        }*/


        Log.i("date", date);
        Cursor cur = database.rawQuery("SELECT SUM(" + COLUMN_ITEM_SALES +
                " ) FROM " + TABLE_NAME2 + " WHERE " + COLUMN_DATE + " = '" + date + "';", null);
        if (cur.moveToFirst()) {
            earnings = cur.getFloat(0);
            Log.i("Earr", Float.toString(cur.getFloat(0)));
        }

//        Log.i("Earnings", Float.toString(earnings));

        cur.close();
        database.close();
        return earnings;
    }

    public LinkedHashMap<String, Integer> getTopFive(String year, String month) {

        database = this.getReadableDatabase();
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        long count = DatabaseUtils.queryNumEntries(database,
                TABLE_NAME1, null,
                null);


        Cursor cursor = database.rawQuery("SELECT " + COLUMN_ITEM_ID + ", " +
                COLUMN_ITEM_NAME + " FROM " + TABLE_NAME1, null);

        while (cursor.moveToNext()) {

            String name = cursor.getString(1);
            int id = cursor.getInt(0);

            Log.i("Iddddddd", String.valueOf(id));


            Cursor cursor1 = database.rawQuery(
                    "SELECT SUM(" + COLUMN_ITEM_QUANTITY + ") " +
                            " FROM " + TABLE_NAME2 +
                            " WHERE " + COLUMN_ITEM_ID + " = " + id + " and strftime('%m'," + COLUMN_DATE + ") = '" + month + "'"
                            + " and strftime('%Y'," + COLUMN_DATE + ") = '" + year + "'" + ";", null);


//            Cursor cursor1 = database.rawQuery(
//                    "SELECT strftime('%m'," + COLUMN_DATE + ")"  +
//                            " FROM " + TABLE_NAME2 +
//                            " WHERE " + COLUMN_ITEM_ID + " = " + id + " ;", null);


/*
            Cursor cursor1 = database.rawQuery(
                    "SELECT strftime('%m'," + COLUMN_DATE + ")"  +
                            " FROM " + TABLE_NAME2 +
                            " WHERE " + COLUMN_ITEM_ID + " = " + id + " ;", null);
*/


            while (cursor1.moveToNext()) {
                Log.i("Col Con", String.valueOf(cursor1.getColumnCount()));
                Log.i("cccccc", String.valueOf(cursor1.getInt(0)));
//                Log.i("Date", "Date is " +cursor1.getString(0));
                map.put(name, cursor1.getInt(0));

            }
            Log.i("namee", name);
        }
        for (String name : map.keySet()) {
            String value = map.get(name).toString();
            System.out.println(name + " " + value);
        }

        LinkedHashMap linkedHashMap = sortHashMapByValues(map);

        for (final Object key : linkedHashMap.keySet()) {
  /* print the key */
            System.out.println("objjj " + key.toString());
            System.out.println(linkedHashMap.get(key.toString()));
        }

/*
        for (String name : map.keySet()) {
            String value = map.get(name).toString();
            System.out.println(name + " " + value);
        }*/

        return linkedHashMap;
    }


    public LinkedHashMap<String, Integer> sortHashMapByValues(
            HashMap<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap =
                new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = passedMap.get(key);
                Integer comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }


    public void updateLog(String itemName, int quantity) {
        //This will update ITEM table and update LOG table

        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        Calendar date = Calendar.getInstance();
        int yy = date.get(Calendar.YEAR);
        String mm = String.valueOf(date.get(Calendar.MONTH) + 1);
        String dd = String.valueOf(date.get(Calendar.DAY_OF_MONTH));

        if (Integer.parseInt(mm) < 10) {
            mm = "0" + mm;
        }
        if (Integer.parseInt(dd) < 10) {
            dd = "0" + dd;
        }

        String strDate = Integer.toString(yy) + "-" + mm + "-" + dd;

        //Retrieval of data required
        int id = 0;
        float price = 0;
        int quantityOld = 0;
        float salesOld = 0;
        String compareDate = "";
        String[] projection = {
                COLUMN_ITEM_ID,
                COLUMN_ITEM_PRICE,
                COLUMN_ITEM_QUANTITY,
                COLUMN_ITEM_SALES
        };

        String[] selection = {
                itemName
        };
        Cursor cursor = database.query(TABLE_NAME1,
                projection,
                COLUMN_ITEM_NAME + " = ?",
                selection,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_ID));
            price = cursor.getFloat(cursor.getColumnIndex(COLUMN_ITEM_PRICE));
            quantityOld = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY));
            salesOld = cursor.getFloat(cursor.getColumnIndex(COLUMN_ITEM_SALES));
        }
        cursor.close();
        float sales = price * quantity;

        //UPDATE ITEM TABLE

        database.execSQL("UPDATE " + TABLE_NAME1 + " SET " + COLUMN_ITEM_QUANTITY + " = " + (quantityOld - quantity) + ", " +
                COLUMN_ITEM_SALES + " = " + (salesOld - sales) + " WHERE " + COLUMN_ITEM_ID + " = " + id + ";");


        //UPDATE / INSERT IN LOG TABLE
        //TODO: Condition to be set.
        long count = DatabaseUtils.queryNumEntries(database,
                TABLE_NAME2,
                COLUMN_ITEM_ID + " = ? AND " + COLUMN_DATE + " = ?",
                new String[]{Integer.toString(id), strDate});
        if (count == 0) {
            contentValues.put(COLUMN_ITEM_ID, id);
            contentValues.put(COLUMN_DATE, strDate);
            contentValues.put(COLUMN_ITEM_QUANTITY, quantityOld - quantity);
            contentValues.put(COLUMN_ITEM_SALES, salesOld - sales);


            database.insert(TABLE_NAME2, null, contentValues);
        } else {
            int logQuantityOld = 0;
            float logSalesOld = 0;
            String[] logProjection = {
                    COLUMN_ITEM_QUANTITY,
                    COLUMN_ITEM_SALES,
                    COLUMN_DATE
            };

            String[] logSelection = {
                    Integer.toString(id)
            };
            Cursor logCursor = database.query(TABLE_NAME2,
                    logProjection,
                    COLUMN_ITEM_ID + " = ?",
                    logSelection,
                    null,
                    null,
                    null,
                    null);
            while (logCursor.moveToNext()) {
                compareDate = logCursor.getString(logCursor.getColumnIndex(COLUMN_DATE));
                if (compareDate.equals(strDate)) {
                    logQuantityOld = logCursor.getInt(logCursor.getColumnIndex(COLUMN_ITEM_QUANTITY));
                    logSalesOld = logCursor.getFloat(logCursor.getColumnIndex(COLUMN_ITEM_SALES));
                }
            }
            logCursor.close();
            database.execSQL("UPDATE " + TABLE_NAME2 + " SET " + COLUMN_ITEM_QUANTITY + " = " + (logQuantityOld - quantity) + ", " + COLUMN_ITEM_SALES + " = " +
                    (logSalesOld - sales) + " WHERE " + COLUMN_ITEM_ID + " = " + id + " AND " + COLUMN_DATE + " = '" + strDate + "';");
        }
        database.close();
    }

    public String calculateDailySales() {
        database = this.getReadableDatabase();
        float sales = 0;
        String[] projection = {
                COLUMN_ITEM_SALES
        };
        Cursor cursor = database.query(TABLE_NAME1,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);


        while (cursor.moveToNext()) {
            sales = sales + cursor.getFloat(cursor.getColumnIndex(COLUMN_ITEM_SALES));
        }
        cursor.close();
        database.close();
        return Float.toString(sales);
    }

    public boolean insertCategory(String category) {
        database = this.getReadableDatabase();
        boolean flag = true;
        String categoryName = "";
        String[] projection = {
                COLUMN_ITEM_CATEGORY
        };

        String[] selection = {
                category
        };
        Cursor cursor = database.query(TABLE_NAME3,
                projection,
                COLUMN_ITEM_CATEGORY + " = ?",
                selection,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CATEGORY));
        }
        cursor.close();
        if (categoryName.equalsIgnoreCase(category)) {
            flag = false;
        } else {
            database.execSQL("INSERT INTO " + TABLE_NAME3 + " VALUES('" + category + "');");
        }
        database.close();
        return flag;
    }

    public void removeCategory(String category) {
        database = this.getReadableDatabase();
        //TODO: Remove items with the category to be removed, first items then category itself.

        database.execSQL("DELETE FROM " + TABLE_NAME3 + " WHERE " + COLUMN_CATEGORY + " = '" + category + "';");
        database.execSQL("DELETE FROM " + TABLE_NAME1 + " WHERE " + COLUMN_ITEM_CATEGORY + " = '" + category + "';");
        database.close();
    }

    public ArrayList<String> getCategory() {
        database = this.getReadableDatabase();
        ArrayList<String> categories = new ArrayList<String>();
        String category;
        String[] projection = {
                COLUMN_CATEGORY
        };
        Cursor cursor = database.query(TABLE_NAME3,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
            categories.add(category);
        }
        cursor.close();
        database.close();
        return categories;
    }

    public boolean everyDayCheck() {
        boolean flag = false;
        Calendar date = Calendar.getInstance();
        int yy = date.get(Calendar.YEAR);
        String mm = String.valueOf(date.get(Calendar.MONTH) + 1);
        String dd = String.valueOf(date.get(Calendar.DAY_OF_MONTH));

        if (Integer.parseInt(mm) < 10) {
            mm = "0" + mm;
        }

        if (Integer.parseInt(dd) < 10) {
            mm = "0" + mm;
        }

        String strDate = Integer.toString(yy) + "-" + mm + "-" + dd;

        database = this.getReadableDatabase();
        String compareDate = "";
        String[] projection = {
                COLUMN_DATE
        };

        String[] selection = {
                strDate
        };
        Cursor cursor = database.query(TABLE_NAME2,
                projection,
                COLUMN_DATE + " = ?",
                selection,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            compareDate = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        }
        if (compareDate.equals("")) {
            int zero = 0;
            database.execSQL("UPDATE " + TABLE_NAME1 + " SET " + COLUMN_ITEM_QUANTITY + " = " + zero + ", " + COLUMN_ITEM_SALES + " = " + zero + ";");
            flag = true;
        }
        cursor.close();
        database.close();
        return flag;
    }

    public void fetchRecordForDay(String date) {

    }
}
// jaineet
