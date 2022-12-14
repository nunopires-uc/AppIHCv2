package pt.uc.dei.ihc.appihc;

import static pt.uc.dei.ihc.appihc.Bundle.CalcDistance;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pt.uc.dei.ihc.appihc.databinding.ActivityMainBinding;
import pt.uc.dei.ihc.appihc.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SearchView.OnQueryTextListener, BaseGpsListener{


    //https://www.c-sharpcorner.com/article/getting-location-address-from-latitude-and-longitude-using-google-maps-geocoding/

    private GoogleMap mMap;
    ArrayList<LatLng> markers = new ArrayList<LatLng>();
    ArrayList<String> titles = new ArrayList<String>();
    private ActivityMapsBinding binding;
    private FirebaseFirestore db;
    Location currentLocation;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    ArrayList<DefaultNote> CacheNotes = new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton CreateNote;
    public String Cidade;


    ListView listView;
    SearchView searchView;
    String[] RegionsArray;
    //ListViewAdapter adapter;
    //ArrayList<>

    ImageButton CreateNoteButton;
    Double Latitude, Longitude;
    String City, Country, Address, CurrentCity;
    ArrayList<DefaultNote> DatabaseNotes = new ArrayList<>();
    ArrayList<MetaNote> metaNotes = new ArrayList<>();
    private static final int PERMISSION_LOCATION = 1000;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CreateNote = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setItemIconTintList(null);
        CreateNoteButton = findViewById(R.id.CreateNoteButton);
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        db.collection("notes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        DefaultNote currentNote = new DefaultNote();
                        currentNote.setAuthor((String) d.get("Author"));

                        currentNote.setCity((String) d.get("City"));
                        currentNote.setTitle((String) d.get("Title"));
                        Log.i("**Title", (String) d.get("Title"));
                        currentNote.setCountry((String) d.get("Country"));
                        currentNote.setDescription((String) d.get("Description"));
                        currentNote.setAddress((String) d.get("Address"));
                        currentNote.setImageUrl((String) d.get("ImageUrl"));
                        currentNote.setLatitude((Double) d.get("Latitude"));
                        currentNote.setLongitude((Double) d.get("Longitude"));
                        currentNote.setPrivacy((Boolean) d.get("isPrivate"));
                        currentNote.setEmails((ArrayList<String>) d.get("contacts"));
                        CacheNotes.add(currentNote);
                        //Log.i("&&db_Note", currentNote.toString());
                    }

                    ArrayList<HistoryClass> historyClass = new ArrayList<>();
                    ArrayList<DefaultNote> NoteOnSite = new ArrayList<>();
                    ArrayList<DefaultNote> history = new ArrayList<>();
                    Double LatitudeOne, LongitudeOne;
                    boolean isAprivateNote = false;
                    for (int i = 0; i < CacheNotes.size(); i++) {
                            Log.i("&&dbn", CacheNotes.get(i).getTitle());

                            LatitudeOne = CacheNotes.get(i).getLatitude();
                            LongitudeOne = CacheNotes.get(i).getLongitude();
                            Log.i("%%Lat", String.valueOf(LatitudeOne));
                            Log.i("%%Lon", String.valueOf(LongitudeOne));
                            NoteOnSite.add(CacheNotes.get(i));


                            if(!history.contains(CacheNotes.get(i))){

                                Log.i("ll-",CacheNotes.get(i).getTitle());
                                for (int j = 0; j < CacheNotes.size(); j++) {
                                    HistoryClass chc = new HistoryClass(CacheNotes.get(j).getLatitude(), CacheNotes.get(j).getLongitude());
                                    Log.i("CalcDistance()" + CacheNotes.get(i).getTitle() + ":" + CacheNotes.get(j).getTitle(), String.valueOf(CalcDistance(LatitudeOne, LongitudeOne, CacheNotes.get(j).getLatitude(), CacheNotes.get(j).getLongitude())));
                                    if (CalcDistance(LatitudeOne, LongitudeOne, CacheNotes.get(j).getLatitude(), CacheNotes.get(j).getLongitude()) <= 0.0001f) {

                                        if (CacheNotes.get(j).isPrivacy()) {
                                            isAprivateNote = true;
                                        }
                                        if(!NoteOnSite.contains(CacheNotes.get(j))){
                                            NoteOnSite.add(CacheNotes.get(j));
                                            history.add(CacheNotes.get(j));
                                        }
                                    }
                                }
                                Log.i("getHistory", history.toString());
                                Log.i("dumpNoteOnSize", String.valueOf(NoteOnSite.size()));
                                Log.i("dumpNoteOnSite", NoteOnSite.toString());
                                MetaNote note = new MetaNote(LatitudeOne, LongitudeOne, (ArrayList<DefaultNote>) NoteOnSite.clone(), isAprivateNote);
                                metaNotes.add(note);
                                NoteOnSite.clear();
                                isAprivateNote = false;
                            }



                            //
                    }

                    db.collection("notifications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                String Description, Title, to, from;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    Description = (String) document.get("Description");
                                    Title = (String) document.get("Title");
                                    to = (String) document.get("to");
                                    from = (String) document.get("from");

                                    try{
                                        Log.i("~~Description", Description);
                                        Log.i("~~Title", Title);
                                        Log.i("~~to", to);
                                        Log.i("~~from", from);
                                    }catch(Exception e){

                                    }

                                    boolean hasPublic = false;
                                    boolean hasFoundNotification = false;
                                    Log.i("~meta", String.valueOf(metaNotes.size()));
                                    for(int m=0;m<metaNotes.size();m++){
                                        LatLng sydney = new LatLng((Double) metaNotes.get(m).getLatitude(), (Double) metaNotes.get(m).getLongitude());

                                        if(metaNotes.get(m).isPrivate()){
                                            Log.i("~~Private", metaNotes.get(m).toString());
                                            for(int h=0;h<metaNotes.get(m).getHashNotes().size();h++){
                                                if(metaNotes.get(m).getHashNotes().get(h).isPrivacy() == false){
                                                    hasPublic = true;
                                                }
                                                Log.i("~~myID", mAuth.getUid());
                                                if(((metaNotes.get(m).getHashNotes().get(h).getAuthor().equals(from)) && (to.equals(mAuth.getUid()))) || (metaNotes.get(m).getHashNotes().get(h).getAuthor().equals(mAuth.getUid()))){
                                                    Log.i("~~here", "");
                                                    hasFoundNotification = true;
                                                }else{

                                                }

                                                /*if((metaNotes.get(m).getHashNotes().get(h).getDescription().equals(Description)
                                                && metaNotes.get(m).getHashNotes().get(h).getTitle().equals(Title)
                                                        && metaNotes.get(m).getHashNotes().get(h).getAuthor().equals(from) && mAuth.getUid().toString().equals(to)
                                                ) || (metaNotes.get(m).getHashNotes().get(h).equals(mAuth.getUid()))){
                                                    mMap.addMarker(new MarkerOptions().position(sydney).title((String) "Nearby testimonials")
                                                            .icon(BitmapFromVector(getApplicationContext(), R.drawable.privatenote))).setTag(m);
                                                }*/
                                            }
                                            if(hasFoundNotification){
                                                mMap.addMarker(new MarkerOptions().position(sydney).title((String) "Nearby testimonials")
                                                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.privatenote))).setTag(m);
                                            }else{
                                                mMap.addMarker(new MarkerOptions().position(sydney).title((String) "Nearby testimonials")
                                                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.testemunho_icone))).setTag(m);
                                            }
                                        }else{
                                            mMap.addMarker(new MarkerOptions().position(sydney).title((String) "Nearby testimonials")
                                                    .icon(BitmapFromVector(getApplicationContext(), R.drawable.testemunho_icone))).setTag(m);
                                        }
                                    }
                                }
                            }
                        }
                    });

                    Log.i("&&*", String.valueOf(metaNotes.size()));
                    for(int i = 0; i< metaNotes.size();i++){
                        Log.i("&&*" + String.valueOf(i), metaNotes.get(i).toString());
                    }
                    Log.i("(metanote)", metaNotes.toString());
                }
            }
        });

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

        CreateNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Log.i("++Lat", String.valueOf(Latitude));
                Log.i("++Lon", String.valueOf(Longitude));


                 */
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                }else{
                    showLocation();
                    Log.i("->Lat", String.valueOf(Latitude));
                    Log.i("->Lon", String.valueOf(Longitude));
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            Intent i = new Intent(MapsActivity.this, InsertNoteActivity.class);
                            i.putExtra("Latitude", Latitude);
                            i.putExtra("Longitude", Longitude);
                            i.putExtra("City", City);
                            i.putExtra("Country", Country);
                            i.putExtra("Address", Address);
                            startActivity(i);
                        }
                    }, 0);


                }
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }catch (Exception e){
            Log.e("LocationManager", e.toString());
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //Atualizar a localização
        /*locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, location -> {
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            //Log.i("++Lat", String.valueOf(location.getLatitude()));
            //Log.i("++Lon", String.valueOf(location.getLongitude()));
        });*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,S
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        //Isto pode dar porcaria
        mMap.getUiSettings().setZoomGesturesEnabled(false);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                Log.i("@k", "markerClicked");

                try{
                    Log.d("»Latitude", String.valueOf(Latitude));
                    Log.d("»Longitude", String.valueOf(Longitude));
                    int tag = (int) marker.getTag();

                    //CalcDistance(double LatitudeOne, double LongitudeOne, double LatitudeTwo, double LongitudeTwo)
                    if(CalcDistance(Latitude, Longitude, metaNotes.get(tag).getLatitude(), metaNotes.get(tag).getLongitude()) < 3.0){
                        try {
                            Intent k = new Intent(MapsActivity.this, NoteList.class);
                            //int tag = (int) marker.getTag();
                            Bundle args = new Bundle();
                            args.putSerializable("@mn", (Serializable) metaNotes.get(tag));
                            Log.i("getNote()", metaNotes.get(tag).toString());
                            k.putExtra("BUNDLE", args);
                            startActivity(k);
                        } catch (Exception e) {
                            Log.e("Exception Intent", e.toString());
                        }
                    }else{
                        Log.i("Too far away", ":)");
                    }
                }catch (Exception e){
                    Log.e("OnClickMarkerException", e.toString());
                }
                return false;
            }
        });

        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setOnMapLongClickListener((GoogleMap.OnMapLongClickListener) this);
        //mMap.setOnMarkerDragListener((GoogleMap.OnMarkerDragListener) this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
            zoomToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        //LatLng UserPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        mMap.addMarker(new MarkerOptions().position(sydney).title("user Position"));

        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.letter_background);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney")
                .snippet("Lorem ipslum hello hello").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        //.
        //
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        //Dá break aqui
        try{
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    Latitude = latLng.latitude;
                    Longitude = latLng.longitude;

                    try {
                        //Desabilitar se houver problemas
                        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(Latitude, Longitude, 1);
                        Log.i("$#Latitude", String.valueOf(addressList.get(0).getLatitude()));
                        Log.i("$#Longitude", String.valueOf(addressList.get(0).getLongitude()));
                        Log.i("$#City", String.valueOf(addressList.get(0).getLocality()));
                        City = String.valueOf(addressList.get(0).getLocality());
                        Cidade = City;
                        Log.i("$#Country", String.valueOf(addressList.get(0).getCountryName()));
                        Country = String.valueOf(addressList.get(0).getCountryName());
                        Log.i("$#AddressLine", String.valueOf(addressList.get(0).getAddressLine(0)));
                        Address = String.valueOf(addressList.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                }
            });
        }catch (Exception e){

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                enableUserLocation();
                zoomToUserLocation();
            }else{
               //
            }
        }
        if(requestCode == PERMISSION_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showLocation();
            }else{
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }

        }else{

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @SuppressLint("MissingPermission")
    private void showLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
