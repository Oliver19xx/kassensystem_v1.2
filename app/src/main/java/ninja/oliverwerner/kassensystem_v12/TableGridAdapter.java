package ninja.oliverwerner.kassensystem_v12;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Oliver on 02.12.2016.
 */

public class TableGridAdapter extends ArrayAdapter<Table> {

    private Context context;
    private ArrayList<Table> arrayList;
    private Button button;

    public TableGridAdapter(Context context, int resource, ArrayList<Table> arrayList) {
        super(context, resource, arrayList);
        Log.d("myMessage","TableGridAdapter");
//        Log.d("myMessage","tableList="+arrayList.get(0).getTableName());
//        Log.d("myMessage","tableList="+arrayList.get(1).getTableName());
//        Log.d("myMessage","tableList="+arrayList.get(2).getTableName());
        this.context = context;
//        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("myMessage","TableGridAdapter - getView()");
        // TODO: 07.12.2016 Wieso wird die Methode getView() nicht ausgeführt? 

        final Table table = getItem(position);

        if (convertView == null) {
            button = new Button(parent.getContext());
            String btnText = table.getTableName();
            Log.d("myMessage", btnText + " - " + position);
            button.setText(btnText);
        } else {
            button = (Button) convertView;
        }

        button.setId(position);

        // Beim Klick auf den Button wird die ID des Tisches an das PHP-Interface übergeben
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(view.getContext(), table.getTableId() + " => " + table.getTableName(), Toast.LENGTH_SHORT).show();

                // Übergabe-Daten sammeln
                Bundle bundle = new Bundle();
                bundle.putString("method", "getTable");
                bundle.putInt("tableID", table.getTableId());
//                Log.d("myMessage","TableGridAdapter - table.getTableId()="+table.getTableId());

                // TableActivity starten
//                Intent intent = new Intent(view.getContext(), TableActivity.class);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
            }
        });
        return button;
    }

}
