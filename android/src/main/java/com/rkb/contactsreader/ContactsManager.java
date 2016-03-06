package com.rkb.contactsreader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.ArrayList;

public class ContactsManager extends ReactContextBaseJavaModule {
    Context context;

    public ContactsManager(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    @Override
    public String getName() {
        return "ContactsAndroid";
    }

    @ReactMethod
    public void getContacts(Callback callback) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        WritableArray contacts = Arguments.createArray();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                WritableMap contact = Arguments.createMap();
                String contactId = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String displayName = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                contact.putString("contactId", contactId);
                if (displayName != null) {
                    contact.putString("displayName", displayName);
                }

                String thumbnailPath = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                if (thumbnailPath != null) {
                    contact.putString("thumbnailPath", thumbnailPath);
                }

                Cursor nameInfoCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = " + contactId, new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE}, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                while (nameInfoCursor.moveToNext()) {
                    String givenName = nameInfoCursor.getString(nameInfoCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    if (givenName != null) {
                        contact.putString("givenName", givenName);
                    }
                    String familyName = nameInfoCursor.getString(nameInfoCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    if (familyName != null) {
                        contact.putString("familyName", familyName);
                    }
                    String middleName = nameInfoCursor.getString(nameInfoCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                    if (middleName != null) {
                        contact.putString("middleName", middleName);
                    }
                    String suffix = nameInfoCursor.getString(nameInfoCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX));
                    if (suffix != null) {
                        contact.putString("suffix", suffix);
                    }
                    String prefix = nameInfoCursor.getString(nameInfoCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
                    if (prefix != null) {
                        contact.putString("prefix", prefix);
                    }
                }
                nameInfoCursor.close();

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId}, null);
                    WritableArray phoneNumbers = Arguments.createArray();
                    while (pCur.moveToNext()) {
                        String phoneNumber = pCur.getString(pCur.getColumnIndex(CommonDataKinds.Phone.NUMBER));
                        phoneNumbers.pushString(phoneNumber);
                    }
                    contact.putArray("phoneNumbers", phoneNumbers);
                    pCur.close();
                }
                contacts.pushMap(contact);
            }
        }
        callback.invoke(contacts);
    }
}
