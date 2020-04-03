package lk.dinuka.translate.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.dinuka.translate.R;

public class MyLanguageAdapter extends RecyclerView.Adapter<MyLanguageAdapter.MyViewHolder> {
    private List<String> mDataset;      // list of all Languages from db will be transferred here

    // Provide a reference to the views for each data item
    // provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public CheckedTextView checkedTextView;       // each data item has a CheckedTextBox
        public MyViewHolder(@NonNull View itemView) {       // constructor of individual view element
            super(itemView);

            checkedTextView =  itemView.findViewById(R.id.language_checked_text_view);
        }

    }

    public MyLanguageAdapter(List<String> allForeignLanguages) {
        mDataset = allForeignLanguages;           // getting received english from db
    }

    @NonNull
    @Override
    public MyLanguageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create new views (invoked by the layout manager)

        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_text_view, parent, false);

        MyLanguageAdapter.MyViewHolder vh = new MyLanguageAdapter.MyViewHolder(v);
        return vh;
    }

    // Replaces content of the view with content required to be displayed
    @Override
    public void onBindViewHolder(@NonNull MyLanguageAdapter.MyViewHolder holder, int position) {
        holder.checkedTextView.setText(mDataset.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
