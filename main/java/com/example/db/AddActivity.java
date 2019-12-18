package com.example.db;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // ADD бутона
        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // взимаме edittext-овете
                EditText text_title = (EditText)findViewById(R.id.editText1);
                EditText text_note = (EditText)findViewById(R.id.editText2);

                if(text_title.getText().toString().equals("")) // ако е тайтъла е празен (не е въведено нищо от клиента)
                {
                    Toast.makeText(AddActivity.this, "Note Title is empty!", Toast.LENGTH_SHORT).show();
                }
                else if(text_note.getText().toString().equals("")) // ако текста е празен (не е въведено нищо от клиента)
                {
                    Toast.makeText(AddActivity.this, "Note Text is empty!", Toast.LENGTH_SHORT).show();
                }
                else // значи е въвел всичко
                {
                    Intent intent = new Intent();
                    // пращаме данните към активитито
                    intent.putExtra("Title", text_title.getText().toString());
                    intent.putExtra("Note", text_note.getText().toString());
                    setResult(1, intent); // код 1
                    finish(); // финишираме активитито
                }
            }
        });

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
