package com.example.mycontactlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.AlphabeticIndex;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private ArrayList<Contact> items;
    private Context adapterContext;

    public ContactAdapter (Context context, ArrayList<Contact> items) {
        super(context,R.layout.list_item,items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            Contact contact = items.get(position);

            if(v == null) {
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item,null);
            }
            TextView contactName = (TextView) v.findViewById(R.id.contactName);
            TextView contactNumber = (TextView) v.findViewById(R.id.phoneNumber);
            TextView street = (TextView) v.findViewById(R.id.streetAddress);
            TextView city = (TextView) v.findViewById(R.id.city);
            TextView state = (TextView) v.findViewById(R.id.state);
            TextView zip = (TextView) v.findViewById(R.id.zipcode);
            Button b = (Button) v.findViewById(R.id.deleteContactButton);
            showStar(v,contact);

            if(position % 2 == 0)
                contactName.setTextColor(Color.parseColor("#ff0000"));
            contactName.setText(contact.getContactName());
            contactNumber.setText(contact.getPhoneNumber());
            zip.setText(contact.getZipCode());
            street.setText(contact.getStreetAddress());
            city.setText(contact.getCity());
            state.setText(contact.getState());

            b.setVisibility(View.INVISIBLE);
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }

    public void showDelete(final int position, final View convertView, final Context context, final Contact contact) {
        View v = convertView;
        final Button b = (Button) v.findViewById(R.id.deleteContactButton);
        if (b.getVisibility() == View.INVISIBLE) {
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideDelete(position, convertView,context);
                    items.remove(contact);
                    deleteOption(contact.getContactID(),context);
                }
            });

        }
        else
            hideDelete(position,convertView,context);
    }
    private void deleteOption(int contactToDelete, Context context) {
        ContactDataSource db = new ContactDataSource(context);
        try {
            db.open();
            db.deleteContact(contactToDelete);
            db.close();
        }
        catch (Exception e) {
            Toast.makeText(adapterContext,"Delete Contact Failed",Toast.LENGTH_LONG).show();
        }
        this.notifyDataSetChanged();
    }
    public void hideDelete(int position, View convertView, Context context) {
        View v = convertView;
        final Button b = (Button) v.findViewById(R.id.deleteContactButton);
        b.setVisibility(View.INVISIBLE);
        b.setOnClickListener(null);
    }
    public void showStar(View converView,Contact contact) {
        View v = converView;
        ImageView star = (ImageView) v.findViewById(R.id.Bff);
        if(contact.getBestFriendForever()==1)
            star.setVisibility(View.VISIBLE);
        else
            star.setVisibility(View.INVISIBLE);

    }
}
