package pt.uc.dei.ihc.appihc;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    //private final String[] title;
    //private final String[] subtitle;
    private  final ArrayList<String> title;
    private  final ArrayList<String> subtitle;
    private final ArrayList<Byte> PrivateValues;

    public MyListAdapter(Activity context, ArrayList<String> title, ArrayList<String> subtitle, ArrayList<Byte> privateValues) {
        super(context, R.layout.custom_list_layout, title);
        this.context = context;
        this.title = title;
        this.subtitle = subtitle;
        PrivateValues = privateValues;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.custom_list_layout, null, true);
        TextView titleText = rootView.findViewById(R.id.title_id);
        TextView subText = rootView.findViewById(R.id.subtitle_id);


        if(PrivateValues.size() > 0){
            if(PrivateValues.get(position) == 1){
                titleText.setText("🔒" + title.get(position));
                subText.setText(subtitle.get(position));
            }else{
                titleText.setText(title.get(position));
                subText.setText(subtitle.get(position));
            }
        }else{
            titleText.setText(title.get(position));
            subText.setText(subtitle.get(position));
        }

        //subText.setText(subtitle.get(position).substring(0, 16).concat("..."));


        return rootView;

    }
}
