package fr.univ_tln.trailscommunity.features.session;

import android.app.DatePickerDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.Settings;
import fr.univ_tln.trailscommunity.models.Coordinate;
import fr.univ_tln.trailscommunity.models.Session;
import fr.univ_tln.trailscommunity.utilities.Snack;
import fr.univ_tln.trailscommunity.utilities.geocoder.GMGeocoder;
import fr.univ_tln.trailscommunity.utilities.json.GsonSingleton;
import fr.univ_tln.trailscommunity.utilities.loader.LoaderDialog;
import fr.univ_tln.trailscommunity.utilities.network.TCRestApi;
import fr.univ_tln.trailscommunity.utilities.validators.DateValidator;
import fr.univ_tln.trailscommunity.utilities.view.ViewUtils;
import io.realm.Realm;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

@EActivity(R.layout.session_form_session)
@OptionsMenu(R.menu.basic_menu)
public class SessionFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = SessionFormActivity.class.getName();

    /**
     * Minimum password length
     */
    public static final int MIN_PASSWORD_LENGTH = 8;

    @ViewById(R.id.departurePlaceField)
    EditText departurePlaceField;

    @ViewById(R.id.arrivalPlaceField)
    EditText arrivalPlaceField;

    @ViewById(R.id.typeActivitySpinner)
    Spinner typeActivitySpinner;

    @ViewById(R.id.startDateField)
    EditText startDateField;

    @ViewById(R.id.passwordField)
    EditText passwordField;

    @ViewById(R.id.progress)
    View mProgressView;

    @ViewById
    CoordinatorLayout coordinatorLayout;

    @RestService
    TCRestApi tcRestApi;

    /**
     * Progress Dialog
     */
    private LoaderDialog progressView;

    /**
     * Activity Initialization after loading views.
     * * Populate spinner (list of activity from `Session.TypeActivity` enum)
     */
    @AfterViews
    void init() {
        progressView = new LoaderDialog(this, getString(R.string.creating));
        setupActivitiesSpinner();
    }

    /**
     * Populate spinner with type of activities.
     */
    private void setupActivitiesSpinner() {
        Session.TypeActivity[] typeActivities = Session.TypeActivity.values();
        if (typeActivities == null)
            throw new AssertionError("typeActivities should not be null");
        if (typeActivities != null) {
            SpinnerAdapter listAdapter =
                    new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeActivities);
            typeActivitySpinner.setAdapter(listAdapter);
        }
    }

    /**
     * Create session button action.
     */
    @Click(R.id.createSessionButton)
    void onClickOnCreateSessionButton() {
        attemptCreateSession();
    }

    /**
     * Display date picker when touch startDateField
     */
    @Click(R.id.startDateField)
    void onClickOnSelectStartDate() {
        Calendar calendar = Calendar.getInstance();

        // Hide keyboard before to show DatePickerDialog
        ViewUtils.closeKeyboard(this, getCurrentFocus());

        // Setup date picker with a minimum date.
        DatePickerDialog pickerDialog = new DatePickerDialog(this, this, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        pickerDialog.show();
    }

    /**
     * Date picker action. User can select a date.
     *
     * @param datePicker date picker source
     * @param year       year selected
     * @param month      month selected
     * @param day        day selected
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        if (DateValidator.validate(calendar)) {
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
            updateInputStartDate(sdf.format(calendar.getTime()));
        } else {
            updateInputStartDate("");
            updateErrorUi(startDateField, "Invalid selected date");
        }
    }

    /**
     * Check all input data when user
     * press on next button edit text.
     *
     * @param textView action source
     * @param actionId type of action(IME_ID)
     * @param keyEvent event
     */
    @EditorAction(R.id.passwordField)
    void onNextActionsEditText(TextView textView, int actionId, KeyEvent keyEvent) {
        Log.d(TAG, "KEY.. " + actionId);
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            attemptCreateSession();
        }
    }

    /**
     *
     */
    private void attemptCreateSession() {
        updateResetErrorUi();

        // Store values at the time of the login attempt.
        String departurePlace = departurePlaceField.getText().toString();
        String arrivalPlace = arrivalPlaceField.getText().toString();
        Session.TypeActivity typeActivity = (Session.TypeActivity) typeActivitySpinner.getSelectedItem();
        String startDate = startDateField.getText().toString();
        String password = passwordField.getText().toString();

        Coordinate departureCoords = GMGeocoder.addressToCoordinates(this, departurePlace);
        Coordinate arrivalCoords = GMGeocoder.addressToCoordinates(this, arrivalPlace);

        boolean cancel = false;
        View focusView = null;

        // Check if the user entered departure place.
        if (TextUtils.isEmpty(departurePlace)) {
            updateErrorUi(departurePlaceField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = departurePlaceField;
            cancel = true;
        } else if (departureCoords == null) {
            updateErrorUi(departurePlaceField, getString(R.string.error_place_does_not_exist));
            if (focusView == null)
                focusView = departurePlaceField;
            cancel = true;
        }

        // Check if the user entered arrival place.
        if (TextUtils.isEmpty(arrivalPlace)) {
            updateErrorUi(arrivalPlaceField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = arrivalPlaceField;
            cancel = true;
        } else if (arrivalCoords == null) {
            updateErrorUi(arrivalPlaceField, getString(R.string.error_place_does_not_exist));
            if (focusView == null)
                focusView = arrivalPlaceField;
            cancel = true;
        }

        if (TextUtils.isEmpty(startDate)) {
            updateErrorUi(startDateField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = startDateField;
            cancel = true;
        }

        //Check if the user entered password
        if (!TextUtils.isEmpty(password)) {
            if (!isPasswordValid(password)) {
                updateErrorUi(passwordField, getString(R.string.error_invalid_password));
                if (focusView == null)
                    focusView = passwordField;
                cancel = true;
            }
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
            progressView.show();
            createSessionTask(departurePlace, arrivalPlace, departureCoords, arrivalCoords, typeActivity.ordinal(), startDate, password);
        }
    }

    /**
     * Check if password is valid
     * His length must be sup 8.
     *
     * @param password optional password from
     * @return
     */
    private boolean isPasswordValid(final String password) {
        if (password == null)
            throw new AssertionError("Password should not be null");
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    /**
     * Put date in EditText.
     */
    @UiThread
    void updateInputStartDate(final String date) {
        startDateField.setText(date);
    }

    /**
     * Reset all error input views.
     */
    @UiThread
    void updateResetErrorUi() {
        departurePlaceField.setError(null);
        arrivalPlaceField.setError(null);
        startDateField.setError(null);
        passwordField.setError(null);
    }

    /**
     * Update the current view by settings error
     * message to the input view.
     *
     * @param view  EditText view
     * @param error error message
     */
    @UiThread
    void updateErrorUi(final EditText view, final String error) {
        if (error == null)
            throw new AssertionError("Password should not be null");
        if (view == null)
            throw new AssertionError("View should not be null");
        view.setError(error);
    }

    /**
     * Update clickable button depending on a status
     *
     * @param status true if we want to
     *               disable all clickable buttons, otherwise, false
     */
    @UiThread
    void updateLockUi(final boolean status) {
        departurePlaceField.setEnabled(!status);
        arrivalPlaceField.setEnabled(!status);
        typeActivitySpinner.setEnabled(!status);
        startDateField.setEnabled(!status);
        passwordField.setEnabled(!status);
    }


    /**
     * Background task which allows
     * to send session data to the server.
     * If the request is succeed, we go to the sessions list.
     * Otherwise, the user put wrong data and should try again...
     *
     * @param departurePlace place where the session should start
     * @param arrivalPlace   place where the session should end
     * @param typeActivity   the type of activities (hiking, paintball, ...). See `Session.TypeActivity`
     * @param password       the optional password to restricted the session.
     */
    @Background
    void createSessionTask(final String departurePlace,
                           final String arrivalPlace,
                           final Coordinate departureCoords,
                           final Coordinate arrivalCoords,
                           final int typeActivity,
                           final String startDate,
                           final String password) {
        updateLockUi(true);

        //Coordinate positionDeparturePlace = GMGeocoder.addressToCoordinates(this, departurePlace);
        //Coordinate positionArrivalPlace = GMGeocoder.addressToCoordinates(this, arrivalPlace);

        Log.d(TAG,
                "departurePlace: " + departurePlace + "\n" +
                        "arrivalPlace: " + arrivalPlace + "\n" +
                        "typeActivity: " + typeActivity + "\n" +
                        "password: " + password + "\n");

        String depCoords = departureCoords.getLatitude() + ";" + departureCoords.getLongitude();
        String arrCoords = arrivalCoords.getLatitude() + ";" + arrivalCoords.getLongitude();
        Session.Builder sessionBuilder = new Session.Builder()
                .setActivity(typeActivity)
                .setDeparturePlace(depCoords)
                .setArrivalPlace(arrCoords)
                .setStartDate(startDate);

        Map<String, Object> params = new HashMap<>();
        params.put("departure_place", depCoords);
        params.put("arrival_place", arrCoords);
        params.put("activity", typeActivity);
        params.put("start_date", startDate);

        // Add password field if the password is available
        if (!TextUtils.isEmpty(password)) {
            sessionBuilder.setPassword(password);
            params.put("password", password);
        }

        try {
            tcRestApi.setHeader("Authorization", Settings.TOKEN_AUTHORIZATION);
            Session session = sessionBuilder.build();
            Log.d(TAG, session.toString());
            ResponseEntity<JsonObject> responseSession = tcRestApi.createSession(session);
            if (responseSession != null) {
                if (responseSession.getStatusCode().is2xxSuccessful()) {
                    Log.d(TAG, responseSession.toString());
                    Session s = getSessionJson(responseSession.getBody());
                    saveSession(s);
                    progressView.dismiss();
                    finish();
                } else
                    Snack.showFailureMessage(coordinatorLayout, getString(R.string.error_request_4xx_5xx_status), Snackbar.LENGTH_LONG);
            }
        } catch (RestClientException e) {
            Log.d(TAG, "error HTTP request: " + e.getLocalizedMessage());
            updateLockUi(false);
            progressView.dismiss();
            Snack.showFailureMessage(coordinatorLayout, getString(R.string.error_request_4xx_5xx_status), Snackbar.LENGTH_LONG);
        }
    }

    /**
     * Parse the response body to get session object.
     *
     * @param json json object received from server.
     * @return a session;
     */
    private Session getSessionJson(final JsonObject json) {
        Session session = null;
        if (json != null) {
            JsonObject data = json.getAsJsonObject("data");
            if (data != null)
                session = GsonSingleton.getInstance().fromJson(data, Session.class);

        }
        return session;
    }

    /**
     * Save the session created by user into realm database.
     *
     * @param session session to store.
     */
    private void saveSession(final Session session) {
        Realm realm = Realm.getDefaultInstance();
        if (realm != null && session != null) {
            realm.beginTransaction();
            realm.copyToRealm(session);
            realm.commitTransaction();
            Log.d(TAG, "Save the session into realm database.");
        }
    }
}
