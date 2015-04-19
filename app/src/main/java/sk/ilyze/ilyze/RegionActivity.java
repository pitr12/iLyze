package sk.ilyze.ilyze;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sk.ilyze.db.DatabaseManager;
import sk.ilyze.model.Region;
import sk.ilyze.model.Resort;

public class RegionActivity extends Activity {
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
        setTitle("Region: '"+region.getName()+"'");
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
//            listView.setOnItemClickListener(new OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    WishItem item = wishItems.get(position);
//                    Intent intent = new Intent(activity,AddWishItemActivity.class);
//                    intent.putExtra(Constants.keyWishItemId, item.getId());
//                    startActivity(intent);
//                }
//            });
        }
    }
}
