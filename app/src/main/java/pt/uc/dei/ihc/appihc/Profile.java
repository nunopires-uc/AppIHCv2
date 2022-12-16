package pt.uc.dei.ihc.appihc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    ListView listView;
    String TAG = "Profile;";
    private FirebaseAuth mAuth;

    ArrayList<String> ttl = new ArrayList<>();
    ArrayList<String> stl = new ArrayList<>();
    ArrayList<DefaultNote> myUserNotes = new ArrayList<>();
    ArrayList<QueryDocumentSnapshot> UserNotes = new ArrayList<QueryDocumentSnapshot>();
    TextView EmailUserField;
    BottomNavigationView bottomNavigationView;
    ImageView ProfilePicture;
    ImageButton settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        ProfilePicture = findViewById(R.id.ProfilePicture);
        ProfilePicture.setImageResource(R.drawable.profile_image);

        settings = findViewById(R.id.Settings);
        listView = findViewById(R.id.listview_id);
        EmailUserField = findViewById(R.id.userEmail);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        EmailUserField.setText(mAuth.getCurrentUser().getEmail());


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent k = new Intent(Profile.this, LoginActivity.class);
                startActivity(k);
            }
        });

        db.collection("notes")
                .whereEqualTo("Author", mAuth.getCurrentUser().getUid().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ttl.add((String) document.get("Title"));

                                String DescriptionShortner = (String) document.get("Description");
                                if(DescriptionShortner.length() > 16){
                                    DescriptionShortner = DescriptionShortner.substring(0, 16);
                                    DescriptionShortner += "...";
                                }

                                stl.add(DescriptionShortner);




                                DefaultNote currentNote = new DefaultNote();
                                currentNote.setAuthor((String) document.get("Author"));
                                currentNote.setCity((String)document.get("City"));
                                currentNote.setTitle((String)document.get("Title"));
                                currentNote.setCountry((String)document.get("Country"));
                                currentNote.setDescription((String)document.get("Description"));
                                currentNote.setAddress((String)document.get("Address"));
                                currentNote.setImageUrl((String)document.get("ImageUrl"));
                                currentNote.setLatitude((Double)document.get("Latitude"));
                                currentNote.setLongitude((Double)document.get("Longitude"));
                                currentNote.setPrivacy((Boolean) document.get("isPrivate"));
                                currentNote.setEmails((ArrayList<String>) document.get("contacts"));
                                myUserNotes.add(currentNote);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            ArrayList<Byte> privateValues = new ArrayList<>();
                            MyListAdapter adapter = new MyListAdapter(Profile.this, ttl, stl, privateValues);

                            listView.setAdapter(adapter);
                            Log.d(TAG, ttl.toString());
                            Log.d(TAG, stl.toString());

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.getMenu().getItem(0).setChecked(false);
        bottomNavigationView.getMenu().getItem(1).setChecked(false);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), Notification.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Profile.this, ttl.get(i), Toast.LENGTH_SHORT).show();
                Intent k = new Intent(Profile.this, ReadNote.class);
                Bundle args = new Bundle();


                args.putSerializable("@hn", (Serializable) myUserNotes.get(i));
                Log.i("getNote()", String.valueOf(myUserNotes.get(i)));
                k.putExtra("BUNDLE", args);
                startActivity(k);
            }
        });
        /*
        android:layout_alignParentBottom="true"

         */

    }
}