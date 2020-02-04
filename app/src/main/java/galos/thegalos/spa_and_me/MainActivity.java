package galos.thegalos.spa_and_me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MY_PREFS_FILENAME = "galos.thegalos.spa_and_me";
    private boolean savedDarkMode;
    private EditText etName;
    private final int CALL_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
        String savedLang = prefs.getString("lang", "iw");
        String currentLanguage = getResources().getConfiguration().locale.getLanguage();
        if (!savedLang.equals(currentLanguage)) {
            setLocale(savedLang);
        }

        // Main onCreate Start!
        setContentView(R.layout.activity_main);

        ImageView ivDarkMode = findViewById(R.id.ivDarkMode);
        savedDarkMode = prefs.getBoolean("dark", false);
        if (savedDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            ivDarkMode.setImageResource(R.drawable.dark_mode_on);
            ConstraintLayout cl = findViewById(R.id.ConstraintLayoutMain);
            cl.setBackgroundResource(R.drawable.bgr_night);
        }

        ImageView ivLang = findViewById(R.id.ivLang);
        final Button btnBook = findViewById(R.id.btnBook);
        etName = findViewById(R.id.etName);

        ImageView ivNavigate = findViewById(R.id.ivNavigate);
        ImageView ivCall = findViewById(R.id.ivCall);
        ImageView ivWebsite = findViewById(R.id.ivWebsite);
        ImageView ivAdd = findViewById(R.id.ivAdd);

        ivNavigate.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        ivWebsite.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        btnBook.setOnClickListener(this);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (etName.getText().toString().length() == 0)
                    btnBook.setEnabled(false);
                else
                    btnBook.setEnabled(true);
            }
        });

        ivLang.setOnClickListener(this);
        ivDarkMode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ivLang:
                showLanguageChangeDialog();
                break;

            case R.id.ivDarkMode:
                if (savedDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    savedDarkMode = false;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    savedDarkMode = true;
                }
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();
                editor.putBoolean("dark", savedDarkMode);
                editor.apply();
                break;

            case R.id.btnBook:
                intent = new Intent(MainActivity.this, Book.class);
                intent.putExtra("Name", etName.getText().toString());
                startActivity(intent);
                break;

            case R.id.ivNavigate:
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=Spa N Me,+Holon+Israel");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;

            case R.id.ivCall:
                if (Build.VERSION.SDK_INT >= 23) {
                    int hasCallPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
                    if (hasCallPermission == PackageManager.PERMISSION_GRANTED)
                        call();
                    else
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
                } else
                    call();
                break;

            case R.id.ivAdd:
                Intent addContactIntent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                addContactIntent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE)
                        .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, getString(R.string.app_name) + " " + getString(R.string.reception))
                        .putExtra(ContactsContract.Intents.Insert.PHONE, "1800-80-80-10")
                        .putExtra(ContactsContract.Intents.Insert.NOTES, "https://spa-n-me.business.site/?utm_source=gmb&utm_medium=referral");
                startActivity(addContactIntent);
                break;

            case R.id.ivWebsite:
                Intent intentWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website)));
                startActivity(intentWebsite);
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void call() {
        String number = "1800-80-80-10";
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        try {
            startActivity(intent);
        } catch (SecurityException ignored){}
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                call();
            else {
                Toast.makeText(this, "sorry, can't work without call permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showLanguageChangeDialog() {
        final String[] listItems = {"עברית", "English"};
        AlertDialog.Builder mb = new AlertDialog.Builder(MainActivity.this);
        mb.setTitle("בחר שפה");//getString(R.string.choose_lang));
        mb.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocale("iw");
                }
                if (i == 1) {
                    setLocale("en");
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog md = mb.create();
        md.show();
    }

    private void setLocale(String lang) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();
        editor.putString("lang", lang);
        editor.apply();
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang.toLowerCase()));
        res.updateConfiguration(conf, dm);
        recreate();
    }
}