package pt.uc.dei.ihc.appihc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class NoteList extends AppCompatActivity {


    ListView listView;
    String[] title = {};
    String[] subtitle = {};
    String DescriptionShortner;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    ArrayList<String> ttl = new ArrayList<>();

    ArrayList<String> stl = new ArrayList<>();

    ArrayList<Byte> PrivateValues = new ArrayList<>();

    ArrayList<DefaultNote> History = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_note_list);
        mAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listview_id);
        db = FirebaseFirestore.getInstance();
        db.collection("notifications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Intent intent = getIntent();
                    Bundle args = intent.getBundleExtra("BUNDLE");
                    MetaNote note = (MetaNote) args.getSerializable("@mn");
                    Log.i("ni!!", note.toString());
                    Log.i("ni[sizeof()]", String.valueOf(note.getHashNotes().size()));
                    String Description, Title, to, from;
                    boolean AreNotificationsAvailable = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        AreNotificationsAvailable = true;
                        Description = (String) document.get("Description");
                        Title = (String) document.get("Title");
                        to = (String) document.get("to");
                        from = (String) document.get("from");

                        for(int i=0;i<note.getHashNotes().size();i++){
                            if(!History.contains(note.getHashNotes().get(i))){
                                if(note.getHashNotes().get(i).isPrivacy()){
                                    if((to.equals(mAuth.getUid())
                                            && note.getHashNotes().get(i).getTitle().equals(Title)
                                            && note.getHashNotes().get(i).getAuthor().equals(from)) || (note.getHashNotes().get(i).getAuthor().equals(mAuth.getUid()))){


                                        ttl.add(note.getHashNotes().get(i).getTitle());
                                        DescriptionShortner = note.getHashNotes().get(i).getDescription();
                                        if(DescriptionShortner.length() > 16){
                                            DescriptionShortner = DescriptionShortner.substring(0, 16);
                                            DescriptionShortner += "...";
                                        }
                                        stl.add(DescriptionShortner);
                                        PrivateValues.add((byte) 1);
                                        History.add(note.getHashNotes().get(i));
                                    }
                                }else{
                                    ttl.add(note.getHashNotes().get(i).getTitle());
                                    DescriptionShortner = note.getHashNotes().get(i).getDescription();
                                    if(DescriptionShortner.length() > 16){
                                        DescriptionShortner = DescriptionShortner.substring(0, 16);
                                        DescriptionShortner += "...";
                                    }
                                    stl.add(DescriptionShortner);
                                    PrivateValues.add((byte) 0);
                                    History.add(note.getHashNotes().get(i));
                                }
                            }

                            Log.i("[ -> ni { i } ]", note.getHashNotes().get(i).toString());



                            //.substring(0, 16);

                            // ![x->@ y->@ z->@] = (x, y, z);
                            //title[i] = note.getHashNotes().get(i).getTitle();
                            //subtitle[i] = note.getHashNotes().get(i).getDescription();
                        }

                    }

                    if(AreNotificationsAvailable == false){
                        for(int i=0;i<note.getHashNotes().size();i++) {
                            ttl.add(note.getHashNotes().get(i).getTitle());
                            DescriptionShortner = note.getHashNotes().get(i).getDescription();
                            if(DescriptionShortner.length() > 16){
                                DescriptionShortner = DescriptionShortner.substring(0, 16);
                                DescriptionShortner += "...";
                            }
                            stl.add(DescriptionShortner);
                            PrivateValues.add((byte) 0);
                        }


                    }

                    MyListAdapter adapter = new MyListAdapter(NoteList.this, ttl, stl, PrivateValues);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(NoteList.this, ttl.get(i), Toast.LENGTH_SHORT).show();
                            Intent k = new Intent(NoteList.this, ReadNote.class);
                            Bundle args = new Bundle();
                            args.putSerializable("@hn", (Serializable) note.getHashNotes().get(i));
                            Log.i("getNote()", String.valueOf(note.getHashNotes().get(i)));
                            k.putExtra("BUNDLE", args);
                            startActivity(k);
                        }
                    });
                }
            }
        });


        //MetaNote note = getIntent().getParcelableExtra("@metanote");
        //









        //Alcançar o minimo de recursos para alcançar um fim. -> eficiência



    }
}