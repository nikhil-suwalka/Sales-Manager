package com.nikhil.salesmanager;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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

    TextView totalSales;
    GridLayout gridLayout;
    ArrayList<String> items;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final SQLiteHelper sqLiteHelper = new SQLiteHelper(getActivity().getApplicationContext());

        totalSales = (TextView) view.findViewById(R.id.totalSales);
        totalSales = (TextView) view.findViewById(R.id.totalSales);


        TextView todaysDate = (TextView) view.findViewById(R.id.todaysDate);
        Calendar date = Calendar.getInstance();
        int yy = date.get(Calendar.YEAR);
        String mm = new SimpleDateFormat("MMM").format(date.getTime());
        int dd = date.get(Calendar.DAY_OF_MONTH);

        String str = Integer.toString(dd) + " " + mm + " " + Integer.toString(yy);
        todaysDate.setText(str);

        items = sqLiteHelper.getItemNames();
        int j = 0;
        gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
        for (int i = 0; i < (items.size() * 4); i = i + 4) {

            //TEXT VIEW FOR SANDWICH NAME
            TextView textView = new TextView(getActivity().getApplicationContext());
            textView.setText(items.get(j));
            textView.setTag(i);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(Color.WHITE);
            gridLayout.addView(textView, new GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER), GridLayout.spec(1, GridLayout.CENTER)));


            //TEXT VIEW FOR COUNTER
            TextView textViewC = new TextView(getActivity().getApplicationContext());
            textViewC.setText(sqLiteHelper.getQuantity(items.get(j)));
            textViewC.setTag(i + 1);
            textViewC.setGravity(Gravity.CENTER_HORIZONTAL);
            textViewC.setPadding(15, 0, 15, 0);
            textViewC.setTextSize(20);
            textViewC.setTextColor(Color.WHITE);
            GridLayout.LayoutParams gridLayoutParamC = new GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER), GridLayout.spec(2, GridLayout.CENTER));
            gridLayoutParamC.width = 250;
            //gridLayoutParamC.height = 150;
            gridLayout.addView(textViewC, gridLayoutParamC);

            //BUTTON FOR INCREMENT
            Button button = new Button(getActivity().getApplicationContext());
            button.setText("+");
            button.setTag(i + 2);
            button.setTextColor(Color.WHITE);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b1 = (Button) v;
                    int position = (Integer) b1.getTag();
                    int tvPosition = position - 2;
                    int cPosition = position - 1;
                    TextView gridChild = (TextView) gridLayout.getChildAt(tvPosition);
                    TextView gridChild2 = (TextView) gridLayout.getChildAt(cPosition);
                    String item = (String) gridChild.getText().toString();
                    sqLiteHelper.insertLog(item, 1);
                    gridChild2.setText(sqLiteHelper.getQuantity(item));
                    totalSales.setText(sqLiteHelper.calculateDailySales());
                    //int rows = gridLayout.getRowCount();
                    Toast.makeText(getActivity().getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                }
            });
            GridLayout.LayoutParams gridLayoutParamAdd = new GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER), GridLayout.spec(3, GridLayout.CENTER));
            gridLayoutParamAdd.width = 125;
            gridLayoutParamAdd.height = 125;

            gridLayout.addView(button, gridLayoutParamAdd);


            //BUTTON FOR DECREMENT
            Button rButton = new Button(getActivity().getApplicationContext());
            rButton.setText("-");
            rButton.setTag(i + 3);
            rButton.setTextColor(Color.WHITE);
            rButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            rButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b1 = (Button) v;
                    int position = (Integer) b1.getTag();
                    int tvPosition = position - 3;
                    int cPosition = position - 2;
                    TextView gridChild = (TextView) gridLayout.getChildAt(tvPosition);
                    TextView gridChild2 = (TextView) gridLayout.getChildAt(cPosition);
                    String item = (String) gridChild.getText().toString();
                    String count = (String) gridChild2.getText().toString();
                    if (Integer.parseInt(count) == 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "You cannot go below count of 0!", Toast.LENGTH_SHORT).show();
                    } else {
                        sqLiteHelper.updateLog(item, 1);
                        gridChild2.setText(sqLiteHelper.getQuantity(item));
                        totalSales.setText(sqLiteHelper.calculateDailySales());
                        Toast.makeText(getActivity().getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            GridLayout.LayoutParams gridLayoutParamRemove = new GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER), GridLayout.spec(4, GridLayout.CENTER));
            gridLayoutParamRemove.width = 125;
            gridLayoutParamRemove.height = 125;
            gridLayout.addView(rButton, gridLayoutParamRemove);

            //INCREMENT FOR NEW ITEM IN RECORDS
            j = j + 1;
        }


        return view;
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

    @Override
    public void onResume() {
        super.onResume();
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getActivity().getApplicationContext());
        if(sqLiteHelper.everyDayCheck()){
            for (int i = 1; i < (items.size() * 4); i = i + 4) {
                TextView gridChild = (TextView) gridLayout.getChildAt(i);
                gridChild.setText("0");
            }
        }

        totalSales.setText(sqLiteHelper.calculateDailySales());
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