/*
 * 
 */
package com.servian.hackathon.usermodeling;

/**
 * @author abdulkareem {@literal <abdulkareem.nalband@globalsoft.com>}
 *
 */

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Inerfaces with IBM WATSON User Modeling Services
 */
public class Profiler {

	/** The executor. */
	private Executor executor = Executor.newInstance().auth(Config.username,
			Config.password);

	/**
	 * Generates profile based on input
	 *
	 * @param input user generated content
	 * @return profile of personality traits
	 */
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
			e.printStackTrace();
		}

		Request profileRequest = Request.Post(profileURI)
				.addHeader("Accept", "application/json")
				.bodyString(content.toString(), ContentType.APPLICATION_JSON);
		try {
			return executor.execute(profileRequest).returnContent().asString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Vizualize.
	 *
	 * @param profile
	 *            genereted by {@link #profile(String) profile} method.
	 * @param image
	 *            link to Image URI
	 * @return html/svg of profile visualization
	 */
	public String vizualize(String profile, String image) {
		URI vizURI = null;
		try {
			vizURI = new URI(Config.baseURL
					+ String.format("api/v2/visualize?w=%d&h=%d&imgurl=%s",
							900, 900, image)).normalize();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		try {
			executor.execute(
					Request.Post(vizURI).bodyString(profile,
							ContentType.APPLICATION_JSON)).returnContent()
					.asString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}
}
