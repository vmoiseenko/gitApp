package com.moiseenko.gitapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.moiseenko.gitapp.R;
import com.moiseenko.gitapp.json.Repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by vmoiseenko on 23.10.2015.
 */
public class RepositoryItemAdapter extends RecyclerView.Adapter {

    public static final int TYPE_HOLDER_REP = 0;

    private Context context;
    private CardItemClickListener cardItemClickListener;
    private List<Repositories.Repos> data;
    private SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm");

    public RepositoryItemAdapter(List<Repositories.Repos> data, Context context, CardItemClickListener cardItemClickListener) {
        this.data = data;
        this.context = context;
        this.cardItemClickListener = cardItemClickListener;
    }

    public class RepositoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvName;
        public TextView tvDescription;
        public TextView tvDate;
        public TextView tvLanguage;

        public RepositoryItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvRepName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvLanguage = (TextView) itemView.findViewById(R.id.tvLanguage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (cardItemClickListener != null) {
                tvName.setTransitionName("tvName"+ getAdapterPosition());
                cardItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_HOLDER_REP:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rep, parent, false);
                viewHolder = new RepositoryItemViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {

            case TYPE_HOLDER_REP:
                RepositoryItemViewHolder h = (RepositoryItemViewHolder) holder;
                Repositories.Repos repository = data.get(position);
                String title = repository.getName();
                String description = repository.getDescription();
                String date = repository.getUpdated_at();

                h.tvName.setText(title);
                h.tvDescription.setText(description);
                h.tvLanguage.setText(repository.getLanguage());
                try {
                    h.tvDate.setText(format.format(input.parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                    h.tvDate.setText(date);

                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public boolean swapViews(int fromPosition, int toPosition) {
        Collections.swap(data, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void removeViews(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemViewType(int position) {
        return TYPE_HOLDER_REP;

    }

    public interface CardItemClickListener {
        void onItemClick(View view, int position);
    }
}
