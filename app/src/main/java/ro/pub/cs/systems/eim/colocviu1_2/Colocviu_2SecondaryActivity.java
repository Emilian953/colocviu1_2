package ro.pub.cs.systems.eim.colocviu1_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Colocviu_2SecondaryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        String result_text = getIntent().getStringExtra("TEXT_RESULT");
        int result = 0;


        if (result_text != null && !result_text.isEmpty()) {
            String[] parts = result_text.split("\\+");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    try {
                        result += Integer.parseInt(trimmed);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }

        result_text = String.valueOf(result);

        Toast.makeText(this, result_text, Toast.LENGTH_SHORT).show();
    }
}