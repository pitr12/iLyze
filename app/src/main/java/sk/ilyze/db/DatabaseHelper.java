package sk.ilyze.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import sk.ilyze.model.Lift;
import sk.ilyze.model.Region;
import sk.ilyze.model.Resort;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "IlyzeDB.sqlite";
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<Resort, Integer> resortDao = null;
    private Dao<Region, Integer> regionDao = null;
    private Dao<Lift, Integer> liftDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database,ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Region.class);
            TableUtils.createTable(connectionSource, Resort.class);
            TableUtils.createTable(connectionSource, Lift.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            List<String> allSql = new ArrayList<String>();
            switch(oldVersion)
            {
                case 1:
                    //allSql.add("alter table AdData add column `new_col` VARCHAR");
                    //allSql.add("alter table AdData add column `new_col2` VARCHAR");
            }
            for (String sql : allSql) {
                db.execSQL(sql);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        }

    }

    public Dao<Resort, Integer> getResortDao() {
        if (null == resortDao) {
            try {
                resortDao = getDao(Resort.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return resortDao;
    }

    public Dao<Region, Integer> getRegionDao() {
        if (null == regionDao) {
            try {
                regionDao = getDao(Region.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return regionDao;
    }

    public Dao<Lift, Integer> getLiftDao() {
        if (null == liftDao) {
            try {
                liftDao = getDao(Lift.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return liftDao;
    }

}