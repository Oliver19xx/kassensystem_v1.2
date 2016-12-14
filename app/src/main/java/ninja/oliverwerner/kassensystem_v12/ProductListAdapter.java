package ninja.oliverwerner.kassensystem_v12;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Oliver on 12.12.2016.
 */

public class ProductListAdapter extends ArrayAdapter<Product> {
    private List<Product> items;
    private int layoutResourceId;
    private Context context;

    public ProductListAdapter(Context context, int layoutResourceId, List<Product> items){
        super(context, layoutResourceId,  items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProductHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId,parent,false);

        holder = new ProductHolder();
        holder.productList = items.get(position);
        holder.numberButton = (Button) row.findViewById(R.id.number_Button);
        holder.numberButton.setTag(holder.productList);

        holder.name = (TextView)row.findViewById(R.id.product_name);
        holder.price = (TextView)row.findViewById(R.id.product_price);

        row.setTag(holder);

        setupItem(holder);
        return row;

    }

    private void setupItem(ProductHolder holder){
        holder.name.setText(holder.productList.getName());
        holder.price.setText(String.valueOf(holder.productList.getPrice())+"€");
        holder.numberButton.setText(String.valueOf(holder.productList.getNumber()));
    }

    public static class ProductHolder{
        Product productList;
        TextView name;
        TextView price;
        Button numberButton;

    }
}
