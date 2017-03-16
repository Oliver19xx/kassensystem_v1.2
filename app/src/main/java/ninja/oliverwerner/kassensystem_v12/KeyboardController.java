package ninja.oliverwerner.kassensystem_v12;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class KeyboardController {

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            try {
                InputMethodManager keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e){

            }
        }
    }

    public static void showKeyboard(Activity activity, View targetView) {
        targetView.requestFocus();
        View focus = activity.getCurrentFocus();
        if (focus != null) {
            InputMethodManager keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(targetView, 0);
        }
    }

}
