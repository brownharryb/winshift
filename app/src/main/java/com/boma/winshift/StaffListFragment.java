package com.boma.winshift;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by BOMA on 11/8/2015.
 */
public class StaffListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recyclerView;
    StaffRecyclerViewAdapter adapter;
    SwipeRefreshLayout refreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.staff_list,container,false);
        refreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.refresh_container);
        recyclerView = (RecyclerView) v.findViewById(R.id.staff_recycler_view);
        adapter = new StaffRecyclerViewAdapter(getActivity(),getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return v;
    }

    private List<StaffListItem> getData(){
        StaffListItem item = new StaffListItem(getActivity());
        return  item.getDataFromDb();
    }

    @Override
    public void onRefresh() {
        new UpdateList().execute();
    }

    class UpdateList extends AsyncTask<Void, Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean updated = InternetHandler.updateDbFromNet(getActivity());
            adapter = new StaffRecyclerViewAdapter(getActivity(),getData());

            return updated;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if(!aVoid){
                MessageClass.message(getActivity(),"Something went wrong.. check internet!");
            }else{
                recyclerView.setAdapter(adapter);
                MessageClass.message(getActivity(), "Refreshed!");
//                MessageClass.log("from on post execute stafflistFragment adapter contains -- ");
            }

            refreshLayout.setRefreshing(false);
        }
    }
}
