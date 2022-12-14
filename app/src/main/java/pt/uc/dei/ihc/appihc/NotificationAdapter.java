package pt.uc.dei.ihc.appihc;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> title;
    private final ArrayList<String> subtitle;
    private final ArrayList<String> dates;

    public NotificationAdapter(Activity context, ArrayList<String> title, ArrayList<String> subtitle, ArrayList<String> dates) {
        //super(context, R.layout.notification_list_layout, title);
        super(context, R.layout.notification_list_layout, title);
        this.context = context;
        this.title = title;
        this.subtitle = subtitle;
        this.dates = dates;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.notification_list_layout, null, true);
        TextView titleText = rootView.findViewById(R.id.title_id);
        TextView subText = rootView.findViewById(R.id.subtitle_id);
        //TextView dateText = rootView.findViewById(R.id.date_id);

        titleText.setText(title.get(position));
        //subText.setText(subtitle.get(position).substring(0, 16).concat("..."));
        subText.setText(subtitle.get(position));

        //dateText.setText(dates.get(position));

        return rootView;

    }

    /*
    private final Activity context;
    //private final String[] title;
    //private final String[] subtitle;
    private  final ArrayList<String> title;
    private  final ArrayList<String> subtitle;


    public MyListAdapter(Activity context, ArrayList<String> title,ArrayList<String> subtitle) {
        super(context, R.layout.custom_list_layout, title);
        this.context = context;
        this.title = title;
        this.subtitle = subtitle;
    }


     */

}
