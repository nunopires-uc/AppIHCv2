package pt.uc.dei.ihc.appihc;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;

public interface BaseGpsListener extends LocationListener, GpsStatus.Listener {

    public void onLocationChanged(Location location);
    public void onProviderDisabled(String provider);
    public void onProviderEnabled(String provider);
    public void onStatusChanged(String provider, int status, Bundle Extras);
    public void onGpsStatusChanged(int event);
}
