package fr.univ_tln.trailscommunity.features.root;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.utilities.Snack;
import fr.univ_tln.trailscommunity.utilities.loader.LoaderDialog;
import fr.univ_tln.trailscommunity.utilities.network.TCRestApi;
import fr.univ_tln.trailscommunity.utilities.validators.EmailValidator;

@EActivity(R.layout.root_signup_activity)
public class SignupActivity extends AppCompatActivity {

    /**
     * Tag used for Logger.
     */
    private static final String TAG = SignupActivity.class.getSimpleName();

    /**
     * Minimum password length
     */
    public static final int MIN_PASSWORD_LENGTH = 8;


    /**
     * Constant which correspond to POST nickname regisration parameter.
     */
    private static final String PARAMS_REGISTRATION_NICKNAME = "nickname";

    /**
     * Constant which correspond to POST email regisration parameter.
     */
    private static final String PARAMS_REGISTRATION_EMAIL = "email";

    /**
     * Constant which correspond to POST phone number regisration parameter.
     */
    private static final String PARAMS_REGISTRATION_PHONE_NUMBER = "phone_number";

    /**
     * Constant which correspond to POST password regisration parameter.
     */
    private static final String PARAMS_REGISTRATION_PASSWORD = "password";

    /**
     * Constant which correspond to POST password confirmation regisration parameter.
     */
    private static final String PARAMS_REGISTRATION_PASSWORD_CONFIRMATION = "password_confirmation";

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

    @ViewById(R.id.codeNumberCountrySpinner)
    Spinner codeNumberCountrySpinner;

    @ViewById(R.id.progress)
    View mProgressView;

    @ViewById(R.id.registerButton)
    Button registerButton;

    @StringArrayRes
    String[] indicatifNumbers;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.login)
    TextView login;

    @ViewById
    CoordinatorLayout coordinatorLayout;

    /**
     * Rest service to get or create
     * information from server.
     */
    @RestService
    TCRestApi tcRestApi;

    /**
     * Progress Dialog
     */
    private LoaderDialog progressView;


    @AfterViews
    void init() {
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/brush.ttf"));
        progressView = new LoaderDialog(this, getString(R.string.request_loader));
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
     * Login textView action
     */
    @Click(R.id.login)
    void onClickOnLoginTextView(){
        finish();
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
            updateErrorUi(confirmPasswordField, getString(R.string.error_not_matching_password));
            if (focusView == null)
                focusView = confirmPasswordField;
            cancel = true;
        } else if (!isPasswordValid(password, confirmPassword)) {
            updateErrorUi(confirmPasswordField, getString(R.string.error_not_matching_password));
            if (focusView == null)
                focusView = confirmPasswordField;
            cancel = true;
        }

        if (TextUtils.isEmpty(number)) {
            updateErrorUi(numberField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = numberField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt, focus on the first
            // form field with an error.
            assert focusView != null;
            if (focusView != null)
                focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            progressView.show();
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
    void updateErrorUi(final TextView view, final String error) {
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
        emailField.setEnabled(!status);
        nicknameField.setEnabled(!status);
        passwordField.setEnabled(!status);
        confirmPasswordField.setEnabled(!status);
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
    private boolean isEmailValid(final String email) {
        return EmailValidator.validate(email);
    }

    /**
     * Check the password length
     *
     * @param password password for validation
     * @return true if password length >= 8, otherwise, false
     */
    private boolean isPasswordValid(final String password) {
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    /**
     * Check if password and confirmation password match.
     *
     * @param password        password for validation
     * @param confirmPassword confirmation password for validation
     * @return true if password equals confirmPassword >= 8, otherwise, false
     */
    private boolean isPasswordValid(final String password, final String confirmPassword) {
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
    /**
     * Background task which allows
     * to send registration data to the server.

     * @param email user email
     * @param nickname user nickname
     * @param password user password
     * @param codeNumberCountry indicatif phone number
     * @param number phone number
     */
    @Background
    void signupTask(final String email,
                    final String nickname,
                    final String password,
                    final String codeNumberCountry,
                    final String number) {
        updateLockUi(true);

        String phoneNumber = codeNumberCountry + number;
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAMS_REGISTRATION_NICKNAME, nickname);
        parameters.put(PARAMS_REGISTRATION_EMAIL, email);
        parameters.put(PARAMS_REGISTRATION_PASSWORD, password);
        parameters.put(PARAMS_REGISTRATION_PHONE_NUMBER, phoneNumber);

        // Is already verified, see the private method `attemptSignup`
        parameters.put(PARAMS_REGISTRATION_PASSWORD_CONFIRMATION, password);


        try {
            ResponseEntity<JsonObject> userResponse = tcRestApi.registration(parameters);
            Log.d(TAG, userResponse.toString());
            updateLockUi(false);
            progressView.dismiss();
            finish();
        } catch (RestClientException e) {
            updateLockUi(false);
            progressView.dismiss();
            Log.e(TAG, e.getLocalizedMessage());
            Snack.showFailureMessage(coordinatorLayout, getString(R.string.error_request_4xx_5xx_status), Snackbar.LENGTH_LONG);
        }
    }
}
