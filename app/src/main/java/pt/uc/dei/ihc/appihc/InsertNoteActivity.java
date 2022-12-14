package pt.uc.dei.ihc.appihc;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.CaseMap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firestore.v1.WriteResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InsertNoteActivity extends AppCompatActivity implements BaseGpsListener{

    private FirebaseFirestore firebaseFirestore;
    private String TAG = "InsertNoteActivity";
    private FirebaseAuth mAuth;
    private int numCity;


    EditText NoteTitle, NoteDescription;
    Button CreateNote, PrivateNote;
    ImageButton LoadImage;
    ImageButton ReturnButton;
    boolean setPrivateNote = false;

    Double Latitude, Longitude;
    String City, Country, Address, SelectedEmails;

    ArrayList<MetaEmail> aMe = new ArrayList<>();

    FusedLocationProviderClient fusedLocationProviderClient;
    public static final int REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        showLocation();
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_insert_note);

        mAuth = FirebaseAuth.getInstance();

        LoadImage = (ImageButton)findViewById(R.id.LoadImage);
        LoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });

        /*ReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        firebaseFirestore = FirebaseFirestore.getInstance();

        NoteTitle = (EditText) findViewById(R.id.NoteTitle);
        NoteTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocation();
                Log.i("uU-Lat", String.valueOf(Latitude));
                Log.i("uU-Lon", String.valueOf(Longitude));
            }
        });
        NoteDescription = (EditText) findViewById(R.id.NoteDescription);
        NoteDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocation();
                Log.i("uU-Lat", String.valueOf(Latitude));
                Log.i("uU-Lon", String.valueOf(Longitude));
            }
        });

        LoadImage = (ImageButton) findViewById(R.id.LoadImage);
        PrivateNote = (Button) findViewById(R.id.PrivateNote);
        CreateNote = (Button) findViewById(R.id.CreateNote);
        ReturnButton = (ImageButton) findViewById(R.id.ReturnButton);
        ReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                    }
                });



        PrivateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(InsertNoteActivity.this, PrivateNoteUsers.class);
                //startActivity(k);
                startActivityForResult(k, 23);
            }
        });



        /*
        PrivateNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setPrivateNote = true;
                } else {
                    // The toggle is disabled
                }
            }
        });*/


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showLocation();


                if(Latitude == null){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            showLocation();
                        }
                    }, 5000);
                }else {
                    Log.i("MeinLat", String.valueOf(Latitude));
                    Log.i("MeinLon", String.valueOf(Longitude));

                    //InsertNote();
                    //FindLocation();

                    Log.i("##", ".INA");
                    for (int i = 0; i < aMe.size(); i++) {
                        Log.i("@@->", aMe.get(i).getEmail().toString());
                    }

                    for (int i = 0; i < aMe.size(); i++) {

                        db.collection("users").whereEqualTo("Email", aMe.get(i).getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Log.d("@e", "QW");
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        String uid = documentSnapshot.getId().toString();
                                        Log.i("@uid", uid);
                                        if (!uid.equals("")) {
                                            Map<String, Object> NewNotification = new HashMap<>();
                                            NewNotification.put("Title", NoteTitle.getText().toString());
                                            NewNotification.put("Description", NoteDescription.getText().toString());
                                            NewNotification.put("Date", FieldValue.serverTimestamp());
                                            NewNotification.put("from", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                            NewNotification.put("to", uid);
                                            db.collection("notifications").add(NewNotification).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                                }
                                            });
                                            //FieldValue.serverTimestamp()
                                        }
                                    }
                                }
                            }
                        });


                        // = getIDbyEmail();


                    }


                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        //Latitude = extras.getDouble("Latitude");
                        //Longitude = extras.getDouble("Longitude");
                        City = extras.getString("City");
                        Country = extras.getString("Country");
                        Address = extras.getString("Address");

                        //The key argument here must match that used in the other activity

                        Log.i("~get|Latitude:", String.valueOf(Latitude));
                        Log.i("~get|Longitude:", String.valueOf(Longitude));
                        Log.i("$#City:", City);
                        Log.i("$#Country:", Country);
                        Log.i("$#Address", Address);

                        boolean setPrivate = false;
                        if (aMe.size() > 0) {
                            setPrivate = true;
                        }

                        InsertNote(Country, City, Address, Latitude, Longitude, setPrivate);


                    }
                }
            }
        });

        LoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void UploadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        launcher.launch(intent);

        //startActivityForResult();
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    Uri phtoUri = result.getData().getData();
                }
            });

    private void FindLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        List<Address> addressList = null;
                        Geocoder geocoder = new Geocoder(InsertNoteActivity.this, Locale.getDefault());
                        try {
                            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.i("$#Latitude", String.valueOf(addressList.get(0).getLatitude()));
                        Log.i("$#Longitude", String.valueOf(addressList.get(0).getLongitude()));
                        Log.i("$#City", String.valueOf(addressList.get(0).getLocality()));
                        Log.i("$#Country", String.valueOf(addressList.get(0).getCountryName()));

                    }
                }
            });
        }else{
            AskForPermission();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23) {
            if (resultCode == RESULT_OK) {
                SelectedEmails = data.getData().toString().replace("[", "").replace("]", "").replace("\n", "").replace(" ", "").trim();
                Log.d("@@", SelectedEmails);
                Log.d("@@", SelectedEmails.split(",").toString());
                if(SelectedEmails.split(",").length > 0){
                    for(int i=0;i<SelectedEmails.split(",").length;i++){
                        MetaEmail me = new MetaEmail(SelectedEmails.split(",")[i]);
                        aMe.add(me);
                    }
                }else{
                    MetaEmail me = new MetaEmail(SelectedEmails);
                    aMe.add(me);
                }
                Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void AskForPermission(){
        ActivityCompat.requestPermissions(InsertNoteActivity.this, new String[]{
               Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                FindLocation();
            }else{
                Toast.makeText(InsertNoteActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void InsertNote(String Country, String City, String Address, Double Latitude, Double Longitude, boolean isPrivate){
        /*
        Title:
        Description:
        Image: https://cdn-icons-png.flaticon.com/512/5110/5110429.png
        Author:
        City:
        Address:
        Country:
        Latitude:
        Longitude:
        isPrivate;
        */

        Map<String, Object> NewNote = new HashMap<>();
        ArrayList<String> PrivateContacts = new ArrayList<>();
        final String[] NoteID = {""};

        if((!NoteTitle.getText().toString().isEmpty()) && (!NoteDescription.getText().toString().isEmpty())){
            NewNote.put("Title", NoteTitle.getText().toString());
            NewNote.put("Description", NoteDescription.getText().toString());
            NewNote.put("Author", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
            NewNote.put("Country", Country);
            NewNote.put("City", City);
            NewNote.put("Address", Address);
            NewNote.put("ImageUrl", "https://cdn-icons-png.flaticon.com/512/5110/5110429.png");
            NewNote.put("Latitude", Latitude);
            NewNote.put("Longitude", Longitude);
            NewNote.put("isPrivate",  isPrivate);

            //Falta isto
            NewNote.put("contacts", PrivateContacts);


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("notes").add(NewNote).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    Toast.makeText(InsertNoteActivity.this, "Testemony added successfully.", Toast.LENGTH_SHORT).show();
                    NoteID[0] = documentReference.getId().toString();


                    DocumentReference washingtonRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    washingtonRef.update("UserNotes", FieldValue.arrayUnion(NoteID[0]));

                    Log.i("x_get(@UserID)", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Log.i("x_get(@NoteID)", documentReference.getId().toString());
                    if(SelectedEmails != null){
                        DocumentReference drefence = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        drefence.update("UserNotes",FieldValue.arrayUnion(documentReference.getId().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Notifications buscar os ids
                                Log.d("@@_", "");
                                    for(int i=0;i<SelectedEmails.split(",").length;i++){
                                        db.collection("users")
                                                .whereEqualTo("Email", SelectedEmails.split(",")[i])
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                                //inserir a notificação
                                                                Map<String, Object> NewNotification = new HashMap<>();
                                                                NewNote.put("Title", NoteTitle.getText().toString());
                                                                NewNote.put("From", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                                NewNote.put("To", document.getId());
                                                                NewNote.put("Date", FieldValue.serverTimestamp());

                                                                db.collection("notifications").add(NewNotification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        Intent k = new Intent(InsertNoteActivity.this, MapsActivity.class);
                                                                        startActivity(k);
                                                                    }
                                                                });
                                                            }
                                                        } else {
                                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                Toast.makeText(InsertNoteActivity.this, "@UserNotes updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(InsertNoteActivity.this, "Request Not Send", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Intent k = new Intent(InsertNoteActivity.this, MapsActivity.class);
                        startActivity(k);
                    }

                    //Return to .Maps Activity
                    //Intent intent = new Intent();
                    //intent.putExtra("newMarker", "1");
                    //setResult(RESULT_OK, intent);
                    //finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding Note", e);
                }
            });


            firebaseFirestore.collection("cityData").document(City).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()){
                            numCity = (int) doc.get("total");
                        }else{
                            numCity = 1;
                        }
                    }
                }
            });

            //Adicionar Testemunho ao Utilizador
            //Não funciona





            //DocumentReference UserCurrentNotes = db.collection("").document();
            //ApiFuture<WriteResult> arrayUnion = washingtonRef.update("regions", FieldValue.arrayUnion("greater_virginia"));


            //Este não funciona
            firebaseFirestore.collection("cityData")
                    .document(City)
                    .update("total", numCity+1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(InsertNoteActivity.this, "Testemony added successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            NoteTitle.setError("Please write a title.");
            NoteDescription.setError("The item Description cannot be empty.");
        }
    }

    public String getIDbyEmail(String Email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String[] uID = {""};
        db.collection("users").whereEqualTo("Email", Email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        uID[0] = documentSnapshot.getId().toString();
                    }
                }
            }
        });
        return uID[0];
    }

    @SuppressLint("MissingPermission")
    private void showLocation(){

        //mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //locationSources.add(LocationManager.GPS_PROVIDER);
        //GpsMyLocationProvider provider = new GpsMyLocationProvider(context);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location -> {
                Latitude = location.getLatitude();
                Longitude = location.getLongitude();
            });
        }else{
            Toast.makeText(this, "Turn on GPS", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
        Log.i(";;+Lat", String.valueOf(location.getLatitude()));
        Log.i(";;+Lon", String.valueOf(location.getLongitude()));

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, pt.uc.dei.ihc.appihc.Bundle Extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }
}