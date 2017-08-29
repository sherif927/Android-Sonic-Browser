package com.compassasu.simplebrowser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kagune on 5/23/2017.
 */
public class Tab {
    private List<String> tabHistory;
    private int currentPosition=-1;
    public static final int STATE_TOUCH_NAVIGATION=0;
    public static final int STATE_BACK_FORWARD_NAVIGATION=2;
    public static final int STATE_NEW_WEBSITE_NAVIGATION=1;
    public static final int STATE_TAB_SELECT=8;
    public static final int STATE_REFRESH=4;

    private Tab(){
        tabHistory=new ArrayList<>();
    }

    public boolean canGoBack(){
        return(currentPosition>0)?true:false;
    }

    public boolean canGoForward(){
        return(currentPosition<tabHistory.size()-1)?true:false;
    }

    public String navigateBack(){
         currentPosition--;
        return tabHistory.get(currentPosition);
    }

    public String navigateForward( ){
       currentPosition++;
        return tabHistory.get(currentPosition);
    }

    public String getCurrentUrl(){
        return tabHistory.get(currentPosition);
    }

    public int getHistorySize(){
        return tabHistory.size();
    }

    public boolean isEmpty(){
        return(currentPosition<0)?true:false;
    }

    public void clearTabHistory(){
        tabHistory.clear();
        currentPosition=-1;
    }

    public void addNewUrl(String url){
        if(!tabHistory.contains(url)){
            tabHistory.add(url);
            currentPosition++;
        }
    }

    public static class TabFactory {
        public static Tab makeTab(){
            return new Tab();
        }
    }



}
