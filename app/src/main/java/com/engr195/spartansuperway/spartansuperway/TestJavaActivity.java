package com.engr195.spartansuperway.spartansuperway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.engr195.spartansuperway.spartansuperway.ui.activities.MainActivity;

import java.util.concurrent.Semaphore;

public class TestJavaActivity extends AppCompatActivity {

    private static Semaphore SEM = new Semaphore(2, true);

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                startActivityOnCompleteCallback();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                startActivityOnCompleteCallback();
            }
        });
    }

    private void startActivityOnCompleteCallback() {
        try {
            // P
            SEM.acquire();
            if (SEM.availablePermits() == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // V
                        SEM.notifyAll();
                        SEM.release();
                        // Proceed to next screens
                        startActivity(new Intent(TestJavaActivity.this, MainActivity.class));
                    }
                });
            } else {
                while (SEM.availablePermits() > 0) {
                    SEM.wait();
                }
                SEM.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
