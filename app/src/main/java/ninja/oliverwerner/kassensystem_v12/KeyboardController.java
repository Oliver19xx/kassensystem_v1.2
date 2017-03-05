package ninja.oliverwerner.kassensystem_v12;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class KeyboardController {

    private Activity activity;
    private InputMethodManager keyboard;

    public KeyboardController(Activity activity) {
        this.activity = activity;
        keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void hideKeyboard() {
        View view = activity.getCurrentFocus();
        if (view != null) {

            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showKeyboard(View targetView) {
        targetView.requestFocus();
        View focus = activity.getCurrentFocus();
        if (focus != null) {
            keyboard.showSoftInput(targetView, 0);
        }
    }

}
