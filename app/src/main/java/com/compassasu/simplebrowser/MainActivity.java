package com.compassasu.simplebrowser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TabFragment.Communicator{

        private CustomProgressBar sonicProgress;
        private int height,width,selectedPosition=0;
        private TabFragment mWebpage;
        private Tab selectedTab;
        private boolean [] flags=new boolean[4];
        private int flagValue;


        //Relative layout containing the WebView
        @BindView(R.id.rel_web_container)
        RelativeLayout webviewContainer;

        @BindView(R.id.webview)
        WebView mWebView;
        //EditText for entering the URLs
        @BindView(R.id.txt_url)
        EditText textURL;

        //Back Navigation button
        @BindView(R.id.img_back)
        ImageView back;
        //Forward Navigation Button
        @BindView(R.id.img_forward)
        ImageView forward;
        //Button that triggers the overflow menu for multiple options , for further reference view showPopUpMenu()
        @BindView(R.id.img_overflow)
        ImageView overFlow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getScreenDimens();
        initImageViews();
        initStackFragment();
        initializeWebView();
    }

    private void showLoader(boolean cancelable){
        if(sonicProgress==null){
            sonicProgress=new CustomProgressBar(MainActivity.this);
            sonicProgress.setCancelable(cancelable);
        } sonicProgress.show();
    }

    private void dismissLoader(){
        sonicProgress.dismiss();
    }

    private void initImageViews(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab.canGoBack()){
                    flags[1]=true;
                    showWebpage(selectedTab.navigateBack());
                }

            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab.canGoForward()){
                    flags[1]=true;
                    showWebpage(selectedTab.navigateForward());
                }
            }
        });

        overFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpMenu();
            }
        });
    }

    private void getScreenDimens(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

    private void showWebpage(String URL){
        mWebView.loadUrl(URL);
    }

    private void showPopUpMenu(){
        PopupMenu popupMenu = new PopupMenu(this,overFlow);
        MenuInflater inflater=popupMenu.getMenuInflater();
        inflater.inflate(R.menu.overflow_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_refresh:
                        //Refresh webview
                        showWebpage(mWebView.getUrl());
                        flags[2]=true;
                        break;
                    case R.id.menu_tabs:
                        //show the tabs fragment
                        lowerWebPage();
                        break;
                    case R.id.menu_history:
                        //navigate to the history activity
                        startActivity(new Intent(MainActivity.this,HistoryActivity.class));
                        finish();
                        break;
                    case R.id.menu_navigate:
                        //webview navigates to what ever is in the textview
                        String newUrl=Constants.HTTP+textURL.getText();
                        showWebpage(newUrl);
                        flags[0]=true;
                        break;
                    case R.id.menu_new_tab:
                        mWebpage.addTab(Tab.TabFactory.makeTab());
                        lowerWebPage();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    private void lowerWebPage() {
        webviewContainer.animate().translationYBy(height).start();
    }

    private void liftWebPage() {
        webviewContainer.animate().translationYBy(-height).start();
    }

    private void updateTabs(){
        TabManager.getReference().getTabs().set(selectedPosition,selectedTab);
    }

    private void initializeWebView(){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());

    }

    private void initStackFragment(){
        mWebpage=new TabFragment();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction mTransaction=manager.beginTransaction();
        mTransaction.add(R.id.stack_container,mWebpage);
        mTransaction.commit();
        selectedTab=TabManager.getReference().getTabs().get(0);

    }

    @Override
    public void selectTab(int position) {
        selectedPosition=position;
        selectedTab=TabManager.getReference().getTabs().get(selectedPosition);

         if(!selectedTab.isEmpty()) {
             showWebpage(selectedTab.getCurrentUrl());
             flags[3] = true;
         }else{
             showWebpage("");
             textURL.setText("");
            clearFlags();
         }

        liftWebPage();

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //view.loadUrl(url);
            return false;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoader(false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissLoader();
            textURL.setText(mWebView.getUrl());
            flagValue= getFlagValue();

            if (flagValue!=Tab.STATE_REFRESH) {

                if(flagValue==Tab.STATE_TOUCH_NAVIGATION || flagValue==Tab.STATE_NEW_WEBSITE_NAVIGATION) {
                    selectedTab.addNewUrl(mWebView.getUrl());
                    TabManager.getReference().addHistoryEntry(mWebView.getUrl());
                }
                updateTabs();
                updateWebPage();

            } clearFlags();
            }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Toast.makeText(MainActivity.this,"An error occured check your connection then try again.",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Toast.makeText(MainActivity.this,"An error occured check your connection then try again.",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Toast.makeText(MainActivity.this,"An error occured try again later.",Toast.LENGTH_LONG).show();


        }

    }

    private int getFlagValue(){
        int value=0;
        for(int i=0;i<4;i++)
            value+=flags[i]?(int)Math.pow(2,i):0;
        return value;
    }

    private void clearFlags(){
        flags[0]=false;
        flags[1]=false;
        flags[2]=false;
        flags[3]=false;
    }

    private void updateWebPage() {
        mWebpage.updateListView();
        mWebpage.updateStack();
    }
}
