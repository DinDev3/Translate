package lk.dinuka.translate.services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.dinuka.translate.R;

import static lk.dinuka.translate.MainActivity.allEnglishFromDB;

public class MyDictionaryAdapter extends RecyclerView.Adapter<MyDictionaryAdapter.MyViewHolder> {
    private List<String> mEnglishDataset;      // list of all english will be transferred here
    private List<String> mTranslatedDataset;      // list of all translated phrases will be transferred here

    // constructor of adapter
    public MyDictionaryAdapter(List<String> mEnglishDataset, List<String> mTranslatedDataset) {
        this.mEnglishDataset = mEnglishDataset;
        this.mTranslatedDataset = mTranslatedDataset;
    }

    @NonNull
    @Override
    public MyDictionaryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create new views (invoked by the layout manager)

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_dictionary_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    // Replaces content of the view with content required to be displayed
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (mEnglishDataset.size() == mTranslatedDataset.size()) {        // to avoid null pointer in translations
            holder.englishTextView.setText(mEnglishDataset.get(position));
            holder.translatedTextView.setText(mTranslatedDataset.get(position));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mEnglishDataset.size();
    }


    // Provide a reference to the views for each data item
    // provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView englishTextView;       // each data item has a String to store the english phrase
        public TextView translatedTextView;       // each data item has a String to store the translated phrase

        public MyViewHolder(@NonNull View itemView) {       // constructor of individual view element
            super(itemView);

            englishTextView = itemView.findViewById(R.id.english_text);
            translatedTextView = itemView.findViewById(R.id.translated_text);
        }

    }
}
