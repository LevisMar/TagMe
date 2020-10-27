package com.example.tagme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/**
 * Adattatore della lista che contiene tutti
 * i libri che saranno oggetto di una transazione
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>
{
    private ArrayList<ListItem> mList;
    private OnItemClickListener mListener;

    /**
     * Costruttore dell'adattatore
     * @param list dei libri da utilizzare
     */
    ListAdapter(ArrayList<ListItem> list)
    {
        mList = list;
    }

    /**
     * Interfaccia con i metodi che l'utente che
     * utilizzerà l'adattatore dovrà implementare
     */
    public interface OnItemClickListener
    {
        /**
         * Quando si clicca su un elemento della lista
         */
        void onItemClick(int position);
        /**
         * Quando si cancella un elemento della lista
         */
        void onDeleteClick(int position);
    }

    /**
     * Associo l'evento OnItemClickListener
     */
    void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder
    {
        // Componenti grafiche che rappresentano un elemento della lista
        ImageView mImageView;
        TextView mTextView;
        ImageView mDeleteImage;

        ListViewHolder(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.listImageView);
            mTextView = itemView.findViewById(R.id.listTextView);
            mDeleteImage = itemView.findViewById(R.id.image_delete);

            itemView.setOnClickListener(v ->
            {
                if(listener != null)
                {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(position);
                    }
                }
            });
            mDeleteImage.setOnClickListener(v ->
            {
                if(listener != null)
                {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        listener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position)
    {
        // Prendo l'attuale elemento della lista
        ListItem currentItem = mList.get(position);
        // Imposto i valori per gli elementi che verranno visualizzati
        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mTextView.setText(currentItem.getmBarcode());
        holder.mDeleteImage.setContentDescription(currentItem.ismIsEraseable());
        // Se è rimuovibile
        if(currentItem.ismIsEraseable().equals("false"))
        {
            // Mostro l'elemento che mi permette di rimuoverlo
            holder.mDeleteImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }
}