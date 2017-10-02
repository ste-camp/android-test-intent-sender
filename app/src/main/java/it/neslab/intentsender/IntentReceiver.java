package it.neslab.intentsender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IntentReceiver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_receiver);

        long t = System.currentTimeMillis();
        Intent i = getIntent();

        Intent retVal = new Intent();
        retVal.putExtra("starting", i.getLongExtra("starting", 0));
        retVal.putExtra("n_tests", i.getIntExtra("n_tests", 0));
        retVal.putExtra("receive", t);
        setResult(RESULT_OK, retVal);
        finish();
    }
}
