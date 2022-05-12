package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditContactActivity extends AppCompatActivity implements Observer {

    private ContactList contact_list = new ContactList();
    private Contact contact;
    private EditText username;
    private EditText email;
    private Context context;
    private int pos;

    private ContactController contact_controller;
    private ContactListController contact_list_controller = new ContactListController(contact_list);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);

        Intent intent = getIntent();
        pos = intent.getIntExtra("position", 0);

        context = getApplicationContext();

        contact_list_controller.addObserver(this);
        contact_list_controller.loadContacts(context);
    }

    public void saveContact(View view) {

        String email_str = email.getText().toString();
        if (email_str.equals("")) {
            email.setError("Empty field!");
            return;
        }

        if (!email_str.contains("@")) {
            email.setError("Must be an email address!");
            return;
        }


        String username_str = username.getText().toString();
        String id = contact_controller.getId();

        // Check that username is unique AND username is changed
        // (Note: if username was not changed
        // then this should be fine, because it was already unique.)

        if (!contact_list_controller.isUsernameAvailable(username_str) &&
                !(contact_controller.getUsername().equals(username_str))) {
            username.setError("Username already taken!");
            return;
        }

        Contact updated_contact = new Contact(username_str, email_str, id);

        //edit contact
        boolean success = contact_list_controller.editContact(contact, updated_contact, context);
        if (!success) {
            return;
        }

        // End EditContactActivity
        finish();
    }

    public void deleteContact(View view) {
        // delete contact

        boolean success = contact_list_controller.deleteContact(contact, context);
        if (!success) {
            return;
        }
        // End EditContactActivity
        contact_list_controller.removeObserver(this);

        finish();
    }

    @Override
    public void update() {
        contact = contact_list_controller.getContact(pos);
        contact_controller = new ContactController(contact);

        username.setText(contact_controller.getUsername());
        email.setText(contact_controller.getEmail());

    }
}