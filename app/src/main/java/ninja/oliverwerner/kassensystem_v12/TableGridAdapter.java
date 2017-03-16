package ninja.oliverwerner.kassensystem_v12;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class TableGridAdapter extends BaseAdapter {
    // TODO: 29.12.2016 Items werden beim hin und her scrollen nicht korekt wieder aufgebaut
    private Context context = null;
    private int resource = 0;
    private ArrayList<Table> arrayList = null;
    public AlertDialog.Builder builder;

    public TableGridAdapter(Context context, int resource, ArrayList<Table> arrayList) {
        Log.d("myMessage", "TableGridAdapter");
        this.context = context;
        this.arrayList = arrayList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

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
            yes_no_dialog(position,getItem(position).getTableId(),getItem(position).getTableState(), button);

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
            button.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {
                    yes_no_dialog(position,getItem(position).getTableId(),getItem(position).getTableState(), v);
                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
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

    public void yes_no_dialog(final int ipos, final int tableid, final int statues, final View button){
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Reservieren");
        if(statues == 0) {
            builder.setMessage("Tisch reservieren?");
        }else if(statues == 1){
            builder.setMessage("Reservierung Stonieren");
        }else{
            builder.setMessage("Belegter Tisch kann nicht reserviert werden!!!");
        }

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                int status;
                if(statues == 0) {
                    status = 1;
                    button.setBackgroundColor(ContextCompat.getColor(context, R.color.tableReserved));
                }else if(statues == 1){
                    status = 0;
                    button.setBackgroundColor(ContextCompat.getColor(context, R.color.tableFree));
                }else{
                    status = 3;
                }
                if (status != 3){
                    getItem(ipos).setTableState(status);
                    try {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("method", "toggleReserve");
                        hashMap.put("tableID", tableid + "".toString());
                        new ActivityDataSource(hashMap).execute().get();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
    }
}
