package pw.aristos.arnylib.network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import cz.msebera.android.httpclient.Header;

import java.util.Arrays;

import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;
import static android.app.DownloadManager.STATUS_FAILED;
public class QueryService extends IntentService {
	private AsyncHttpClient aClient = new SyncHttpClient();
	private String test_url ="https://pik.ru/luberecky/singlepage?data=ChessPlan&id=2b3ecc9b-bfad-e611-9fbe-001ec9d5643c&private_key=1&format=json&domain=pik.ru";
	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */
	public QueryService(String name) {
		super(name);
	}

	public QueryService() {
		super("QueryService");
	}

	protected void onHandleIntent(Intent intent) {
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		String command = intent.getStringExtra("command");
		final Bundle b = new Bundle();
		if(command.equals("query")){
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);
			try {
				aClient.get(this, test_url, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
						Log.i(QueryService.class.getSimpleName(), "onSuccess: responseBody = " + new String(responseBody));
						b.putByteArray("results", responseBody);
						receiver.send(STATUS_SUCCESSFUL, b);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
						b.putString(Intent.EXTRA_TEXT, Arrays.toString(responseBody));
						receiver.send(STATUS_FAILED, b);
					}
					// ... onSuccess here
				});
			} catch(Exception e) {
				b.putString(Intent.EXTRA_TEXT, e.toString());
				receiver.send(STATUS_FAILED, b);
			}
		}
	}
}