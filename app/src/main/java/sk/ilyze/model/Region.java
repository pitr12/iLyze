package sk.ilyze.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable
public class Region {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @ForeignCollectionField
    private ForeignCollection<Resort> resorts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Resort> getResorts() {
        ArrayList<Resort> resotList = new ArrayList<Resort>();

        for(Resort r : resorts){
            resotList.add(r);
        }

        return resotList;
    }

    public void setResorts(ForeignCollection<Resort> resorts) {
        this.resorts = resorts;
    }
}
