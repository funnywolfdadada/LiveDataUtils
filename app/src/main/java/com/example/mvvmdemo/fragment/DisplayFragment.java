package com.example.mvvmdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mvvmdemo.R;
import com.example.mvvmdemo.repositories.StateData;
import com.example.mvvmdemo.viewmodels.RandomNumberViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class DisplayFragment extends Fragment {

    private RandomNumberViewModel mViewModel;

    private TextView mTopText;
    private TextView mBottomText;
    private TextView mLeftText;
    private TextView mRightText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_display, container, false);
        mTopText = view.findViewById(R.id.top_text);
        mBottomText = view.findViewById(R.id.bottom_text);
        mLeftText = view.findViewById(R.id.left_text);
        mRightText = view.findViewById(R.id.right_text);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) { return; }
        mViewModel = ViewModelProviders.of(getActivity()).get(RandomNumberViewModel.class);
        mViewModel.getTopNumberLiveData().observe(this, stateData -> onDataChanged(stateData, mTopText));
        mViewModel.getBottomNumberLiveData().observe(this, stateData -> onDataChanged(stateData, mBottomText));
        mViewModel.getLeftNumberLiveData().observe(this, stateData -> onDataChanged(stateData, mLeftText));
        mViewModel.getRightNumberLiveData().observe(this, stateData -> onDataChanged(stateData, mRightText));

        mTopText.setOnClickListener(v -> mViewModel.updateTop());
        mBottomText.setOnClickListener(v -> mViewModel.updateBottom());
        mLeftText.setOnClickListener(v -> mViewModel.updateLeft());
        mRightText.setOnClickListener(v -> mViewModel.updateRight());
    }

    private void onDataChanged(StateData<Integer> stateData, TextView textView) {
        if (stateData == null || textView == null) { return; }
        switch (stateData.state) {
            case StateData.STATE_READY:
                textView.setText(String.valueOf(stateData.data));
                break;
            case StateData.STATE_ERROR:
                textView.setText("Error");
                break;
            case StateData.STATE_LOADING:
                textView.setText("Loading...");
                break;
            default:
        }
    }
}
