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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        return item;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
