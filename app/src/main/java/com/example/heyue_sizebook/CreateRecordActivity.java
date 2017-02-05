package com.example.heyue_sizebook;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Create the record and save it into Json, when the "edit" situation or create
 * button is selected, jump to the UI.
 *
 * This class is a class of the project. <br> In this class,
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
public class CreateRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String FILENAME = "file.sav";
    private ArrayList<Record> recordList = new ArrayList<>();
    private Record newRecord;
    private Calendar newRecordDate;
    private EditText personNameEditText;
    private EditText recordDateEditText;
    private EditText neckSizeEditText;
    private EditText bustSizeEditText;
    private EditText chestSizeEditText;
    private EditText waistSizeEditText;
    private EditText hipSizeEditText;
    private EditText inseamSizeEditText;
    private EditText commentEditText;
    // the flag for "edit" method
    private int index = -1;
    private Button completeRecordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_record);

        // setup each EditText for person's name, neck's size, bust's size,
        // chest's size, waist's size, hip's size, inseam's size, and comment.
        personNameEditText = (EditText) findViewById(R.id.person_name);
        neckSizeEditText = (EditText) findViewById(R.id.neck_size);
        bustSizeEditText = (EditText) findViewById(R.id.bust_size);
        chestSizeEditText = (EditText) findViewById(R.id.chest_size);
        waistSizeEditText = (EditText) findViewById(R.id.waist_size);
        hipSizeEditText = (EditText) findViewById(R.id.hip_size);
        inseamSizeEditText = (EditText) findViewById(R.id.inseam_size);
        commentEditText = (EditText) findViewById(R.id.comment_size);

        // setup each the complete button.
        completeRecordButton = (Button) findViewById(R.id.complete);
        completeRecordButton.setOnClickListener(this);

        // setup EditText for Record's date
        recordDateEditText = (EditText) findViewById(R.id.record_date);
        // setup dialog and date format for Record's date
        recordDateEditText.setFocusable(false);
        recordDateEditText.setOnClickListener(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");

        // when the date's EditText is clicked, a date dialog popup,
        // let user chooses the date.
        recordDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDate = Calendar.getInstance();
                new DatePickerDialog(CreateRecordActivity.this, onDateSetListener,
                        newRecordDate.get(Calendar.YEAR), newRecordDate.get(Calendar.MONTH),
                        newRecordDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // load all records
        loadAllRecord();

        Gson gson = new Gson();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        // get the Str that the "Edit/View Details" option is selected on the main interface
        if (extras != null) {
            // if the string is "Edit",
            String jsonStr = extras.getString("EDIT");
            // load the selected record
            newRecord = gson.fromJson(jsonStr, Record.class);
            // set the flag to the index of the record list
            index = getIndex(newRecord);
            // get the date and person name of the record
            newRecordDate = newRecord.getDate();
            personNameEditText.setText(newRecord.getPersonName());
            // if the date isn't null, set the content to the new record
            if (newRecord.getDate() != null) {
                recordDateEditText.setText(sdf.format(newRecordDate.getTime()));
            }
            // if the neck, bust, chest, waist, hip, inseam doesn't equal 0,
            // set the content to the new record
            if (newRecord.getNeckSize() != 0) {
                neckSizeEditText.setText(Double.toString(newRecord.getNeckSize()));
            }
            if (newRecord.getBustSize() != 0) {
                bustSizeEditText.setText(Double.toString(newRecord.getBustSize()));
            }
            if (newRecord.getChestSize() != 0) {
                chestSizeEditText.setText(Double.toString(newRecord.getChestSize()));
            }
            if (newRecord.getWaistSize() != 0) {
                waistSizeEditText.setText(Double.toString(newRecord.getWaistSize()));
            }
            if (newRecord.getHipSize() != 0) {
                hipSizeEditText.setText(Double.toString(newRecord.getHipSize()));
            }
            if (newRecord.getInseamSize() != 0) {
                inseamSizeEditText.setText(Double.toString(newRecord.getInseamSize()));
            }
            // if the comment isn't null, set the comment to the new record
            if (newRecord.getComment() != null) {
                commentEditText.setText(newRecord.getComment());
            }
        }
    }

    /**
     * when the complete button is pressed, finish creating record
     *
     * @param v: View
     */
    @Override
    public void onClick(View v) {
        if (v == completeRecordButton) {
            createRecord();
        }
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        /**
         * The On date set listener. Set the date dialog's format
         *
         * @param view: DatePicker method
         * @param year: the year
         * @param monthOfYear: the month
         * @param dayOfMonth: the day
         */
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            newRecordDate.set(Calendar.YEAR, year);
            newRecordDate.set(Calendar.MONTH, monthOfYear);
            newRecordDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            recordDateEditText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }
    };

    /**
     * Create record.Check if the person name field(which is mandatory) is filled,
     * and the size fields are positive integer
     */
    protected void createRecord() {
        Intent intent = new Intent(this, MainActivity.class);
        // Get record name from user input
        String personName = personNameEditText.getText().toString();
        Record newRecord = new Record();
        // if person name is not given
        if (personName.isEmpty()) {
            // show the warning
            personNameEditText.setError("Person's name is empty!");
            return;
        }
        // if person name already exists
        if (checkPersonName(personName)) {
            // show the warning
            personNameEditText.setError("The person already exits!");
            return;
        }
        // if the size fields are not empty
        if (!neckSizeEditText.getText().toString().isEmpty()) {
            // if neckSize isn't positive, round to at most one decimal place
            double neckSize =parseDouble(neckSizeEditText.getText().toString());
            if (!checkSize(neckSize, neckSizeEditText)) return;
            newRecord.setNeckSize(neckSize);
        }
        if (!bustSizeEditText.getText().toString().isEmpty()) {
            // if bustSize isn't positive, round to at most one decimal place
            double bustSize = parseDouble(bustSizeEditText.getText().toString());
            if (!checkSize(bustSize, bustSizeEditText)) return;
            newRecord.setBustSize(bustSize);
        }
        if (!chestSizeEditText.getText().toString().isEmpty()) {
            // if chestSize isn't positive, round to at most one decimal place
            double chestSize = parseDouble(chestSizeEditText.getText().toString());
            if (!checkSize(chestSize, chestSizeEditText)) return;
            newRecord.setChestSize(chestSize);
        }
        if (!waistSizeEditText.getText().toString().isEmpty()) {
            // if WaistSize isn't positive, round to at most one decimal place
            double waistSize = parseDouble(waistSizeEditText.getText().toString());
            if (!checkSize(waistSize, waistSizeEditText)) return;
            newRecord.setWaistSize(waistSize);
        }
        if (!hipSizeEditText.getText().toString().isEmpty()) {
            // if HipSize isn't positive, round to at most one decimal place
            double hipSize = parseDouble(hipSizeEditText.getText().toString());
            if (!checkSize(hipSize, hipSizeEditText)) return;
            newRecord.setHipSize(hipSize);
        }
        if (!inseamSizeEditText.getText().toString().isEmpty()) {
            // if InseamSize isn't positive, round to at most one decimal place
            double inseamSize = parseDouble(inseamSizeEditText.getText().toString());
            if (!checkSize(inseamSize, inseamSizeEditText)) return;
            newRecord.setInseamSize(inseamSize);
        }
        // if the comment isn't empty
        if (!commentEditText.getText().toString().isEmpty()) {
            // set the comment
            String comment = commentEditText.getText().toString();
            newRecord.setComment(comment);
        }
        // if the date isn't empty
        if (!recordDateEditText.getText().toString().isEmpty()) {
            // set the date
            newRecord.setDate(newRecordDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
        }
        try {
            // set the person name to new record
            newRecord.setPersonName(personName);
            // if the flag is "Edit", then set the new record to cover the old one
            if (index != -1) {
                recordList.set(index, newRecord);
            // if the flag is "Create", then add the new record to the record list
            } else {
                recordList.add(newRecord);
            }
            // save the record list to Json
            saveInFile();
            // close the activity
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    /**
     * Check the if the inputted size is positive.
     * If it is, the returns true, else returns false and a warmming message.
     *
     * @param checkNumber: the inputted size
     * @param editText: the corresponding editText
     * @return true or false
     */
    private Boolean checkSize(double checkNumber, EditText editText) {
        // if it is positive, set the size
        if (checkNumber > 0 ){
            editText.setText(Double.toString(checkNumber));
            return true;
        }
        // else, returns a warning message
        editText.setError("Input Number must be positive number!");
        return false;
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

    /**
     * Round the inputted size to at most one decimal place.
     * Check if it is numeric type.
     *
     * @param str: inputted size
     * @return num: inputted size which round to at most one decimal place
     */
    private double parseDouble(String str) {
        double num = 0;
        try {
            num = Double.parseDouble(str);
            DecimalFormat df = new DecimalFormat("#,##0.0");
            num = Double.valueOf(df.format(num));
            return num;
        } catch (NumberFormatException e) {
            Toast toast = Toast.makeText(this, "Expect a number", Toast.LENGTH_SHORT);
            toast.show();
        }
        return num;
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
            fis.close();
        } catch (FileNotFoundException e) {
            recordList = new ArrayList<Record>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    /**
     * check the index of the selected record in the record list
     * set the flag to "Edit"
     *
     * @param record: the selected record
     * @return
     */
    private int getIndex(Record record) {
        int index = 0;
        // for record which already exits in the record list
        for (Record r : recordList) {
            // if find the same one
            if (r.getPersonName().equals(record.getPersonName())) {
                // then break
                break;
            }
            // else, continue the loop
            ++index;
        }
        return index;
    }

    /**
     * Check if the person name already in the list
     *
     * @param name: the person's name
     * @return true or false
     */
    private Boolean checkPersonName(String name){
        // for record in the record list
        for (Record r : recordList) {
            // if the name equals
            if (r.getPersonName().equals(name)) {
                // if it is under "Edit" situation
                if (index != -1) {
                    // if the person name stays the same, then continue
                    if (newRecord.getPersonName().equals(name)) continue;
                }
                return true;
            }
        }
        return false;
    }
}
