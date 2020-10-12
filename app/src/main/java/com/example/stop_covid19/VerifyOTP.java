package com.example.stop_covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.stop_covid19.Databasehelper.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    PinView pinView;
    String codeBySystem;
    String phoneNo,firstname,lastname,email,password, whattodo;
    ImageView cancel_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        //hooks

        pinView= findViewById(R.id.pinview_otp);
        cancel_image= findViewById(R.id.cancel_image);

        //Get all the data from intent
        firstname = getIntent().getStringExtra("firstname");
        lastname = getIntent().getStringExtra("lastname");
         email = getIntent().getStringExtra("email");
         password = getIntent().getStringExtra("password");
         phoneNo = getIntent().getStringExtra("phoneno");


         whattodo = getIntent().getStringExtra("Whattodo");  //we are getting this from forgot password activity

        sendVerificationCodeToUser(phoneNo);

        cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                finish();
            }
        });

    }

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeBySystem = s;   //now we have the otp ocde inside the global variable
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();  //basically this code is system inter code

            if (code!=null){

                pinView.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {

    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem,code);
    signInWithPhoneAuthCredential(credential);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(VerifyOTP.this, "Verification  Completed.", Toast.LENGTH_LONG).show();

                            if (whattodo.equals("updateData")){
                                updateOldUsersData();
                                finish();
                            }
                            else if (whattodo.equals("createNewUser")){
                                storeNewUsersData();
                                //startActivity(new Intent(VerifyOTP.this,LoginScreen.class));
                                finish();
                            }
                        }

                        else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                                Toast.makeText(VerifyOTP.this, "Verification Not Completed! Try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

    }

    private void updateOldUsersData(){

        Intent intent = new Intent(getApplicationContext(), SetnewPassword.class);
        intent.putExtra("phoneno",phoneNo);
        startActivity(intent);
        finish();

    }

    private void storeNewUsersData() {

        FirebaseDatabase rootnode = FirebaseDatabase.getInstance();   //basically this line is saying that compiler start pointing to the database.

    DatabaseReference reference = rootnode.getReference("Users") ;  //to pointing the another table or you can say reference you need to do this

        UserHelperClass userHelperClass = new UserHelperClass(firstname,lastname,email,password,phoneNo);

        reference.child(phoneNo).setValue(userHelperClass);

    }

    public void callNextScreenFromOTP(View view){  //if the user manually enter the code

        String code = pinView.getText().toString().trim();

        if (!code.isEmpty()){
            verifyCode(code);
        }
    }


}