package sk.ilyze.ilyze;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import sk.ilyze.db.DatabaseManager;
import sk.ilyze.model.Lift;
import sk.ilyze.model.Resort;


public class ResortActivity extends ActionBarActivity {
    private Resort resort;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_resort, null);
        listView = (ListView) contentView.findViewById(R.id.list_view);

        setContentView(contentView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int resortId = getIntent().getExtras().getInt(Constants.keyResortId);
        resort= DatabaseManager.getInstance().getResortWithId(resortId);
        setupListView();
        setTitle(Constants.appName + " " +resort.getName()+" - lanovky");
    }

    private void setupListView() {
        if (null != resort) {
            final List<Lift> lifts = resort.getLifts();
            List<String> titles = new ArrayList<String>();
            for (Lift l: lifts) {
                titles.add(l.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
            listView.setAdapter(adapter);
//            final Activity activity = this;
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Resort item = resorts.get(position);
//                    Intent intent = new Intent(activity,ResortActivity.class);
//                    intent.putExtra(Constants.keyResortId, item.getId());
//                    startActivity(intent);
//                }
//            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resort, menu);
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
