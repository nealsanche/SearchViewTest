package com.example.searchviewtest.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.searchviewtest.R;
import com.example.searchviewtest.databinding.ItemNameBinding;
import com.example.searchviewtest.databinding.MainFragmentBinding;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.Section;
import com.xwray.groupie.viewbinding.BindableItem;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private GroupAdapter<GroupieViewHolder> mGroupAdapter = new GroupAdapter<>();
    private Section mSection = new Section();

    private MainFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupAdapter.add(mSection);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.list.setAdapter(mGroupAdapter);
        binding.list.setLayoutManager(new LinearLayoutManager(view.getContext()));

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.onFilter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mViewModel.onFilter(newText);
                return true;
            }
        });

        mGroupAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                if (item instanceof NameItem) {
                    String name = ((NameItem)item).mName;
                    Toast.makeText(view.getContext(), "You tapped " + name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mViewModel.names.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                ArrayList<NameItem> nameItems = new ArrayList<>();

                for (String name : strings) {
                    nameItems.add(new NameItem(name));
                }

                mSection.update(nameItems);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    private static class NameItem extends BindableItem<ItemNameBinding> {

        private String mName;

        public NameItem(String name) {
            mName = name;
        }

        @Override
        public int getLayout() {
            return R.layout.item_name;
        }

        @NonNull
        @Override
        protected ItemNameBinding initializeViewBinding(@NonNull View view) {
            return ItemNameBinding.bind(view);
        }

        @Override
        public void bind(@NonNull ItemNameBinding viewBinding, int position) {
            viewBinding.textView.setText(mName);
        }
    }
}
