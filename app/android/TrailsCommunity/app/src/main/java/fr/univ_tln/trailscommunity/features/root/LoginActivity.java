package fr.univ_tln.trailscommunity.features.root;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.Settings;
import fr.univ_tln.trailscommunity.features.sessions.SessionsActivity_;
import fr.univ_tln.trailscommunity.models.User;
import fr.univ_tln.trailscommunity.utilities.Snack;
import fr.univ_tln.trailscommunity.utilities.loader.LoaderDialog;
import fr.univ_tln.trailscommunity.utilities.network.TCRestApi;
import fr.univ_tln.trailscommunity.utilities.validators.EmailValidator;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * A login screen that offers login via email/password.
 */
@EActivity(R.layout.root_login_activity)
public class LoginActivity extends AppCompatActivity {

    /**
     * Tag used for Logger.
     */
    private static final String TAG = LoginActivity.class.getSimpleName();

    /**
     * Minimum password length
     */
    public static final int MIN_PASSWORD_LENGTH = 8;

    /**
     * Constant which correspond to POST email Auth parameter.
     */
    private static final String PARAMS_AUTH_EMAIL = "email";

    /**
     * Constant which correspond to POST password Auth parameter.
     */
    private static final String PARAMS_AUTH_PASSWORD = "password";

    /**
     * Constant which correspond to POST Auth parameter.
     */
    private static final String PARAMS_AUTH_AUTH = "auth";

    /**
     * Input email used to authenticate the user.
     */
    @ViewById(R.id.emailField)
    AutoCompleteTextView emailView;

    /**
     * Input password used to authenticate the user.
     */
    @ViewById(R.id.passwordField)
    EditText passwordView;

    /**
     * Button action to login to the API.
     */
    @ViewById(R.id.email_sign_in_button)
    Button loginButton;

    @ViewById
    CoordinatorLayout coordinatorLayout;

    /**
     * Rest service to get
     * information from server.
     */
    @RestService
    TCRestApi tcRestApi;

    /**
     * Progress Dialog
     */
    private LoaderDialog progressView;

    @ViewById(R.id.title)
    TextView title;


    @AfterViews
    void init() {
        setTitle(R.string.title_login_activity);
        //emailView.setText("ysee@mail.com");
        //passwordView.setText("testtest");

        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/brush.ttf"));

        progressView = new LoaderDialog(this, getString(R.string.authenticating));

        //
        // Init global realm settings
        //
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    /**
     * Check all input data when user
     * press on next button edit text.
     *
     * @param textView action source
     * @param actionId type of action(IME_ID)
     * @param keyEvent event
     */
    @EditorAction({R.id.passwordField, R.id.emailField})
    void onNextActionsEditText(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == R.id.login || actionId == EditorInfo.IME_ACTION_NEXT) {
            attemptLogin();
        }
    }

    /**
     * Sign in button action.
     */
    @Click(R.id.email_sign_in_button)
    void onClickOnSigninButton() {
        attemptLogin();
    }

