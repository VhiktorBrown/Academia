package com.theelitedevelopers.academia.modules.add_announcements_assignments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.ActivityAddAssignmentBinding;
import com.theelitedevelopers.academia.modules.authentication.RegisterActivity;
import com.theelitedevelopers.academia.modules.authentication.data.models.Student;
import com.theelitedevelopers.academia.modules.main.MainActivity;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddAssignmentActivity extends AppCompatActivity {
    ActivityAddAssignmentBinding binding;
    Calendar assDueDate = Calendar.getInstance();
    String date="", dateToday="";
    SimpleDateFormat simpleDateFormat, simpleTimeFormat;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    Timestamp dateDue, datePosted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAssignmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addAssignmentButton.setOnClickListener(v -> {
            String assignmentTitle = binding.assignmentTitle.getText().toString();
            String assignmentDescription = binding.assignmentDescription.getText().toString();
            String courseCode = binding.courseCode.getText().toString();
            String courseTitle = binding.courseTitle.getText().toString();
            String lecturerName = binding.lecturer.getText().toString();

            if(assignmentTitle.length() > 0 && assignmentDescription.length() > 0 &&
            courseCode.length() > 0 && courseTitle.length() > 0 && lecturerName.length() > 0){
                if(!date.equals("")){
                    Assignment assignment = new Assignment();
                    assignment.setTitle(assignmentTitle);
                    assignment.setDescription(assignmentDescription);
                    assignment.setCourseCode(courseCode);
                    assignment.setCourseTitle(courseTitle);
                    assignment.setLecturerName(lecturerName);
                    assignment.setDatePosted(datePosted);
                    assignment.setDateDue(dateDue);

                    binding.progressBar.setVisibility(View.VISIBLE);

                    saveAssignmentToDB(assignment);

                }else {
                    Toast.makeText(this, "Please, select the date of submission for this assignment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.selectDate.setOnClickListener(v -> showDateTimePicker());
    }

    private void saveAssignmentToDB(Assignment assignment){

        Map<String, Object> assignmentMap = new HashMap<>();
        assignmentMap.put("title", assignment.getTitle());
        assignmentMap.put("description", assignment.getDescription());
        assignmentMap.put("courseCode", assignment.getCourseCode());
        assignmentMap.put("courseTitle", assignment.getCourseTitle());
        assignmentMap.put("lecturerName", assignment.getLecturerName());
        assignmentMap.put("datePosted", assignment.getDatePosted());
        assignmentMap.put("dateDue", assignment.getDateDue());
        assignmentMap.put("image", "");
        assignmentMap.put("uid", SharedPref.getInstance(getApplicationContext()).getString(Constants.UID));

        // Add a new document with a generated ID
        database.collection("assignments")
                .add(assignmentMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        binding.progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(AddAssignmentActivity.this, "Assignment added to DB successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(AddAssignmentActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }



    public void showDateTimePicker(){
        final Calendar currentDate = Calendar.getInstance();
        assDueDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            assDueDate.set(year, month, dayOfMonth);
            showTimePicker();

            simpleDateFormat = new SimpleDateFormat("EEE dd MMM yyyy");

            simpleTimeFormat = new SimpleDateFormat("hh:mm aa");


            String sourceFormat = "EEE MMM d HH:mm:ss z yyyy";
            String destinationFormat = "EEE, d MMM yyyy HH:mm:ss";

            //convert date to Universal format
            date = AppUtils.Companion.convertDateFromOneFormatToAnother(sourceFormat, destinationFormat, assDueDate.getTime().toString());
            dateToday = AppUtils.Companion.convertDateFromOneFormatToAnother(sourceFormat, destinationFormat, new Date().toString());

        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void showTimePicker(){
        Calendar currentDate = Calendar.getInstance();

        new TimePickerDialog(this, (timePicker, i, i1) -> {

            assDueDate.set(Calendar.HOUR_OF_DAY, i);
            assDueDate.set(Calendar.MINUTE, i1);

            simpleDateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
            simpleTimeFormat = new SimpleDateFormat("hh:mm aa");


            String sourceFormat = "EEE MMM d HH:mm:ss z yyyy";
            String destinationFormat = "EEE, d MMM yyyy HH:mm:ss";

            //convert date to Universal format
            date = AppUtils.Companion.convertDateFromOneFormatToAnother(sourceFormat, destinationFormat, assDueDate.getTime().toString());
            binding.selectDate.setText(simpleDateFormat.format(assDueDate.getTime()) +". "+ simpleTimeFormat.format(assDueDate.getTime()));

            try {
                getTimeStamp(date, dateToday);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
    }

    private void getTimeStamp(String date, String dateToday) throws ParseException {
        String destinationFormat = "EEE, d MMM yyyy HH:mm:ss";

        dateDue = new Timestamp(Objects.requireNonNull(
                AppUtils.Companion.convertToDateFormat(destinationFormat, date)));
        datePosted = new Timestamp(Objects.requireNonNull(
                AppUtils.Companion.convertToDateFormat(destinationFormat, dateToday)));
    }

}