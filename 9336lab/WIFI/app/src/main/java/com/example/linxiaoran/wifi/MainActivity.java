package com.example.linxiaoran.wifi;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity{

    private WifiManager wifimanager;
    private ConnectivityManager connectivitymanager;
    private Button find_all;
    private Button find_four;
    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findview();
        init_system_service();
        set_click_listener();

    }

    private void findview(){
        find_all = (Button)findViewById(R.id.but1);
        find_four = (Button)findViewById(R.id.but2);
        list_view = (ListView)findViewById(R.id.listview);
    }

    private void init_system_service(){
        wifimanager = (WifiManager)getSystemService(WIFI_SERVICE);
        wifimanager.setWifiEnabled(true);
    }

    private void set_click_listener(){
        find_all.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                task1();
            }
        });
        find_four.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                task2();
            }
        });
    }

    private void task1(){
        ArrayList<ScanResult> scan_result = (ArrayList<ScanResult>)wifimanager.getScanResults();
        ArrayList<String> print_result = new ArrayList<>();
        scan_result = sortresult(scan_result);
        final ArrayList<String> result_ssid = new ArrayList<>();
        for(int i = 0; i<scan_result.size();++i){
            int t_n = i+1;
            if(scan_result.get(i).capabilities.equals("[ESS]")){
                print_result.add(t_n+"."+scan_result.get(i).SSID+"-"+scan_result.get(i).BSSID+"-"+scan_result.get(i).level+"(OPEN)");
                result_ssid.add(scan_result.get(i).SSID);
            }
            else{
                print_result.add(t_n+"."+scan_result.get(i).SSID+"-"+scan_result.get(i).BSSID+"-"+scan_result.get(i).level+"(PASSWORD_REQUIRED)");
                result_ssid.add(scan_result.get(i).SSID);
            }
        }
        list_view.setAdapter(new ArrayAdapter<>(this, R.layout.show_list,print_result));
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Link(position,result_ssid);
            }
        });
    }


    private void task2(){
        ArrayList<ScanResult> scan_result = (ArrayList<ScanResult>)wifimanager.getScanResults();
        ArrayList<String> print_result = new ArrayList<>();
        final ArrayList<String> result_ssid = new ArrayList<>();
        scan_result = sortresult(scan_result);
        String encryption;
        Map ap_map =new HashMap();
        int temp_num = 1;
        for(int i = 0; i<scan_result.size();++i) {
            if (scan_result.get(i).capabilities.equals("[ESS]")) {
                encryption = "OPEN";
            } else {
                encryption = "PASSWORD_REQUIRED";
            }
            if (!ap_map.containsKey(scan_result.get(i).SSID) && ap_map.size() < 4) {
                ap_map.put(scan_result.get(i).SSID, 0);
                print_result.add(temp_num + "." + scan_result.get(i).SSID + "-" + scan_result.get(i).BSSID + "-" + scan_result.get(i).level + encryption);
                temp_num += 1;
                result_ssid.add(scan_result.get(i).SSID);
            }
            if (ap_map.containsKey(scan_result.get(i).SSID) && ap_map.size() < 4) {
                continue;
            }
        }
        list_view.setAdapter(new ArrayAdapter<>(this, R.layout.show_list,print_result));
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Link(position,result_ssid);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) private void Link(final int position, final ArrayList<String> result_ssid){
        final View layout_window = getLayoutInflater().inflate(R.layout.dialog, null);

        new AlertDialog.Builder(this).
                setTitle(result_ssid.get(position)).
                setIcon(android.R.drawable.ic_dialog_info).
                setView(layout_window).
                setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText user = (EditText) layout_window.findViewById(R.id.edit_username);
                        EditText password = (EditText) layout_window.findViewById(R.id.edit_password);
                        String username = user.getText().toString();
                        String key = password.getText().toString();
                        WifiConfiguration config = new WifiConfiguration();
                        WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
                        config.SSID = "\"" + result_ssid.get(position) + "\"";
                        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
                        enterpriseConfig.setIdentity(username);
                        enterpriseConfig.setPassword(key);
                        enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
                        config.enterpriseConfig=enterpriseConfig;
//                        config.preSharedKey = "\""+key+"\"";
//                        config.SSID = "\"" + result_ssid.get(position) + "\"";
//
//                        config.enterpriseConfig.setIdentity(username);
//                        config.enterpriseConfig.setIdentity(key);
//                        config.enterpriseConfig.setPhase2Method(WifiEnterpriseConfig.Phase2.MSCHAPV2);

//                        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//                        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//                        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
//                        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
//                        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//                        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//                        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//                        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//                        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//                        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                        int netId = wifimanager.addNetwork(config);
                        wifimanager.saveConfiguration();
                        wifimanager.disconnect();
                        wifimanager.enableNetwork(netId, true);
                        wifimanager.reconnect();
                        connectivitymanager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
                        while(true) {
                            NetworkInfo message = connectivitymanager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                            if (message.isConnected()) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Connected Success")
                                        .setMessage("ssid:" + result_ssid.get(position) + "\n")
                                        .setPositiveButton("Confirm", null)
                                        .create().show();
                                break;
                            }
                        }
                    }
                }).
                setNegativeButton("Cancel",null).
                show();
    }

    private ArrayList<ScanResult> sortresult(ArrayList<ScanResult> list_result) {
        for (int j = list_result.size()-1; j >=0;--j){
            for (int k = list_result.size()-1 ;k>=0;--k){
                if (list_result.get(j).level<list_result.get(k).level){
                    ScanResult temp = null;
                    temp = list_result.get(j);
                    list_result.set(j,list_result.get(k));
                    list_result.set(k,temp);
                }
            }
        }
        return list_result;
    }
}