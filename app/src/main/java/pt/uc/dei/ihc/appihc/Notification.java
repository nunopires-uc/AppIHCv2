package pt.uc.dei.ihc.appihc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> ttl = new ArrayList<>();
    ArrayList<String> stl = new ArrayList<>();
    ArrayList<String> dts = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        listView = findViewById(R.id.listview_id);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
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

        db.collection("notifications").whereEqualTo("to", FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                        if(documentSnapshot.exists()){
                            String uid = documentSnapshot.getId().toString();
                            ttl.add((String) documentSnapshot.get("Title"));

                            if(((String) documentSnapshot.get("Description")).length() > 16){
                                String Ds = (String) ((String) documentSnapshot.get("Description")).substring(0, 16);
                                Ds += "...";
                                stl.add(Ds);
                            }else {
                                stl.add((String) documentSnapshot.get("Description"));
                            }
                            dts.add(documentSnapshot.getTimestamp("Date").toDate().toString());
                        }




                    }
                    NotificationAdapter adapter = new NotificationAdapter(Notification.this, ttl, stl, dts);
                    listView.setAdapter(adapter);
                }
            }
        });
        //db.collection("users").whereEqualTo("Email", aMe.get(i).getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
    }
}