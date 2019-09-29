package com.example.mycontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class ContactDataSource {
    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    public  ContactDataSource(Context context) {
        dbHelper = new ContactDBHelper(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();

    }
    public boolean insertContact(Contact c) {
        boolean didSucceed = false;
        try {
            ContentValues initalValues = new ContentValues();

            initalValues.put("contactname",c.getContactName());
            initalValues.put("streetaddress", c.getStreetAddress());
            initalValues.put("city",c.getCity());
            initalValues.put("state",c.getState());
            initalValues.put("zipcode",c.getZipCode());
            initalValues.put("phonenumber",c.getPhoneNumber());
            initalValues.put("cellnumber",c.getCellNumber());
            initalValues.put("email",c.geteMail());
            initalValues.put("birthday",String.valueOf(c.getBirthday().getTimeInMillis()));
            initalValues.put("bestFriendForever", c.getBestFriendForever());

            System.out.println(c.getBestFriendForever());
            didSucceed = database.insert("contact",null,initalValues) > 0;
        }
        catch (Exception e) {

        }
        return didSucceed;
    }
    public boolean updateContact(Contact c) {
        boolean didSucceed = false;
        try {
            Long rowID = (long) c.getContactID();
            ContentValues updateVales = new ContentValues();

            updateVales.put("contactname",c.getContactName());
            updateVales.put("streetaddress", c.getStreetAddress());
            updateVales.put("city",c.getCity());
            updateVales.put("state",c.getState());
            updateVales.put("zipcode",c.getZipCode());
            updateVales.put("phonenumber",c.getPhoneNumber());
            updateVales.put("cellnumber",c.getCellNumber());
            updateVales.put("email",c.geteMail());
            updateVales.put("birthday",String.valueOf(c.getBirthday().getTimeInMillis()));
            updateVales.put("bestFriendForever", c.getBestFriendForever());

            didSucceed = database.update("contact",updateVales,"_id=" + rowID,null) > 0;
        }
        catch (Exception e) {

        }
        return didSucceed;
    }
    public int getLastContactId() {
        int lastId = -1;
        try {
            String query= "Select MAX(_id) from contact";
            Cursor cursor = database.rawQuery(query,null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch(Exception e) {
            lastId = -1;
        }
        return lastId;
    }
    public void updateAddress (ContactAddress c) {

        try {

            ContentValues updateAddress = new ContentValues();
            updateAddress.put("streetaddress", c.getStreet());
            updateAddress.put("city", c.getCity());
            updateAddress.put("state", c.getState());
            updateAddress.put("zipcode", c.getZipcode());
            database.update("contact",updateAddress,"_id=" + c.getContactID(),null);
        }
        catch (Exception e) {
           e.toString();
        }

        //testing
        System.out.println(c.getCity());
    }
    public ArrayList<String> getContactName() {
        ArrayList<String> contactNames = new ArrayList<>();

        try {
            String query = "Select contactname from contact";
            Cursor cursor = database.rawQuery(query,null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contactNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            contactNames = new ArrayList<>();
        }
        return contactNames;
    }
    public ArrayList<Contact> getContacts(String sortField, String sortOrder) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try {
            String query = "Select * from contact Order by " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query,null);

            Contact newContact;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newContact  = new Contact();
                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setStreetAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipCode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.seteMail(cursor.getString(8));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(calendar);
                newContact.setBestFriendForever(cursor.getInt(10));
                contacts.add(newContact);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            contacts = new ArrayList<Contact>();
        }
        return contacts;
    }
    public Contact getSpecificContact(int contactId) {
        Contact contact = new Contact();

            String query = "Select * from contact WHERE _id =" + contactId;
            Cursor cursor = database.rawQuery(query,null);

            if(cursor.moveToFirst()) {
                contact  = new Contact();
                contact.setContactID(cursor.getInt(0));
                contact.setContactName(cursor.getString(1));
                contact.setStreetAddress(cursor.getString(2));
                contact.setCity(cursor.getString(3));
                contact.setState(cursor.getString(4));
                contact.setZipCode(cursor.getString(5));
                contact.setPhoneNumber(cursor.getString(6));
                contact.setCellNumber(cursor.getString(7));
                contact.seteMail(cursor.getString(8));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                contact.setBirthday(calendar);
                contact.setBestFriendForever(cursor.getInt(10));

                cursor.close();
            }

        return contact;
    }
    public boolean deleteContact(int contactId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("contact", "_id=" + contactId,null) > 0;
        }
        catch (Exception e) {}
        System.out.print(didDelete);
        return didDelete;

    }
}
