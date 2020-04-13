package lk.dinuka.translate.services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lk.dinuka.translate.R;

public class MyDisplayAdapter extends RecyclerView.Adapter<MyDisplayAdapter.MyViewHolder> {
    private List<String> mDataset;      // list of all english from db will be transferred here


    // constructor of adapter
    public MyDisplayAdapter(List<String> myDataset) {
        mDataset = myDataset;           // getting received english from db
    }


    @NonNull
    @Override
    public MyDisplayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create new views (invoked by the layout manager)

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_display_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replaces content of the view with content required to be displayed
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.textView.setText(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    // Provide a reference to the views for each data item
    // provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;       // each data item has a String

        public MyViewHolder(@NonNull View itemView) {       // constructor of individual view element
            super(itemView);

            textView = itemView.findViewById(R.id.text_display_line);
        }

    }

    public void filteredResults(ArrayList<String> filteredPhrases){         // used to filter search results
        mDataset = filteredPhrases;     //assigning updated list after filtering
        notifyDataSetChanged();
    }
}
