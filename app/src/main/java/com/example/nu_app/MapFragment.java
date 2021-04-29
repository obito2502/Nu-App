package com.example.nu_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;


public class MapFragment extends Fragment {

    GoogleMap map;
    SearchView searchView;

    HashMap<String, LatLng> listOfLocations = new HashMap<>();

    LatLng mainHall;
    LatLng oldSportCenter;
    LatLng newSportCenter;
    LatLng block22;
    LatLng block23;
    LatLng block24;
    LatLng block25;
    LatLng block27;
    LatLng block26;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        searchView = view.findViewById(R.id.sv_location);

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.getUiSettings().setZoomGesturesEnabled(false);

                map.setMapType(map.MAP_TYPE_SATELLITE);


                LatLngBounds ter = new LatLngBounds(
                        new LatLng(51.08567418957864, 71.39025975647185),
                        new LatLng(51.0959977981645, 71.40399266624658)
                );

                map.setLatLngBoundsForCameraTarget(ter);

                LatLng NU = new LatLng(51.088815, 71.398456);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(NU, 16.0f));


                oldSportCenter = new LatLng(51.08866754556695, 71.3953503926065);
                listOfLocations.put("Old Sport Center", oldSportCenter);

                newSportCenter = new LatLng(51.087562, 71.394986);
                listOfLocations.put("New Sport Center", newSportCenter);

                block22 = new LatLng(51.089111952259444, 71.39655240850266);
                listOfLocations.put("Block 22", block22);

                block23 = new LatLng(51.08841110143812, 71.39630564528014);
                listOfLocations.put("Block 23", block23);

                block24 = new LatLng(51.08758893597656, 71.39601596671457);
                listOfLocations.put("Block 24", block24);

                block25 = new LatLng(51.087003, 71.395791);
                listOfLocations.put("Block 25", block25);

                block26 = new LatLng(51.08682102053868, 71.39686388203043);
                listOfLocations.put("Block 26", block26);

                block27 = new LatLng(51.08744102581408, 71.39708918758143);
                listOfLocations.put("Block 27", block27);

                mainHall = new LatLng(51.09068550444344, 71.39545653629384);
                listOfLocations.put("Main Hall", mainHall);

                map.addMarker(new MarkerOptions().position(oldSportCenter).title("Old Sport Center"));
                map.addMarker(new MarkerOptions().position(newSportCenter).title("New Sport Center"));
                map.addMarker(new MarkerOptions().position(block22).title("Block 22"));
                map.addMarker(new MarkerOptions().position(block23).title("Block 23"));
                map.addMarker(new MarkerOptions().position(block24).title("Block 24"));
                map.addMarker(new MarkerOptions().position(block25).title("Block 25"));
                map.addMarker(new MarkerOptions().position(block26).title("Block 26"));
                map.addMarker(new MarkerOptions().position(block27).title("Block 27"));
                map.addMarker(new MarkerOptions().position(mainHall).title("Main Hall"));

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("Submit = " + query);
                if(listOfLocations.containsKey(query)) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(listOfLocations.get(query), 16.0f));
                    map.clear();
                    map.addMarker(new MarkerOptions().position(listOfLocations.get(query)).title(query));
                }



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("Change = " + newText);
                if(newText.equals("")) {
                    System.out.println("CLEANED");
                    for(Map.Entry<String, LatLng> e: listOfLocations.entrySet()) {
                        map.addMarker(new MarkerOptions().position(e.getValue()).title(e.getKey()));
                    }
                }

                return false;
            }
        });

        return view;
    }

}