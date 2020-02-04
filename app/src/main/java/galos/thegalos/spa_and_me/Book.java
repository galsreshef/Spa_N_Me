package galos.thegalos.spa_and_me;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static android.view.View.VISIBLE;

public class Book extends AppCompatActivity implements View.OnClickListener {

    private ScrollView scrollView;
    private LinearLayout LinearLayoutName;
    private final ArrayList<String> tvArrNames = new ArrayList<>();
    private RadioGroup rGroup;
    private Button btnDateChange, btnTimeChange, btnReset, btnNext, btnName, btnQuantity;
    private ImageView ivMassage1, ivMassage2, ivMassage3, ivInfo1, ivInfo2, ivInfo3;
    private TextView tvDate, tvTime, tvQuantity;
    private EditText etQuantity, etPhone, etName;
    private int keep = 0, hourSelected, minSelected, daySelected, monthSelected, yearSelected, temporary = 0;
    private String typeOfMassage = "", name = "", time;
    private boolean boolHour, boolMinute, boolDate, boolPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        scrollView = findViewById(R.id.scrollViewBook);
        SharedPreferences prefs = getSharedPreferences("galos.thegalos.spa_and_me", MODE_PRIVATE);
        boolean savedDarkMode = prefs.getBoolean("dark", false);
        if (savedDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            scrollView.setBackgroundResource(R.drawable.bgr2_night);
        }

        name = getIntent().getStringExtra("Name");
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        btnReset = findViewById(R.id.btnReset);
        btnNext = findViewById(R.id.btnNext);
        btnQuantity = findViewById(R.id.btnQuantity);
        ivInfo1 = findViewById(R.id.ivInfo1);
        ivInfo2 = findViewById(R.id.ivInfo2);
        ivInfo3 = findViewById(R.id.ivInfo3);
        btnDateChange = findViewById(R.id.btnDateChange);
        btnTimeChange = findViewById(R.id.btnTimeChange);
        btnName = findViewById(R.id.btnName);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        tvQuantity = findViewById(R.id.tvQuantity);
        etPhone = findViewById(R.id.etPhone);
        LinearLayoutName = findViewById(R.id.LinearLayoutName);
        rGroup = findViewById(R.id.rGroup);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        ivMassage1 = findViewById(R.id.ivMassage1);
        ivMassage2 = findViewById(R.id.ivMassage2);
        ivMassage3 = findViewById(R.id.ivMassage3);
        ivInfo1.setOnClickListener(this);
        ivInfo2.setOnClickListener(this);
        ivInfo3.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnName.setOnClickListener(this);
        btnDateChange.setOnClickListener(this);
        btnTimeChange.setOnClickListener(this);
        btnQuantity.setOnClickListener(this);

        String str = getString(R.string.hello) + name + getString(R.string.welcome_to_booking);
        tvWelcome.setText(str);

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (rGroup.getCheckedRadioButtonId()) {
                    case R.id.rbShiatsu:
                        typeOfMassage = getString(R.string.shiatsu);
                        break;
                    case R.id.rbThai:
                        typeOfMassage = getString(R.string.thai_massage);
                        break;
                    case R.id.rbSwedish:
                        typeOfMassage = getString(R.string.swedish_massage);
                        break;
                }

