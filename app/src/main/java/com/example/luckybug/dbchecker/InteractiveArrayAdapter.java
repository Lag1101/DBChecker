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
        protected Spinner spinner;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.gooditem, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.label);
            viewHolder.spinner = (Spinner) view.findViewById(R.id.spinner);

            viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    // An item was selected. You can retrieve the selected item using
                    // parent.getItemAtPosition(pos)
                    GoodModel element = (GoodModel) viewHolder.spinner.getTag();
                    String strChoose = viewHolder.spinner.getSelectedItem().toString();
                    Toast.makeText(getContext(), strChoose, Toast.LENGTH_SHORT).show();
                    element.setSelected(pos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });
            view.setTag(viewHolder);
            viewHolder.spinner.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).spinner.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getDescription());
        holder.spinner.setSelection(list.get(position).isSelected());
        return view;
    }
}