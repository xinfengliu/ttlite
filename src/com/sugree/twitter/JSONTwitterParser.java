// modified by XinfengLiu in Dec 2009
package com.sugree.twitter;

import java.util.Date;
import java.util.Vector;

import com.substanceofcode.utils.Log;
import com.substanceofcode.utils.StringUtil;
import com.substanceofcode.twitter.model.Status;
import org.json.me.JSONArray;
import org.json.me.JSONObject;
import org.json.me.JSONException;

import com.sugree.utils.DateUtil;


public class JSONTwitterParser {
	public static Vector parseStatuses(String payload) throws TwitterException {
		Vector statuses = new Vector();

		try {
			JSONArray json = new JSONArray(payload);
			JSONObject status = null;
			JSONObject user = null;
			for (int i=0; i<json.length(); i++) {
				try {
					status = json.getJSONObject(i);
				} catch (JSONException je) {
					throw new TwitterException("expect status object "+json.get(i));
				}
				try {
					user = status.getJSONObject("user");
				} catch (JSONException je) {
					throw new TwitterException("expect user object "+json.get(i));
				}

				String screenName = StringUtil.decodeEntities(user.getString("screen_name"));
				long id = status.getLong("id");
				String text = StringUtil.decodeEntities(status.getString("text"));
				Date createAt = DateUtil.parseDate(status.getString("created_at"));
				String source = StringUtil.removeHtml(status.getString("source"));

                //Log.debug ("favorited:"+status.getString("favorited"));
                //Log.debug("created_at" + status.getString("created_at"));

				boolean favorited = status.getString("favorited").equals("true");
				statuses.addElement(new Status(id, screenName, text, createAt, source, favorited));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new TwitterException(e);
		} catch (TwitterException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TwitterException(e);
		}
        statuses.trimToSize();
		return statuses;
	}

	public static Status parseStatus(String payload) throws TwitterException {
		Status s = null;
                //Log.debug("Return Status:\n" + payload);
		try {
			JSONObject status = new JSONObject(payload);
			JSONObject user = status.getJSONObject("user");

			String screenName = StringUtil.decodeEntities(user.getString("screen_name"));
			long id = status.getLong("id");
			String text = StringUtil.decodeEntities(status.getString("text"));
			Date createAt = DateUtil.parseDate(status.getString("created_at"));
			String source = StringUtil.removeHtml(status.getString("source"));
			boolean favorited = status.getString("favorited").equals("true");
                        //Log.debug("favorited: "+status.getString("favorited"));
			s = new Status(id, screenName, text, createAt, source, favorited);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new TwitterException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TwitterException(e);
		}
		return s;
	}

	public static Vector parseDirectMessages(String payload) throws TwitterException {
		Vector statuses = new Vector();

		try {
			JSONArray json = new JSONArray(payload);
			JSONObject message = null;
			String screenName = "";
			for (int i=0; i<json.length(); i++) {
				try {
					message = json.getJSONObject(i);
				} catch (JSONException je) {
					throw new TwitterException("expect message object "+je+" "+json.get(i));
				}
				try {
					screenName = message.getString("sender_screen_name");
				} catch (JSONException je) {
					throw new TwitterException("expect sender screen name "+je+" "+json.get(i));
				}

				long id = message.getLong("id");
				String text = StringUtil.decodeEntities(message.getString("text"));
				Date createAt = DateUtil.parseDate(message.getString("created_at"));
				String source = "dm";
				statuses.addElement(new Status(id, screenName, text, createAt, source, false));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new TwitterException(e);
		} catch (TwitterException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TwitterException(e);
		}
        statuses.trimToSize();
		return statuses;
	}
}

//	public static String parseTest(String payload) throws TwitterException {
//		String s = null;
//		try {
//			JSONObject o = new JSONObject(payload);
//			s = o.toString(2);
//		} catch (JSONException e) {
//			s = payload;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new TwitterException(e);
//		}
//		return s;
//	}
//
//	public static String parseScheduleDowntime(String payload) throws TwitterException {
//		String s = null;
//		try {
//			JSONObject o = new JSONObject(payload);
//			s = o.toString(2);
//		} catch (JSONException e) {
//			s = payload;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new TwitterException(e);
//		}
//		return s;
//	}
//}
