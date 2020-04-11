package lk.dinuka.translate.services;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.dinuka.translate.R;

public class MyTranslateAdapter extends RecyclerView.Adapter<MyTranslateAdapter.MyViewHolder> {

    private List<String> mDataset;      // list of all english from db will be transferred here

    private int lastSelectedPosition;      // stores the last selection position of the recycler elements in the recycler view
    private OnTransAdapterListener onTransAdapterListener;

    // constructor of adapter
    public MyTranslateAdapter(List<String> mDataset, OnTransAdapterListener onTransAdapterListener, int lastSelectedPosition) {
        this.mDataset = mDataset;           // getting received english from db
        this.onTransAdapterListener = onTransAdapterListener;
        this.lastSelectedPosition =  lastSelectedPosition;
    }

    @NonNull
    @Override
    public MyTranslateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Create new views (invoked by the layout manager)

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_translate_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v, onTransAdapterListener);
        return vh;

    }

    // Replaces content of the view with content required to be displayed
    @Override
    public void onBindViewHolder(@NonNull MyTranslateAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset.get(position));

        // supposed to colour only the chosen position
        if (position == lastSelectedPosition) {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#A7DAFF"));         // use this only if position is the chosen position

        } else {
            holder.relativeLayout.setBackground(ContextCompat.getDrawable(holder.relativeLayout.getContext(), R.drawable.border));         // use this only if position is the chosen position
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    // Provide a reference to the views for each data item
    // provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;       // each data item has a String
        RelativeLayout relativeLayout;          // used to set the colour of the chosen text later
        OnTransAdapterListener onTransAdapterListener;      // adding the interface to the view holder

        public MyViewHolder(@NonNull final View itemView, OnTransAdapterListener onTransAdapterListener) {       // constructor of individual view element
            super(itemView);
            this.onTransAdapterListener = onTransAdapterListener;

            textView = itemView.findViewById(R.id.translateText_display_line);

            relativeLayout = itemView.findViewById(R.id.translate_relativeLayout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            lastSelectedPosition = getAdapterPosition();
            onTransAdapterListener.onEnglishClick(getAdapterPosition());    //calling onEnglishClick and passing in the adapter position
            notifyDataSetChanged();
        }
    }

    public interface OnTransAdapterListener{
        void onEnglishClick(int position);
    }
}
