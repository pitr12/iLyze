package sk.ilyze.ilyze;


import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
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

        int resortId = getIntent().getExtras().getInt(Constants.keyResortId);
        resort= DatabaseManager.getInstance().getResortWithId(resortId);
        insertContent(contentView);

        setContentView(contentView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        createLiftTable();
        setTitle(Constants.appName + " - " + resort.getName());
    }

    protected void insertContent(View contentView){

        TextView desc = (TextView) contentView.findViewById(R.id.descriptionText);
        desc.setText(resort.getDescription());


        TextView services = (TextView) contentView.findViewById(R.id.servicesText);
        services.setText(resort.getServices());

        TextView elevation = (TextView) contentView.findViewById(R.id.elevationText);
        elevation.setText(resort.getElevation() + " m");

        TextView length = (TextView) contentView.findViewById(R.id.lengthText);
        length.setText(resort.getLength() + " m");

        TextView running = (TextView) contentView.findViewById(R.id.runningText);
        running.setText(resort.getRunning() + " %");

        TextView snow = (TextView) contentView.findViewById(R.id.snowText);
        snow.setText(resort.getSnow() + " cm");

        TextView web = (TextView) contentView.findViewById(R.id.webText);
        web.setText(resort.getUrl());
    }

    protected void createLiftTable(){
        TableLayout liftsLayout = (TableLayout)findViewById(R.id.lifts);
        liftsLayout.setStretchAllColumns(true);
        liftsLayout.bringToFront();

        List<Lift> lifts = resort.getLifts();

        for(int i = 0; i < lifts.size(); i++){
            Lift lift = lifts.get(i);
            Drawable operationIcon = null;
            Drawable typeIcon = null;
            InputStream ims = null;
            // load image
            try {
                AssetManager assetManager = getAssets();
                // get input stream
                if(lift.getOperational() == 0) {
                    ims = assetManager.open("no.png");
                }
                else{
                    ims = assetManager.open("yes.png");
                }
                // load image as Drawable
                operationIcon = Drawable.createFromStream(ims, null);
            }
            catch(IOException ex) {
                return;
            }

            try {
                AssetManager assetManager = getAssets();
                // get input stream
                ims = assetManager.open("types/" + lift.getType() + ".png");
                // load image as Drawable
                typeIcon = Drawable.createFromStream(ims, null);
            }
            catch(IOException ex) {
                return;
            }

            TableRow tr =  new TableRow(this);

            TextView c1 = new TextView(this);
            c1.setText(lift.getName());
            c1.setWidth(275);

            ImageView img = new ImageView(this);
            img.setPadding(-40, 10, 0, 10);
            img.setMinimumWidth(75);
            img.setMinimumHeight(75);
            img.setImageDrawable(typeIcon);

            TextView c3 = new TextView(this);
            c3.setText("" + lift.getLength());
            c3.setPadding(10,0,0,0);
            TextView c4 = new TextView(this);
            c4.setText("" + lift.getCapacity());
            c4.setPadding(10, 0, 0, 0);


            ImageView img2 = new ImageView(this);
            img2.setMaxWidth(100);
            img2.setMaxHeight(100);
            img2.setPadding(-40, 10, 0, 10);
            img2.setMinimumWidth(75);
            img2.setMinimumHeight(75);
            img2.setImageDrawable(operationIcon);

            tr.addView(c1);
            tr.addView(img);
            tr.addView(c3);
            tr.addView(c4);
            tr.addView(img2);

            View v = new View(this);
            v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
            v.setBackgroundColor(Color.rgb(255, 255, 255));

            liftsLayout.addView(tr);
            liftsLayout.addView(v);
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
