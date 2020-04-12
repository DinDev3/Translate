package lk.dinuka.translate.services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import lk.dinuka.translate.R;

import static lk.dinuka.translate.Dictionary.savedLanguages;

// Adapter for Dictionary Subscriptions class
public class MyDicSubsAdapter extends RecyclerView.Adapter<MyDicSubsAdapter.MyViewHolder> {
    private List<String> mDataset;      // list of all Languages from db will be transferred here

    public MyDicSubsAdapter(List<String> availableLanguages) {
        mDataset = availableLanguages;           // getting received list of available languages
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create new views (invoked by the layout manager)

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.checkedTextView.setText(mDataset.get(position));


        Boolean savedStatus = false;
        if (savedLanguages.contains(mDataset.get(position))) {        // if language has been saved by the user
            savedStatus = true;
        }
        // depending on the saved status; display the check--

        // if changes were made to the subscription before rotation of device,
//        Boolean subscriptionChanged = foreignSubChanges.get(mDataset.get(position));


        if (savedStatus) {
            holder.checkedTextView.setCheckMarkDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.tick));
            holder.checkedTextView.setChecked(true);
        } else {
            holder.checkedTextView.setCheckMarkDrawable(null);
            holder.checkedTextView.setChecked(false);
        }

//        if (subscriptionChanged != null) {        // only if changes were made
//            if (subscriptionChanged) {
//                holder.checkedTextView.setCheckMarkDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.tick));
//                holder.checkedTextView.setChecked(true);
//            } else {
//                holder.checkedTextView.setCheckMarkDrawable(null);
//                holder.checkedTextView.setChecked(false);
//            }
//        }

//
//        // perform on Click Event Listener on CheckedTextView
//        holder.checkedTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Boolean value = holder.checkedTextView.isChecked();
//                if (value) {
//                    // set check mark drawable and set checked property to false
//
//                    holder.checkedTextView.setCheckMarkDrawable(null);
//                    holder.checkedTextView.setChecked(false);
////                    Toast.makeText(context, "un-Checked", Toast.LENGTH_LONG).show();
//                } else {
//                    // set check mark drawable and set checked property to true
//
////                    holder.checkedTextView.setCheckMarkDrawable(R.drawable.checked);
//                    holder.checkedTextView.setCheckMarkDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.tick));
//                    holder.checkedTextView.setChecked(true);
////                    Toast.makeText(context, "Checked", Toast.LENGTH_LONG).show();
//                }
//
//
//                // update HashMap with new subscription status -----------------
//                foreignSubChanges.put(holder.checkedTextView.getText().toString(), holder.checkedTextView.isChecked());
//
//                if ((foreignSubChanges.get(holder.checkedTextView.getText().toString())) ==
//                        (foreignLanguageSubs.get(holder.checkedTextView.getText().toString()))) {
//
//                    // if the final change is the same as the initially existed subscription status, the language is
//                    // removed from the subscription changes HashMap
//
//                    foreignSubChanges.remove(holder.checkedTextView.getText().toString());
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckedTextView checkedTextView;       // each data item has a CheckedTextBox

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkedTextView = itemView.findViewById(R.id.language_checked_text_view);

        }
    }
}
