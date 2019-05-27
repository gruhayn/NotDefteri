package com.example.notdefteri;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.notdefteri.Adapter.NoteAdapter;
import com.example.notdefteri.Entity.Note;
import com.example.notdefteri.Listener.NoteClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ListNotes extends AppCompatActivity {
    private RecyclerView rv;
    private List<Note> notes;
    private FirebaseFirestore db;
    private NoteAdapter adapter;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        db = FirebaseFirestore.getInstance();
        rv = findViewById(R.id.recycler_view);
        layout = findViewById(R.id.layout1);

        Query query = db.collection("Notes").orderBy("timestamp");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }


                notes = queryDocumentSnapshots.toObjects(Note.class);
                Log.d("Note List", " => " +notes.toString());

                adapter = new NoteAdapter(notes, new NoteClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        popup(notes.get(position),position);
                    }
                });

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);
            }

        });

    }

    public void popup(final Note note,final int i){
        Log.d("tag", "onClick: ");
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup = layoutInflater.inflate(R.layout.popup,null);
        final PopupWindow popupWindow = new PopupWindow(
                popup,
                DrawerLayout.LayoutParams.WRAP_CONTENT,
                DrawerLayout.LayoutParams.WRAP_CONTENT
        );
        popupWindow.showAtLocation(layout, Gravity.CENTER,0,0);
        Button btn_iptal = popup.findViewById(R.id.btn_iptal);
        btn_iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        Button btn_sil = popup.findViewById(R.id.sil);
        btn_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Notes").document(note.getId()).delete();
                adapter.removeAt(i);
                notes.remove(note);
                popupWindow.dismiss();
            }
        });

    }
}
