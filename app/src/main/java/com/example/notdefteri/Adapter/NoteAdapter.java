package com.example.notdefteri.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.notdefteri.Entity.Note;
import com.example.notdefteri.Listener.NoteClickListener;
import com.example.notdefteri.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{
    private List<Note> notes;
    private NoteClickListener listener;

    public NoteAdapter(List<Note> notes, NoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item,viewGroup,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int i) {
        Log.d("tag", "onBindViewHolder: "+i);
        holder.title.setText(notes.get(i).getTitle());
        holder.body.setText(notes.get(i).getBody());
        holder.date.setText(notes.get(i).getTimestamp().toString());
    }

    @Override
    public int getItemCount() {
        Log.d("tag", "getItemCount: "+notes.size());
        return notes.size();
    }
    public void removeAt(int position) {
        notes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notes.size());
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        TextView title, body, date;
        public NoteViewHolder(final View v) {
            super(v);

            layout = v.findViewById(R.id.layout);
            title = v.findViewById(R.id.tv_title);
            body = v.findViewById(R.id.tv_body);
            date = v.findViewById(R.id.tv_date);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v,getAdapterPosition());
                }
            });
        }
    }
}
