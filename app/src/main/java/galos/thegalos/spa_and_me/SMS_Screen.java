package galos.thegalos.spa_and_me;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SMS_Screen extends Activity {
    /**
     * Called when the activity is first created.
     */
    private EditText etSMSbody;
    private EditText etNumber;
    private Button btnSendSMS;
    private final int SMS_PERMISSION_REQUEST = 2;
    private final int CONTACTS_PERMISSION_REQUEST = 3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_screen);
        Button btnContact = findViewById(R.id.btnContact);
        String typeOfMassage = getIntent().getStringExtra("smsBody1");
        int day = getIntent().getIntExtra("smsBody2", 0);
        int month = getIntent().getIntExtra("smsBody3", 1);
        int year = getIntent().getIntExtra("smsBody4", 2000);
        int hour = getIntent().getIntExtra("smsBody5", 0);
        int minutes = getIntent().getIntExtra("smsBody6", 0);
        getIntent().getStringExtra("Name");
        etNumber = findViewById(R.id.etNumber);
        etSMSbody = findViewById(R.id.etSMSbody);
        etNumber.setText("");

        String smsBody = (getString(R.string.hi_i_booked) + typeOfMassage + getString(R.string.treatment1) + "\n" + getString(R.string.treatment2) + day + "/" + month + "/" + year + getString(R.string.at) + hour + ":" + ((minutes < 10) ? "0" + minutes : minutes) + "\n" + getString(R.string.its_going_to));

        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (etNumber.getText().toString().equals(""))
                    btnSendSMS.setEnabled(false);
                else
                    btnSendSMS.setEnabled(true);
            }
        });


        etSMSbody.setText(smsBody);
        btnSendSMS = findViewById(R.id.btnSendSMS);
        btnSendSMS.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //check if we are in 23 android version and above
                if (Build.VERSION.SDK_INT >= 23) {
                    int hasCallPermission = checkSelfPermission(Manifest.permission.SEND_SMS);
                    //if i have the permission already
                    if (hasCallPermission == PackageManager.PERMISSION_GRANTED)
                        sendSMS();
                    else //request for permission
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST);
                }
                //if i have android version  below 23
                else
                    sendSMS();
            }
        });

        btnContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    openContacts();
            }
        });
    }

    private void openContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CONTACTS_PERMISSION_REQUEST);
        }
    }

    private void sendSMS() {
        SmsManager sm = SmsManager.getDefault();
        String number = etNumber.getText().toString();
        String msg = etSMSbody.getText().toString();
        String currentLanguage = getResources().getConfiguration().locale.getLanguage();

        if (currentLanguage.equals("iw")) {
            sm.sendMultipartTextMessage(number, null, splitInParts(msg), null, null);
        }
        else
            sm.sendTextMessage(number, null, msg, null, null);
        Toast.makeText(SMS_Screen.this, getString(R.string.sms_sent), Toast.LENGTH_SHORT).show();
        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACTS_PERMISSION_REQUEST && resultCode == Activity.RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            assert contactUri != null;
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                etNumber.setText(number);
            }
            assert cursor != null;
            cursor.close();
        }
    }




    private ArrayList<String> splitInParts(String s) {
        int len = s.length();
        ArrayList<String> smsHebArr = new ArrayList<>();

        // Number of parts
        int nparts = (len + 50 - 1) / 50;
        String[] parts = new String[nparts];

        // Break into parts
        int offset = 0;
        int i = 0;
        while (i < nparts) {
            parts[i] = s.substring(offset, Math.min(offset + 50, len));
            offset += 50;
            i++;
        }
        for (i = 0; i < parts.length; i++) {
            smsHebArr.add(parts[i]);
        }
        return smsHebArr;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACTS_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openContacts();
            else {
                Toast.makeText(this, "sorry, can't work without call permission", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == SMS_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                sendSMS();
            else {
                Toast.makeText(this, "sorry, can't work without call permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}


