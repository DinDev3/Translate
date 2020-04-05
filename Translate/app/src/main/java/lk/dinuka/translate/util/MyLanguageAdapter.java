package lk.dinuka.translate.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.dinuka.translate.MainActivity;
import lk.dinuka.translate.R;

import static lk.dinuka.translate.LanguageSubscription.foreignSubChanges;
import static lk.dinuka.translate.MainActivity.foreignLanguageSubs;

public class MyLanguageAdapter extends RecyclerView.Adapter<MyLanguageAdapter.MyViewHolder> {
    private List<String> mDataset;      // list of all Languages from db will be transferred here

    private int lastSelectedPosition = -1;      // stores the checkTextView selection position


    public MyLanguageAdapter(List<String> allForeignLanguages) {
        mDataset = allForeignLanguages;           // getting received english from db
    }


    @NonNull
    @Override
    public MyLanguageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create new views (invoked by the layout manager)

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_text_view, parent, false);

        MyLanguageAdapter.MyViewHolder vh = new MyLanguageAdapter.MyViewHolder(v);
        return vh;
    }


    // Replaces content of the view with content required to be displayed
    @Override
    public void onBindViewHolder(@NonNull final MyLanguageAdapter.MyViewHolder holder, int position) {
        holder.checkedTextView.setText(mDataset.get(position));


        Boolean subscriptionStatus = MainActivity.foreignLanguageSubs.get(mDataset.get(position));
        // depending on the subscription status in the HashMap received from the database, display the check--

        if (subscriptionStatus){
            holder.checkedTextView.setCheckMarkDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.tick));
            holder.checkedTextView.setChecked(true);
        }else{
            holder.checkedTextView.setCheckMarkDrawable(null);
            holder.checkedTextView.setChecked(false);
        }


        // perform on Click Event Listener on CheckedTextView
        holder.checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean value = holder.checkedTextView.isChecked();
                if (value) {
                    // set check mark drawable and set checked property to false

                    holder.checkedTextView.setCheckMarkDrawable(null);
                    holder.checkedTextView.setChecked(false);
//                    Toast.makeText(context, "un-Checked", Toast.LENGTH_LONG).show();
                } else {
                    // set check mark drawable and set checked property to true

//                    holder.checkedTextView.setCheckMarkDrawable(R.drawable.checked);
                    holder.checkedTextView.setCheckMarkDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.tick));
                    holder.checkedTextView.setChecked(true);
//                    Toast.makeText(context, "Checked", Toast.LENGTH_LONG).show();
                }


                // update HashMap with new subscription status -----------------
                foreignSubChanges.put(holder.checkedTextView.getText().toString(),holder.checkedTextView.isChecked());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    // Provide a reference to the views for each data item
    // provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckedTextView checkedTextView;       // each data item has a CheckedTextBox

        public MyViewHolder(@NonNull View itemView) {       // constructor of individual view element
            super(itemView);

            checkedTextView = itemView.findViewById(R.id.language_checked_text_view);

        }

    }

}