                tvQuantity.setVisibility(VISIBLE);
                etQuantity.setVisibility(VISIBLE);
                ivMassage1.setVisibility(VISIBLE);
            }
        });

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (etName.getText().toString().length() == 0)
                    btnName.setEnabled(false);
                else
                    btnName.setEnabled(true);
            }
        });


        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                btnQuantity.setVisibility(VISIBLE);
            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                boolPhone = etPhone.getText().toString().length() != 0;
                btnDateChange.setVisibility(VISIBLE);
                btnTimeChange.setVisibility(VISIBLE);
                tvTime.setVisibility(VISIBLE);
                tvDate.setVisibility(VISIBLE);
                nextReady();
            }
        });


    }

    private void nextReady() {
        if (boolDate && boolHour && boolMinute && boolPhone)
            btnNext.setEnabled(true);
        else
            btnNext.setEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View v) {

        if (v == btnName) {
            if (temporary < keep) {
                TextView name = new TextView(Book.this);
                name.setText(etName.getText());
                name.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
                tvArrNames.add(etName.getText().toString());
                LinearLayoutName.addView(name);
                etName.setText("");
                temporary += 1;

            } if (temporary == keep) {
                etName.setEnabled(false);
                btnName.setEnabled(false);
                etPhone.setVisibility(VISIBLE);
                ivMassage2.setVisibility(VISIBLE);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }

        } else if (v == btnDateChange) {
            showDatePicker();

        } else if (v == btnTimeChange) {
            showTimePicker();

        } else if (v == btnNext) {
            if (etPhone.getText().toString().equals("") || tvTime.getText().toString().equals("") || tvDate.getText().toString().equals(""))
                Toast.makeText(Book.this, R.string.please_enter_phone_number, Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(Book.this, galos.thegalos.spa_and_me.FinalizeBook.class);
                intent.putExtra("Quantity", keep);
                intent.putExtra("hour", hourSelected);
                intent.putExtra("minutes", minSelected);
                intent.putExtra("Day", daySelected);
                intent.putExtra("Month", monthSelected);
                intent.putExtra("Year", yearSelected);
                intent.putExtra("typeOfMassage", typeOfMassage);
                intent.putExtra("Phone", etPhone.getText().toString());
                intent.putExtra("Name", name);
                intent.putStringArrayListExtra("Names", tvArrNames);
                startActivity(intent);
                finish();
            }
        } else if (v == btnReset) {
            finish();
            startActivity(getIntent());

        } else if (v == btnQuantity) {
            etName.setVisibility(VISIBLE);
            btnName.setVisibility(VISIBLE);
            keep = Integer.parseInt(etQuantity.getText().toString());
            etQuantity.setEnabled(false);
            btnQuantity.setEnabled(false);

        } else if (v == ivInfo1){
            callSearch(1);

        } else if (v == ivInfo2){
            callSearch(2);

        } else if (v == ivInfo3){
            callSearch(3);
        }
    }

    private void showDatePicker() {
        final Calendar calDate = Calendar.getInstance();
        int day = calDate.get(Calendar.DAY_OF_MONTH);
        int month = calDate.get(Calendar.MONTH);
        int year = calDate.get(Calendar.YEAR);

        // date picker dialog
        DatePickerDialog datePicker = new DatePickerDialog(Book.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        tvDate.setText(strDate);
                        daySelected = dayOfMonth;
                        monthSelected = monthOfYear;
                        yearSelected = year;
                        boolDate = true;
                        nextReady();
                    }
                }, year, month, day);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000);
        datePicker.show();
        btnDateChange.setVisibility(VISIBLE);
        tvDate.setVisibility(VISIBLE);
    }

    private void showTimePicker() {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if (view.isShown()) {
                    minSelected = minute;
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                    boolHour = hourOfDay >= 10 && hourOfDay < 22;
                    boolMinute = minute == 0 || minute == 15 || minute == 30 || minute == 45;
                    if (minute < 10) {
                        time = hourOfDay + ":0" + minute;
                    } else {
                        time = hourOfDay + ":" + minute;
                    }
                    hourSelected = hourOfDay;
                    if (!boolHour || !boolMinute) {
                        if (!boolHour)
                            Toast.makeText(Book.this, R.string.valid_hours, Toast.LENGTH_SHORT).show();
                        if (!boolMinute)
                            Toast.makeText(Book.this, R.string.min_intervals, Toast.LENGTH_SHORT).show();
                        showTimePicker();
                    } else {
                        tvTime.setText(time);
                        nextReady();
                        ivMassage3.setVisibility(VISIBLE);
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(Book.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle(getString(R.string.min_intervals));
        Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    private void callSearch(int i) {
        String searchFor = getString(R.string.what_is);
        if (i==1)
            searchFor = searchFor + getString(R.string.shiatsu);
        else if (i==2)
            searchFor = searchFor + getString(R.string.thai_massage);
        else if (i==3)
            searchFor = searchFor + getString(R.string.swedish_massage);

        Intent viewSearch = new Intent(Intent.ACTION_WEB_SEARCH);
        viewSearch.putExtra(SearchManager.QUERY, searchFor);
        startActivity(viewSearch);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.you_got_the_power));
        builder.setMessage(R.string.cancel_order);
        builder.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();
    }
}