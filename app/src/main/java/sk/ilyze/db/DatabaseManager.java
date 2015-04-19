package sk.ilyze.db;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import sk.ilyze.model.Lift;
import sk.ilyze.model.Region;
import sk.ilyze.model.Resort;

public class DatabaseManager {
    static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (null==instance) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseHelper helper;
    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    public List<Region> getAllRegions() {
        List<Region> regions = null;
        try {
            regions = getHelper().getRegionDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regions;
    }

    public void addRegion(Region r) {
        try {
            getHelper().getRegionDao().create(r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Region getReionWithId(int regionId) {
        Region region = null;
        try {
            region = getHelper().getRegionDao().queryForId(regionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return region;
    }

    public Resort getResortWithId(int resortId) {
        Resort resort = null;
        try {
            resort = getHelper().getResortDao().queryForId(resortId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resort;
    }

    public Resort newResort() {
        Resort resort = new Resort();
        try {
            getHelper().getResortDao().create(resort);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resort;
    }

    public void updateResort(Resort item) {
        try {
            getHelper().getResortDao().update(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Lift newLift() {
        Lift lift = new Lift();
        try {
            getHelper().getLiftDao().create(lift);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lift;
    }

    public void updateLift(Lift item) {
        try {
            getHelper().getLiftDao().update(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isDbEmpty() throws SQLException {
        double count = getHelper().getRegionDao().countOf();
        if(count == 0.0){
            return true;
        }
        return false;
    }
}
