package pt.uc.dei.ihc.appihc;

import static pt.uc.dei.ihc.appihc.Bundle.CalcDistance;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        double lc = CalcDistance(53.32055555555556, -1.7297222222222221, 53.31861111111111, -1.6997222222222223);
        Log.d("Dist:", Double.toString(lc));

    }


}