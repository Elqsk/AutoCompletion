package com.example.auto_complete;

import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SuggesterViewAdapter extends RecyclerView.Adapter<SuggesterViewAdapter.SuggesterViewHolder> {

    private final ArrayList<SpannableStringBuilder> dataSet;

    public SuggesterViewAdapter(ArrayList<SpannableStringBuilder> dataSet) {
        this.dataSet = dataSet;

        Log.d("kkang", "Suggester View Adapter / SuggesterViewAdapter(...) / ArrayList<String> dataSet: " + dataSet);
        Log.d("kkang", " ");
    }

    // ─────────────────────────────────────────────────────────────────────────────────────────────

    @Override
    public int getItemCount() {
        Log.d("kkang", "Suggester View Adapter / getItemCount() / int dataSet.size(): " + dataSet.size());
//        Log.d("kkang", " ");

        return dataSet.size();
    }

    // ─────────────────────────────────────────────────────────────────────────────────────────────

    public OnSuggesterViewItemClickListener listener;

    public void setOnClickListener(OnSuggesterViewItemClickListener listener) {
        this.listener = listener;

//        Log.d("kkang", "Suggester View Adapter / setOnItemClickListener(...) / listener: " + listener);
//        Log.d("kkang", " ");
    }

    // ---------------------------------------------------------------------------------------------

    @NonNull
    @Override
    public SuggesterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suggester_view, parent, false);

//        Log.d("kkang", "Suggester View Adapter / SuggesterViewHolder / View itemView: " + itemView);
//        Log.d("kkang", " ");

        return new SuggesterViewHolder(itemView, listener);
    }

    // ---------------------------------------------------------------------------------------------

    public static class SuggesterViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public SuggesterViewHolder(@NonNull View itemView, OnSuggesterViewItemClickListener listener) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onClick(position);

                        Log.d("kkang", "Suggester View Adapter / SuggesterViewHolder / SuggesterViewHolder(...) / ... / onClick(...) position: " + position);
                        Log.d("kkang", " ");
                    }
                }
            });

//            Log.d("kkang", "Suggester View Adapter / SuggesterViewHolder / TextView textView: " + textView);
//            Log.d("kkang", " ");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────────────────────

    @Override
    public void onBindViewHolder(@NonNull SuggesterViewHolder holder, int position) {

        holder.textView.setText(dataSet.get(position));

        Log.d("kkang", "Suggester View Adapter / onBindViewHolder(...)");
        Log.d("kkang", " ");
    }
}
