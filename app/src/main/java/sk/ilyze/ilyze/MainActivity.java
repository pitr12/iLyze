package sk.ilyze.ilyze;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sk.ilyze.db.DatabaseManager;
import sk.ilyze.model.Region;
import sk.ilyze.model.Resort;


public class MainActivity extends ActionBarActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager.init(this);

        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_main, null);
        listView = (ListView) contentView.findViewById(R.id.list_view);

        setContentView(contentView);

        createRegion();
    }

    protected void createRegion(){
        Region r = new Region();
        r.setName("region2");
        DatabaseManager.getInstance().addRegion(r);

        createResort("resort1");
    }

    private void createResort(String name) {
        Region region = DatabaseManager.getInstance().getAllRegions().get(0);
        if (null!=region) {
            Resort item = DatabaseManager.getInstance().newResort();
            item.setName(name);
            item.setRegion(region);
            DatabaseManager.getInstance().updateResort(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupListView(listView);
    }

    private void setupListView(ListView lv) {
        final List<Region> regions = DatabaseManager.getInstance().getAllRegions();

        List<String> titles = new ArrayList<String>();
        for (Region r : regions) {
            titles.add(r.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        lv.setAdapter(adapter);

        final Activity activity = this;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Region region = regions.get(position);
                Intent intent = new Intent(activity, RegionActivity.class);
                intent.putExtra(Constants.keyRegionId, region.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
