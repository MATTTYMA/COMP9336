package com.example.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver peerReceiver;
    private static final String TAG = "WiFiDirectActivity";
    private List<WifiP2pDevice> deviceList = new ArrayList<WifiP2pDevice>();
    private ListView listView;
    private TextView text1;
    private Button buttonDiscover;
    private Button check;
    IntentFilter peerfilter;
    private WifiP2pManager wifiP2pManager;
    private Channel wifiDirectChannel;

    private void initializeWiFiDirect() {
        wifiP2pManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);

        wifiDirectChannel = wifiP2pManager.initialize(this, getMainLooper(),
                new ChannelListener() {
                    public void onChannelDisconnected() {
                        initializeWiFiDirect();
                    }
                }
        );
    }


    private ActionListener actionListener = new ActionListener() {
        public void onFailure(int reason) {
            String errorMessage = "WiFi Direct Failed: ";
            switch (reason) {
                case WifiP2pManager.BUSY :
                    errorMessage += "Framework busy."; break;
                case WifiP2pManager.ERROR :
                    errorMessage += "Internal error."; break;
                case WifiP2pManager.P2P_UNSUPPORTED :
                    errorMessage += "Unsupported."; break;
                default:
                    errorMessage += "Unknown error."; break;
            }
            text1.setText(errorMessage);
        }

        public void onSuccess() {
            text1.setText("Scanning...");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView) findViewById(R.id.text1);

        listView = (ListView)findViewById(R.id.listview);

        initializeWiFiDirect();
        buttonDiscover = (Button)findViewById(R.id.peer);
        buttonDiscover.setEnabled(false);
        buttonDiscover.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                discoverPeers();
            }
        });

        check = (Button)findViewById(R.id.check);

        check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //listView.setAdapter(new ArrayAdapter<WifiP2pDevice>(getApplicationContext(), R.layout.activity_list, R.id.text2, deviceList));
                //aa = new ArrayAdapter<WifiP2pDevice>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceList);
                status();
            }
        });

    }

    private void status() {

        peerReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                    // Check to see if Wi-Fi is enabled and notify appropriate activity
                    int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                    if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                        // Wifi P2P is enabled
                        text1.setText("Wifi P2P is enabled.");
                        buttonDiscover.setEnabled(true);
                    } else {
                        // Wi-Fi P2P is not enabled
                        text1.setText("Wifi P2P is NOT enabled.");
                    }

                } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                    // Call WifiP2pManager.requestPeers() to get a list of current peers
                    if (wifiP2pManager != null) {
                        wifiP2pManager.requestPeers(wifiDirectChannel,new WifiP2pManager.PeerListListener() {
                            public void onPeersAvailable(WifiP2pDeviceList peers) {
                                text1.setText("Peer discovered!");
                                deviceList.clear();
                                deviceList.addAll(peers.getDeviceList());
                                listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.activity_list, R.id.text2, deviceList));
                            }
                        });
                    }
                } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                    // Respond to new connection or disconnections
                } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                    // Respond to this device's wifi state changing
                }

            }
        };
        peerfilter = new IntentFilter();
        peerfilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        peerfilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        peerfilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        peerfilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        //unregisterReceiver(peerReceiver);
        registerReceiver(peerReceiver, peerfilter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long arg3) {
                connectTo(deviceList.get(index));
            }
        });
    }

    private void discoverPeers() {
        deviceList.clear();
        listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.activity_list, R.id.text2, deviceList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                connectTo(deviceList.get(index));
            }
        });

        wifiP2pManager.discoverPeers(wifiDirectChannel, actionListener);
    }


    private void connectTo(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        wifiP2pManager.connect(wifiDirectChannel, config, actionListener);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(peerReceiver);
    }
}