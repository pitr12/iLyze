package sk.ilyze.ilyze;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sk.ilyze.db.DatabaseManager;
import sk.ilyze.model.Lift;
import sk.ilyze.model.Region;
import sk.ilyze.model.Resort;
import sk.ilyze.model.Weather;


public class MainActivity extends ActionBarActivity{
    ListView listView;
    TextView txtLat;
    protected LocationManager locationManager;


    protected String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("init_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    protected void initDatabase() throws JSONException {
        JSONArray data = new JSONArray(loadJSONFromAsset());

        for(int i=0; i<data.length(); i++){
            JSONObject region = data.getJSONObject(i);
            String region_name = region.getString("region_name");

            Region r = createRegion(region_name);

            JSONArray resorts = region.getJSONArray("resorts");
            for(int j=0; j<resorts.length(); j++){
                JSONObject resort = resorts.getJSONObject(j);
                String resort_name = resort.getString("name");
                Resort rs = createResort(resort_name,r);

                JSONArray lifts = resort.getJSONArray("lifts");
                for(int k=0; k<lifts.length(); k++){
                    JSONObject lift = lifts.getJSONObject(k);
                    String lift_name = lift.getString("name");

                    createLift(lift_name,rs);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager.init(this);

        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_main, null);
        listView = (ListView) contentView.findViewById(R.id.list_view);

        setContentView(contentView);
        setTitle(Constants.appName + " Všetky regióny");

        try {
            if(DatabaseManager.getInstance().isDbEmpty()) {
                try {
                    initDatabase();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        txtLat = (TextView) contentView.findViewById(R.id.textview1);

        String city = "Bratislava,sk";
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});
    }

    protected Region createRegion(String name){
        Region r = new Region();
        r.setName(name);
        DatabaseManager.getInstance().addRegion(r);
        return r;
    }

    protected Resort createResort(String name, Region region) {
        if (null!=region) {
            Resort item = DatabaseManager.getInstance().newResort();
            item.setName(name);
            item.setRegion(region);
            DatabaseManager.getInstance().updateResort(item);
            return item;
        }
        return  null;
    }

    protected void createLift(String name, Resort resort) {
        if (null!=resort) {
            Lift item = DatabaseManager.getInstance().newLift();
            item.setName(name);
            item.setResort(resort);
            DatabaseManager.getInstance().updateLift(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupListView(listView);
    }

    private void setupListView(ListView lv) {
        final List<Region> regions = DatabaseManager.getInstance().getAllRegions();

        RegionAdapter adapter = new RegionAdapter(this, regions);
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

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                weather.iconData = ( (new WeatherClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }




        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

//            if (weather.iconData != null && weather.iconData.length > 0) {
//                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
//                imgView.setImageBitmap(img);
//            }

            //cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            //condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            txtLat.setText(weather.location.getCity() + " - " + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
//            hum.setText("" + weather.currentCondition.getHumidity() + "%");
//            press.setText("" + weather.currentCondition.getPressure() + " hPa");
//            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
//            windDeg.setText("" + weather.wind.getDeg() + "�");

        }
    }
}
