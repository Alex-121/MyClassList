package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

public class ContactSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_settings);
        initMapButton();
        initListButton();
        initSettingButton();
        initSettings();
        initSortByClick();
        initSortOrderClick();
        initBackgroundClick();
    }
    private void initListButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactSettingsActivity.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initMapButton() {
        ImageButton ibMap = (ImageButton) findViewById(R.id.imageButtonMap);
        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactSettingsActivity.this, ContactMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initSettingButton() {
        ImageButton ibSettings = (ImageButton) findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactSettingsActivity.this, ContactSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ImageButton editSettings = (ImageButton) findViewById(R.id.imageButtonSettings);
                editSettings.setEnabled(false);
            }
        });
    }
    private void initSettings() {
        String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield","contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).getString("sortorder", "ASC");
        String bgColor = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("color","grey");

        RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
        RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);
        RadioButton rbBirthday = (RadioButton) findViewById(R.id.radioBirthday);

        if(sortBy.equalsIgnoreCase("contactname"))
            rbName.setChecked(true);

        else if(sortBy.equalsIgnoreCase("city"))
            rbCity.setChecked(true);

        else
            rbBirthday.setChecked(true);

        RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
        RadioButton rbDescending =(RadioButton) findViewById(R.id.radioDescending);

        if(sortOrder.equalsIgnoreCase("ASC"))
            rbAscending.setChecked(true);
        else
            rbDescending.setChecked(true);

        RadioButton rbGrey = (RadioButton) findViewById(R.id.radioGrey);
        RadioButton rbYellow = (RadioButton) findViewById(R.id.radioYellow);
        RadioButton rbPurple = (RadioButton) findViewById(R.id.radioPurple);
        RadioButton rbRed = (RadioButton) findViewById(R.id.radioRed);
        RadioButton rbBlue = (RadioButton) findViewById(R.id.radioBlue);
        RadioButton rbOrange = (RadioButton) findViewById(R.id.radioOrange);

        switch (bgColor) {
            case "grey": rbGrey.setChecked(true);
                 findViewById(R.id.scrollView1).setBackgroundResource(R.color.grey);break;
            case "yellow":  rbYellow.setChecked(true);
                findViewById(R.id.scrollView1).setBackgroundResource(R.color.yellow); break;
            case "purple":rbPurple.setChecked(true);
                findViewById(R.id.scrollView1).setBackgroundResource(R.color.purple); break;
            case "orange":rbOrange.setChecked(true);
                findViewById(R.id.scrollView1).setBackgroundResource(R.color.orange); break;
            case"red":rbRed.setChecked(true);
                findViewById(R.id.scrollView1).setBackgroundResource(R.color.red); break;
            case"blue":rbBlue.setChecked(true);
                findViewById(R.id.scrollView1).setBackgroundResource(R.color.blue);

        }

    }
    private void initSortByClick() {
        RadioGroup rgSortBy = (RadioGroup) findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
                RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);
                if (rbName.isChecked())
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).
                            edit().putString("sortfield","contactname").commit();
                else if (rbCity.isChecked())
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE)
                            .edit().putString("sortfield","city").commit();
                else
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE)
                            .edit().putString("sortfield","birthday").commit();
            }
        });
    }
    private void initSortOrderClick() {
        RadioGroup rgSortOrder = (RadioGroup) findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
                if(rbAscending.isChecked())
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE)
                            .edit().putString("sortorder", "ASC").commit();
                else
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE)
                            .edit().putString("sortorder","DESC").commit();
            }
        });
    }
    private void initBackgroundClick() {
        RadioGroup rgBackground = (RadioGroup) findViewById(R.id.radioGroupBackground);
        rgBackground.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbGrey = (RadioButton) findViewById(R.id.radioGrey);
                RadioButton rbYellow = (RadioButton) findViewById(R.id.radioYellow);
                RadioButton rbPurple = (RadioButton) findViewById(R.id.radioPurple);
                RadioButton rbRed = (RadioButton) findViewById(R.id.radioRed);
                RadioButton rbBlue = (RadioButton) findViewById(R.id.radioBlue);
                RadioButton rbOrange = (RadioButton) findViewById(R.id.radioOrange);

                if(rbGrey.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).
                            edit().putString("color","grey").commit();
                    findViewById(R.id.scrollView1).setBackgroundResource(R.color.grey);}
                else if (rbPurple.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).
                            edit().putString("color","purple").commit();
                    findViewById(R.id.scrollView1).setBackgroundResource(R.color.purple);}
                else if (rbYellow.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).
                            edit().putString("color","yellow").commit();
                    findViewById(R.id.scrollView1).setBackgroundResource(R.color.yellow);}
                else if (rbOrange.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).
                            edit().putString("color","orange").commit();
                    findViewById(R.id.scrollView1).setBackgroundResource(R.color.orange);}
                else if (rbBlue.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).
                            edit().putString("color","blue").commit();
                    findViewById(R.id.scrollView1).setBackgroundResource(R.color.blue);}
                else{
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).
                            edit().putString("color","red").commit();
                    findViewById(R.id.scrollView1).setBackgroundResource(R.color.red);}

            }
        });
    }

}
