package sk.ilyze.ilyze;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sk.ilyze.model.Region;
import sk.ilyze.model.Resort;

public class ResortAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Resort> resorts;
    private Context context;

    public ResortAdapter(Context context, List<Resort> resorts) {
        this.resorts = resorts;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return resorts.size();
    }

    @Override
    public Object getItem(int position) {
        return resorts.get(position);
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
            convertView = this.inflater.inflate(R.layout.resort_row, parent, false);

            holder.name = (TextView) convertView.findViewById(R.id.textView1);
            holder.length = (TextView) convertView.findViewById(R.id.textView2);
            holder.running = (TextView) convertView.findViewById(R.id.textView3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Resort resort = resorts.get(position);
        holder.name.setText(resort.getName());
        holder.length.setText(Integer.toString(resort.getLength()) + " m");
        holder.running.setText(Integer.toString(resort.getRunning()) + "%");

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView length;
        TextView running;
    }

}