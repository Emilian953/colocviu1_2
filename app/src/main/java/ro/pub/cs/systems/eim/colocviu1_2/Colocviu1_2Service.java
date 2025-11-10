package ro.pub.cs.systems.eim.colocviu1_2;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Colocviu1_2Service extends Service {
    public static final int SUM_THRESHOLD = 10;

    public static final String ACTION_1 = "com.example.examplepracticaltest01.ACTION_1";
    public static final String ACTION_2 = "com.example.examplepracticaltest01.ACTION_2";
    public static final String ACTION_3 = "com.example.examplepracticaltest01.ACTION_3";

    public static final long INTERVAL_MS = 4_000L;

    private final Random random = new Random();

    private String resultNow;

    private Handler handler;
    private Runnable broadcastRunnable;
    private boolean isRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            resultNow = intent.getStringExtra("RESULT_KEY");
        }

        // pornim runnable-ul doar o dată
        if (!isRunning) {
            isRunning = true;
            handler = new Handler();
            broadcastRunnable = new Runnable() {
                @Override
                public void run() {
                    sendRandomBroadcast();
                    handler.postDelayed(this, INTERVAL_MS);
                }
            };
            handler.post(broadcastRunnable);
        }

        return START_REDELIVER_INTENT;
    }

    private void sendRandomBroadcast() {
        // 1. alegem acțiunea
        String action;
        int choice = random.nextInt(3); // 0,1,2
        if (choice == 0) {
            action = ACTION_1;
        } else if (choice == 1) {
            action = ACTION_2;
        } else {
            action = ACTION_3;
        }

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        broadcastIntent.putExtra("timestamp", timestamp);

        broadcastIntent.setPackage(getApplicationContext().getPackageName());

        sendBroadcast(broadcastIntent);

//        android.widget.Toast.makeText(
//                getApplicationContext(),
//                "Broadcast sent!\n",
//                android.widget.Toast.LENGTH_LONG
//        ).show();
//
//        android.util.Log.d("PracticalTest01Service", "Broadcast trimis cu actiunea: " + action);
    }

    @Override
    public void onDestroy() {
        // oprim runnable-ul ca să nu mai trimită mesaje
        if (handler != null && broadcastRunnable != null) {
            handler.removeCallbacks(broadcastRunnable);
        }
        isRunning = false;
        super.onDestroy();
    }
}