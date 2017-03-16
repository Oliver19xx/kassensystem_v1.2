package ninja.oliverwerner.kassensystem_v12;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static ninja.oliverwerner.kassensystem_v12.R.styleable.CoordinatorLayout;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private final Context CONTEXT = this;
    private Snackbar snackbar;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mServerView;
    private View mProgressView;
    private View mLoginFormView;

    // Für die Installation einer Verknüpfung
    private static final String FILENAME = "KS v1.2";
    SharedPreferences sharedPrefs;
    boolean isAppInstalled = false;

    // Login Daten Speichern
    private static final String VAL_IP = "SERVERIP";
    private static final String VAL_EMAIL = "EMAIL";
    private static final String VAL_CHECK = "CHECK";
    private String server;
    private String email;
    private String password;
    private String cbState;
    private CheckBox cbSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //todo Short-Cut erstellen (läuft noch nicht)
//        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        isAppInstalled = sharedPrefs.getBoolean("isAppInstalled", false);
//        if (isAppInstalled == false) {
//            Intent shortcutIntent = new Intent(getApplicationContext(), LoginActivity.class);
//            shortcutIntent.setAction(Intent.ACTION_MAIN);
//            Intent intent = new Intent();
//            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "KS v1.2");
//            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));
//            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//            getApplicationContext().sendBroadcast(intent);
//            SharedPreferences.Editor editor = sharedPrefs.edit();
//            editor.putBoolean("isAppInstalled", true);
//            editor.commit();
//
//        }
        sharedPrefs = getSharedPreferences(FILENAME, 0);

        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mServerView = (EditText) findViewById(R.id.server);

        Button mEmailSignInButton = (Button) findViewById(R.id.login_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean check_t = true;


//        mEmailView = (EditText) findViewById(R.id.email);
//        mPasswordView = (EditText) findViewById(R.id.etpassword);
//        mServerView = (EditText) findViewById(R.id.etserver);
        sharedPrefs = getSharedPreferences(FILENAME, 0);
        cbSaveData = (CheckBox) findViewById(R.id.checkBox);
        cbState = sharedPrefs.getString("CHECK", null);
        server = sharedPrefs.getString("SERVERIP", null);
        String vv_c = sharedPrefs.getString(VAL_CHECK, "");
        if (server == null) {
            mServerView.setText("");
            mServerView.requestFocus();
        } else {
            mServerView.setText(sharedPrefs.getString(VAL_IP, ""));
            mEmailView.requestFocus();
            if (cbState == null) {
                mEmailView.setText("");
                mPasswordView.setText("");
                mEmailView.requestFocus();
            } else {
                if (vv_c.equals("True")) {
                    mPasswordView.requestFocus();
                    cbSaveData.setChecked(check_t);
                    mEmailView.setText(sharedPrefs.getString(VAL_EMAIL, ""));
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Username und Server speichern
        String server = mServerView.getText().toString().trim();
        String email = mEmailView.getText().toString().trim();

        sharedPrefs = getSharedPreferences(FILENAME, 0);
        SharedPreferences sharedPrefs = getSharedPreferences(FILENAME, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        cbSaveData = (CheckBox) findViewById(R.id.checkBox);
        if (cbSaveData.isChecked() == true) {
            cbState = "True";
        } else {
            cbState = "False";
            mEmailView.setText("");
        }
        editor.putString(VAL_IP, server).commit();
        editor.putString(VAL_EMAIL, email).commit();
        editor.putString(VAL_CHECK, cbState).commit();
        editor.commit();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Focus von dem Textfeld nehmen
        KeyboardController.hideKeyboard(LoginActivity.this);

        // Reset errors.
        mServerView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        server = mServerView.getText().toString().trim();
        email = mEmailView.getText().toString().trim();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid server adress
        if (TextUtils.isEmpty(server)) {
            mServerView.setError(getString(R.string.error_field_required));
            focusView = mServerView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            loginRequest();
        }
    }

    private void loginRequest() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Rückgabe", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonObject = jsonResponse.getJSONObject("data");
                    String return_message = jsonObject.getString("return_message");
                    Log.d("return_message", return_message);
                    if (return_message.equals("success")){
                        Log.d("Login:", "login_success");
                        Snackbar.make(findViewById(android.R.id.content), "Login erfolgreich!", Snackbar.LENGTH_INDEFINITE)
                                .setDuration(8000)
                                .show();
                        Intent intent = new Intent(CONTEXT,MainActivity.class);
                        startActivity(intent);
                    } else {
                        OnClickListener mOnClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                KeyboardController.showKeyboard(LoginActivity.this,mPasswordView);
                            }
                        };
                        showProgress(false);
                        mPasswordView.setText("");
                        Snackbar.make(findViewById(android.R.id.content), "Login-Daten prüfen!", Snackbar.LENGTH_INDEFINITE)
                                .setDuration(8000)
                                .setAction("OK", mOnClickListener)
                                .setActionTextColor(Color.GREEN)
                                .show();
                    }
                } catch (JSONException e) {
                    e.getStackTrace();
                }
            }

        };
        LoginRequest loginRequest = new LoginRequest(email, password, server, responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(loginRequest);
    }

    private boolean isEmailValid(String email) {
        // Prüfe die eingegebene E-Mail-Adresse mittels regex
        return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public void onBackPressed() {
        Snackbar.make(mEmailView, R.string.please_login, Snackbar.LENGTH_INDEFINITE);
    }
}

