package com.alphacholera.musiccatalogue.HistoryFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alphacholera.musiccatalogue.DatabaseManagement;
import com.alphacholera.musiccatalogue.R;
import com.alphacholera.musiccatalogue.UtilityClasses.History;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseManagement databaseManagement;
    ArrayList<History> histories;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.history_tab, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_history);
        linearLayout = view.findViewById(R.id.no_history_linear_layout);
        databaseManagement = new DatabaseManagement(getContext());
        histories = databaseManagement.fetchAllHistoryOfUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HistoryAdapter adapter = new HistoryAdapter(getContext(), histories, new HistoryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(), "Played at " + histories.get(position).getDateAndTime(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        if (histories.isEmpty()) {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
