package pt.uc.dei.ihc.appihc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LandingPage extends AppCompatActivity {

    Button Start;
    ProgressBar progressBar;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_landing_page);

        //progressBar = findViewById(R.id.progressBar);
        //firebaseFirestore = FirebaseFirestore.getInstance();

        //FirebaseAuth.getInstance().getCurrentUser().getUid() -> get user

        //fStore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener

        /*ArrayList<DefaultNote> NearByNotes = new ArrayList<>();

        firebaseFirestore.collection("notes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE); // Hide Progress bar
                if (task.isSuccessful() && task.getResult() != null) {
                    Intent k = new Intent(LandingPage.this, LoginActivity.class);
                    startActivity(k);
                }else{
                    Toast.makeText(LandingPage.this, "Currently logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });*/



        Start = (Button) findViewById(R.id.ButtonStart);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(LandingPage.this, LoginActivity.class);
                startActivity(k);
            }
        });
    }
}