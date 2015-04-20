package sk.ilyze.ilyze;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sk.ilyze.db.DatabaseManager;
import sk.ilyze.model.Region;

public class RegionAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Region> regions;
    private Context context;

    public RegionAdapter(Context context, List<Region> regions) {
        this.regions = regions;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return regions.size();
    }

    @Override
    public Object getItem(int position) {
        return regions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.region_row, parent, false);

            holder.txt1 = (TextView) convertView.findViewById(R.id.textView1);
            holder.txt2 = (TextView) convertView.findViewById(R.id.textView2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Region region = regions.get(position);
        holder.txt1.setText(region.getName());
        holder.txt2.setText("Počet stredísk: " +Integer.toString(region.getResorts().size()));

        return convertView;
    }

    private class ViewHolder {
        TextView txt1;
        TextView txt2;
    }

}