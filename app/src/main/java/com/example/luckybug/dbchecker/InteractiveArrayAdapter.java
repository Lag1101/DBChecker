package com.example.luckybug.dbchecker;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InteractiveArrayAdapter extends ArrayAdapter<GoodModel> {

    private final List<GoodModel> list;
    private final Activity context;

    public InteractiveArrayAdapter(Activity context, List<GoodModel> list) {
        super(context, R.layout.gooditem, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected TextView check;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.gooditem, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.label);
            viewHolder.check = (TextView) view.findViewById(R.id.check);

            view.setTag(viewHolder);
            viewHolder.check.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).check.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getDescription());
        holder.check.setText(list.get(position).getCheck());
        return view;
    }
}