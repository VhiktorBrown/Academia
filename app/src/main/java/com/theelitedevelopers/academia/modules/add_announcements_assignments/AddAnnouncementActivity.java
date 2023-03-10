package com.theelitedevelopers.academia.modules.add_announcements_assignments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.data.remote.ServiceGenerator;
import com.theelitedevelopers.academia.core.data.request.Notification;
import com.theelitedevelopers.academia.core.data.request.NotificationBody;
import com.theelitedevelopers.academia.core.data.request.NotificationMessage;
import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.ActivityAddAnnouncementBinding;
import com.theelitedevelopers.academia.modules.main.MainActivity;
import com.theelitedevelopers.academia.modules.main.data.models.Announcement;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AddAnnouncementActivity extends AppCompatActivity {
    ActivityAddAnnouncementBinding binding;
    Calendar assDueDate = Calendar.getInstance();
    String date="", dateToday="";
    SimpleDateFormat simpleDateFormat, simpleTimeFormat;
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAnnouncementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goBack.setOnClickListener(v -> onBackPressed());

        binding.addAnnouncementButton.setOnClickListener(v -> {
            String assignmentTitle = binding.announcementTitle.getText().toString();
            String assignmentDescription = binding.announcementDescription.getText().toString();

            if(assignmentTitle.length() > 0 && assignmentDescription.length() > 0){
                Announcement announcement = new Announcement();
                announcement.setTitle(assignmentTitle);
                announcement.setDescription(assignmentDescription);
                try {
                    announcement.setDate(getTimeStamp());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                announcement.setAnnouncerId(SharedPref.getInstance(getApplicationContext()).getString(Constants.UID));
                announcement.setAnnouncerName(SharedPref.getInstance(getApplicationContext()).getString(Constants.NAME));

                binding.progressBar.setVisibility(View.VISIBLE);
                saveAnnouncementToDB(announcement);
            }
        });
    }

    private void setUpNotificationData(Announcement announcement){
        Notification notification = new Notification();
        notification.setTo("/topics/"+Constants.ASSIGNMENT_TOPIC);
        NotificationBody notificationBody = new NotificationBody();
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTitle("New Announcement: "+announcement.getTitle());
        notificationMessage.setBody(announcement.getDescription());

        notificationBody.setTitle("New Announcement: "+announcement.getTitle());
        notificationBody.setBody(announcement.getDescription());

        notification.setData(notificationBody);
        notification.setNotification(notificationMessage);

        notification.setPriority("high");

        sendNotification(notification, announcement);
    }


    private void saveAnnouncementToDB(Announcement announcement){

        Map<String, Object> announcementMap = new HashMap<>();
        announcementMap.put("title", announcement.getTitle());
        announcementMap.put("description", announcement.getDescription());
        announcementMap.put("announcerId", announcement.getAnnouncerId());
        announcementMap.put("announcerName", announcement.getAnnouncerName());
        announcementMap.put("date", announcement.getDate());

        // Add a new document with a generated ID
        database.collection("announcements")
                .add(announcementMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        binding.progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(AddAnnouncementActivity.this, "Announcement added to DB successfully", Toast.LENGTH_SHORT).show();

                        setUpNotificationData(announcement);
                    }
                })
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Log.w(TAG, "Error adding document", e);
                });
    }

    private void sendNotification(Notification notification, Announcement announcement){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "key=" + Constants.PUSH_NOT_KEY);

        Single<Response<JSONObject>> sendNotification = ServiceGenerator.getInstance()
                .getApi().sendNotification(headers, notification);
        sendNotification.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<JSONObject>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Response<JSONObject> sentNotificationResponse) {
                        if(sentNotificationResponse.isSuccessful()){
                            startActivity(new Intent(AddAnnouncementActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        sendNotification(notification, announcement);
                    }
                });
    }


    private Timestamp getTimeStamp() throws ParseException {
        String sourceFormat = "EEE MMM d HH:mm:ss z yyyy";
        String destinationFormat = "EEE, d MMM yyyy HH:mm:ss";

        Date date = AppUtils.Companion.convertToDateFormat(destinationFormat,
                Objects.requireNonNull(AppUtils.Companion.convertDateFromOneFormatToAnother(
                        sourceFormat, destinationFormat, new Date().toString())));

        assert date != null;
        return new Timestamp(date);
    }
}