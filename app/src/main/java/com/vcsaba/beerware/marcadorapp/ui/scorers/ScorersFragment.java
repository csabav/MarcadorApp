package com.vcsaba.beerware.marcadorapp.ui.scorers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vcsaba.beerware.marcadorapp.R;

public class ScorersFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scorers, container, false);
        final TextView textView = root.findViewById(R.id.text_scorers);
        textView.setText("This is the Scorers fragment!");
        return root;
    }
}