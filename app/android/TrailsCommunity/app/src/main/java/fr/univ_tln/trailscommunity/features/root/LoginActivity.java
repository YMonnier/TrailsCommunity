package fr.univ_tln.trailscommunity.features.root;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.utilities.validators.EmailValidator;

/**
 * A login screen that offers login via email/password.
 */

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewById(R.id.emailField)
    AutoCompleteTextView emailView;

    @ViewById(R.id.passwordField)
    EditText passwordView;

    @ViewById(R.id.login_progress)
    View progressView;



    /**
     * Checking password length.
     * @param textView action source
     * @param actionId password imActionId
     * @param keyEvent event
     */
    @EditorAction(R.id.passwordField)
    void onEditorActionsOnPasswordTextView(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
            attemptLogin();
        }
    }

    /**
     *
     */
    @Click(R.id.email_sign_in_button)
    void onClickOnSigninButton() {
        attemptLogin();
    }

    @Click(R.id.createAccount)
    void onClickOnSignupLink() {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            //emailView.setError(getString(R.string.error_field_required));
            updateErrorUi(passwordView, getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            //passwordView.setError(getString(R.string.error_invalid_password));
            updateErrorUi(passwordView, getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            //emailView.setError(getString(R.string.error_field_required));
            updateErrorUi(emailView, getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            updateErrorUi(emailView, getString(R.string.error_invalid_email));
            //emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
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
            userLoginTask(email, password);
            //authTask = new UserLoginTask(email, password);
            //authTask.execute((Void) null);
        }
    }

    /**
     * Update the current view by settings error
     * message to the input view.
     * @param view input view
     * @param error error message
     */
    @UiThread
    void updateErrorUi(EditText view, String error) {
        view.setError(error);
    }

    /**
     * Update clickable button depending on a status
     * @param status true if we want to
     *               disable all clickable buttons, otherwise, false
     */
    @UiThread
    void updateLockUi(boolean status) {
        passwordView.setEnabled(!status);
        emailView.setEnabled(!status);
    }

    /**
     * Check if the email is valid.
     * That is to say, check if the pattern is valid: mail@mail.com
     * @param email string email
     * @return true if valid, otherwise, false
     */
    private boolean isEmailValid(String email) {
        return EmailValidator.validate(email);
    }

    /**
     * Check the password length
     * @param password password for validation
     * @return true if password length >= 8, otherwise, false
     */
    private boolean isPasswordValid(String password) {
        boolean test = password.length() >= 8;
        Log.d("LoginActivity", "isPasswordValid:: " + test);
        return password.length() >= 8;
    }


    /**
     * Background task which allows
     * to send credential data to the server.
     * If the connexion is succeed, we go to the sessions list.
     * Otherwise, the user put wrong data and should try again...
     * @param email user email
     * @param password password email
     */
    @Background
    void userLoginTask(final String email, final String password) {
        updateLockUi(true);

        //Request...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Success request
        // Intent intent = new Intent(LoginActivity.this, SessionViewActivity.class);
        // startActivity(intent);
        // finish();

        updateLockUi(false);
        showProgress(false);
    }

    /**
     * Shows the progress UI and hides the login form.
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

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}

