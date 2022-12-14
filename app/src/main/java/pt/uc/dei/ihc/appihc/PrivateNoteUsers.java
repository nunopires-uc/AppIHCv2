package pt.uc.dei.ihc.appihc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrivateNoteUsers extends AppCompatActivity {

    ListView listView;
    ArrayList<String> Contacts = new ArrayList<>();
    private FirebaseFirestore db;
    ArrayList<String> newContacts = new ArrayList<>();
    ArrayList<String> selectedContacts = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Button joinUsers;
    EditText AddUser;


    /*@Override
    public boolean onnewCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.list_checked, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_done){
            String itemSelected = "Selected item: \n";
            for(int i=0;i<listView.getCount();i++){
                Toast.makeText(this, itemSelected, Toast.LENGTH_SHORT).show();
                if(listView.isItemChecked(i)){
                    itemSelected += listView.getItemAtPosition(i) + "\n";
                    selectedContacts.add((String) listView.getItemAtPosition(i));
                }
            }
            Toast.makeText(this, itemSelected, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //-getSupportActionBar().hide();
        setContentView(R.layout.activity_private_note_users);

        db = FirebaseFirestore.getInstance();

        joinUsers = findViewById(R.id.joinUsers);
        AddUser = findViewById(R.id.AddUser);
        listView = findViewById(R.id.listview);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Contacts = (ArrayList<String>) doc.get("Contacts");
                        for(int i=0;i<Contacts.size();i++){
                            Contacts.set(i, Contacts.get(i).replace(" ", "").replace("\n", ""));
                        }
                        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, Contacts);
                        listView.setAdapter(adapter);
                        Log.d("@@..", Contacts.toString());
                        /*if(Contacts.get(0).equals("")){
                            Log.i("@_NoContacts", "true");
                        }*/
                    }
                }
            }
        });



        //get from
        //Contacts = new ArrayList<>();
        //Contacts.add("hello@email.com");
        //Contacts.add("xta@email.com");
        //Contacts.add("crsa@email.com");



        AddUser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                    Contacts.add(AddUser.getText().toString());
                    newContacts.add(AddUser.getText().toString());
                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, Contacts);
                    listView.setAdapter(adapter);
                    AddUser.getText().clear();
                }
                return false;
            }
        });

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar.make(view, listView.getItemAtPosition(i).toString(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                Toast.makeText(getApplicationContext(), listView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        joinUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pos = listView.getCheckedItemPositions().toString().replace("{", "").replace("}", "").replace(" ", "");
                if(pos.contains(",")){
                    for(int i=0;i<pos.split(",").length;i++){
                        Log.i("@_dx", pos.split(",")[i].split("=")[1]);
                        if(pos.split(",")[i].split("=")[1].equals("true")){
                            selectedContacts.add((String) listView.getItemAtPosition(Integer.parseInt(pos.split(",")[i].split("=")[0])));
                        }
                    }
                }else{
                    String idx = pos.split("=")[0];
                    Log.i("@_cx", pos.split("=")[0]);
                    try{
                        selectedContacts.add((String) listView.getItemAtPosition(Integer.parseInt(idx)));
                    }catch(Exception e){

                    }

                }

                /*Intent data = new Intent();
                data.setData(Uri.parse(selectedContacts.toString()));
                setResult(RESULT_OK, data);
                finish();*/




                DocumentReference washingtonRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Log.i("@@size", String.valueOf(newContacts.size()));
                Log.i("@@size", String.valueOf(Contacts.size()));




                for(int i=0;i<newContacts.size();i++){
                    String Email = newContacts.get(i).replace(" ", "");
                    if(Contacts.contains(Email)){
                        Log.i("@@xx", "true");
                    }

                    Log.i("@@hbh", "..");
                    //if(!Contacts.contains(newContacts.get(i).replace(" ", "")) || (Contacts.size() == 0)){
                        Log.i("@@cnc", "!-!");
                        washingtonRef.update("Contacts", FieldValue.arrayUnion(newContacts.get(i).replace(" ", ""))).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("@_itemList",listView.getCheckedItemPositions().toString());
                                Log.i("@itemList", String.valueOf(listView.getCheckedItemCount()));

                                //String[] pos = listView.getCheckedItemPositions().toString().replace("{", "").replace("}", "").split(",");

                                String pos = listView.getCheckedItemPositions().toString().replace("{", "").replace("}", "").replace(" ", "");
                                if(pos.contains(",")){
                                    for(int i=0;i<pos.split(",").length;i++){
                                        Log.i("@_dx", pos.split(",")[i].split("=")[1]);
                                        if(pos.split(",")[i].split("=")[1].equals("true")){
                                            selectedContacts.add((String) listView.getItemAtPosition(Integer.parseInt(pos.split(",")[i].split("=")[0])));
                                        }
                                    }
                                }else{
                                    String idx = pos.split("=")[0];
                                    Log.i("@_cx", pos.split("=")[0]);
                                    selectedContacts.add((String) listView.getItemAtPosition(Integer.parseInt(idx)));
                                }
                            }
                        });
                    //}
                }




                Intent data = new Intent();
                data.setData(Uri.parse(selectedContacts.toString()));
                setResult(RESULT_OK, data);
                finish();





                // ,m,; kk; klk; (;;){}
                //insert new contacts \_||-@_/ { M } \_@-\\_|
                //. . . . . . . . . . . . . . . . . . . . . . . .
                // . . . . . . . . . . . . . . . . . . . . . . . .
                //. . . . . . . . . . . . . . . . . . . . . . . . .
                // . . . . . . . . . . . . . . . . . . . . . . . . .
                //. . . . . . . . . . . . . . . . . . . . . . . . . .
                // . . . . . . . . . . . . . . . . . . . . . . . . . .
                // . . . . . . . . . . . . . . . . . . . . . . . . . . .
                // . . . . . . . . . . . . . . . . . . . . . . . . . . . .
                //. . . . . . . . . . . . . . . . . . . . . . . . . . . . .
                //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
                // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .



                /*
                Log.i("@_contacts", selectedContacts.toString());
                Log.i("@itemList", String.valueOf(pos));
                for(int i=0;i<listView.getCheckedItemCount()-1;i++){

                }
                //for(int i=0;i<listView.getCheckedItemPositions())                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
                SparseBooleanArray sp = listView.getCheckedItemPositions();
                Log.i("@list", Contacts.toString());
                for(int i = 0; i < listView.getCheckedItemCount(); i++){
                    Log.i("@sp", String.valueOf(sp.valueAt(i)));
                    if(sp.valueAt(i) == true){
                        selectedContacts.add((String) listView.getItemAtPosition(i));
                    }
                }
                Toast.makeText(getApplicationContext(), selectedContacts.toString(), Toast.LENGTH_SHORT).show();*/
                //onOptionsItemSelected();
            }
        });
        //ArrayAdapter adapter = new ArrayAdapter(PrivateNoteUsers.this, android.R.layout.simple_list_item_1, Contacts);
        //listView.setAdapter(adapter);
    }


}