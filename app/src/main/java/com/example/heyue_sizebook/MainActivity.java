package com.example.heyue_sizebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Display the count of records, and the current records.
 *
 * This class is the main view class of the project. <br> In this class,
 * user interaction and file manipulation is performed.
 * All files are in the form of "json" files that are stored in Emulator's
 * accessible from Android Device Monitor:
 * <pre>
 *     pre-formatted text: <br>
 *         File Explorer -> data -> data -> ca.ualberta.cs.MainActivity -> files -> file.sav.
 * </pre>
 * <code> begin <br>
 * some pseudo code here <br>
 * end.</code>
 * The file name is indicated in the &nbsp &nbsp &nbsp FILENAME constant.
 * <ul>
 * <li>item 1</li>
 * <li>item 2</li>
 * <li>item 3</li>
 * </ul>
 * <ol>
 * <li>item 1</li>
 * <li>item 2</li>
 * <li>item 3</li>
 * </ol>
 *
 * @author Heyue Huang
 * @version 1.4.2 17-01-28
 * @since 1.0
 */
public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "file.sav";
    private ArrayList<Record> recordList = new ArrayList<>();
    private ArrayAdapter<Record> adapter;
    private ListView oldRecordList;
    private TextView countTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createButton = (Button) findViewById(R.id.create);

        oldRecordList = (ListView) findViewById(R.id.oldRecordList);
        countTextView = (TextView) findViewById(R.id.count_of_people);

        /**
         * Enable click event for ListView, when one record is clicked,
         * there will show a dialog, user can choose edit or delete
         */
        oldRecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openTheRecord(position);

            }
        });

        /**
         * Enable click button, when the button was pressed,
         * the UI will change to CreateRecordActivity.
         */
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateRecordActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Show All records which are stored in Emulator on the initial interface
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        adapter = new ArrayAdapter<Record>(this,
                R.layout.list_item, recordList);
        oldRecordList.setAdapter(adapter);
        // load all records form Json
        loadAllRecord();
        // show the count num of records
        countTextView.setText(String.format("Count: %s", recordList.size()));
    }

    /**
     * Reload all the records when the button is pressed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        recordList.clear();
        loadAllRecord();

        adapter.notifyDataSetChanged();
    }

    /**
     * Open the selected record, popup a dialog with two options "Delete" and "Edit"
     */
    private void openTheRecord(final int position) {
        Record editRecord = recordList.get(position);
        // create a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // give the dialog a title
        builder.setTitle("Edit record")
                // the option delete
                .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    // when delete is chose
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // remove the selected record from screen
                        adapter.remove(recordList.get(position));
                        // remove the selected record from recordlist
                        recordList.remove(position);
                        // screen show the new records after deleting
                        adapter.notifyDataSetChanged();
                        // save the new records in Json
                        saveInFile();
                        // set the new count
                        countTextView.setText(String.format("Count: %s", recordList.size()));
                    }
                })
                // the option edit and view
                .setPositiveButton("VIEW DETAIL/EDIT", new DialogInterface.OnClickListener() {
                    // when edit is chose
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // change to CreateRecordActivity UI
                        Intent intent = new Intent(MainActivity.this, CreateRecordActivity.class);
                        // get the stored information of the selected record from Json
                        Gson gson = new Gson();
                        String recordStr = gson.toJson(recordList.get(position));
                        // change to the "Edit" function
                        intent.putExtra("EDIT",recordStr);
                        startActivity(intent);
                    }
                });
        // create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * load all records from saved json files into Arraylist
     */
    private void loadAllRecord() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            // load the records to record list
            Gson gson = new Gson();
            // Taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2017-01-26
            recordList = gson.fromJson(in, new TypeToken<ArrayList<Record>>(){}.getType());

            // clear the previous record list which is showed on the screen
            adapter.clear();
            // add new list to adapter
            adapter.addAll(recordList);
            // screen changes
            adapter.notifyDataSetChanged();
            fis.close();
        } catch (FileNotFoundException e) {
            recordList = new ArrayList<Record>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    /**
     * save all records into json string and saved it in emulator storage
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            // save the record list to Json
            Gson gson = new Gson();
            gson.toJson(recordList, out);

            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
