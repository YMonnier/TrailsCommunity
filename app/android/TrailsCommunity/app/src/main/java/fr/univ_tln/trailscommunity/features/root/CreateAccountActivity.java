package fr.univ_tln.trailscommunity.features.root;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.univ_tln.trailscommunity.utils.CustomRequest;

import fr.univ_tln.trailscommunity.R;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailField;
    private EditText nicknameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText numberField;

    private Spinner codeNumberCountrySpinner;

    private TextView codeNumberCountryError;

    private View mProgressView;

    private Button registerButton;

    private CreateUserAccountTask createUserAccountTask;

    private static final String url = "http://";
    private RequestQueue queue;
    private CustomRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        init();

        //Delete after test
        addCodeNumberInSpinner();
    }

    private void init() {
        emailField = (EditText) findViewById(R.id.emailField);
        nicknameField = (EditText) findViewById(R.id.nicknameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        confirmPasswordField = (EditText) findViewById(R.id.confirmPasswordField);
        numberField = (EditText) findViewById(R.id.numberField);
        registerButton = (Button) findViewById(R.id.registerButton);

        codeNumberCountrySpinner = (Spinner) findViewById(R.id.codeNumberCountrySpinner);

        codeNumberCountryError = (TextView) findViewById(R.id.numberCodeCountryError);

        mProgressView = (ProgressBar) findViewById(R.id.progress);

        registerButton.setOnClickListener(this);
    }

    /**
      * Delete after test
      */
    private void addCodeNumberInSpinner(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        arrayAdapter.add("+33");
        arrayAdapter.add("+34");
        arrayAdapter.add("+35");
        this.codeNumberCountrySpinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        createCreateAccountTask();
    }

    public void createCreateAccountTask() {
        if (createUserAccountTask != null) {
            return;
        }

        resetErrors();

        String email = emailField.getText().toString();
        String nickname = nicknameField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();
        String codeNumberCountry = codeNumberCountrySpinner.getSelectedItem().toString();
        String number = numberField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(email)){
            emailField.setError(getString(R.string.error_field_required));
            focusView = emailField;
            cancel = true;
        }
        else if(!isEmailValid(email)){
            emailField.setError(getString(R.string.error_invalid_email));
            focusView = emailField;
            cancel = true;
        }

        if(TextUtils.isEmpty(nickname)){
            nicknameField.setError(getString(R.string.error_field_required));
            focusView = nicknameField;
            cancel = true;
        }

        if(TextUtils.isEmpty(password)){
            passwordField.setError(getString(R.string.error_field_required));
            focusView = passwordField;
            cancel = true;
        }
        else if(!isPasswordValid(password, confirmPassword)){
            passwordField.setError(getString(R.string.error_correspon_password));
            focusView = passwordField;
            cancel = true;
        }

        if(TextUtils.isEmpty(codeNumberCountry)){
            codeNumberCountryError.setError(getString(R.string.error_invalid_code_number_country));
            cancel = true;
        }

        if(TextUtils.isEmpty(number)){
            numberField.setError(getString(R.string.error_field_required));
            focusView = numberField;
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
            createUserAccountTask = new CreateUserAccountTask(email, nickname, password, codeNumberCountry, number);
            createUserAccountTask.execute((Void) null);
        }
    }

    private void resetErrors() {
        // Reset errors.
        emailField.setError(null);
        nicknameField.setError(null);
        passwordField.setError(null);
        numberField.setError(null);
        codeNumberCountryError.setError(null);
    }

    private boolean isPasswordValid(String password, String confirmPassword) {
        return password.length() >= 8 && password.equals(confirmPassword);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
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
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class CreateUserAccountTask extends AsyncTask<Void, Void, Boolean> implements Response.ErrorListener, Response.Listener<JSONObject> {

        private final String mEmail;
        private final String mNickname;
        private final String mPassword;
        private final String mCodeNumberCountry;
        private final String mNumber;

        public CreateUserAccountTask(String email, String nickname, String password, String codeNumberCountry, String number) {
            this.mEmail = email;
            this.mNickname = nickname;
            this.mPassword = password;
            this.mCodeNumberCountry = codeNumberCountry;
            this.mNumber = number;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            Map<String, String> parameters = new HashMap<>();

            queue = Volley.newRequestQueue(CreateAccountActivity.this);
            request = new CustomRequest(Request.Method.POST, url, parameters, this, this);
            queue.add(request);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            createUserAccountTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            createUserAccountTask = null;
            showProgress(false);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ERROR", error.toString());
        }

        @Override
        public void onResponse(JSONObject response) {
            Log.e("RESPONSE", response.toString());
        }
    }
}
