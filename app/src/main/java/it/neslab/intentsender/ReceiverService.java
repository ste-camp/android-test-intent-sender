package it.neslab.intentsender;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ReceiverService extends Service {
    private ReceiverBinder binder = new ReceiverBinder();
    private long latestTime;
    private long latestStartTime;
    private int mode;
    private int total;

    public ReceiverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        latestTime = System.currentTimeMillis();
        latestStartTime = intent.getLongExtra("starting", 0L);
        mode = intent.getIntExtra("mode", 0);
        total = intent.getIntExtra("n_tests", 0);
        return binder;
    }

    public long getLatestIntentTime(){
        return latestTime;
    }

    public long getLatestStartTime(){
        return latestStartTime;
    }

    public int getMode(){
        return mode;
    }

    public int getTotal(){
        return total;
    }

    public class ReceiverBinder extends Binder {
        public ReceiverService getService(){
            return ReceiverService.this;
        }
    }
}
