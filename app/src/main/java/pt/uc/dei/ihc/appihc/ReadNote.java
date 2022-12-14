package pt.uc.dei.ihc.appihc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ReadNote extends AppCompatActivity {

    TextView NoteTitle, NoteDescription;
    String Title;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_read_note);

        NoteTitle = findViewById(R.id.NoteTitle);
        NoteDescription = findViewById(R.id.NoteDescription);

        firebaseFirestore = FirebaseFirestore.getInstance();

        /*
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Title = extras.getString("Title");
        }
         */

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        DefaultNote note = (DefaultNote) args.getSerializable("@hn");

        NoteTitle.setText(note.getTitle());
        NoteDescription.setText(note.getDescription());

        /*
        firebaseFirestore.collection("notes").whereEqualTo("Title", Title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NoteDescription.setText((CharSequence) document.get("Title"));
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/
    }
}