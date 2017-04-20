package pw.aristos.arnylib.network;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONObject;
import pw.aristos.arnylib.service.OperationProvider;

import java.util.HashMap;
public class ApiService extends AbstractNetworkService {
	@Override
	protected void runJSONRequest(OperationProvider provider, final OnJSONRequestResult result) {
		HashMap<String, Object> operationData = provider.getOperationData();
		int method = (int) operationData.get("method");
		String url = (String) operationData.get("url");
		JSONObject params = (JSONObject) operationData.get("params");
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url,
				params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				result.onResult(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				result.onError(error.toString());
			}
		});
		VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
	}

	@Override
	protected void runStringRequest(OperationProvider provider, final OnStringRequestResult result) {
		HashMap<String, Object> operationData = provider.getOperationData();
		int method = (int) operationData.get("method");
		String url = (String) operationData.get("url");
		StringRequest strReq = new StringRequest(method, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						result.onSuccess(response);
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				result.onError(error.toString());
			}
		});
		VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq);
	}
}
