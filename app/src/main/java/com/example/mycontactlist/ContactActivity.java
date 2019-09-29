package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import androidx.support.v4.app.FragmentActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
//import androidx.support.v4.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;;
import android.location.Address;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.Calendar;

public class ContactActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    private Contact currentContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initListButton();
        initMapButton();
        initSettingButton();
        initToggleButton();
        initChangeDateButton();
        initTextChanged();
        initSaveButton();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            initContact(extras.getInt("contactid"));
        }
        else
            currentContact = new Contact();

        setForEditing(false);

    }
    private void initListButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initMapButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, ContactMapActivity.class);
                if(currentContact.getContactID() ==-1)
                    Toast.makeText(getBaseContext(),"Contact must be saved before it can be mapped",
                            Toast.LENGTH_LONG).show();
                else
                    intent.putExtra("contactid", currentContact.getContactID());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initSettingButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, ContactSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton)findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }
        });
    }
    private void setForEditing(boolean enabled) {
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editAddress = (EditText)findViewById(R.id.editAddress);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        EditText editState = (EditText) findViewById(R.id.editState);
        EditText editZip = (EditText) findViewById(R.id.editZipcode);
        EditText editPhone = (EditText) findViewById(R.id.editPhone);
        EditText editCell = (EditText) findViewById(R.id.editCell);
        EditText editEmail = (EditText) findViewById(R.id.editEMail);
        Button buttonChange = (Button) findViewById(R.id.btnBirthday);
        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZip.setEnabled(enabled);
        editPhone.setEnabled(enabled);
        editCell.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        buttonChange.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
        checkBox.setEnabled(enabled);

        if(enabled) {
            editName.requestFocus();
        }
        else {
            ScrollView s = (ScrollView) findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);
            s.clearFocus();
        }
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        TextView birthDay = (TextView) findViewById(R.id.textBirthday);
        birthDay.setText(DateFormat.format("MM/dd/yyyy", selectedTime.getTimeInMillis()).toString());
        currentContact.setBirthday(selectedTime);
    }
    private void initChangeDateButton() {
        Button changeDate = (Button) findViewById(R.id.btnBirthday);
        changeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm,"DatePick");
            }
        });
    }

    private void initTextChanged() {
        final EditText etContactName = (EditText) findViewById(R.id.editName);
        etContactName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setContactName(etContactName.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        final EditText etStreetAddress = (EditText) findViewById(R.id.editAddress);
        etStreetAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentContact.setStreetAddress(etStreetAddress.getText().toString());
            }
        });

        final EditText etCity = (EditText) findViewById(R.id.editCity);
        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentContact.setCity(etCity.getText().toString());
            }
        });

        final EditText etState = (EditText) findViewById(R.id.editState);
        etState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentContact.setState(etState.getText().toString());
            }
        });

        final EditText etZipcode = (EditText) findViewById(R.id.editZipcode);
        etZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentContact.setZipCode(etZipcode.getText().toString());
            }
        });

        final EditText etPhoneNumber = (EditText) findViewById(R.id.editPhone);
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentContact.setPhoneNumber(etPhoneNumber.getText().toString());
            }
        });
        final EditText etCellPhone = (EditText) findViewById(R.id.editCell);
        etCellPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentContact.setCellNumber(etCellPhone.getText().toString());
            }
        });
        final EditText etEMail = (EditText) findViewById(R.id.editEMail);
        etEMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentContact.seteMail(etEMail.getText().toString());
            }

        });

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkBox.isChecked())
                    currentContact.setBestFriendForever(1);
                else
                    currentContact.setBestFriendForever(0);
            }
        });

        etPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etCellPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private void initSaveButton() {
        Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                boolean wasSucessful = false;
                ContactDataSource ds = new ContactDataSource(ContactActivity.this);
                try{
                    ds.open();
                    if (currentContact.getContactID() ==-1) {
                        wasSucessful = ds.insertContact(currentContact);
                        if (wasSucessful) {
                            int newId=ds.getLastContactId();
                            currentContact.setContactID(newId);
                        }
                    }
                    else
                        wasSucessful = ds.updateContact(currentContact);
                    ds.close();
                }
                catch (Exception e) {
                    wasSucessful = false;
                }

                if(wasSucessful) {
                    ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editName = (EditText) findViewById(R.id.editName);
        imm.hideSoftInputFromWindow(editName.getWindowToken(),0);
        EditText editAddress = (EditText) findViewById(R.id.editAddress);
        imm.hideSoftInputFromWindow(editName.getWindowToken(),0);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        imm.hideSoftInputFromWindow(editName.getWindowToken(),0);
        EditText editState = (EditText) findViewById(R.id.editState);
        imm.hideSoftInputFromWindow(editName.getWindowToken(),0);
        EditText editZipcode = (EditText) findViewById(R.id.editZipcode);
        imm.hideSoftInputFromWindow(editName.getWindowToken(),0);
        EditText editPhone = (EditText) findViewById(R.id.editPhone);
        imm.hideSoftInputFromWindow(editName.getWindowToken(),0);
        EditText editCell = (EditText) findViewById(R.id.editCell);
        imm.hideSoftInputFromWindow(editName.getWindowToken(),0);
        EditText editEMail = (EditText) findViewById(R.id.editEMail);
        imm.hideSoftInputFromWindow(editName.getWindowToken(),0);

    }

    private void initContact(int id) {
        ContactDataSource ds = new ContactDataSource(ContactActivity.this);
        try {
            ds.open();
            currentContact = ds.getSpecificContact(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this,"Load Contact Failed", Toast.LENGTH_LONG).show();
        }
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editAddress = (EditText)findViewById(R.id.editAddress);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        EditText editState = (EditText) findViewById(R.id.editState);
        EditText editZip = (EditText) findViewById(R.id.editZipcode);
        EditText editPhone = (EditText) findViewById(R.id.editPhone);
        EditText editCell = (EditText) findViewById(R.id.editCell);
        EditText editEmail = (EditText) findViewById(R.id.editEMail);
        TextView birthDay = (TextView) findViewById(R.id.textBday);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        editName.setText(currentContact.getContactName());
        editAddress.setText(currentContact.getStreetAddress());
        editCity.setText(currentContact.getCity());
        editState.setText(currentContact.getCity());
        editZip.setText(currentContact.getZipCode());
        editPhone.setText(currentContact.getPhoneNumber());
        editCell.setText(currentContact.getCellNumber());
        editEmail.setText(currentContact.geteMail());
        birthDay.setText(DateFormat.format("MM/dd/yy", currentContact.getBirthday().getTimeInMillis()).toString());
        if(currentContact.getBestFriendForever() == 1)
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);
    }
}
