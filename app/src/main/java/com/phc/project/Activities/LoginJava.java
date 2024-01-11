package com.phc.project.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.phc.project.MainActivity;
import com.phc.project.R;
import com.phc.project.Utils.DBReferences;
import com.phc.project.Utils.Utils;

import io.paperdb.Paper;

public class LoginJava extends AppCompatActivity {

    public static final String TAG = "LoginJava";
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText email;
    private TextInputEditText password;
    private Button mButton;
    private Dialog dialog;
    TextView registerText;
    CheckBox checkBox;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_java);
        Paper.init(this);
        initViews();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(this, Register.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.enter, R.anim.exit);
//                finish();
            }
        });

    }

    private void validateFields() {
        boolean bool1 = validate(email, emailLayout);
        boolean bool2 = validate(password, passwordLayout);
        if (bool1 && bool2) {
            if (checkBox.isChecked()) {
                //signInAsDoctor();
            } else {
                signInAsPatient();
            }
        } else {
            Utils.Companion.showShortMessage(this, "Please fill all the fields...");
        }
    }

    private boolean validate(TextInputEditText email, TextInputLayout emailLayout) {
        if (email.getText().toString().trim().equals("") || TextUtils.isEmpty(email.getText().toString().trim())) {
            emailLayout.setBackgroundResource(R.drawable.bg_error);
            return false;
        }
        return true;
    }

    private void initViews() {
        registerText = findViewById(R.id.text_register);
        checkBox = findViewById(R.id.checkbox);
        mButton = findViewById(R.id.login);
        dialog = Utils.Companion.progressDialog(this);
        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);
        emailLayout = findViewById(R.id.edit_emaillayout);
        passwordLayout = findViewById(R.id.edit_passwordlayout);
        pref = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    private void sendUserToDoctorDashboard(String username, String phone, String email, String image,
                                           String address,
                                           String rating,
                                           String specialization,
                                           String status,
                                           String rate
    ) {

        Intent intent = new Intent(this, DoctorDashboard.class);
//        intent.putExtra("email", email)
//        intent.putExtra("username", username)
//        intent.putExtra("image", image)
//        intent.putExtra("address", address)
//        intent.putExtra("rating", rating)
//        intent.putExtra("specialization", specialization)
        intent.putExtra("status", status);
//        intent.putExtra("rate", rate)
//        intent.putExtra("phone", phone)
        editor.putBoolean("isUserLogedIn", true);
        editor.putString("email", email);
        Paper.book().write("email", email);
        Paper.book().write("status", status);
        editor.putString("phone", phone);
        editor.putString("address", address);
        editor.putBoolean("isDoctor", true);
        editor.putString("image", image);
        editor.putString("rating", rating);
        editor.putString("username", username);
        editor.putString("specialization", specialization);
        editor.commit();
        Log.d(TAG, "Status is " + status);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }

    private void sendUserToPatientDashboard(
            String username,
            String phone,
            String email,
            String image,
            String adresss
    ) {
        Intent intent = new Intent(this, MainActivity.class);
        editor.putBoolean("isUserLogedIn", true);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.putString("address", adresss);
        editor.putString("image", image);
        editor.putString("username", username);
        if (checkBox.isChecked()) {
            editor.putBoolean("isDoctor", true);
        } else {
            editor.putBoolean("isDoctor", false);
        }
        editor.commit();
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }

    private void signInAsPatient() {
        dialog.show();
        DBReferences.Companion.getPatientRef().orderByChild("email").equalTo(email.getText().toString().trim())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Utils.Companion.showShortMessage(
                                LoginJava.this,
                                "Something went wrong, please try later."
                        );
                        dialog.dismiss();
                    }
                });
    }
}



