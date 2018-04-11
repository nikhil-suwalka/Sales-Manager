package com.nikhil.salesmanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthlySales.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthlySales#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MonthlySales extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MonthlySales() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlySales.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthlySales newInstance(String param1, String param2) {
        MonthlySales fragment = new MonthlySales();
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

    CalendarView calendar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_monthly_sales, container, false);;
        calendar = rootView.findViewById(R.id.calendar);

        calendar.setMaxDate(Calendar.getInstance().getTimeInMillis());





        final SQLiteHelper sqLiteHelper = new SQLiteHelper(this.getActivity().getApplicationContext());

        //CalendarView calendarView = new CalendarView(getActivity().getApplicationContext());
        calendar.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String mm = Integer.toString(month);
                String dd = Integer.toString(dayOfMonth);
                if((month+1)<10){
                    mm = "0" + (month+1);
                }
                if(dayOfMonth<10){
                     dd = "0" + dayOfMonth;
                }


                String strDate = year + "-" + mm + "-" + dd;
                Toast.makeText(getActivity().getApplicationContext(), "Date is : " + dd +" / " + mm + " / " + year, Toast.LENGTH_SHORT).show();


                float dateEarning = sqLiteHelper.getMonthlyEarning(strDate);
                TextView monthlySales = (TextView) getActivity().findViewById(R.id.monthEarnings);
                monthlySales.setText(String.format("Total Sales: %s", dateEarning));
                Log.i("floatt", Float.toString(dateEarning));


            }
        });
;

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
