package com.example.nu_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nu_app.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class CalendarFragment extends Fragment {
    CustomCalendar calendar;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);

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

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students").child(user.getEmail().replace("@nu.edu.kz", "").replace(".", "_"));
        final DatabaseReference eventsReference = FirebaseDatabase.getInstance().getReference("posts");

        reference.child("selectedEvents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    System.out.println("First one = " + childSnapshot.getValue());


                    eventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            for(DataSnapshot eventChild: dataSnapshot2.getChildren()) {
                                Post postChecker = eventChild.getValue(Post.class);

                                if (childSnapshot.getValue().equals(postChecker.getTitle())) {
                                    System.out.println("Yeahhhh, sovpadenie = = " + postChecker.getDate().substring(5,7));

                                    dates.put(9, "event");
                                    dates.put(10, "event");
                                    calendar.setDate(calendarOf, dates);
                                }

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

        return view;
    }
}