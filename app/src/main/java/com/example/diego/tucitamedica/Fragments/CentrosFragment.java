package com.example.diego.tucitamedica.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diego.tucitamedica.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CentrosFragment extends Fragment {


    public CentrosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_centros, container, false);
    }

}
