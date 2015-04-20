package sk.ilyze.ilyze;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class RegionActivity extends ActionBarActivity {
    private Region region;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_region, null);
        listView = (ListView) contentView.findViewById(R.id.list_view);

        setContentView(contentView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int regionId = getIntent().getExtras().getInt(Constants.keyRegionId);
        region = DatabaseManager.getInstance().getReionWithId(regionId);
        setupListView();
        setTitle(Constants.appName + " " +region.getName());
    }

    private void setupListView() {
        if (null != region) {
            final List<Resort> resorts = region.getResorts();
            List<String> titles = new ArrayList<String>();
            for (Resort r : resorts) {
                titles.add(r.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
            listView.setAdapter(adapter);
            final Activity activity = this;
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Resort item = resorts.get(position);
                    Intent intent = new Intent(activity,ResortActivity.class);
                    intent.putExtra(Constants.keyResortId, item.getId());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_region, menu);
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
