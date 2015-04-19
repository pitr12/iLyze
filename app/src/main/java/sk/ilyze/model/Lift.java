package sk.ilyze.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Lift {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Resort resort;

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

    public Resort getResort() {
        return resort;
    }

    public void setResort(Resort resort) {
        this.resort = resort;
    }
}
