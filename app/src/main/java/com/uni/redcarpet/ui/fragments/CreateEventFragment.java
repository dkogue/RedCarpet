package com.uni.redcarpet.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.uni.redcarpet.R;

import java.util.Date;
import java.util.GregorianCalendar;



public class CreateEventFragment extends Fragment implements View.OnClickListener {

    EditText etorganizer, etevent_name, etaddress;
    Spinner spinner;
    DatePicker datepicker;
    String[] event;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates) {

        View v = inflater.inflate(R.layout.fragment_create_event, container, false);

        // associate member variables with the xml buttons
        Button b3 = (Button) v.findViewById(R.id.button3);
        Button b2 = (Button) v.findViewById(R.id.button2);

        // enable event handling
        b3.setOnClickListener(this);
        b2.setOnClickListener(this);

        etorganizer = (EditText) v.findViewById(R.id.organizer);
        etevent_name = (EditText) v.findViewById(R.id.event_title);
        etaddress = (EditText) v.findViewById(R.id.address);

        datepicker = (DatePicker) v.findViewById(R.id.datePicker2);

        /*spinner = (Spinner) v.findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // create event button
            case R.id.button3:

                String organizer = etorganizer.getText().toString();
                String event_name = etevent_name.getText().toString();
                String address = etaddress.getText().toString();
                // String type = spinner.getSelectedItem().toString();
                String type = "Party";
                int day = datepicker.getDayOfMonth();
                int month = datepicker.getMonth()+1;
                int year = datepicker.getYear();

                String date = month+"-"+day+"-"+year;

                Date currDate = new Date();

                Date setDate = new GregorianCalendar(year, month-1, day+1).getTime();

                System.out.println(setDate);
                System.out.println(currDate);

                event = new String[5];
                event[0] = organizer;
                event[1] = event_name;
                event[2] = address;
                event[3] = type;
                event[4] = date;

                // check if fields are all filled out
                if (organizer.equals("") ||
                        event_name.equals("") ||
                        address.equals("") ||
                        type.equals("")) {

                    new AlertDialog.Builder(getActivity())
                            //.setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Empty field(s)!")
                            .setMessage("Please complete the form before creating an event.")
                            .setPositiveButton("Ok", null)
                            .show();
                    break;
                }
                else if (setDate.compareTo(currDate) < 0){
                    new AlertDialog.Builder(getActivity())
                            //.setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Invalid date!")
                            .setMessage("The date you entered has already passed!")
                            .setPositiveButton("Ok", null)
                            .show();
                    break;
                }
                else {
                    // check if the address is valid
                    MapFragment mf = new MapFragment();

                    if (mf.getLocationFromAddress(getContext(), address) == null) {
                        new AlertDialog.Builder(getActivity())
                                //.setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("Invalid address!")
                                .setMessage("Please enter a valid address to create a new event.")
                                .setPositiveButton("Ok", null)
                                .show();
                        break;
                    }

                    //System.out.println("Create Event - type: "+type);
                    //Event current_event = new Event(organizer, type, address, date, event_name);
                    //EventJson.saveEventToFirebase(current_event);
                }


                new AlertDialog.Builder(getActivity())
                        //.setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Event details set!")
                        .setMessage("Event details for \"" + event_name + "\" have been successfully registered. Click continue and confirm your address.")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                // update login status
                                Bundle args = new Bundle();
                                // args.putBoolean("hasLoggedIn", MainActivity.hasLoggedIn);
                                args.putStringArray("currEvent",event);
                                CreateEventMapFragment newFragment = new CreateEventMapFragment();
                                newFragment.setArguments(args);
                                newFragment.setDetails(etaddress.getText().toString(), etevent_name.getText().toString(), 0.0f, null);

                                fragmentTransaction.replace(R.id.frame, newFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }

                        })
                        /*.setNegativeButton("Manage events", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        })*/
                        .show();

                break;
            // cancel button
            case R.id.button2:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                // args.putBoolean("hasLoggedIn", MainActivity.hasLoggedIn);
                // We need to get back to the list of events in the eventfragment.
                Fragment newFragment = new EventsFragment();
                newFragment.setArguments(args);

                fragmentTransaction.replace(R.id.frame, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            default:
        }
    }
}
