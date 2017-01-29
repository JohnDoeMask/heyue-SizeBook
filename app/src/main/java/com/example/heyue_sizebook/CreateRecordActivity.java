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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
/**
 * Created by heyuehuang on 2017-01-28.
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
    private Map<EditText, String> editTextMap = new HashMap<>();
    private ArrayList<EditText> editTextList = new ArrayList<>();

    private Button completeRecordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_record);

        // setup each EditText and their onClickListner
        personNameEditText = (EditText) findViewById(R.id.person_name);

        recordDateEditText = (EditText) findViewById(R.id.record_date);
        recordDateEditText.setFocusable(false);
        recordDateEditText.setOnClickListener(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");

        neckSizeEditText = (EditText) findViewById(R.id.neck_size);
        bustSizeEditText = (EditText) findViewById(R.id.bust_size);
        chestSizeEditText = (EditText) findViewById(R.id.chest_size);
        waistSizeEditText = (EditText) findViewById(R.id.waist_size);
        hipSizeEditText = (EditText) findViewById(R.id.hip_size);
        inseamSizeEditText = (EditText) findViewById(R.id.inseam_size);
        commentEditText = (EditText) findViewById(R.id.comment_size);

//        editTextMap.put(recordDateEditText, null);
//        editTextMap.put(bustSizeEditText, null);
//        editTextMap.put(chestSizeEditText, null);
//        editTextMap.put(waistSizeEditText, null);
//        editTextMap.put(hipSizeEditText, null);
//        editTextMap.put(inseamSizeEditText, null);
//        editTextMap.put(commentEditText, null);

        completeRecordButton = (Button) findViewById(R.id.complete);
        completeRecordButton.setOnClickListener(this);

        recordDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDate = Calendar.getInstance();
                new DatePickerDialog(CreateRecordActivity.this, onDateSetListener,
                        newRecordDate.get(Calendar.YEAR), newRecordDate.get(Calendar.MONTH),
                        newRecordDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Gson gson = new Gson();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String jsonStr = extras.getString("EDIT");
            newRecord = gson.fromJson(jsonStr, Record.class);
            newRecordDate = newRecord.getDate();
            personNameEditText.setText(newRecord.getPersonName());
            if (newRecord.getDate() != null) {
                Log.i("Debug",sdf.format(newRecordDate.getTime()));
                recordDateEditText.setText(sdf.format(newRecordDate.getTime()));
            }
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
            if (newRecord.getComment() != null) {
                Log.i("Debug", newRecord.getComment());
                commentEditText.setText(newRecord.getComment());
            }
            //Log.i("Debug", newRecord.getComment());
        }
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            newRecordDate.set(Calendar.YEAR, year);
            newRecordDate.set(Calendar.MONTH, monthOfYear);
            newRecordDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            recordDateEditText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            Log.i("Debug", recordDateEditText.getText().toString());
        }
    };


    protected void createRecord() {
        Intent intent = new Intent(this, MainActivity.class);
        // Get record name from user input
        String personName = personNameEditText.getText().toString();
        Record newRecord = new Record();
        // if record name is not given
        if (personName.isEmpty()) {
            personNameEditText.setError("Record name is empty!");
            return;
        } else if (!neckSizeEditText.getText().toString().isEmpty()) {
            // if neckSize isn't positive
            double neckSize =parseDouble(neckSizeEditText.getText().toString());

            if (!checkSize(neckSize, neckSizeEditText)) return;
            Log.i("Debug", Double.toString(neckSize));
            newRecord.setNeckSize(neckSize);
        } else if (!bustSizeEditText.getText().toString().isEmpty()) {
            // if bustSize isn't positive
            double bustSize = parseDouble(bustSizeEditText.getText().toString());
            if (!checkSize(bustSize, bustSizeEditText)) return;
            newRecord.setBustSize(bustSize);
        } else if (!chestSizeEditText.getText().toString().isEmpty()) {
            // if chestSize isn't positive
            double chestSize = parseDouble(chestSizeEditText.getText().toString());
            if (!checkSize(chestSize, chestSizeEditText)) return;
            newRecord.setChestSize(chestSize);
        } else if (!waistSizeEditText.getText().toString().isEmpty()) {
            // if WaistSize isn't positive
            double waistSize = parseDouble(waistSizeEditText.getText().toString());
            if (!checkSize(waistSize, waistSizeEditText)) return;
            newRecord.setWaistSize(waistSize);
        } else if (!hipSizeEditText.getText().toString().isEmpty()) {
            // if HipSize isn't positive
            double hipSize = parseDouble(hipSizeEditText.getText().toString());
            if (!checkSize(hipSize, hipSizeEditText)) return;
            newRecord.setHipSize(hipSize);
        } else if (!inseamSizeEditText.getText().toString().isEmpty()) {
            // if InseamSize isn't positive
            double inseamSize = parseDouble(inseamSizeEditText.getText().toString());
            if (!checkSize(inseamSize, inseamSizeEditText)) return;
            newRecord.setInseamSize(inseamSize);
        } else if (!commentEditText.getText().toString().isEmpty()) {
            // if InseamSize isn't positive
            String comment = commentEditText.getText().toString();
            Log.i("Debug", comment);
            newRecord.setComment(comment);
        } else if (!recordDateEditText.getText().toString().isEmpty()) {
//            Log.i("Debug", "date is not empty");
            newRecord.setDate(newRecordDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
//            Log.i("Debug", sdf.format(newRecordDate.getTime()));
        }
        try {
            Log.i("Debug", "Done!!!!!!");
            newRecord.setPersonName(personName);
//            newRecord.setDate(newRecordDate);
            recordList.add(newRecord);
            saveInFile();
            finish();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }


    private Boolean checkSize(double checknumber, EditText editText) {
        if (checknumber > 0 ){
            editText.setText(Double.toString(checknumber));
            return true;
        }
        editText.setError("Input Number must be positive number!");
        return false;
    }


    @Override
    public void onClick(View v) {
        if (v == completeRecordButton) {
            Log.i("Debug", "button clicked");
            createRecord();


        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

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

    private double parseDouble(String str) {
        double num = 0;
        try {
            num = Double.parseDouble(str);
            DecimalFormat df = new DecimalFormat("#,##0.0");
            num = Double.valueOf(df.format(num));
            Log.i("Debug", Double.toString(num));
            return num;
        } catch (NumberFormatException e) {
            Log.i("Debug", e.getMessage());
            Toast toast = Toast.makeText(this, "Expect a number", Toast.LENGTH_SHORT);
            toast.show();
        }
        return num;
    }
}
