package sebet.space;

import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sebet.space.decoding.FormatDecoder;
import sebet.space.decoding.XmlComposer;
import sebet.space.list.ListAdapter;
import sebet.space.listener.OnBarcodeReceivedListener;
import sebet.space.networking.SocketClient;
import sebet.space.utils.EnumTasks;

public class Space extends AppCompatActivity {

    public static Space activity;
    public static Context context;

    public Toolbar toolbar;

    public static RecyclerView rv_tasks;
    public static ListAdapter listAdapter;

    public static FormatDecoder decoder;
    public ExecutorService service;
    public SocketClient client;
    public static BarcodeCapture barcodeCapture;

    public boolean backPressed;
    public String lastScan;

    public static EnumTasks activeTask;
    public static List<EnumTasks> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getBaseContext();
        activity = this;

        setContentView(R.layout.activity_space);

        initViews();
        initContent();

        service = Executors.newFixedThreadPool(3);
        client = new SocketClient();
        client.setPort(4000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_space, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_scan) {
            reset();
            lastScan = "";
            Toast.makeText(context, "Neuer Scan", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!backPressed){
            backPressed = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressed = false;
                }
            }, 1500);
            Toast.makeText(context, "Erneut drücken zum Beenden", Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }
    }

    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv_tasks = (RecyclerView) findViewById(R.id.rv_tasks);
        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.bv_Barcode);
        barcodeCapture.setShowDrawRect(true);
        barcodeCapture.setSupportMultipleScan(false);
        barcodeCapture.setTouchAsCallback(false);
        barcodeCapture.shouldAutoFocus(true);
        barcodeCapture.setShouldShowText(false);
        barcodeCapture.refresh();

        lastScan = "";

        barcodeCapture.setRetrieval(new OnBarcodeReceivedListener() {
            @Override
            public void onRetrieved(final Barcode barcode) {
                Space.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!lastScan.equals(barcode.displayValue) ) {
                            lastScan = barcode.displayValue;
                            Log.d("-Space-", lastScan);
                            if (decoder.getTaskForCode(barcode.displayValue) == activeTask) {
                                decoder.decode(barcode.displayValue);
                                if (activeTask == tasks.get(tasks.size() - 1)) {
                                    send(decoder.getIP(), XmlComposer.XmlAsString(decoder));
                                    reset();
                                    Toast.makeText(context, "Erfolgreich gesendet!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                updateTasks();

                            } else {
                                reset();
                                Toast.makeText(context, "Falscher Code gescannt!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

            }
        });
    }

    public void initContent() {
        decoder = new FormatDecoder();
        activeTask = null;
        tasks = new ArrayList<>();
        tasks.add(EnumTasks.PUMP);
        tasks.add(EnumTasks.DRUG_CONTAINER);

        initRecyclerView();
        updateTasks();
    }

    public void initRecyclerView() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv_tasks.setLayoutManager(lm);

        listAdapter = new ListAdapter(context, tasks);
        rv_tasks.setAdapter(listAdapter);
    }

    public void updateTasks(){
        if(activeTask == null) {
            activeTask = tasks.get(0);
        } else {
            int index = tasks.indexOf(activeTask);
            if(index == tasks.size() - 1){
                activeTask = tasks.get(0);
            } else {
                activeTask = tasks.get(index + 1);
            }
        }
        if(listAdapter != null)
            listAdapter.notifyDataSetChanged();
    }

    public void reset() {
        initContent();
    }

    public void send(String host, String data){
        client.setHost(host);
        service.submit(client.pass(data));
    }
}
