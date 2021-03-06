package sk.ilyze.ilyze;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

import sk.ilyze.db.DatabaseManager;
import sk.ilyze.model.Lift;
import sk.ilyze.model.Region;
import sk.ilyze.model.Resort;
import sk.ilyze.model.Weather;


public class MainActivity extends ActionBarActivity{
    protected ListView listView;
    protected TextView weather;
    protected TextView location;
    protected ImageView weatherIcon;
    protected GPSTracker gps;
    protected double latitude;
    protected double longitude;
    protected String city;



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
                int resort_length = resort.getInt("length");
                int resort_running = resort.getInt("running");
                int resort_elevation = resort.getInt("elevation");
                String resort_description = resort.getString("description");
                String resort_services = resort.getString("services");
                String resort_url = resort.getString("url");
                int resort_snow = resort.getInt("snow");

                Resort rs = createResort(resort_name,r, resort_length, resort_running, resort_elevation, resort_description,resort_services,resort_url,resort_snow);

                JSONArray lifts = resort.getJSONArray("lifts");
                for(int k=0; k<lifts.length(); k++){
                    JSONObject lift = lifts.getJSONObject(k);
                    String lift_name = lift.getString("name");
                    int lift_type = lift.getInt("type");
                    int lift_capacity = lift.getInt("capacity");
                    int lift_length = lift.getInt("length");
                    int lift_operational = lift.getInt("operational");

                    createLift(lift_name,rs, lift_type, lift_length, lift_capacity, lift_operational);
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


        //location
        gps = new GPSTracker(this);
        location = (TextView) contentView.findViewById(R.id.loc);
        if(gps.canGetLocation()){
           latitude = gps.getLatitude();
           longitude = gps.getLongitude();

            location.setText("Lat: " +latitude + "\n Long: " +longitude);
        }else{
            gps.showSettingsAlert();
        }

        //weather
        if(WeatherClient.CheckInternet(this)) {
            weather = (TextView) contentView.findViewById(R.id.textview1);
            weatherIcon = (ImageView) contentView.findViewById(R.id.condIcon);

            String city = "Bratislava,sk";
            String lat = latitude + "";
            String lon = longitude + "";

            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{city, lat, lon});
        }

    }

    protected Region createRegion(String name){
        Region r = new Region();
        r.setName(name);
        DatabaseManager.getInstance().addRegion(r);
        return r;
    }

    protected Resort createResort(String name, Region region, int length, int running, int elevation, String description, String services, String url, int snow) {
        if (null!=region) {
            Resort item = DatabaseManager.getInstance().newResort();
            item.setName(name);
            item.setRegion(region);
            item.setLength(length);
            item.setRunning(running);
            item.setElevation(elevation);
            item.setDescription(description);
            item.setServices(services);
            item.setUrl(url);
            item.setSnow(snow);
            DatabaseManager.getInstance().updateResort(item);
            return item;
        }
        return  null;
    }

    protected void createLift(String name, Resort resort, int type, int length, int capacity, int operational) {
        if (null!=resort) {
            Lift item = DatabaseManager.getInstance().newLift();
            item.setName(name);
            item.setResort(resort);
            item.setType(type);
            item.setLength(length);
            item.setCapacity(capacity);
            item.setOperational(operational);
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
            String data = null;
            try {
                data = ( (new WeatherClient()).getWeatherData(params[0],params[1],params[2]));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

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

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);

                String icon = weather.currentCondition.getIcon() + ".png";

                // load image
                try {
                    AssetManager assetManager = getAssets();
                    // get input stream
                    InputStream ims = assetManager.open("weather/"+icon);
                    // load image as Drawable
                    Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                    if(d != null) {
                        weatherIcon.setImageDrawable(d);
                    }
                }
                catch(IOException ex) {
                    return;
                }
            }

            //cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            //condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            MainActivity.this.weather.setText(weather.location.getCity() + ": " + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
//            hum.setText("" + weather.currentCondition.getHumidity() + "%");
//            press.setText("" + weather.currentCondition.getPressure() + " hPa");
//            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
//            windDeg.setText("" + weather.wind.getDeg() + "�");

        }
    }
}
