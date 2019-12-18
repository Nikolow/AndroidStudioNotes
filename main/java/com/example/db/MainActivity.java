package com.example.db;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener
{
    MyRecyclerViewAdapter adapter; // recyclerview адаптера

    private List<Note> notesList = new ArrayList<>(); // лист от бележки
    private DatabaseHelper db; // обекта за DB връзката
    RecyclerView recyclerView;

    private int LastClicked=0; // стойността която се запазва при кликане на итем от recyclerview-a

    @Override
    public void onItemClick(View view, int position) // при натискане на итем от recyclerview-a
    {
        Note n; // създаваме бележка
        LastClicked = position;
        n = adapter.getItem(position); // казваме коя да е точно от итем-а

        // стартираме ново активити, което е ForResult
        Intent intent = new Intent(getApplicationContext(), ShowActivity.class);

        // изпращаме му данните, които искаме
        intent.putExtra("note_title", n.getTitle());
        intent.putExtra("note_text", n.getNote());
        intent.putExtra("note_time", n.getTimestamp());

        // чакаме резултат с код 2
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) // функцията, която приема резултата
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2) // ако кода е 2
        {
            // вземаме данните, което ни е пратило другото активити
            String TitleSave=data.getStringExtra("Title");
            String NoteSave=data.getStringExtra("Note");

            if(TitleSave != null && NoteSave != null) // ако не са NULL
            {
                if(TitleSave.equals("delete") && NoteSave.equals("delete")) // ако са DELETE
                {
                    deleteNote(LastClicked); // трием
                    Toast.makeText(MainActivity.this, "Note DELETED!", Toast.LENGTH_SHORT).show();
                }
                else // ако не са, значи са с други данни - ъпдейт
                {
                    updateNote(TitleSave, NoteSave, LastClicked); // ъпдейтваме
                    Toast.makeText(MainActivity.this, "Note Updated!", Toast.LENGTH_SHORT).show();
                }
            }

            this.recreate(); // правеим recreate (вика отново onCreate), за да попълни данните на ново
        }

        if(requestCode==1) // ако кода е 1
        {
            // вземаме данните, което ни е пратило другото активити
            String TitleAdd=data.getStringExtra("Title");
            String NoteAdd=data.getStringExtra("Note");

            if(TitleAdd != null && NoteAdd != null) // ако не са NULL
            {
                createNote(TitleAdd, NoteAdd); // създаваме нова бележка с данните
                Toast.makeText(MainActivity.this, "Note Added!", Toast.LENGTH_SHORT).show();
            }

            this.recreate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) // при създаване на активитито
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Бутона, който ни служи за ДОБАВЯНЕ на нова бележка (праща ни в другото активити)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivityForResult(intent, 1); // създаваме активити ForResult с код 1
            }
        });


        // създаваме (иницализираме) DB обекта
        db = new DatabaseHelper(this);
        notesList.addAll(db.getAllNotes()); // попълваме листата с всички взети от базата данни


        // правим recyclerview-a
        recyclerView = findViewById(R.id.RList); // взимаме кой е обекта
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // под всеки айтъм имаме линия

        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // сетваме layouta
        adapter = new MyRecyclerViewAdapter(this, notesList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        enableSwipeToDeleteAndUndo(); // swipe
    }

    private void enableSwipeToDeleteAndUndo()  // swipe
    {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this)
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)  // след като свърши swipe-a
            {
                final int position = viewHolder.getAdapterPosition(); // вземаме позицията на итема
                adapter.removeItem(position); // махаме го от адаптора
                //deleteNote(position); // махаме го от бд + от листата
            }
        };

        // закачаме го към ресайкълвюто
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }





    private void createNote(String title, String note) // функция за създаване на бележка
    {
        // инсъртваме бележка в базата данни и взимаме неговото ID
        long id = db.insertNote(title, note);

        // вземаме туко що създадената бележка от базата данни
        Note n = db.getNote(id);

        if (n != null)
        {
            // добавяме новата бележка в арей листата на 0 позицияя
            notesList.add(0, n);
        }
    }

    private void updateNote(String title, String note, int position) // функция за ъпдейт на дадена бележка
    {
        Note n = notesList.get(position); // създаваме бележка, която казваме коя да е точно

        // ъпдейтваме данните
        n.setTitle(title);
        n.setNote(note);

        // ъпдейтваме бележката в базата данни
        db.updateNote(n);

        // рефрешваме листата
        notesList.set(position, n);
    }

    private void deleteNote(int position) // функция за изтриване на дадена бележка
    {
        // изтриваме бележката от базата данни
        db.deleteNote(notesList.get(position));

        // премахваме бележката от листа
        notesList.remove(position);
    }


    @Override
    public void onBackPressed() // при натискането на бутона за назад от андроид
    {
        Intent intent=new Intent();
        setResult(1,intent); // код 1
        finish(); // финиширане активитито
        super.onBackPressed();
    }
}
