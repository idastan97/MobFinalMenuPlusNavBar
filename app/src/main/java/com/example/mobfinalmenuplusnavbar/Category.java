package com.example.mobfinalmenuplusnavbar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Icon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.Destroyable;

public class Category {
    public final static String TABLE_NAME = "CATEGORIES";
    public final static String ID_COLUMN = "_id";
    public final static String NAME_COLUMN = "NAME";
    public final static String DESCRIPTION_COLUMN = "DESCRIPTION";
    public final static String ICON_COLUMN = "ICON";
    private final static String CREATE_TABLE_SCRIPT = "CREATE TABLE " + TABLE_NAME + " ("
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME_COLUMN + " TEXT UNIQUE NOT NULL, "
            + DESCRIPTION_COLUMN + " TEXT DEFAULT \"\" NOT NULL,"
            + ICON_COLUMN + " INTEGER DEFAULT " + R.drawable.category_base_icon + " NOT NULL);";
    private final static List<Category> BASE_ITEMS = Arrays.asList(
            new Category("General", null, 0),
            new Category("Debt", null, R.drawable.category_debt_icon));

    private long id;
    private String name;
    private String description;
    private int icon;

    public static void init_table(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_SCRIPT);
        for (Category category: BASE_ITEMS){
            if (category.getIcon() == 0 ){
                db.execSQL("INSERT INTO "
                        + TABLE_NAME + " "
                        + "(" + NAME_COLUMN + ") VALUES (\"" + category.getName() + "\");"
                );
            } else {
                db.execSQL("INSERT INTO "
                        + TABLE_NAME + " "
                        + "(" + NAME_COLUMN + ", " + ICON_COLUMN + ") VALUES "
                        + "(\"" + category.getName() + "\", " + category.getIcon() + ");"
                );
            }

        }
    }

    public Category(String name, String description, int icon){
        this.id = -1;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public Category(long id, String name, String description, int icon){
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public static List<Category> filter(String whereClause, String[] whereArgs){
        Cursor cursor = DBHelper.db.query(
                TABLE_NAME,
                new String[]{ID_COLUMN, NAME_COLUMN, DESCRIPTION_COLUMN, ICON_COLUMN},
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        List<Category> res = new ArrayList<>();
        while (cursor.moveToNext()){
            long id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int icon = cursor.getInt(3);
            Category item = new Category(id, name, description, icon);
            res.add(item);
        }
        cursor.close();
        return res;
    }

//    legacy
    public static List<Category> filter(String where){
        Cursor cursor = DBHelper.db.rawQuery("SELECT"
                + ID_COLUMN + ", "
                + NAME_COLUMN + ", "
                + DESCRIPTION_COLUMN + ", "
                + ICON_COLUMN + " "
                + "FROM " + TABLE_NAME + " "
                + where, null
        );
        List<Category> res = new ArrayList<>();
        while (cursor.moveToNext()){
            long id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int icon = cursor.getInt(3);
            Category item = new Category(id, name, description, icon);
            res.add(item);
        }
        cursor.close();
        return res;
    }

    public void save(){
        //        Some legacy code

//        List<String> value_names = new ArrayList<>();
//        List<String> values = new ArrayList<>();

        ContentValues values = new ContentValues();

//        value_names.add("name");
//        values.add("\"" + name + "\"");
        values.put(NAME_COLUMN, name);

//        value_names.add("description");
//        values.add("\"" + description + "\"");
        values.put(DESCRIPTION_COLUMN, description);

        if (icon != 0){
//            value_names.add(ICON_COLUMN);
//            values.add("icon");
            values.put(ICON_COLUMN, icon);
        }
//        DBHelper.save_item(TABLE_NAME, id, value_names, values);
        long res = DBHelper.save_item(TABLE_NAME, id, values);
        if (this.id < 0) {
            this.id = res;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIcon() {
        return icon;
    }
}
