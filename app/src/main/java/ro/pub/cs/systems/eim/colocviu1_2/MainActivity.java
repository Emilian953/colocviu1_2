package ro.pub.cs.systems.eim.colocviu1_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button add, compute;
    private TextView all_terms;
    private EditText next_term;

    private static final int SECONDARY_ACTIVITY_REQUEST_CODE = 1;
    private android.content.BroadcastReceiver messageReceiver;

    private boolean serviceStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inițializăm toate elementele UI
        add = findViewById(R.id.add);
        compute = findViewById(R.id.compute);
        all_terms = findViewById(R.id.all_terms);
        next_term = findViewById(R.id.next_term);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = next_term.getText().toString().trim();

                if (content.isEmpty()) {
                    return;
                }

                String current = all_terms.getText().toString();

                if (current.isEmpty()) {
                    all_terms.setText(content);
                } else {
                    all_terms.setText(current + " + " + content);
                }

                checkAndStartService();

                next_term.setText("");


            }
        });

        compute = findViewById(R.id.compute);

        compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendMessage = all_terms.getText().toString();

                Intent intent = new Intent(MainActivity.this,
                        Colocviu_2SecondaryActivity.class);

                intent.putExtra("TEXT_RESULT", sendMessage);

                startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
            }
        });

        messageReceiver = new android.content.BroadcastReceiver() {
            @Override
            public void onReceive(android.content.Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) return;

                String action = intent.getAction();
                String timestamp = intent.getStringExtra("timestamp");

                // doar ca să vezi de unde a venit
                String msg = action + "\n" +
                        "time: " + timestamp;

                android.util.Log.d("Test02", msg);

                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putCharSequence("RESULT_GEN", next_term.getText());
        savedInstanceState.putCharSequence("COUNTER_GEN", all_terms.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence resultSquence = savedInstanceState.getCharSequence("RESULT_GEN");
        CharSequence counterSequence = savedInstanceState.getCharSequence("COUNTER_GEN");

        next_term.setText(resultSquence);
        all_terms.setText(counterSequence);
    }

    private void checkAndStartService() {
        int result = Integer.parseInt(next_term.getText().toString());


        if (result > 10 && !serviceStarted) {
            Intent serviceIntent = new Intent(getApplicationContext(), Colocviu1_2Service.class);
            serviceIntent.putExtra("RESULT_KEY", all_terms.getText().toString());
            startService(serviceIntent);
            serviceStarted = true;

            // doar ca feedback
            Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();

        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() {
        super.onResume();
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction(Colocviu1_2Service.ACTION_1);
        filter.addAction(Colocviu1_2Service.ACTION_2);
        filter.addAction(Colocviu1_2Service.ACTION_3);
        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(messageReceiver, filter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(messageReceiver, filter);
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageReceiver);
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        stopService(new Intent(getApplicationContext(), Colocviu1_2Service.class));
        super.onDestroy();
    }
}
