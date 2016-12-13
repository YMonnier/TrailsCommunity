package fr.univ_tln.trailscommunity.features.session;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.models.Coordinate;
import fr.univ_tln.trailscommunity.models.Session;
import fr.univ_tln.trailscommunity.utilities.geocoder.GMGeocoder;
import fr.univ_tln.trailscommunity.utilities.validators.DateValidator;
import fr.univ_tln.trailscommunity.utilities.view.ViewUtils;

import static fr.univ_tln.trailscommunity.R.id.arrivalPlaceField;
import static fr.univ_tln.trailscommunity.R.id.departurePlaceField;
import static fr.univ_tln.trailscommunity.R.id.nicknameField;
import static fr.univ_tln.trailscommunity.R.id.passwordField;

@EActivity(R.layout.session_modify_session_activity)
public class ModifySessionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

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

    @ViewById(R.id.saveSessionButton)
    Button saveButton;

    @AfterViews
    void init() {
        setTitle("Modify session");
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
     * Save button action.
     */
    @Click(R.id.saveSessionButton)
    void onClickOnSaveButton() {
        saveSessionModification();
    }

    private void saveSessionModification() {
        updateResetErrorUi();

        String departurePlace = departurePlaceField.getText().toString();
        String arrivalPlace = arrivalPlaceField.getText().toString();
        Session.TypeActivity typeActivity = (Session.TypeActivity) typeActivitySpinner.getSelectedItem();
        String startDate = startDateField.getText().toString();
        String password = passwordField.getText().toString();

        Coordinate departureCoords = GMGeocoder.addressToCoordinates(this, departurePlace);
        Coordinate arrivalCoords = GMGeocoder.addressToCoordinates(this, arrivalPlace);

        boolean cancel = false;
        View focusView = null;

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

        if(TextUtils.isEmpty(startDate)){
            updateErrorUi(startDateField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = startDateField;
            cancel = true;
        }

        if(TextUtils.isEmpty(password)){
            updateErrorUi(passwordField, getString(R.string.error_field_required));
            if (focusView == null)
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
            saveModificationTask(departurePlace, arrivalPlace, departureCoords, arrivalCoords, typeActivity.ordinal(), startDate, password);
        }
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
     * Put date in EditText.
     */
    @UiThread
    void updateInputStartDate(final String date) {
        startDateField.setText(date);
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
        view.setError(error);
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
     * Update clickable button/inputs depending on a status
     *
     * @param status true if we want to
     *               disable all clickable buttons/inputs, otherwise, false
     */
    @UiThread
    void updateLockUi(final boolean status) {
        departurePlaceField.setEnabled(!status);
        arrivalPlaceField.setEnabled(!status);
        typeActivitySpinner.setEnabled(!status);
        startDateField.setEnabled(!status);
        passwordField.setEnabled(!status);
        saveButton.setEnabled(!status);
    }

    /**
     * Shows the progress UI
     *
     * @param show progress status, true to set visible progress,
     *             false to set invisible progress
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
     * to send session information data to the server.
     *
     * @param departurePlace
     * @param arrivalPlace
     * @param departureCoords
     * @param arrivalCoords
     * @param typeActivity
     * @param startDate
     * @param password
     */
    @Background
    void saveModificationTask(final String departurePlace,
                              final String arrivalPlace,
                              final Coordinate departureCoords,
                              final Coordinate arrivalCoords,
                              final int typeActivity,
                              final String startDate,
                              final String password) {
        updateLockUi(true);

        //Request...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        updateLockUi(false);
        showProgress(false);
    }

}
