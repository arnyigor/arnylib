package pw.aristos.arnylib.network;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
public class GZipRequest extends StringRequest {

	public GZipRequest(int method, String url, Response.Listener listener, Response.ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}

	public GZipRequest(String url, Response.Listener listener, Response.ErrorListener errorListener) {
		super(url, listener, errorListener);
	}

//	@Override
//	protected Response parseNetworkResponse(NetworkResponse response) {
//
//
//		return Response.success(unzipString(response.data), HttpHeaderParser.parseCacheHeaders(response));
//	}

	// parse the gzip response using a GZIPInputStream
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		StringBuilder output = new StringBuilder();
		try {
			GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(response.data));
			InputStreamReader reader = new InputStreamReader(gStream);
			BufferedReader in = new BufferedReader(reader, 65536);

			String read;

			while ((read = in.readLine()) != null) {
				output.append(read).append("\n");
			}
			reader.close();
			in.close();
			gStream.close();
		} catch (IOException e) {
			return Response.error(new ParseError());
		}

		return Response.success(output.toString(), HttpHeaderParser.parseCacheHeaders(response));
	}

}