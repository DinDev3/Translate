package lk.dinuka.translate.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.dinuka.translate.R;

public class MyEditAdapter extends RecyclerView.Adapter<MyEditAdapter.MyViewHolder>{

    private List<String> mDataset;      // list of all english from db will be transferred here

    private int lastSelectedPosition = -1;      // stores the radio selection position



    // constructor of adapter
    public MyEditAdapter(List<String> myDataset) {
        mDataset = myDataset;           // getting received english from db
    }


    @NonNull
    @Override
    public MyEditAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create new views (invoked by the layout manager)

        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_edit_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replaces content of the view with content required to be displayed
    @Override
    public void onBindViewHolder(@NonNull MyEditAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset.get(position));

        // since only one radio button is allowed to be selected,
        // this condition un-checks previous selections
        holder.selectionState.setChecked(lastSelectedPosition == position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    // Provide a reference to the views for each data item
    // provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;       // each data item has a String
        public RadioButton selectionState;

        public MyViewHolder(@NonNull View itemView) {       // constructor of individual view element
            super(itemView);

            textView =  itemView.findViewById(R.id.text_display_line);
            selectionState = itemView.findViewById(R.id.radio_selection_english);


            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

//                    System.out.println(lastSelectedPosition);         // test whether desired radio was selected
//                    System.out.println(textView.getText());
                }
            });
        }

    }
}