    /**
     * Sign up view action
     */
    @Click(R.id.createAccount)
    void onClickOnSignupLink() {
        startActivity(new Intent(this, SignupActivity_.class));
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
            updateErrorUi(passwordView, getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            updateErrorUi(passwordView, getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            updateErrorUi(emailView, getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            updateErrorUi(emailView, getString(R.string.error_invalid_email));
            focusView = emailView;
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

            progressView.show();

            userLoginTask(email, password);
        }
    }

    /**
     * Update the current view by settings error
     * message to the input view.
     *
     * @param view  input view
     * @param error error message
     */
    @UiThread
    void updateErrorUi(final EditText view, final String error) {
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
        passwordView.setEnabled(!status);
        emailView.setEnabled(!status);
        loginButton.setEnabled(!status);
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
     * Background task which allows
     * to send credential data to the server.
     * If the connexion is succeed, we go to the sessions list.
     * Otherwise, the user put wrong data and should try again...
     *
     * @param email    user email
     * @param password password email
     */
    @Background
    void userLoginTask(final String email, final String password) {
        updateLockUi(true);

        Map<String, Object> auth = new HashMap<>();
        Map<String, String> sub = new HashMap<>();

        sub.put(PARAMS_AUTH_EMAIL, email);
        sub.put(PARAMS_AUTH_PASSWORD, password);
        auth.put(PARAMS_AUTH_AUTH, sub);

        try {
            ResponseEntity<JsonObject> responseLogin = tcRestApi.login(auth);
            System.out.println(responseLogin);
            Log.d(TAG, "response login: " + responseLogin);

            if (responseLogin == null)
                throw new AssertionError("response login should not be null");

            if (responseLogin != null) {
                if (responseLogin.getStatusCode().is2xxSuccessful()) {
                    JsonElement je = responseLogin.getBody();

                    // Login, get auth token
                    String token = je.getAsJsonObject().get("jwt").getAsString();
                    Settings.TOKEN_AUTHORIZATION = token;
                    Log.d(TAG, "token: " + token);

                    // Get current user information
                    tcRestApi.setHeader(Settings.AUTHORIZATION_HEADER_NAME, token);
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    Log.d(TAG, "FCM TEST Token: " + token);
                    ResponseEntity<JsonObject> responseUser = tcRestApi.user(deviceToken);
                    if (responseUser == null)
                        throw new AssertionError("response user should not be null");

                    if (responseUser != null) {
                        Log.d(TAG, "response user: " + responseUser);
                        saveUser(responseUser.getBody().getAsJsonObject());

                        updateLockUi(false);
                        progressView.dismiss();
                        startActivity(new Intent(this, SessionsActivity_.class));
                    }
                } else {
                    Snack.showFailureMessage(coordinatorLayout, getString(R.string.error_request_4xx_5xx_status), Snackbar.LENGTH_LONG);
                }
            } else {
                Snack.showFailureMessage(coordinatorLayout, getString(R.string.error_request_4xx_5xx_status), Snackbar.LENGTH_LONG);
            }
            updateLockUi(false);
            progressView.dismiss();
        } catch (RestClientException e) {
            Log.d(TAG, "error HTTP request from userLoginTask: " + e.getLocalizedMessage());
            Snack.showFailureMessage(coordinatorLayout, getString(R.string.error_request_4xx_5xx_status), Snackbar.LENGTH_LONG);
            updateLockUi(false);
            progressView.dismiss();
        }
    }

    /**
     * Save all information about the
     * current user authenticated into Realm database.
     *
     * @param userJsonObject json from /users/me route.
     */
    private void saveUser(final JsonObject userJsonObject) {
        JsonObject data = userJsonObject.getAsJsonObject("data");
        Log.d(TAG, "data: " + data);
        int userId = data.get("id").getAsInt();
        Settings.userId = userId;

        // Create Realm instance
        /*
      Realm database instance.
     */
        Realm realm = Realm.getDefaultInstance();
        if (realm != null) {
            realm.beginTransaction();

            // Disable activation user
            RealmResults<User> users = realm.where(User.class).findAll();
            if (users != null) {
                for (User u : users) {
                    u.setActive(false);
                }
            }

            User userExist = realm.where(User.class).equalTo("id", userId).findFirst();
            if (userExist == null) {
                User user = realm.createObjectFromJson(User.class, new Gson().toJson(data));
                Log.d(TAG, "Created a new user: " + user.toString());
            } else {
                // Enable activation user
                userExist.setActive(true);
                Log.d(TAG, "Update existing user: " + userExist.toString());
            }
            realm.commitTransaction();
            realm.close();
        }
    }

    /**
     * Check if a user is already
     * authenticated into the application.
     * If that is the case, the user is redirect to list session view,
     * otherwise, he has to login from the login form.
     */
    /*
    private void findActiveUser() {
        // Create Realm instance
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("active", true).findFirst();
        if (user != null) {
            Settings.userId = user.getId();
            startActivity(new Intent(this, SessionsActivity_.class));
        }
        realm.close();
    }*/
}

