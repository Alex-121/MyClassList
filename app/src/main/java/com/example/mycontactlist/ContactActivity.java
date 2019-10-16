package com.example.mycontactlist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import androidx.support.v4.app.FragmentActivity;
//import androidx.support.v4.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
;

public class ContactActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    private Contact currentContact;
    final int PERMISSION_REQUEST_PHONE = 102;
    final int PERMISSION_REQUEST_CAMERA = 103;
    final int PERMISSION_REQUEST_TEXT = 104;
    final int CAMERA_REQUEST = 1888;

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
        initCallFunction();
        initImageButton();

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
        ImageButton picture = (ImageButton) findViewById(R.id.imageContact);

        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZip.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        buttonChange.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
        checkBox.setEnabled(enabled);
        picture.setEnabled(enabled);

        if(enabled) {
            editName.requestFocus();
            editPhone.setInputType(InputType.TYPE_CLASS_PHONE);
            editCell.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        else {

            ScrollView s = (ScrollView) findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);
            s.clearFocus();
            editPhone.setInputType(InputType.TYPE_NULL);
            editCell.setInputType(InputType.TYPE_NULL);
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
        ImageButton picture = (ImageButton) findViewById(R.id.imageContact);

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

        if (currentContact.getPicture()!=null)
            picture.setImageBitmap(currentContact.getPicture());
        else
            picture.setImageResource(R.drawable.photoicon);

    }

    private void initCallFunction() {
        EditText editPhone = (EditText) findViewById(R.id.editPhone);
        editPhone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                checkPhonePermission(currentContact.getPhoneNumber());
                return false;
            }
        });
        EditText editCell = (EditText) findViewById(R.id.editCell);
        editCell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                checkPhonePermission(currentContact.getCellNumber(), ("test"));
                return false;
            }
        });
    }

    private void checkPhonePermission(String phoneNumber) {
        if(Build.VERSION.SDK_INT >= 23) {
            if(ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(ContactActivity.this, Manifest.permission.CALL_PHONE)) {
                    Snackbar.make(findViewById(R.id.activity_contact), "MyContactList requires this permission to place a text from the app.",
                            Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(ContactActivity.this, new String[] {Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_TEXT);
                        }
                    }).show();
                }
                else {
                    ActivityCompat.requestPermissions(ContactActivity.this, new String[] {Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_TEXT);
                }
            }
            else
                callContact(phoneNumber);
        }
        else
            callContact(phoneNumber);
    }

    private void checkPhonePermission(String phoneNumber, String text) {
        if(Build.VERSION.SDK_INT >= 23) {
            if(ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(ContactActivity.this, Manifest.permission.SEND_SMS)) {
                    Snackbar.make(findViewById(R.id.activity_contact), "MyContactList requires this permission to place a text from the app.",
                            Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(ContactActivity.this, new String[] {Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_TEXT);
                        }
                    }).show();
                }
                else {
                    ActivityCompat.requestPermissions(ContactActivity.this, new String[] {Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_TEXT);
                }
            }
            else
                textContact(phoneNumber);
        }
        else
            textContact(phoneNumber);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permmissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_PHONE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ContactActivity.this, "You may now call from this app.",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(ContactActivity.this, "You will not be able to make calls from this app.", Toast.LENGTH_LONG).show();
            }
            case PERMISSION_REQUEST_CAMERA: {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    takePhoto();
                else
                    Toast.makeText(ContactActivity.this, "You will not be able to save contact pictures from this app", Toast.LENGTH_LONG).show();
            }
            case PERMISSION_REQUEST_TEXT:{
                if (grantResults.length >0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(ContactActivity.this,"You may now text from this app.",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ContactActivity.this, "You will not be able to text contacts from this app",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void callContact(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return ;
        }
        else
            startActivity(intent);
    }
    private void textContact(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("sms:" + phoneNumber));
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            return ;
        }
        else
            startActivity(intent);
    }

    private void initImageButton() {
        ImageButton ib = (ImageButton) findViewById(R.id.imageContact);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 23) {
                    if(ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(ContactActivity.this, Manifest.permission.CAMERA)) {
                            Snackbar.make(findViewById(R.id.activity_contact), "MyContactList requires this permission to take pictures.",
                                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(ContactActivity.this, new String[] {Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                                }
                            }).show();
                        }
                        else {
                            ActivityCompat.requestPermissions(ContactActivity.this, new String[] {Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                        }
                    }
                    else
                        takePhoto();
                }
                else
                    takePhoto();
            }
        });

    }

    public void takePhoto() {
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camerIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST)
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 144, 144, true);
                ImageButton imageContact = (ImageButton) findViewById(R.id.imageContact);
                imageContact.setImageBitmap(scaledPhoto);
                currentContact.setPicture(scaledPhoto);
            }
    }
}
