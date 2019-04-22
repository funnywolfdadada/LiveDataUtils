package com.example.mvvmdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvvmdemo.R;
import com.example.mvvmdemo.viewmodels.RandomNumberViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * @author funnywolf
 * @since 2019-04-22
 */
public class ActionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_action, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) { return; }
        RandomNumberViewModel viewModel = ViewModelProviders.of(getActivity()).get(RandomNumberViewModel.class);
        view.findViewById(R.id.clear_text).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Clear", Toast.LENGTH_SHORT).show();
            viewModel.clear();
        });
        view.findViewById(R.id.random_text).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Update all", Toast.LENGTH_SHORT).show();
            viewModel.updateAll();
        });
    }
}
