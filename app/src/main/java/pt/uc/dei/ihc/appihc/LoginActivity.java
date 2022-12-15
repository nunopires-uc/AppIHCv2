package pt.uc.dei.ihc.appihc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button LoginButton, RegisterButton;
    EditText EditTextEmail, EditTextPassword;
    private FirebaseAuth mAuth;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        EditTextEmail = findViewById(R.id.EditTextEmail);
        EditTextPassword = findViewById(R.id.EditTextPassword);



        //EditTextEmail.setText("nuno.pires@ubi.pt");
        //EditTextPassword.setText("pixarmovies");


        //EditTextEmail.setText("tangerina@ubi.pt");
        //EditTextPassword.setText("pixarmovies");

        //EditTextEmail.setText("xyz@email.com");
        //EditTextPassword.setText("mypassword");

        if(!canGetLocation()){
            showSettingsAlert();
        }

        EditTextEmail.setText("dani@sapo.pt");
        EditTextPassword.setText("lahaine");

        //nunopires@email.com
        //nunopires

        //EditTextEmail.setText("cris@germany.org");
        //EditTextPassword.setText("veteran");

        //EditTextEmail.setText("mr@robot.org");
        //EditTextPassword.setText("elliot");

        /*
        tangerina@ubi.pt
        pixarmovies

        dani@sapo.pt
        lahaine

        void@science.com
        universe

        xyz@email.com
        mypassword

        cris@germany.org
        veteran

        mr@robot.org
        elliot

         */


        mAuth = FirebaseAuth.getInstance();
        LoginButton = (Button) findViewById(R.id.ButtonLogin);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser(EditTextEmail.getText().toString(), EditTextPassword.getText().toString());
            }
        });

        tv1 = (TextView) findViewById(R.id.ButtonRegister);
        tv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent k = new Intent(LoginActivity.this, Register.class);
                startActivity(k);
                return true;
            }
        });

        /*RegisterButton = (Button) findViewById(R.id.ButtonRegisterNoBG);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
         */



    }

    private void LoginUser(String Email, String Password){
        if(TextUtils.isEmpty(Email)){
            Toast.makeText(getApplicationContext(),"Please enter an email", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(Password)){
            Toast.makeText(getApplicationContext(),"Please define a password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("oi?", "oi");
                    Intent k = new Intent(LoginActivity.this, MapsActivity.class);
                    startActivity(k);
                }else{
                    Toast.makeText(getApplicationContext(),"Login failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean canGetLocation() {
        boolean result = true;
        LocationManager lm;
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        return gpsEnabled && networkEnabled;
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Your gps isn\'t active");

        // Setting Dialog Message
        alertDialog.setMessage("Enable your gps services");

        // On pressing Settings button
        alertDialog.setPositiveButton(
                getResources().getString(R.string.button_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alertDialog.show();
    }
}