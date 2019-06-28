package com.example.abhishek.notezero.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.abhishek.notezero.Model.IntentData;
import com.example.abhishek.notezero.Model.ItemData;
import com.example.abhishek.notezero.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteActivity extends Activity {

    private IntentData intentData;
    private ItemData itemData;
    private EditText title_edit, description_edit;

    //FileHandling Class
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        title_edit = (EditText) findViewById(R.id.note_title_edittext_id);
        description_edit = (EditText) findViewById(R.id.note_description_edittext_id);

        Intent i = getIntent();
        intentData = new IntentData();
        intentData.setType(i.getStringExtra("type"));

        if(intentData.getType().equals("note_old")){
            intentData.setName(i.getStringExtra("name"));
            intentData.setPath(i.getStringExtra("path"));
        }else if(intentData.getType().equals("note_new")){
            intentData.setPath(i.getStringExtra("path"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(intentData.getType().equals("note_old")){
            loadSavedNote();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (validateAndSaveNote()) {
            saveNote();
        }
    }

    private void loadSavedNote() {
        try {
            File noteFile = new File(intentData.getPath(), intentData.getName()+".zerodata");
            fileInputStream = new FileInputStream(noteFile);
            objectInputStream = new ObjectInputStream(fileInputStream);
            itemData = new ItemData();
            itemData = (ItemData) objectInputStream.readObject();
            SetDataToNote();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetDataToNote(){
        title_edit.setText(itemData.getTitle());
        description_edit.setText(itemData.getDescription());
    }

    private boolean validateAndSaveNote() {

        String title = title_edit.getText().toString();
        String description = description_edit.getText().toString();
        itemData = new ItemData();

        if (title != null) {
            if (!title.trim().isEmpty()) {
                itemData.setTitle(title);
                if (!description.trim().isEmpty()) {
                    itemData.setDescription(description);
                    return true;
                }
            }
        } else if (description != null) {
            if (!description.trim().isEmpty()) {
                itemData.setDescription(description);
                if (!title.trim().isEmpty()) {
                    itemData.setTitle(title);
                } else {
                    String dateTitle = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                    itemData.setTitle(dateTitle);
                }
                return true;
            }
        }

        return false;
    }

    private void saveNote() {
        try {

            File noteFile = new File(itemData.getPath(), itemData.getTitle() + ".zerodata");
            fileOutputStream = new FileOutputStream(noteFile);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(itemData);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
