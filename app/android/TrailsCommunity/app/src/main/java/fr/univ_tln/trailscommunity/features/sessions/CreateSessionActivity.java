package fr.univ_tln.trailscommunity.features.sessions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.models.Position;
import fr.univ_tln.trailscommunity.utilities.network.CustomRequest;

@EActivity(R.layout.sessions_create_session)
public class CreateSessionActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{

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

    private RequestQueue queue;

    private CustomRequest request;

    @StringArrayRes
    String[] activities;

    private Calendar myCalendar = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener date;

    /**
     * Create session button action.
     */
    @Click(R.id.createSessionButton)
    void onClickOnCreateSessionButton() {
        attemptCreateSession();
    }

    public Position convertAdressToCoordinates(String address){
        Geocoder geocoder = new Geocoder(this);
        Position position = null;
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();
            Log.e("LATITUDE", latitude + "");
            Log.e("LONGITUDE", longitude + "");
            position = new Position(latitude, longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return position;
    }

    /**
     * Generate dialog to display date picker
     */
    public void displayDatePicker(){
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }

    /**
     * Update and put date in EditText
     */
    private void updateLabel(){
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        startDateField.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * Display date picker when touch editText startDateField
     */
    @Click(R.id.startDateField)
    void onClickOnSelectStartDate(){
        displayDatePicker();
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    /**
     *
     */
    private void attemptCreateSession() {
        updateResetErrorUi();

        // Store values at the time of the login attempt.
        String departurePlace = departurePlaceField.getText().toString();
        String arrivalPlace = arrivalPlaceField.getText().toString();
        String typeActivity = typeActivitySpinner.getSelectedItem().toString();
        String startDate = startDateField.getText().toString();
        String password = passwordField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if the user entered departure place.
        if (TextUtils.isEmpty(departurePlace)) {
            updateErrorUi(departurePlaceField, getString(R.string.error_field_required));
            focusView = departurePlaceField;
            cancel = true;
        }

        // Check if the user entered arrival place.
        if (TextUtils.isEmpty(arrivalPlace)) {
            updateErrorUi(arrivalPlaceField, getString(R.string.error_field_required));
            focusView = arrivalPlaceField;
            cancel = true;
        }

        if(TextUtils.isEmpty(startDate)){
            updateErrorUi(startDateField, getString(R.string.error_field_required));
            focusView = startDateField;
            cancel = true;
        }

        //Check if the user entered password
        if(TextUtils.isEmpty(password)){
            updateErrorUi(passwordField, getString(R.string.error_field_required));
            focusView = passwordField;
            cancel = true;
        }
        else if (!isPasswordValid(password)) {
            updateErrorUi(passwordField, getString(R.string.error_invalid_password));
            focusView = passwordField;
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
            createSessionTask(departurePlace, arrivalPlace, typeActivity, password);
        }
    }

    /**
     * Check if password is valid
     * His length must be sup 8
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    /**
     * Reset all error input views.
     */
    @UiThread
    void updateResetErrorUi() {
        // Reset errors.
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
    void updateErrorUi(EditText view, String error) {
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
        departurePlaceField.setEnabled(!status);
        arrivalPlaceField.setEnabled(!status);
        typeActivitySpinner.setEnabled(!status);
        startDateField.setEnabled(!status);
        passwordField.setEnabled(!status);
    }

    /**
     * Shows the progress UI.
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
     * @param departurePlace
     * @param arrivalPlace
     * @param typeActivity
     * @param password
     */
    @Background
    void createSessionTask(final String departurePlace,
                    final String arrivalPlace,
                    final String typeActivity,
                    final String password) {
        updateLockUi(true);

        Log.e("CREATE SESSION", "true");

        //Request...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Position positionDeparturePlace = convertAdressToCoordinates(departurePlace);
        Position positionArrivalPlace = convertAdressToCoordinates(arrivalPlace);

        /*
        Map<String, String> parameters = new HashMap<>();

        queue = Volley.newRequestQueue(this);
        request = new CustomRequest(Request.Method.POST, url, parameters, this, this);
        queue.add(request);
        */
        // Success request

        updateLockUi(false);
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
