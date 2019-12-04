package com.example.socialhour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.DataTypes.SocialHourEvent;
import com.example.DataTypes.Group;
import com.example.DataTypes.User;
import com.example.services.DBConnection;
import com.example.services.GenerateMeetingTimes;
import com.google.api.client.util.DateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class InputGenerateMeetingTimes extends AppCompatActivity {
    private DBConnection dbConnection;
    private Button createButton, early, late;
    private EditText eventName, length;
    private static TextView currentDate;
    private static int year, month, day;
    private static int lowerHour, lowerMinute;
    private static int upperHour, upperMinute;
    public static String eventNameText;
    public static ArrayList<LocalDateTime> times;
    public static int lenHour, lenMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_generate_meeting_times);

        createButton = findViewById(R.id.genMeetingTimes);
        eventName = findViewById(R.id.editText);
        currentDate =findViewById(R.id.chosenDate);
        length = findViewById(R.id.length);

        //date = findViewById(R.id.DateInput);
        /*early = findViewById(R.id.StartTime);
        late = findViewById(R.id.EndTime);*/
        dbConnection = DBConnection.getInstance();



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lowerMinute < 30){
                    lowerMinute = 0;
                }
                else{
                    lowerMinute = 30;
                }
                if(upperMinute < 30){
                    upperMinute = 0;
                }
                else{
                    upperMinute = 30;
                }
                int intLowerTime = lowerHour * 100 + lowerMinute;
                int intUpperTime = upperHour * 100 + upperMinute;
                int eventLength = Integer.parseInt(length.getText().toString());
                lenHour = eventLength / 60;
                lenMin = eventLength % 60;
                eventLength =  lenHour * 100 + lenMin;
                System.out.println("EVENT LENGTH : " + eventLength);
                times = GenerateMeetingTimes.generateMeetingTime(SingleGroupPage.selectedGroup.getId(),month,day,year, intLowerTime, intUpperTime, eventLength);
                System.out.println(times);
                eventNameText = eventName.getText().toString();
                /*User currentUser = dbConnection.getCurrentUser();

                String uniqueID = User.getUserKey(currentUser.getEmail());

                Group currentGroup = SingleGroupPage.selectedGroup;

                String name = eventName.getText().toString();

                //Hard coded for now
                DateTime startTime = new DateTime("2019-12-22T13:30:00.000");
                DateTime endTime = new DateTime("2019-12-22T14:30:00.000");
                System.out.println(startTime.toString());
                System.out.println(endTime.toString());

                String groupID = currentGroup.getId();

                String eventID = UUID.randomUUID().toString();

                ArrayList<String> attendees = new ArrayList<>();

                attendees.add(uniqueID);

                SocialHourEvent newSocialHourEvent = new SocialHourEvent(name, startTime, endTime, groupID, eventID, attendees);

                dbConnection.addEventToDB(newSocialHourEvent);*/

                startActivity(new Intent(getApplicationContext(), ViewGenMeetingTimes.class));

            }
        });


    }
    public void showTimePickerDialog1(View v) {
        DialogFragment newFragment = new TimePickerFragment1();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment1 extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            InputGenerateMeetingTimes.lowerHour = hourOfDay;
            InputGenerateMeetingTimes.lowerMinute = minute;
        }
    }

    public void showTimePickerDialog2(View v) {
        DialogFragment newFragment = new TimePickerFragment2();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment2 extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            InputGenerateMeetingTimes.upperHour = hourOfDay;
            InputGenerateMeetingTimes.upperMinute = minute;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            InputGenerateMeetingTimes.year = year;
            InputGenerateMeetingTimes.month = month + 1;
            InputGenerateMeetingTimes.day = day;
            String dateText = month+1 + "/" + day + "/" + year;
            InputGenerateMeetingTimes.currentDate.setText(dateText);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
