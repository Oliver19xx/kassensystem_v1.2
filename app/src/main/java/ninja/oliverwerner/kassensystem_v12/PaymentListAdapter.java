package ninja.oliverwerner.kassensystem_v12;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Marcel on 15.01.2017.
 */

public class PaymentListAdapter extends ArrayAdapter<Product> {
    private List<Product> items;
    private int layoutResourceId;
    private Context context;
    private View v;
    public AlertDialog.Builder builder;


    public PaymentListAdapter(Context context, int layoutResourceId, List<Product> items){
        super(context, layoutResourceId,  items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        v = convertView;

        if(v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(layoutResourceId, null);
        }

        Product item = getItem(position);

        if (item != null){
            TextView product_name = (TextView) v.findViewById(R.id.product_name_pay);
            TextView product_price = (TextView) v.findViewById(R.id.product_price_pay);
            TextView product_value = (TextView) v.findViewById(R.id.product_value);
            Button number_Button = (Button) v.findViewById(R.id.number_Button_pay);
            if (product_name != null){
                product_name.setText(item.getName());
            }

            if (product_price != null){
                product_price.setText(""+item.getPrice());
            }
            if (product_value != null){
                product_value.setText(""+item.getNumber());
            }
            if (number_Button != null){
                number_Button.setText(""+item.getValue());
            }
            number_Button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    int productid = getItem(position).getProductID();
                    String name = getItem(position).getName() + "".toString();
                    String sPri = getItem(position).getPrice() + "".toString();
                    String sBez = getItem(position).getNumber() + "".toString();
                    int tableid = getItem(position).getTableId();
                    int value = getItem(position).getValue();
                    int bez = Integer.parseInt(sBez.toString());
                    double dPri = Double.parseDouble(sPri.toString());
                    if (PaymentActivity.plus_minus.isChecked()) {
                        if (value > 0) {
                            value--;
                            remove(getItem(position));
                            insert(new Product(productid, name, dPri, bez, tableid,value), position);
                            double dCost = Double.parseDouble(PaymentActivity.sum_Price.getText().toString());
                            dCost = dCost - dPri;
                            dCost = Math.round(dCost * 100);
                            dCost = dCost / 100;
                            PaymentActivity.sum_Price.setText(Double.toString(dCost));

                        }
                    } else if (value < bez){
                        value++;
                        remove(getItem(position));
                        insert(new Product(productid, name, dPri, bez, tableid,value), position);
                        double dCost = Double.parseDouble(PaymentActivity.sum_Price.getText().toString());
                        dCost = dCost + dPri;
                        dCost = Math.round(dCost * 100);
                        dCost = dCost / 100;
                        PaymentActivity.sum_Price.setText(Double.toString(dCost));
                    }
                }
            });
        }
        return v;
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }
}