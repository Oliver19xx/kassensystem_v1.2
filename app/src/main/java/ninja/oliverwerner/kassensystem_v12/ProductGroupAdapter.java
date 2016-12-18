package ninja.oliverwerner.kassensystem_v12;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Oliver on 14.12.2016.
 */

public class ProductGroupAdapter extends BaseAdapter {

    private Context context = null;
    private int resource = 0;
    private ArrayList<ProductGroup> arrayList = null;

    public ProductGroupAdapter(Context context, int resource, ArrayList<ProductGroup> arrayList) {
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("myMessage","ProductGroupAdapter - getView()");

        View item;
        Button button = new Button(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            item = new View(context);
            item = inflater.inflate(this.resource,null);
            button = (Button) item.findViewById(R.id.grid_button);
            ProductGroup productGroup = (ProductGroup)getItem(position);
            button.setText(productGroup.getpGroupName());
        } else {
            item = convertView;
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(getItem(position).getpGroupId());
                String productGroupId = sb.toString();

                String productGroupName = getItem(position).getpGroupName();

                // Übergabe-Daten sammeln
                Bundle bundle = new Bundle();
                bundle.putString("method", "getProducts");
                bundle.putString("pGroupID", productGroupId);
                bundle.putString("productGroupName", productGroupName);

                // TableActivity starten
                Intent intent = new Intent(view.getContext(), ProductsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return item;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public ProductGroup getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
