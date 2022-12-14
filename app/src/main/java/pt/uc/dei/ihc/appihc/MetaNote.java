package pt.uc.dei.ihc.appihc;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;

public class MetaNote implements Serializable {

    private Double Latitude;
    private Double Longitude;
    private ArrayList<DefaultNote> HashNotes;
    private boolean isPrivate;

    public MetaNote(Double latitude, Double longitude, ArrayList<DefaultNote> hashNotes) {
        Latitude = latitude;
        Longitude = longitude;
        HashNotes = hashNotes;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public MetaNote(Double latitude, Double longitude, ArrayList<DefaultNote> hashNotes, boolean isPrivate) {
        Latitude = latitude;
        Longitude = longitude;
        HashNotes = hashNotes;
        this.isPrivate = isPrivate;
    }

    public MetaNote(Double latitude, Double longitude) {
        Latitude = latitude;
        Longitude = longitude;
        HashNotes = new ArrayList<>();
    }



    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public ArrayList<DefaultNote> getHashNotes() {
        return HashNotes;
    }

    public void append (DefaultNote note){
        HashNotes.add(note);
    }


    public void setHashNotes(ArrayList<DefaultNote> hashNotes) {
        HashNotes = hashNotes;
    }

    @Override
    public String toString() {
        return "MetaNote{" +
                "Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", HashNotes=" + HashNotes +
                '}';
    }
}
