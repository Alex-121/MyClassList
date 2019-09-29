package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ContactListActivity extends AppCompatActivity {
    ArrayList<Contact> contacts;
    boolean isDeleting = false;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initMapButton();
        initSettingButton();
        disableList();
        initItemClick();
        initAddContactButton();
        initDeleteButton();

    }
    @Override
    public void onResume() {
       super.onResume();
        String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield", "contactname");
        String orderBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
        String bgColor = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("color","grey");
        switch (bgColor) {
            case "grey":
                findViewById(R.id.Contacts).setBackgroundResource(R.color.grey);
                break;
            case "yellow":
                findViewById(R.id.Contacts).setBackgroundResource(R.color.yellow);
                break;
            case "purple":
                findViewById(R.id.Contacts).setBackgroundResource(R.color.purple);
                break;
            case "orange":
                findViewById(R.id.Contacts).setBackgroundResource(R.color.orange);
                break;
            case "red":
                findViewById(R.id.Contacts).setBackgroundResource(R.color.red);
                break;
            case "blue":
                findViewById(R.id.Contacts).setBackgroundResource(R.color.blue);
        }




        ContactDataSource ds = new ContactDataSource(this);

        try {
            ds.open();
            contacts = ds.getContacts(sortBy,orderBy);
            ds.close();

            if(contacts.size() > 0) {
                ListView listView = (ListView) findViewById(R.id.Contacts);
                adapter = new ContactAdapter(this,contacts);
                listView.setAdapter(adapter);}

            else {
                Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this,"Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
    }
    private void initMapButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, ContactMapActivity.class);
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
                Intent intent = new Intent(ContactListActivity.this, ContactSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void disableList() {
        ImageButton editList = (ImageButton) findViewById(R.id.imageButtonList);
        editList.setEnabled(false);
    }

    private void initItemClick() {
        ListView listView = (ListView) findViewById(R.id.Contacts);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact selectedContact = contacts.get(position);
                if(isDeleting) {
                    adapter.showDelete(position, view, ContactListActivity.this, selectedContact);
                }
                else
                {
                    Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
                    intent.putExtra("contactid",selectedContact.getContactID());
                    startActivity(intent);
                }
            }
        });
    }

    private void initAddContactButton() {
        Button newContact = (Button) findViewById(R.id.buttonAdd);
        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initDeleteButton() {
        final Button deleteButton = (Button) findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDeleting) {
                    deleteButton.setText("Delete");
                    isDeleting = false;
                    adapter.notifyDataSetChanged();
                }
                else {
                    deleteButton.setText("Done Deleting");
                    isDeleting = true;
                }
            }
        });
    }
}
