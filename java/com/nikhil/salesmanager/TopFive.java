package com.nikhil.salesmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TopFive.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopFive#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopFive extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner month, year;
    Button displayButton;


    private OnFragmentInteractionListener mListener;

    public TopFive() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopFive.
     */
    // TODO: Rename and change types and number of parameters
    public static TopFive newInstance(String param1, String param2) {
        TopFive fragment = new TopFive();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_top_five, container, false);

        month = rootView.findViewById(R.id.month);
        year = rootView.findViewById(R.id.year);
        displayButton = rootView.findViewById(R.id.displayButton);

        ArrayList<String> monthList = new ArrayList<>(Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));

        ArrayList<String> yearList = new ArrayList<>();

        Calendar date = Calendar.getInstance();
        int yy = date.get(Calendar.YEAR);

        for (int i = 2018; i <= yy; i++) {
            yearList.add(String.valueOf(i));
        }

               ArrayAdapter<String> mothAdapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(), R.layout.spinner_item, monthList);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(), R.layout.spinner_item, yearList);

        month.setAdapter(mothAdapter);
        year.setAdapter(yearAdapter);

        final SQLiteHelper sqLiteHelper = new SQLiteHelper(this.getActivity().getApplicationContext());


        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinkedHashMap topFive=  sqLiteHelper.getTopFive("2018", "02");



            }
        });



        return rootView;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
