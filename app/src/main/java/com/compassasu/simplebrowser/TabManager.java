package com.compassasu.simplebrowser;

import android.content.SharedPreferences;
import android.renderscript.Allocation;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kagune on 5/23/2017.
 */
public class TabManager {
    private static TabManager mReference;
    private static List<Tab> tabList;
    private static List<String> history;
    private SharedPreferences historyPreferences;

    private TabManager(){
        //private constructor for singleton purposes
        tabList=new LinkedList<>();
        tabList.add(Tab.TabFactory.makeTab());
        history=new ArrayList<>();
    }

    public static TabManager getReference(){
        if(mReference==null) mReference=new TabManager();
        return mReference;
    }

    public void allocateNewTab(Tab t){
            tabList.add(t);
    }

    public List<Tab> getTabs(){
        return tabList;
    }

    public void deleteTab(int position){
        tabList.remove(position);
    }

    public void addHistoryEntry(String URL){
        if(!history.contains(URL))
        history.add(URL);
    }

    public void saveHistory(List<String> history){

    }


}
