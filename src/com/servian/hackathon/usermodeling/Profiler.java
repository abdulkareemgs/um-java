package com.servian.hackathon.usermodeling;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class Profiler {
	private Executor executor = Executor.newInstance().auth(Config.username,
			Config.password);

	public String profile(String input) {
		JSONObject contentItem = new JSONObject();
		contentItem.put("userid", UUID.randomUUID().toString());
		contentItem.put("id", UUID.randomUUID().toString());
		contentItem.put("sourceid", "freetext");
		contentItem.put("contenttype", "text/plain");
		contentItem.put("language", "en");
		contentItem.put("content", input);

		// Display the user's input for them, so they can
		// see the text that was analyzed

		JSONObject content = new JSONObject();
		JSONArray contentItems = new JSONArray();
		content.put("contentItems", contentItems);
		contentItems.add(contentItem);

		URI profileURI = null;
		try {
			profileURI = new URI(Config.baseURL + "api/v2/profile").normalize();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Request profileRequest = Request.Post(profileURI)
				.addHeader("Accept", "application/json")
				.bodyString(content.toString(), ContentType.APPLICATION_JSON);
		try {
			return executor.execute(profileRequest).returnContent().asString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public String vizualize(String profile, String image) {
		URI vizURI = null;
		try {
			vizURI = new URI(Config.baseURL
					+ String.format("api/v2/visualize?w=%d&h=%d&imgurl=%s",
							900, 900, image)).normalize();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			executor.execute(
					Request.Post(vizURI).bodyString(profile,
							ContentType.APPLICATION_JSON)).returnContent()
					.asString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}
}
