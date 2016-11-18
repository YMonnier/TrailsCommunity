package fr.univ_tln.trailscommunity.features.root;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import fr.univ_tln.trailscommunity.R;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailField;
    private EditText nicknameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText numberField;

    private Spinner codeNumberCountrySpinner;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailField = (EditText) findViewById(R.id.emailField);
        nicknameField = (EditText) findViewById(R.id.nicknameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        confirmPasswordField = (EditText) findViewById(R.id.confirmPasswordField);
        numberField = (EditText) findViewById(R.id.numberField);
        registerButton = (Button) findViewById(R.id.registerButton);

        codeNumberCountrySpinner = (Spinner) findViewById(R.id.codeNumberCountrySpinner);

        registerButton.setOnClickListener(this);

        //Delete after test
        addCodeNumberInSpinner();
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
        finish();
    }
}
