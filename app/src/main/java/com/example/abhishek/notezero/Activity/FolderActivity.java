package com.example.abhishek.notezero.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.notezero.Adapter.RecyclerViewAdapter;
import com.example.abhishek.notezero.Model.IntentData;
import com.example.abhishek.notezero.Model.ItemData;
import com.example.abhishek.notezero.Model.ListViewData;
import com.example.abhishek.notezero.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FolderActivity extends AppCompatActivity {

    private Context context;
    private FloatingActionButton fab;
    private ItemData folderPref;
    private IntentData intentData;
    private ItemData itemData;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter myAdapter;
    private ListViewData listViewData;
    private ArrayList<ListViewData> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        context = this;

        //ItemData object initialization
        itemData = new ItemData();

        Intent i = getIntent();
        if(i.getStringExtra("title") == null){
            itemData.setTitle("Home");
            itemData.setPath(context.getFilesDir().getAbsolutePath().toString()+"/Home/");
            itemData.setType("folder");
            createHomeFolder();
        }else{
            itemData.setType(i.getStringExtra("type"));
            itemData.setTitle(i.getStringExtra("title"));
            itemData.setPath(i.getStringExtra("path"));
        }
        setTitle(itemData.getTitle());

        itemsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.folder_recycler_view_id);
        myAdapter = new RecyclerViewAdapter(itemsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);

        fab = (FloatingActionButton) findViewById(R.id.new_item_fab_id);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewItemDialog();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        LoadListData();

    }


    private void LoadListData() {

        try{

            Toast.makeText(context,"path : "+itemData.getPath(), Toast.LENGTH_SHORT).show();
            Toast.makeText(context,"parent path :"+itemData.getPath(), Toast.LENGTH_SHORT).show();

            File thisFolder = new File(itemData.getPath(),itemData.getTitle());
            File[] files  = thisFolder.listFiles();
            Toast.makeText(context, "listFiles : "+thisFolder.listFiles().toString()
                    , Toast.LENGTH_SHORT).show();

            for(int i=0;i<files.length;i++){
                Toast.makeText(context,"files : "+files[i].getName(), Toast.LENGTH_SHORT).show();
                /*
                ListViewData listViewData = new ListViewData();
                listViewData.setTitle(files[i].getName());
                itemsList.add(listViewData);





                */
            }

            myAdapter.notifyDataSetChanged();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        File thisFileTemp = new File(itemData.getPath());

        startActivity(new Intent(FolderActivity.this,FolderActivity.class)
                .putExtra("type","folder")
                .putExtra("title",thisFileTemp.getParentFile().getName().toString())
                .putExtra("path",thisFileTemp.getParentFile().getAbsolutePath().toString()));
    }

    private void createHomeFolder(){
        try{

            File home = new File(context.getFilesDir().getAbsolutePath().toString(),"Home");
            FileOutputStream fileOutputStream = new FileOutputStream(home);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(itemData);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void NewItemDialog() {
        CharSequence files[] = new CharSequence[]{"Note", "Folder"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(files, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    startActivity(new Intent(FolderActivity.this, NoteActivity.class)
                            .putExtra("type", "note_new")
                            .putExtra("path",itemData.getPath()+"/"+itemData.getTitle()+"/"));
                } else if (which == 1) {
                    NewFolderDialog();
                }
            }
        });
        builder.show();
    }



    private void NewFolderDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.new_folder_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText folderName = (EditText) dialogView.findViewById(R.id.new_folder_dialog_edittext_id);

        dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!folderName.equals(null)) {
                    if (!folderName.getText().toString().trim().isEmpty()) {
                        String name = folderName.getText().toString();
                        CreateNewFolder(name);
                        Toast.makeText(context, "folder created !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FolderActivity.this, FolderActivity.class)
                                .putExtra("type","folder")
                                .putExtra("title", name)
                                .putExtra("path", itemData.getPath()+"/"+name));
                    } else {
                        Toast.makeText(context, "Enter folder name", Toast.LENGTH_SHORT).show();
                        NewFolderDialog();
                    }
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog newFolderDialog = dialogBuilder.create();
        newFolderDialog.show();
    }

    private void CreateNewFolder(String name) {
        File newFolder = new File(itemData.getPath(),name);
        newFolder.mkdir();
    }

}
