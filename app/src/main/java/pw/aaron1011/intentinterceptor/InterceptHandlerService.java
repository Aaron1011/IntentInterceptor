package pw.aaron1011.intentinterceptor;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class InterceptHandlerService extends IntentService {

    private static final String TAG = "InterceptorHandler";

    public InterceptHandlerService() {
        super("InterceptorHandlerWorker");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        CapturedIntent data = intent.getParcelableExtra("capturedIntent");
        Log.d(TAG, "Got data: " + data);

    }
}
