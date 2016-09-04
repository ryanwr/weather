package com.ryanwelch.weather.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.models.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SearchSuggestionsAdapter";

    private ArrayList<SearchSuggestion> mItems = new ArrayList<>();

    private Listener mListener;
    private OnBindSuggestionCallback mOnBindSuggestionCallback;

    private Context mContext;

    public interface OnBindSuggestionCallback {
        void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView,
                              SearchSuggestion item, int itemPosition);
    }


    public interface Listener {
        void onItemSelected(SearchSuggestion item);

    }

    public static class SearchSuggestionViewHolder extends RecyclerView.ViewHolder {

        public TextView body;
        public ImageView leftIcon;

        private Listener mListener;

        public interface Listener {

            void onItemClicked(int adapterPosition);
        }

        public SearchSuggestionViewHolder(View v, Listener listener) {
            super(v);

            mListener = listener;
            body = (TextView) v.findViewById(R.id.body);
            leftIcon = (ImageView) v.findViewById(R.id.left_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int adapterPosition = getAdapterPosition();
                    if (mListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                        mListener.onItemClicked(adapterPosition);
                    }
                }
            });
        }
    }

    public SearchSuggestionsAdapter(Context context, Listener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void setItems(List<? extends SearchSuggestion> searchSuggestions) {
        mItems.clear();
        mItems.addAll(searchSuggestions);
        notifyDataSetChanged();
    }

    public ArrayList<? extends SearchSuggestion> getItems() {
        return mItems;
    }

    public void setOnBindSuggestionCallback(OnBindSuggestionCallback callback) {
        this.mOnBindSuggestionCallback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_search_item, viewGroup, false);

        SearchSuggestionViewHolder viewHolder = new SearchSuggestionViewHolder(view,
                new SearchSuggestionViewHolder.Listener() {

                    @Override
                    public void onItemClicked(int adapterPosition) {

                        if (mListener != null) {
                            mListener.onItemSelected(mItems.get(adapterPosition));
                        }
                    }

                });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {

        SearchSuggestionViewHolder viewHolder = (SearchSuggestionViewHolder) vh;

        SearchSuggestion suggestionItem = mItems.get(position);
        viewHolder.body.setText(suggestionItem.getBody());

        if (mOnBindSuggestionCallback != null) {
            mOnBindSuggestionCallback.onBindSuggestion(viewHolder.itemView, viewHolder.leftIcon, viewHolder.body,
                    suggestionItem, position);
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }
}