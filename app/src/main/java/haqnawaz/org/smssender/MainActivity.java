package haqnawaz.org.smssender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int MIN_DELAY_MS = 18000; // minimum delay in milliseconds
    private static final int MAX_DELAY_MS = 25000; // maximum delay in milliseconds

    //private String[] phoneNumbers = {"03014448184", "03454333315", "03004083168", "03008463688", "03218300142", "03218488996", "03145422500", "03338251853", "03334441732", "03008847619"};
    private String[] phoneNumbers = {"03344037449", "03067188175", "03336531552", "03144814425", "03144175819" };
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private Handler handler;
    private Random random;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = findViewById(R.id.send_button);
        listView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        handler = new Handler();
        random = new Random();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsRunnable smsRunnable = new SmsRunnable();
                handler.post(smsRunnable);
            }
        });

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
//        }



        
    }


    private void requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        } else {
            // permission already granted run sms send
            sendSms(phone, message);
        }
    }

    private class SmsRunnable implements Runnable {
        private String sms;

        @Override
        public void run() {
            SmsManager smsManager = SmsManager.getDefault();

            for (String phoneNumber : phoneNumbers) {
                sms =   phoneNumber + ": It is a test message";
                adapter.add(getCurrentTime() + " " + sms);
                smsManager.sendTextMessage(phoneNumber, null, sms, null, null);

                try {
                    Thread.sleep(random.nextInt(MAX_DELAY_MS - MIN_DELAY_MS + 1) + MIN_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private String getCurrentTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
    }
}
