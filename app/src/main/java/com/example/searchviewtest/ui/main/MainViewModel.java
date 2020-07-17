package com.example.searchviewtest.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainViewModel extends ViewModel {

    private List<String> mDogNames = new ArrayList<>(Arrays.asList(
            "Echo",
            "Spot",
            "Lemmie",
            "Wolfie",
            "Charlie",
            "Kia",
            "Haley"
    ));

    private String mFilter = "";

    public MutableLiveData<List<String>> names = new MutableLiveData<>();

    public void onResume() {
        updateNames();
    }

    public void onFilter(String newFilter) {
        if (newFilter == null) {
            mFilter = "";
        } else {
            mFilter = newFilter;
        }
        updateNames();
    }

    private void updateNames() {
        names.setValue(mDogNames.stream()
                .filter(Pattern.compile(".*" + mFilter + ".*").asPredicate())
                .collect(Collectors.<String>toList())
        );
    }
}
