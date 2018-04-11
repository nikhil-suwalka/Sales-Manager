package com.nikhil.salesmanager;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.nikhil.salesmanager.SQLiteHelper.TABLE_NAME3;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRemoveItems.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRemoveItems#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRemoveItems extends Fragment {


    Button buttonAdd, buttonRemove, buttonAddCategory, buttonRemoveCategory;
    Spinner addCategory, removeItem, removeCategory;
    EditText itemName, itemCost, category;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddRemoveItems() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRemoveItems.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRemoveItems newInstance(String param1, String param2) {
        AddRemoveItems fragment = new AddRemoveItems();
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
        View rootView = inflater.inflate(R.layout.fragment_add_remove_items, container, false);

        buttonAdd = (Button) rootView.findViewById(R.id.buttonAdd);
        buttonRemove = (Button) rootView.findViewById(R.id.buttonRemove);
        addCategory = (Spinner) rootView.findViewById(R.id.addCategory);
        removeItem = (Spinner) rootView.findViewById(R.id.removeItem);
        itemName = (EditText) rootView.findViewById(R.id.itemName);
        itemCost = (EditText) rootView.findViewById(R.id.itemCost);

        buttonAddCategory = (Button) rootView.findViewById(R.id.buttonAddCategory);
        buttonRemoveCategory = (Button) rootView.findViewById(R.id.buttonRemoveCategory);
        removeCategory = (Spinner) rootView.findViewById(R.id.removeCategory);
        category = (EditText) rootView.findViewById(R.id.category);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this.getActivity().getApplicationContext());
        ArrayList<String> categories = new ArrayList<String>();
        categories = sqLiteHelper.getCategory();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(), R.layout.spinner_item, categories);
        addCategory.setAdapter(dataAdapter);
        removeCategory.setAdapter(dataAdapter);
        removeItem.setAdapter(dataAdapter);

        ArrayList<String> items = sqLiteHelper.getItemNames();
        //TODO: Remove manual category additions, make UI and implement correctly!
/*        categories.add("Veg");
        categories.add("Non-veg");
        categories.add("Momos");*/
        //Sending categories to spinner


        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this.getActivity().getApplicationContext(), R.layout.spinner_item, items);
        removeItem.setAdapter(dataAdapter2);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(itemName.getText()) || TextUtils.isEmpty(itemCost.getText().toString()) || addCategory.getSelectedItem() == null)
                    Toast.makeText(getActivity().getApplicationContext(), "Please do not leave any fields empty!", Toast.LENGTH_SHORT).show();
                else {
                    SQLiteHelper sqLiteHelper = new SQLiteHelper(getActivity().getApplicationContext());
                    ItemModel item = new ItemModel();
                    String nameOfItem = itemName.getText().toString();
                    float costOfItem = Float.parseFloat(itemCost.getText().toString());
                    String category = (String) addCategory.getSelectedItem();
                    item.setItemName(nameOfItem);
                    item.setCatgeory(category);
                    item.setQuantity(0);
                    item.setPrice(costOfItem);
                    item.setSales(0);
                    if (!sqLiteHelper.insertItem(item)) {
                        Toast.makeText(getActivity().getApplicationContext(), "This item already exists!", Toast.LENGTH_SHORT).show();
                    } else {
                        itemName.getText().clear();
                        itemCost.getText().clear();
                        ArrayList<String> items = sqLiteHelper.getItemNames();
                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, items);
                        removeItem.setAdapter(dataAdapter2);
                        Toast.makeText(getActivity().getApplicationContext(), "Item added successfully.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeItem.getSelectedItem() == null)
                    Toast.makeText(getActivity().getApplicationContext(), "Please do not leave any fields empty!", Toast.LENGTH_SHORT).show();
                else {
                    SQLiteHelper sqLiteHelper = new SQLiteHelper(getActivity().getApplicationContext());
                    String itemName = removeItem.getSelectedItem().toString();
                    sqLiteHelper.removeItem(itemName);
                    ArrayList<String> items = sqLiteHelper.getItemNames();
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, items);
                    removeItem.setAdapter(dataAdapter2);
                    Toast.makeText(getActivity().getApplicationContext(), "Item removed successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(category.getText())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please do not leave the field empty!", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteHelper sqLiteHelper = new SQLiteHelper(getActivity().getApplicationContext());
                    String categoryName = category.getText().toString();
                    if(!sqLiteHelper.insertCategory(categoryName)){
                        Toast.makeText(getActivity().getApplicationContext(), "This category already exists!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        ArrayList<String> cat = sqLiteHelper.getCategory();
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, cat);
                        addCategory.setAdapter(dataAdapter);
                        removeCategory.setAdapter(dataAdapter);
                        category.getText().clear();
                        Toast.makeText(getActivity().getApplicationContext(), "Category added successfully.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonRemoveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeCategory.getSelectedItem() == null)
                    Toast.makeText(getActivity().getApplicationContext(), "Please do not leave the field empty!", Toast.LENGTH_SHORT).show();
                else {
                    SQLiteHelper sqLiteHelper = new SQLiteHelper(getActivity().getApplicationContext());
                    String categoryName = removeCategory.getSelectedItem().toString();
                    sqLiteHelper.removeCategory(categoryName);
                    ArrayList<String> cat = sqLiteHelper.getCategory();
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, cat);
                    addCategory.setAdapter(dataAdapter);
                    removeCategory.setAdapter(dataAdapter);
                    Toast.makeText(getActivity().getApplicationContext(), "Category removed successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inflate the layout for this fragment
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