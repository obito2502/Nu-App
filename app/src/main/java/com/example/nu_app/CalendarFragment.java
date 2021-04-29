package com.example.nu_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nu_app.adapters.PostAdapter;
import com.example.nu_app.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class CalendarFragment extends Fragment {
    CustomCalendar calendar;
    ArrayList<Post> events = new ArrayList<>();

    private TextView event_title;
    private TextView event_place;
    private TextView event_date;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);

        event_title = view.findViewById(R.id.event_title);
        event_date = view.findViewById(R.id.event_date);
        event_place = view.findViewById(R.id.event_place);

        calendar = view.findViewById(R.id.calendar);
        HashMap<Object, Property> descHashMap = new HashMap<>();
        Property property = new Property();
        property.layoutResource = R.layout.default_view;
        property.dateTextViewResource = R.id.default_text;
        descHashMap.put("default", property);

        //view of current date
        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.current_view;
        currentProperty.dateTextViewResource = R.id.current_text;
        descHashMap.put("current", currentProperty);

        //view of date with selected event
        Property eventProperty = new Property();
        eventProperty.layoutResource = R.layout.event_view;
        eventProperty.dateTextViewResource = R.id.event_text;
        descHashMap.put("event", eventProperty);

        calendar.setMapDescToProp(descHashMap);

        final HashMap<Integer, Object> dates = new HashMap<>();
        final Calendar calendarOf = Calendar.getInstance();
        dates.put(calendarOf.get(Calendar.DAY_OF_MONTH), "current");
        calendar.setDate(calendarOf, dates);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students").child(user.getEmail().replace("@nu.edu.kz", "").replace(".", "_"));
        final DatabaseReference eventsReference = FirebaseDatabase.getInstance().getReference("posts");

        reference.child("selectedEvents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                    eventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            for(DataSnapshot eventChild: dataSnapshot2.getChildren()) {
                                Post postChecker = eventChild.getValue(Post.class);

                                if(childSnapshot.getValue().equals(postChecker.getTitle())) {
                                    final Integer date_of_event = Integer.parseInt(postChecker.getDate().substring(0,2));

                                    dates.put(date_of_event, "event");
                                    calendar.setDate(calendarOf, dates);

                                    events.add(postChecker);
                                }

                                calendar.setDate(calendarOf, dates);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        calendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String selectedDay = selectedDate.getTime().toString().substring(8,10);

                for (Post e: events) {

                    if(e.getDate().substring(0,2).equals(selectedDay)) {
                        event_title.setText(e.getTitle());
                        event_date.setText(e.getDate());
                        event_place.setText(e.getLocation());
                        break;
                    }
                    event_title.setText("");
                    event_date.setText("");
                    event_place.setText("");
                }
            }
        });

        return view;
    }
}