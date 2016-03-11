package com.cs160.joleary.catnip;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment2012.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment2012#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2012 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private String STATE_VALUE;
    private String COUNTY_VALUE;
    private String OBAMA_VALUE;
    private String ROMNEY_VALUE;

    private TextView state;
    private TextView county;
    private TextView obama;
    private TextView romney;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Fragment2012.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2012 newInstance(String s, String c, String o, String r) {
        Fragment2012 fragment = new Fragment2012();
        Bundle args = new Bundle();
        Log.d("FRAGMENT2012", s + c + o + r);
        args.putString("STATE", s);
        args.putString("COUNTY", c);
        args.putString("OBAMA", o);
        args.putString("ROMNEY", r);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment2012() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FRAGMENT2012", "oncreate");

        if (getArguments() != null) {
            STATE_VALUE = getArguments().getString("STATE");
            COUNTY_VALUE = getArguments().getString("COUNTY");
            OBAMA_VALUE = getArguments().getString("OBAMA");
            ROMNEY_VALUE = getArguments().getString("ROMNEY");
            Log.d("FRAGMENT2012",STATE_VALUE+COUNTY_VALUE+OBAMA_VALUE+ROMNEY_VALUE);

            /*state.setText(STATE_VALUE);
            county.setText(COUNTY_VALUE);
            obama.setText(OBAMA_VALUE);
            romney.setText(ROMNEY_VALUE);*/
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("FRAGMENT2012","oncreate view");
        View view = inflater.inflate(R.layout.vote2012, container, false);
        state = (TextView) view.findViewById(R.id.state);
        county = (TextView) view.findViewById(R.id.county);
        obama = (TextView) view.findViewById(R.id.obamavote);
        romney = (TextView) view.findViewById(R.id.romneyvote);

        state.setText(STATE_VALUE);
        county.setText(COUNTY_VALUE);
        obama.setText(OBAMA_VALUE);
        romney.setText(ROMNEY_VALUE);
        Log.d("FRAGMENT2012",STATE_VALUE+COUNTY_VALUE+OBAMA_VALUE+ROMNEY_VALUE+"oncreatview");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
