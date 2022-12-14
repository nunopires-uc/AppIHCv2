package pt.uc.dei.ihc.appihc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import pt.uc.dei.ihc.appihc.databinding.ActivityMainBinding;


/*
Documentation
https://github.com/firebase/snippets-android/blob/5a26948011a816fe047348b6d54d46ec3d280795/auth/app/src/main/java/com/google/firebase/quickstart/auth/MainActivity.java#L354-L363
 */

public class MainActivity extends AppCompatActivity {

    /*
    Button loginButton;
    EditText EditTextEmail, EditTextPassword;

    private FirebaseAuth mAuth;

    private static final String TAG = "MainActivity";
     */

    ActivityMainBinding binding;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;


                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.notification:
                        break;
                }
                return false;
            }
        });

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        //replaceFragment(new HomeFragment());

        /*
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    Log.i("Home", "clicked");
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.notification:
                    replaceFragment(new NotificationsFragment());
                    break;
            }
            return true;
        });
        */
        /*
        mAuth = FirebaseAuth.getInstance();

        loginButton = (Button) findViewById(R.id.b1);
        EditTextEmail = (EditText) findViewById(R.id.text1);
        EditTextPassword = (EditText) findViewById(R.id.text2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Simple Button 1", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                //signIn(EditTextEmail.getText().toString(), EditTextPassword.getText().toString());
            }
        });*/
    }

    /*
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }*/

    /*
    public void checkCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){

        }else{

        }
    }*/

    /*
    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail:sucess");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d(TAG, user.getEmail());
                    //startActivity(new Intent(MainActivity.this, NavigationActivity.class));
                    startActivity(new Intent(MainActivity.this, InsertNoteActivity.class));
                }else{
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/
}