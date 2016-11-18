package fr.univ_tln.trailscommunity.features.root;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.json.JSONObject;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.utilities.validators.EmailValidator;
import fr.univ_tln.trailscommunity.utils.CustomRequest;

@EActivity(R.layout.activity_signup)
public class SignupActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    /**
     * Minimum password length
     */
    public static final int MIN_PASSWORD_LENGTH = 8;

    @ViewById(R.id.emailField)
    EditText emailField;

    @ViewById(R.id.nicknameField)
    EditText nicknameField;

    @ViewById(R.id.passwordField)
    EditText passwordField;

    @ViewById(R.id.confirmPasswordField)
    EditText confirmPasswordField;

    @ViewById(R.id.numberField)
    EditText numberField;

    @ViewById//(R.id.codeNumberCountrySpinner)
            Spinner codeNumberCountrySpinner;

    @ViewById(R.id.numberCodeCountryError)
    TextView codeNumberCountryError;

    @ViewById(R.id.progress)
    View mProgressView;

    @ViewById(R.id.registerButton)
    Button registerButton;

    private RequestQueue queue;

    private CustomRequest request;

    @StringArrayRes
    String[] indicatifNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addCodeNumberInSpinner();
    }

    /**
     * Delete after test
     */
    private void addCodeNumberInSpinner() {
        SpinnerAdapter codeNumberCountryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, indicatifNumbers);
        System.out.println(codeNumberCountryAdapter);
        System.out.println(codeNumberCountrySpinner);
        this.codeNumberCountrySpinner.setAdapter(codeNumberCountryAdapter);
    }

    /**
     * Check all input data when user
     * press on next button edit text.
     *
     * @param textView action source
     * @param actionId type of action(IME_ID)
     * @param keyEvent event
     */
    @EditorAction({R.id.nicknameField,
            R.id.emailField,
            R.id.passwordField,
            R.id.confirmPasswordField,
            R.id.numberField})
    void onNextActionsEditText(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == R.id.login || actionId == EditorInfo.IME_ACTION_NEXT) {
            attemptSignup();
        }
    }

    /**
     * Sign up button action.
     */
    @Click(R.id.registerButton)
    void onClickOnRegisterButton() {
        attemptSignup();
    }


    /**
     * Attempts to register a account.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual signup attempt is made.
     */
    private void attemptSignup() {
        updateResetErrorUi();

        String email = emailField.getText().toString();
        String nickname = nicknameField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        Object selectedItem = codeNumberCountrySpinner.getSelectedItem();
        assert selectedItem != null;
        String codeNumberCountry = null;
        if (selectedItem != null) {
            codeNumberCountry = selectedItem.toString();
        }
        String number = numberField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            updateErrorUi(emailField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = emailField;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailField.setError(getString(R.string.error_invalid_email));
            if (focusView == null)
                focusView = emailField;
            cancel = true;
        }
        if (TextUtils.isEmpty(nickname)) {
            updateErrorUi(nicknameField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = nicknameField;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            updateErrorUi(passwordField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = passwordField;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            updateErrorUi(passwordField, getString(R.string.error_not_matching_password));
            if (focusView == null)
                focusView = passwordField;
            cancel = true;
        } else if (!isPasswordValid(password, confirmPassword)) {
            updateErrorUi(confirmPasswordField, getString(R.string.error_not_matching_password));
            if (focusView == null)
                focusView = passwordField;
            cancel = true;
        }

        if (codeNumberCountry != null) {
            if (TextUtils.isEmpty(codeNumberCountry)) {
                updateErrorUi(codeNumberCountryError, getString(R.string.error_invalid_code_number_country));
                cancel = true;
            }
        }

        if (TextUtils.isEmpty(number)) {
            updateErrorUi(numberField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = numberField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            assert focusView != null;
            if (focusView != null)
                focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            signupTask(email, nickname, password, codeNumberCountry, number);
        }
    }

    /**
     * Reset all error input views.
     */
    @UiThread
    void updateResetErrorUi() {
        // Reset errors.
        emailField.setError(null);
        nicknameField.setError(null);
        passwordField.setError(null);
        numberField.setError(null);
        codeNumberCountryError.setError(null);
    }

    /**
     * Update the current view by settings error
     * message to the input view.
     *
     * @param view  EditText view
     * @param error error message
     */
    @UiThread
    void updateErrorUi(EditText view, String error) {
        view.setError(error);
    }

    /**
     * Update the current view by settings error
     * message to the text view.
     *
     * @param view  TextView view
     * @param error error message
     */
    @UiThread
    void updateErrorUi(TextView view, String error) {
        view.setError(error);
    }

    /**
     * Update clickable button depending on a status
     *
     * @param status true if we want to
     *               disable all clickable buttons, otherwise, false
     */
    @UiThread
    void updateLockUi(boolean status) {
        nicknameField.setEnabled(!status);
        passwordField.setEnabled(!status);
        confirmPasswordField.setEnabled(!status);
        codeNumberCountryError.setEnabled(!status);
        codeNumberCountrySpinner.setEnabled(!status);
        numberField.setEnabled(!status);
    }

    /**
     * Check if the email is valid.
     * That is to say, check if the pattern is valid: mail@mail.com
     *
     * @param email string email
     * @return true if valid, otherwise, false
     */
    private boolean isEmailValid(String email) {
        return EmailValidator.validate(email);
    }

    /**
     * Check the password length
     *
     * @param password password for validation
     * @return true if password length >= 8, otherwise, false
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    /**
     * Check if password and confirmation password match.
     *
     * @param password        password for validation
     * @param confirmPassword confirmation password for validation
     * @return true if password equals confirmPassword >= 8, otherwise, false
     */
    private boolean isPasswordValid(String password, String confirmPassword) {
        return password.length() >= MIN_PASSWORD_LENGTH && password.equals(confirmPassword);
    }

    /**
     * Shows the progress UI and hides the login form.
     *
     * @param show progress status, true to set visible progress,
     *             false to set unvisible progress
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @UiThread
    void showProgress(final boolean show) {
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
     * Background task which allows
     * to send credential data to the server.
     * If the connexion is succeed, we go to the sessions list.
     * Otherwise, the user put wrong data and should try again...
     *
     * @param email    user email
     * @param password password email
     */
    @Background
    void signupTask(final String email,
                    final String nickname,
                    final String password,
                    final String codeNumberCountry,
                    final String number) {
        updateLockUi(true);

        //Request...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        Map<String, String> parameters = new HashMap<>();

        queue = Volley.newRequestQueue(SignupActivity.this);
        request = new CustomRequest(Request.Method.POST, url, parameters, this, this);
        queue.add(request);
        */
        // Success request
        // Intent intent = new Intent(LoginActivity.this, SessionViewActivity.class);
        // startActivity(intent);
        // finish();

        updateLockUi(false);
        showProgress(false);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        // Intent intent = new Intent(this, LoginActivity_.class);
        // startActivity(intent);
        Log.e("RESPONSE", response.toString());
    }
}
