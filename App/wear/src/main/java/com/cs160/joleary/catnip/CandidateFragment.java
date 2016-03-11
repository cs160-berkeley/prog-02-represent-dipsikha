package com.cs160.joleary.catnip;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by dipsikhahalder on 3/9/16.
 */
public class CandidateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private String NAME_VALUE;
    private String PARTY_VALUE;


    private TextView name;
    private TextView party;


    //private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Fragment2012.
     */
    // TODO: Rename and change types and number of parameters
    public static CandidateFragment newInstance(String name, String party) {
        CandidateFragment fragment = new CandidateFragment();
        Bundle args = new Bundle();
        args.putString("NAME", name);
        args.putString("PARTY", party);
        fragment.setArguments(args);
        return fragment;
    }

    public CandidateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            NAME_VALUE = getArguments().getString("NAME");
            PARTY_VALUE = getArguments().getString("PARTY");

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("FRAGMENT2012","oncreate view");
        View view = inflater.inflate(R.layout.candidatefrag, container, false);
        name = (TextView) view.findViewById(R.id.name);
        party = (TextView) view.findViewById(R.id.party);
        LinearLayout bg = (LinearLayout) view.findViewById(R.id.background);
        if (PARTY_VALUE.equals("Democrat")) {
            bg.setBackgroundColor(getResources().getColor(R.color.democrat));
        }

        if (PARTY_VALUE.equals("Republican")) {
            bg.setBackgroundColor(getResources().getColor(R.color.republican));
        }

        name.setText(NAME_VALUE);
        party.setText(PARTY_VALUE);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/
}
