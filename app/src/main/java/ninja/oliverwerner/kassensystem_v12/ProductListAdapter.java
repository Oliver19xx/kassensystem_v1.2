package ninja.oliverwerner.kassensystem_v12;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
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
        View v = convertView;

        if(v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(layoutResourceId, null);
        }

        Product item = getItem(position);

        if (item != null){
            TextView product_name = (TextView) v.findViewById(R.id.product_name);
            TextView product_price = (TextView) v.findViewById(R.id.product_price);
            TextView number_Button = (TextView) v.findViewById(R.id.number_Button);

            if (product_name != null){
                product_name.setText(item.getName());
            }

            if (product_price != null){
                product_name.setText(""+item.getPrice());
            }

            if (number_Button != null){
                product_name.setText(item.getNumber());
            }
        }

        return v;

    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }
}
