package pt.uc.dei.ihc.appihc;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Bundle {


    /*
    Nova versão: https://github.com/CartoDB/mobile-sdk/



     */


    /*
    Adicionar Email ao registo
    Adicionar Contactos ao criar Nota
    Testar criar nota
     */

    private Context context;
    private FirebaseFirestore db;

    public static double CalcDistance(double LatitudeOne, double LongitudeOne, double LatitudeTwo, double LongitudeTwo){
        LatitudeOne = Math.toRadians(LatitudeOne);
        LongitudeOne = Math.toRadians(LongitudeOne);
        LatitudeTwo = Math.toRadians(LatitudeTwo);
        LongitudeTwo = Math.toRadians(LongitudeTwo);

        //Haversine Formula
        double DistanceLatitude = LatitudeTwo - LatitudeOne;
        double DistanceLongitude = LongitudeTwo - LongitudeOne;
        double a = Math.pow(Math.sin(DistanceLatitude / 2), 2) +
                Math.cos(LatitudeOne) * Math.cos(LatitudeTwo) *
                        Math.pow(Math.sin(DistanceLongitude / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        double radius = 6371;


        return c * radius;
    }


    //https://www.geeksforgeeks.org/how-to-save-arraylist-to-sharedpreferences-in-android/
    //https://stackoverflow.com/questions/33380455/why-am-i-getting-this-error-cannot-resolve-method-getsharedpreferences-java-l
    public ArrayList<DefaultNote> loadNotes(Context context){
        ArrayList<DefaultNote> nearNotes = new ArrayList<>();
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("", 0);
        return nearNotes;
    }

    public void Dictionary(){
        ArrayList<Hashtable<String, Object>> NotePoints = new ArrayList<>();
        Hashtable<String, Object> NoteConfig1 = new Hashtable<String, Object>();
        ArrayList<DefaultNote> notesCC = new ArrayList<>();
        NoteConfig1.put("Latitude", 40.1970917);
        NoteConfig1.put("Longitude", -8.4106286);
        NoteConfig1.put("NoteData", notesCC);
    }

    public void getfsNotes(){
        db = FirebaseFirestore.getInstance();
        db.collection("notes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {

                    }
                }
            }
        });
    }

    public boolean InsertDummyData(){
        return false;
    }

    public void queryTesting(){
        db.collection("cities")
                .whereEqualTo("city", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Bundle", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("Bundle", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public static boolean saveNotes(){
        return true;
    }
}
