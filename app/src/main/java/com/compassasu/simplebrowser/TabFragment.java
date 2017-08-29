package com.compassasu.simplebrowser;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.StackView;

import com.compassasu.simplebrowser.Adapters.StackAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends Fragment {
    private StackView stackView;
    private StackAdapter adapter;
    private ArrayList<Stack_Items> list;
    private Communicator comm;
    private TabManager manager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Initializing the Views inside the fragment layout
        View v=inflater.inflate(R.layout.fragment_tabs, container, false);
        stackView=(StackView)v.findViewById(R.id.stackView);
        list=new ArrayList<>();
        comm=(Communicator) getContext();
        manager=TabManager.getReference();
        updateListView();
        adapter=new StackAdapter(getContext(),list);
        stackView.setAdapter(adapter);


        stackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                comm.selectTab(i);
            }
        });

        stackView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                manager.deleteTab(i);
                list.remove(i);
                updateStack();
                return true;
            }
        });
        return v;
    }

    public void updateListView(){
        List<Tab> tabs=manager.getTabs();
        list.clear();
        for(Tab t : tabs){
                Tab s=t;
                if(t.isEmpty())
                list.add(new Stack_Items("Tab"));
                else
                list.add(new Stack_Items(t.getCurrentUrl()));
            }
    }

    public void updateStack(){
        adapter.notifyDataSetChanged();
    }

    public void addTab(Tab t){
        manager.allocateNewTab(t);
        updateListView();
        updateStack();
    }

    interface Communicator{
        public void selectTab(int position);
    }
}
