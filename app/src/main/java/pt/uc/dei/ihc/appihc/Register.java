package pt.uc.dei.ihc.appihc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {


    private Button ButtonRegister, ReturnLogin;
    private EditText EdtPassword, EdtEmail;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        ButtonRegister = findViewById(R.id.ButtonRegister);
        ReturnLogin = findViewById(R.id.ReturnLogin);
        EdtEmail = findViewById(R.id.EdtEmail);
        EdtPassword = findViewById(R.id.EdtPassword);
        progressBar = findViewById(R.id.progressbar);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ReturnLogin.setOnClickListener(view -> finish());
        ButtonRegister.setOnClickListener(view -> RegisterUser(EdtEmail.getText().toString(), EdtPassword.getText().toString()));
    }

     private void RegisterUser(String Email, String Password){
        progressBar.setVisibility(View.VISIBLE);

         Map<String, Object> NewUser = new HashMap<>();
         ArrayList<String> Contacts = new ArrayList<>();
         ArrayList<String> UserNotes = new ArrayList<>();
         ArrayList<String> Notifications = new ArrayList<>();

         NewUser.put("Contacts", Contacts);
         NewUser.put("UserNotes", UserNotes);
         NewUser.put("Notifications", Notifications);
         NewUser.put("Email", Email);


        if(TextUtils.isEmpty(Email)){
            Toast.makeText(getApplicationContext(),"Please enter an email", Toast.LENGTH_LONG).show();
            return;
        }

         if(TextUtils.isEmpty(Password)){
             Toast.makeText(getApplicationContext(),"Please define a password", Toast.LENGTH_LONG).show();
             return;
         }

         mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                     db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(NewUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                             Toast.makeText(getApplicationContext(),"Registration successfull", Toast.LENGTH_LONG).show();
                             progressBar.setVisibility(View.GONE);

                             /*mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     if(task.isSuccessful()){
                                         Log.i("oi?", "oi");
                                         Intent k = new Intent(Register.this, MapsActivity.class);
                                         startActivity(k);
                                         progressBar.setVisibility(View.GONE);
                                     }else{
                                         Toast.makeText(getApplicationContext(),"Login failed", Toast.LENGTH_LONG).show();
                                     }
                                 }
                             });*/


                         }
                     });
                 }else{
                     Toast.makeText(getApplicationContext(),"Email already registered", Toast.LENGTH_LONG).show();
                     progressBar.setVisibility(View.GONE);
                 }
             }
         });
        return;
    }
}