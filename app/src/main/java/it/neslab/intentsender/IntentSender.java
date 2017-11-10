package it.neslab.intentsender;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

import it.neslab.intentreceiver.ServiceInterface;

public class IntentSender extends AppCompatActivity {

    public static final int MEASURE_START = 1;
    public static final int MEASURE_CONTINUE = 2;

    public static final String ACTION_TEST = "it.neslab.intentsender.test_internal";
    public static final String ACTION_TEST_EXTERNAL = "it.neslab.intentsender.test_internal_external";

    private Intent baseIntent;

    private Long measurement_first;
    private Integer measurement_count;
    private Long measurement_sum;
    private Long[] marshalling_sums = {0L,0L,0L,0L,0L};

    private MessengerConnection externalConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_sender);

        Button invokeInternalImplButton = (Button) findViewById(R.id.intent_impl_internal);
        Button invokeInternalExplButton = (Button) findViewById(R.id.intent_expl_internal);
        Button invokeExternalImplButton = (Button) findViewById(R.id.intent_impl_external);
        Button invokeExternalExplButton = (Button) findViewById(R.id.intent_expl_external);
        Button invokeInternalDirectCall = (Button) findViewById(R.id.function_call);
        Button invokeServiceInternal = (Button) findViewById(R.id.intent_service_internal);
        Button invokeServiceExternal = (Button) findViewById(R.id.intent_service_external);
        Button invokeServiceAidl = (Button) findViewById(R.id.intent_service_aidl);

        invokeInternalImplButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.intent_expl_external).setEnabled(false);
                findViewById(R.id.intent_impl_external).setEnabled(false);
                findViewById(R.id.intent_expl_internal).setEnabled(false);
                findViewById(R.id.intent_impl_internal).setEnabled(false);
                findViewById(R.id.intent_service_internal).setEnabled(false);
                findViewById(R.id.intent_service_external).setEnabled(false);
                findViewById(R.id.intent_service_aidl).setEnabled(false);
                findViewById(R.id.function_call).setEnabled(false);

                Intent in = new Intent(ACTION_TEST);
                in.setPackage(IntentSender.this.getPackageName());
                in.putExtra("n_tests", 100);
                baseIntent = (Intent)in.clone();
                in.putExtra("starting", System.currentTimeMillis());

                setResultText("Inizio misurazione...");
                IntentSender.this.startActivityForResult(in, IntentSender.MEASURE_START);
            }
        });

        invokeInternalExplButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.intent_expl_external).setEnabled(false);
                findViewById(R.id.intent_impl_external).setEnabled(false);
                findViewById(R.id.intent_expl_internal).setEnabled(false);
                findViewById(R.id.intent_impl_internal).setEnabled(false);
                findViewById(R.id.intent_service_internal).setEnabled(false);
                findViewById(R.id.intent_service_external).setEnabled(false);
                findViewById(R.id.intent_service_aidl).setEnabled(false);
                findViewById(R.id.function_call).setEnabled(false);

                Intent in = new Intent(IntentSender.this, IntentReceiver.class);
                in.putExtra("n_tests", 100);
                baseIntent = (Intent)in.clone();
                in.putExtra("starting", System.currentTimeMillis());

                IntentSender.this.startActivityForResult(in, IntentSender.MEASURE_START);
            }
        });

        invokeExternalImplButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.intent_expl_external).setEnabled(false);
                findViewById(R.id.intent_impl_external).setEnabled(false);
                findViewById(R.id.intent_expl_internal).setEnabled(false);
                findViewById(R.id.intent_impl_internal).setEnabled(false);
                findViewById(R.id.intent_service_internal).setEnabled(false);
                findViewById(R.id.intent_service_external).setEnabled(false);
                findViewById(R.id.intent_service_aidl).setEnabled(false);
                findViewById(R.id.function_call).setEnabled(false);

                Intent in = new Intent(ACTION_TEST_EXTERNAL);
                in.putExtra("n_tests", 100);
                baseIntent = (Intent)in.clone();
                in.putExtra("starting", System.currentTimeMillis());

                setResultText("Inizio misurazione...");
                IntentSender.this.startActivityForResult(in, IntentSender.MEASURE_START);
            }
        });

        invokeExternalExplButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.intent_expl_external).setEnabled(false);
                findViewById(R.id.intent_impl_external).setEnabled(false);
                findViewById(R.id.intent_expl_internal).setEnabled(false);
                findViewById(R.id.intent_impl_internal).setEnabled(false);
                findViewById(R.id.intent_service_internal).setEnabled(false);
                findViewById(R.id.intent_service_external).setEnabled(false);
                findViewById(R.id.intent_service_aidl).setEnabled(false);
                findViewById(R.id.function_call).setEnabled(false);

                Intent in = new Intent();
                in.setComponent(new ComponentName("it.neslab.intentreceiver", "it.neslab.intentreceiver.IntentReceiver"));
                in.putExtra("n_tests", 100);
                baseIntent = (Intent)in.clone();
                in.putExtra("starting", System.currentTimeMillis());

                setResultText("Inizio misurazione...");
                IntentSender.this.startActivityForResult(in, IntentSender.MEASURE_START);
            }
        });

        invokeInternalDirectCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.intent_expl_external).setEnabled(false);
                findViewById(R.id.intent_impl_external).setEnabled(false);
                findViewById(R.id.intent_expl_internal).setEnabled(false);
                findViewById(R.id.intent_impl_internal).setEnabled(false);
                findViewById(R.id.intent_service_internal).setEnabled(false);
                findViewById(R.id.intent_service_external).setEnabled(false);
                findViewById(R.id.intent_service_aidl).setEnabled(false);
                findViewById(R.id.function_call).setEnabled(false);

                InternalCalled inte = new InternalCalled();
                Long start, end;
                int i;
                measurement_sum = 0L;
                for(i=0; i<100; i++){
                    start = System.nanoTime();
                    end = inte.getTime();

                    measurement_sum += end - start;

                    setResultText("Misura in corso: test n°".concat(Integer.toString(i)).concat("/100"));
                }
                setResultText("Misura Terminata: tempo medio "
                        .concat(Float.toString(measurement_sum.floatValue()/ (float) i))
                        .concat("ns su ").concat(Integer.toString(i)).concat(" test "));

                findViewById(R.id.intent_expl_external).setEnabled(true);
                findViewById(R.id.intent_impl_external).setEnabled(true);
                findViewById(R.id.intent_expl_internal).setEnabled(true);
                findViewById(R.id.intent_impl_internal).setEnabled(true);
                findViewById(R.id.intent_service_internal).setEnabled(true);
                findViewById(R.id.intent_service_external).setEnabled(true);
                findViewById(R.id.intent_service_aidl).setEnabled(true);
                findViewById(R.id.function_call).setEnabled(true);
            }
        });

        invokeServiceInternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.intent_expl_external).setEnabled(false);
                findViewById(R.id.intent_impl_external).setEnabled(false);
                findViewById(R.id.intent_expl_internal).setEnabled(false);
                findViewById(R.id.intent_impl_internal).setEnabled(false);
                findViewById(R.id.intent_service_internal).setEnabled(false);
                findViewById(R.id.intent_service_external).setEnabled(false);
                findViewById(R.id.intent_service_aidl).setEnabled(false);
                findViewById(R.id.function_call).setEnabled(false);

                Intent serv = new Intent(IntentSender.this, ReceiverService.class);
                serv.putExtra("n_tests", 100);
                baseIntent = (Intent)serv.clone();
                serv.putExtra("mode", MEASURE_START);
                serv.putExtra("starting", System.currentTimeMillis());

                IntentSender.this.bindService(serv, new IntentTestConnection(), BIND_AUTO_CREATE);
            }
        });

        invokeServiceExternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.intent_expl_external).setEnabled(false);
                findViewById(R.id.intent_impl_external).setEnabled(false);
                findViewById(R.id.intent_expl_internal).setEnabled(false);
                findViewById(R.id.intent_impl_internal).setEnabled(false);
                findViewById(R.id.intent_service_internal).setEnabled(false);
                findViewById(R.id.intent_service_external).setEnabled(false);
                findViewById(R.id.intent_service_aidl).setEnabled(false);
                findViewById(R.id.function_call).setEnabled(false);

                Intent serv = new Intent();
                serv.setComponent(new ComponentName("it.neslab.intentreceiver", "it.neslab.intentreceiver.ReceiverService"));
                serv.putExtra("n_tests", 100);
                baseIntent = (Intent)serv.clone();
                serv.putExtra("mode", MEASURE_START);
                serv.putExtra("starting", System.currentTimeMillis());

                externalConn = new MessengerConnection();
                IntentSender.this.bindService(serv, externalConn, BIND_AUTO_CREATE);
            }
        });

        invokeServiceAidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.intent_expl_external).setEnabled(false);
                findViewById(R.id.intent_impl_external).setEnabled(false);
                findViewById(R.id.intent_expl_internal).setEnabled(false);
                findViewById(R.id.intent_impl_internal).setEnabled(false);
                findViewById(R.id.intent_service_internal).setEnabled(false);
                findViewById(R.id.intent_service_external).setEnabled(false);
                findViewById(R.id.intent_service_aidl).setEnabled(false);
                findViewById(R.id.function_call).setEnabled(false);

                Intent serv = new Intent();
                serv.setComponent(new ComponentName("it.neslab.intentreceiver", "it.neslab.intentreceiver.AidlService"));
                serv.putExtra("n_tests", 100);
                baseIntent = (Intent)serv.clone();
                serv.putExtra("mode", MEASURE_START);
                serv.putExtra("starting", System.currentTimeMillis());

                IntentSender.this.bindService(serv, new AidlConnection(), BIND_AUTO_CREATE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode){
            case MEASURE_START:
                measurement_count = 0;
                measurement_sum = 0L;
                measurement_first = intent.getLongExtra("receive", 0) - intent.getLongExtra("starting", 0);
            case MEASURE_CONTINUE:
                measurement_count++;
                measurement_sum += intent.getLongExtra("receive", 0) - intent.getLongExtra("starting", 0);
                Integer total = intent.getIntExtra("n_tests", 0);
                setResultText("Misura in corso: test n°".concat(measurement_count.toString()).concat("/").concat(total.toString()));
                if(measurement_count < total){
                    Intent next = (Intent)baseIntent.clone();
                    next.putExtra("starting", System.currentTimeMillis());
                    startActivityForResult(next, MEASURE_CONTINUE);
                }
                else{
                    setResultText("Misura Terminata: tempo medio "
                            .concat(Float.toString(measurement_sum.floatValue()/measurement_count.floatValue()))
                            .concat("ms su ").concat(measurement_count.toString()).concat(" test ")
                            .concat("| Test a Freddo: ").concat(measurement_first.toString()).concat("ms"));

                    findViewById(R.id.intent_expl_external).setEnabled(true);
                    findViewById(R.id.intent_impl_external).setEnabled(true);
                    findViewById(R.id.intent_expl_internal).setEnabled(true);
                    findViewById(R.id.intent_impl_internal).setEnabled(true);
                    findViewById(R.id.intent_service_internal).setEnabled(true);
                    findViewById(R.id.intent_service_external).setEnabled(true);
                    findViewById(R.id.intent_service_aidl).setEnabled(true);
                    findViewById(R.id.function_call).setEnabled(true);
                }
                break;
        }
    }

    private void setResultText(String t){
        TextView v = (TextView) findViewById(R.id.intents_result);
        v.setText(t);
    }

    private class InternalCalled{
        long getTime(){
            return System.nanoTime();
        }
    }

    private class IntentTestConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ReceiverService.ReceiverBinder binder = (ReceiverService.ReceiverBinder)service;

            switch (binder.getService().getMode()){
                case MEASURE_START:
                    measurement_count = 0;
                    measurement_sum = 0L;
                    measurement_first = binder.getService().getLatestIntentTime() - binder.getService().getLatestStartTime();
                case MEASURE_CONTINUE:
                    measurement_count++;
                    measurement_sum += binder.getService().getLatestIntentTime() - binder.getService().getLatestStartTime();
                    Integer total = binder.getService().getTotal();

                    IntentSender.this.unbindService(this);

                    setResultText("Misura in corso: test n°".concat(measurement_count.toString()).concat("/").concat(total.toString()));
                    if(measurement_count < total){
                        Intent next = (Intent)baseIntent.clone();
                        next.putExtra("starting", System.currentTimeMillis());
                        next.putExtra("mode", MEASURE_CONTINUE);
                        IntentSender.this.bindService(next, this, BIND_AUTO_CREATE);
                    }
                    else{
                        setResultText("Misura Terminata: tempo medio "
                                .concat(Float.toString(measurement_sum.floatValue()/measurement_count.floatValue()))
                                .concat("ms su ").concat(measurement_count.toString()).concat(" test ")
                                .concat("| Test a Freddo: ").concat(measurement_first.toString()).concat("ms"));

                        findViewById(R.id.intent_expl_external).setEnabled(true);
                        findViewById(R.id.intent_impl_external).setEnabled(true);
                        findViewById(R.id.intent_expl_internal).setEnabled(true);
                        findViewById(R.id.intent_impl_internal).setEnabled(true);
                        findViewById(R.id.intent_service_internal).setEnabled(true);
                        findViewById(R.id.intent_service_external).setEnabled(true);
                        findViewById(R.id.intent_service_aidl).setEnabled(true);
                        findViewById(R.id.function_call).setEnabled(true);
                    }
                    break;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private class MessengerConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger requester = new Messenger(service);
            Message request = new Message();
            request.replyTo = new Messenger(new RepliesHandler());
            try {
                requester.send(request);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private class RepliesHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            unbindService(externalConn);

            switch (data.getInt("mode")){
                case MEASURE_START:
                    measurement_count = 0;
                    measurement_sum = 0L;
                    measurement_first = data.getLong("time") - data.getLong("starting");
                case MEASURE_CONTINUE:
                    measurement_count++;
                    measurement_sum += data.getLong("time") - data.getLong("starting");
                    Integer total = data.getInt("n_tests");

                    setResultText("Misura in corso: test n°".concat(measurement_count.toString()).concat("/").concat(total.toString()));
                    if(measurement_count < total){
                        Intent next = (Intent)baseIntent.clone();
                        next.putExtra("starting", System.currentTimeMillis());
                        next.putExtra("mode", MEASURE_CONTINUE);
                        IntentSender.this.bindService(next, externalConn, BIND_AUTO_CREATE);
                    }
                    else{
                        setResultText("Misura Terminata: tempo medio "
                                .concat(Float.toString(measurement_sum.floatValue()/measurement_count.floatValue()))
                                .concat("ms su ").concat(measurement_count.toString()).concat(" test ")
                                .concat("| Test a Freddo: ").concat(measurement_first.toString()).concat("ms"));

                        findViewById(R.id.intent_expl_external).setEnabled(true);
                        findViewById(R.id.intent_impl_external).setEnabled(true);
                        findViewById(R.id.intent_expl_internal).setEnabled(true);
                        findViewById(R.id.intent_impl_internal).setEnabled(true);
                        findViewById(R.id.intent_service_internal).setEnabled(true);
                        findViewById(R.id.intent_service_external).setEnabled(true);
                        findViewById(R.id.intent_service_aidl).setEnabled(true);
                        findViewById(R.id.function_call).setEnabled(true);
                    }
                    break;
            }
        }
    }

    private class AidlConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceInterface aidlService = ServiceInterface.Stub.asInterface(service);

            try {
                switch (aidlService.getMode()){
                    case MEASURE_START:
                        measurement_count = 0;
                        measurement_sum = 0L;
                        measurement_first = aidlService.getCallTime() - aidlService.getStart();
                    case MEASURE_CONTINUE:
                        measurement_count++;
                        measurement_sum += aidlService.getCallTime() - aidlService.getStart();
                        Integer total = aidlService.getNTests();

                        IntentSender.this.unbindService(this);

                        setResultText("Misura in corso: test n°".concat(measurement_count.toString()).concat("/").concat(total.toString()));
                        testMarshall(aidlService);
                        if(measurement_count < total){
                            Intent next = (Intent)baseIntent.clone();
                            next.putExtra("starting", System.currentTimeMillis());
                            next.putExtra("mode", MEASURE_CONTINUE);
                            IntentSender.this.bindService(next, this, BIND_AUTO_CREATE);
                        }
                        else{
                            setResultText("Misura Terminata: tempo medio di bind "
                                    .concat(Float.toString(measurement_sum.floatValue()/measurement_count.floatValue()))
                                    .concat("ms su ").concat(measurement_count.toString()).concat(" test ")
                                    .concat("| Test a Freddo: ").concat(measurement_first.toString()).concat("ms")
                                    .concat("\nChiamata a metodo con 1int,1double,1Map(100size): ").concat(Float.toString(marshalling_sums[0].floatValue()/measurement_count.floatValue()))
                                    .concat("\nChiamata a metodo con 1int,1double,1Map(200size): ").concat(Float.toString(marshalling_sums[1].floatValue()/measurement_count.floatValue()))
                                    .concat("\nChiamata a metodo con 1int,1double,1Map(300size): ").concat(Float.toString(marshalling_sums[2].floatValue()/measurement_count.floatValue()))
                                    .concat("\nChiamata a metodo con 1int,1double,1Map(400size): ").concat(Float.toString(marshalling_sums[3].floatValue()/measurement_count.floatValue()))
                                    .concat("\nChiamata a metodo con 1int,1double,1Map(500size): ").concat(Float.toString(marshalling_sums[4].floatValue()/measurement_count.floatValue())));

                            findViewById(R.id.intent_expl_external).setEnabled(true);
                            findViewById(R.id.intent_impl_external).setEnabled(true);
                            findViewById(R.id.intent_expl_internal).setEnabled(true);
                            findViewById(R.id.intent_impl_internal).setEnabled(true);
                            findViewById(R.id.intent_service_internal).setEnabled(true);
                            findViewById(R.id.intent_service_external).setEnabled(true);
                            findViewById(R.id.intent_service_aidl).setEnabled(true);
                            findViewById(R.id.function_call).setEnabled(true);
                        }
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        private void testMarshall(ServiceInterface s){
            HashMap<Integer, Integer> map = new HashMap<>();

            Random r = new Random();
            Long ts;
            for(int i=0; i<5; i++) {
                for(int j=0;j<100;j++){
                    map.put(j, r.nextInt());
                }
                try {
                    ts = System.currentTimeMillis();
                    marshalling_sums[i] += s.testMethod(10, 19852.5, map) - ts;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
