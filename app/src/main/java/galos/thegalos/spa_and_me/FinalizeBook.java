package galos.thegalos.spa_and_me;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FinalizeBook extends AppCompatActivity implements View.OnClickListener {

    private int hour, minutes, day, month, year;
    private Button btnEmail, btnSMS, btnFinish, btnBook, btnCalendar, btnWhatsapp;
    private TextView tvTellAll;
    private String typeOfMassage, emailBody, smsBody, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_book);

        SharedPreferences prefs = getSharedPreferences("galos.thegalos.spa_and_me", MODE_PRIVATE);
        boolean savedDarkMode = prefs.getBoolean("dark", false);
        if (savedDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            ConstraintLayout cl = findViewById(R.id.ConstraintLayoutFinalize);
            cl.setBackgroundResource(R.drawable.bgr3_night);
        }

        name = getIntent().getStringExtra("Name");
        typeOfMassage = getIntent().getStringExtra("typeOfMassage");
        hour = getIntent().getIntExtra("hour", 0);
        minutes = getIntent().getIntExtra("minutes", 0);
        day = getIntent().getIntExtra("Day", 0);
        month = getIntent().getIntExtra("Month", 0) + 1;
        year = getIntent().getIntExtra("Year", 0);
        String phone = getIntent().getStringExtra("Phone");
        int quantity = getIntent().getIntExtra("Quantity", 0);
        ArrayList<String> tvArrNames = Objects.requireNonNull(getIntent().getExtras()).getStringArrayList("Names");
        StringBuilder names = new StringBuilder();
        TextView textView5 = findViewById(R.id.tvOrder);
        assert tvArrNames != null;
        for (int i = 0; i < tvArrNames.size(); i++) {
            names.append(tvArrNames.get(i)).append("\n");
        }

        tvTellAll = findViewById(R.id.tvTellAll);
        btnWhatsapp = findViewById(R.id.btnWhatsapp);
        btnWhatsapp.setOnClickListener(this);

        TextView tvYourOrder = findViewById(R.id.tvYourOrder);
        String str = name + getString(R.string.your_order_details);
        tvYourOrder.setText(str);

        String text = (getString(R.string.order_for) + quantity + getString(R.string.people) + "\n" + names + getString(R.string.on) + day + "/" + month + "/" + year + getString(R.string.at) + hour + ":" + ((minutes < 10) ? "0" + minutes : minutes) + "\n" + getString(R.string.type_of_treatment) + typeOfMassage + "\n" + getString(R.string.phone_number) + phone);
        emailBody = (getString(R.string.hi_your_order) + names + getString(R.string.total_of) + quantity + getString(R.string.people_on) + day + "/" + month + "/" + year + getString(R.string.at) + hour + ":" + ((minutes < 10) ? "0" + minutes : minutes) + "\n" + getString(R.string.type_of_treatment) + typeOfMassage + "\n" + getString(R.string.thank_you));
        smsBody = (getString(R.string.hi_i_booked) + " " + typeOfMassage + " " + getString(R.string.treatment) + day + "/" + month + "/" + year + getString(R.string.at) + hour + ":" + ((minutes < 10) ? "0" + minutes : minutes) + getString(R.string.its_going_to));
        textView5.setText(text);

        btnFinish = findViewById(R.id.btnFinish);
        btnBook = findViewById(R.id.btnBook);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnSMS = findViewById(R.id.btnSMS);
        btnEmail = findViewById(R.id.btnEmail);

        btnBook.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
        btnCalendar.setOnClickListener(this);
        btnSMS.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        // builder.setCancelable(false);
        builder.setTitle(R.string.you_got_the_power);
        builder.setMessage(R.string.reorder_home_stay);
        builder.setPositiveButton(R.string.reorder,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FinalizeBook.this , R.string.reorder, Toast.LENGTH_LONG).show();
                Intent intentBook = new Intent(FinalizeBook.this, Book.class);
                intentBook.putExtra("Name", name);
                startActivity(intentBook);
                finish();
            }
        });
        builder.setNegativeButton(R.string.home,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNeutralButton(R.string.stay,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FinalizeBook.this, R.string.i_want_to_stay, Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBook:
                btnEmail.setVisibility(VISIBLE);
                btnCalendar.setVisibility(VISIBLE);
                btnSMS.setVisibility(VISIBLE);
                tvTellAll.setVisibility(VISIBLE);
                btnWhatsapp.setVisibility(VISIBLE);
                btnFinish.setEnabled(true);
                btnBook.setVisibility(GONE);
                break;

            case R.id.btnEmail:
                Intent intentEmail = new Intent(Intent.ACTION_SEND);
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.spa_booking));
                intentEmail.putExtra(Intent.EXTRA_TEXT, emailBody);
                intentEmail.setType("message/rfc822");
                startActivity(Intent.createChooser(intentEmail, getString(R.string.choose_email)));
                break;

            case R.id.btnCalendar:

                openCalendar();
                break;

            case R.id.btnSMS:
                Intent intentSMS = new Intent(FinalizeBook.this, SMS_Screen.class);
                intentSMS.putExtra("smsBody1", typeOfMassage);
                intentSMS.putExtra("smsBody2", day);
                intentSMS.putExtra("smsBody3", month);
                intentSMS.putExtra("smsBody4", year);
                intentSMS.putExtra("smsBody5", hour);
                intentSMS.putExtra("smsBody6", minutes);
                startActivity(intentSMS);
                break;

            case R.id.btnFinish:
                finish();
                break;

            case R.id.btnWhatsapp:
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                String whatsapp = smsBody + "\n" + getString(R.string.website);
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, whatsapp);
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
                }
        }
    }

    private void openCalendar() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month - 1, day, hour, minutes);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month - 1, day, hour + 1, minutes);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, getString(R.string.spanme_booking))
                .putExtra(CalendarContract.Events.DESCRIPTION, getString(R.string.spa_treatment))
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Spa N Me,+Holon+Israel")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(CalendarContract.Events.CALENDAR_COLOR, getResources().getColor(android.R.color.holo_red_light));//"#FF0000");
        startActivity(intent);
    }
}