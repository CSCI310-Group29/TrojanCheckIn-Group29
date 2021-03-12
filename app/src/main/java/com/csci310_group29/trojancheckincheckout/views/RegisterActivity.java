package com.csci310_group29.trojancheckincheckout.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.csci310_group29.trojancheckincheckout.R;
import com.csci310_group29.trojancheckincheckout.data.models.User;
import com.csci310_group29.trojancheckincheckout.data.remote.AuthRemoteDataSource;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private AuthRemoteDataSource authRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authRepo = new AuthRemoteDataSource();

        Spinner spinner = (Spinner) findViewById(R.id.major_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.majors_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner1 = (Spinner) findViewById(R.id.accountTypeSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.account_type, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

    }

    private void onRegister(View view) {
        TextInputEditText firstNameText = findViewById(R.id.NameInput);
        EditText emailText = findViewById(R.id.EmailInput);
        EditText passwordText = findViewById(R.id.PasswordInput);
        EditText lastNameText = findViewById(R.id.LastNameInput);
        EditText SIDText = findViewById(R.id.idInput);
        String first = firstNameText.getText().toString();
        String last = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String pass = passwordText.getText().toString();
        String id = SIDText.getText().toString();


        Spinner mSpinner = findViewById(R.id.major_spinner);
        String major =  mSpinner.getSelectedItem().toString();
        Spinner tSpinner = findViewById(R.id.accountTypeSpinner);
        String type =  mSpinner.getSelectedItem().toString();
        Boolean isStudent = (type.equals("Student")) ? true : false;

        User newUser = new User(null,isStudent,first,last,major,id);


        authRepo.createUser(email,pass,newUser);


    }
}