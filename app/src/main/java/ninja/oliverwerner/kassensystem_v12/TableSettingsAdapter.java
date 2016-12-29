package ninja.oliverwerner.kassensystem_v12;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Oliver on 29.12.2016.
 */

public class TableSettingsAdapter extends BaseAdapter {

    private int layoutResourceId;
    private List<Table> items;
    private Context context;
    private View v;


    public TableSettingsAdapter(Context context, int resource, List<Table> objects) {
        this.items = objects;
        this.layoutResourceId = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("myMessage","TableSettingsAdapter - getView() - "+position+"/"+getCount());

        v = convertView;

        if(v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(layoutResourceId, null);
        }

        Table item = getItem(position);

        if (item != null) {
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            TextView tableName = (TextView) v.findViewById(R.id.tableName);
            Button apply_button = (Button) v.findViewById(R.id.apply_button);

            if (checkBox != null) {
                if (item.getTableState() == 1) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            }

            if (tableName != null) {
                tableName.setText(item.getTableName());
            }

            if (apply_button != null) {
                apply_button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                    }
                });
            }
        }
        return v;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public Table getItem(int position) {
        return getItem(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
