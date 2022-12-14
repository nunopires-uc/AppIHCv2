package pt.uc.dei.ihc.appihc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kotlin.collections.ArraysKt;

public class ListViewAdapterSearch {
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<String> Regions;


    public ListViewAdapterSearch(Context mContext, ArrayList<String> regions) {
        this.mContext = mContext;
        Regions = regions;
        inflater = LayoutInflater.from(mContext);
    }

    public class ViewHolder{
        TextView name;
    }

    public int getItemId(int position){
        return position;
    }

    /*
    public View getView(final int position, View viewm ViewGroup parent){
        final ViewHolder holder;
        if(v)
        return view;
    }*/
}
