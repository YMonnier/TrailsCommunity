package fr.univ_tln.trailscommunity.features.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import fr.univ_tln.trailscommunity.R;


public class SessionViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView sessionListView;
    private ArrayAdapter<String> arrayAdapterSessionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_session_view);

        this.sessionListView = (ListView) findViewById(R.id.sessionListView);
        this.sessionListView.setOnItemClickListener(this);
        addItemInSessionListView();

    }


    /**
     * Add String item in session listView for test
     */
    private void addItemInSessionListView() {
        this.arrayAdapterSessionListView = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        this.arrayAdapterSessionListView.add("Session test");
        this.sessionListView.setAdapter(this.arrayAdapterSessionListView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("ITEM SELECTED", "Id : " + position);
        Intent intent = new Intent(SessionViewActivity.this, SessionActivity.class);
        startActivity(intent);
    }
}
