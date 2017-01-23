package ninja.oliverwerner.kassensystem_v12;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class TableGridAdapter extends BaseAdapter {
    // test commit
    // TODO: 29.12.2016 Items werden beim hin und her scrollen nicht korekt wieder aufgebaut
    private Context context = null;
    private int resource = 0;
    private ArrayList<Table> arrayList = null;

    public TableGridAdapter(Context context, int resource, ArrayList<Table> arrayList) {
        Log.d("myMessage", "TableGridAdapter");
        this.context = context;
        this.arrayList = arrayList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        Log.d("myMessage", "TableGridAdapter - getView()");

        View v;
        Button button;


        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(resource, parent, false);
        } else {
            v = convertView;
        }
        try {
            button = (Button) v.findViewById(R.id.grid_button);
            button.setText(getItem(position).getTableName());
            switch (getItem(position).getTableState()) {
                case 0:
                    // Der Tisch ist frei
                    button.setBackgroundColor(ContextCompat.getColor(context, R.color.tableFree));
                    break;
                case 1:
                    // Der Tisch ist reserviert
                    button.setBackgroundColor(ContextCompat.getColor(context, R.color.tableReserved));
                    break;
                case 2:
                    // Der Tisch ist besetzt
                    button.setBackgroundColor(ContextCompat.getColor(context, R.color.tableTaken));
                    break;
                default:
            }


            // Beim Klick auf den Button wird die ID des Tisches an das PHP-Interface übergeben
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(getItem(position).getTableId());
                    String tableID = sb.toString();

                    // Übergabe-Daten sammeln
                    Bundle bundle = new Bundle();
                    bundle.putString("method", "getTable");
                    bundle.putString("tableID", tableID);

                    // TableActivity starten
                    Intent intent = new Intent(view.getContext(), TableActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.getStackTrace();
        }
        return v;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Nullable
    @Override
    public Table getItem(int position) {
        return this.arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
