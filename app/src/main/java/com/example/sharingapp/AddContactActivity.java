package com.example.sharingapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {

    private ContactList contact_list = new ContactList();
    private Context context;
    private EditText username;
    private EditText email;

    private ContactListController contact_list_controller = new ContactListController(contact_list);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);

        context = getApplicationContext();

        contact_list_controller.loadContacts(context);
    }

    public void saveContact(View view) {
        String username_str = username.getText().toString();
        String email_str = email.getText().toString();

        if (username_str.equals("")){
            username.setError("Empty field!");
            return;
        }

        if (email_str.equals("")){
            email.setError("Empty field!");
            return;
        }

        if (!email_str.contains("@")){
            email.setError("Must be an email address!");
            return;
        }

        if (!contact_list_controller.isUsernameAvailable(username_str)){
            username.setError("Username already taken!");
            return;
        }


        Contact contact = new Contact(username_str, email_str, null);

        // add contact
        boolean success = contact_list_controller.addContact(contact, context);
        if (!success) {
            return;
        }

        // End AddContactActivity
        finish();
    }
}