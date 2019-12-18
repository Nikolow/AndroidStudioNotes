package com.example.db;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ShowActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) // при създаване
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);


        Intent intent = getIntent();
        // вземаме данните, които са изпратени от другото ентити
        String title=intent.getStringExtra("note_title");
        String note=intent.getStringExtra("note_text");
        String time=intent.getStringExtra("note_time");

        // взимаме edittext-овете
        final EditText text_title = (EditText)findViewById(R.id.editText2);
        final EditText text_note = (EditText)findViewById(R.id.editText3);
        final EditText text_time = (EditText)findViewById(R.id.editText4);

        // сетваме им данните, които ги взехме
        text_title.setText(title);
        text_note.setText(note);
        text_time.setText(time);


        // SAVE бутона
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(text_title.getText().toString().equals("")) // ако тайтъила е празен (тоест клиента го изтрие)
                {
                    Toast.makeText(ShowActivity.this, "Note Title is empty!", Toast.LENGTH_SHORT).show();
                }
                else if(text_note.getText().toString().equals("")) // ако е текста е празен (клиента го изтрие)
                {
                    Toast.makeText(ShowActivity.this, "Note Text is empty!", Toast.LENGTH_SHORT).show();
                }
                else // значи не са празни
                {
                    Intent intent = new Intent();
                    // прехвърляме данните
                    intent.putExtra("Title", text_title.getText().toString());
                    intent.putExtra("Note", text_note.getText().toString());
                    setResult(2, intent); // код 2
                    finish(); // финишираме активитито
                }
            }
        });



        // DELETE бутона
        FloatingActionButton fab3 = findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                // прехвърляме данните
                intent.putExtra("Title", "delete");
                intent.putExtra("Note", "delete");
                setResult(2, intent); // код 2
                finish(); // финишираме активитото
            }
        });
    }

    @Override
    public void onBackPressed() // при натискането на бутона за назад от андроид
    {
        Intent intent=new Intent();
        setResult(2,intent); // код 2
        finish(); // финиширане активитито
        super.onBackPressed();
    }
}
