package fr.univ_tln.trailscommunity.features.root;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import fr.univ_tln.trailscommunity.R;

@EActivity(R.layout.root_profile_activity)
public class ProfileActivity extends AppCompatActivity {

    @ViewById(R.id.nicknameField)
    EditText nicknameField;

    @ViewById(R.id.numberField)
    EditText phoneNumberField;

    @ViewById(R.id.progress)
    View mProgressView;

    @ViewById(R.id.save)
    Button saveButton;

    @AfterViews
    void init() {
        setTitle("User informations");
        nicknameField.setText("GreatNickname");
        phoneNumberField.setText("6 00 00 00 00");
    }

    /**
     * Save button action.
     */
    @Click(R.id.save)
    void onClickOnSaveButton() {
        saveProfile();
    }

    /**
     * Method which allows to save user information
     * to database and send it to the server side.
     * Before to send the data, we check if all data are available and valid.
     * If it is not the case, an error focus is shown.
     */
    private void saveProfile() {
        updateResetErrorUi();

        String nickname = nicknameField.getText().toString();
        String number = phoneNumberField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nickname)) {
            updateErrorUi(nicknameField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = nicknameField;
            cancel = true;
        }

        if (TextUtils.isEmpty(number)) {
            updateErrorUi(phoneNumberField, getString(R.string.error_field_required));
            if (focusView == null)
                focusView = phoneNumberField;
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
            saveProfileTask(nickname, number);
        }
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
     * Reset all error input views.
     */
    @UiThread
    void updateResetErrorUi() {
        nicknameField.setError(null);
        phoneNumberField.setError(null);
    }

    /**
     * Update clickable button/inputs depending on a status
     *
     * @param status true if we want to
     *               disable all clickable buttons/inputs, otherwise, false
     */
    @UiThread
    void updateLockUi(boolean status) {
        nicknameField.setEnabled(!status);
        phoneNumberField.setEnabled(!status);
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
     * to send user information data to the server.
     *
     * @param nickname user nickname
     * @param phoneNumber user phone number
     */
    @Background
    void saveProfileTask(final String nickname,
                    final String phoneNumber) {
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
