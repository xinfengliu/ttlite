// modified by XinfengLiu in Dec 2009
package com.sugree.twitter.tasks;


import com.substanceofcode.twitter.model.Status;
import com.substanceofcode.tasks.AbstractTask;
import com.substanceofcode.twitter.TwitterApi;
import com.substanceofcode.utils.Log;
import com.sugree.twitter.TwitterController;

public class RequestObjectTask extends AbstractTask {
	private TwitterController controller;
	private TwitterApi api;
	private int objectType;
	private String id;

	public final static int FAVORITE_CREATE = 0;
	public final static int FAVORITE_DESTROY = 1;
	public final static int TEST = 2;
	public final static int SCHEDULE_DOWNTIME = 3;

	public RequestObjectTask(TwitterController controller, TwitterApi api, int objectType, String id) {
		this.controller = controller;
		this.api = api;
		this.objectType = objectType;
		this.id = id;
	}

	public void doTask() {
		Status status = null;
		String info = null;

		try {
			switch (objectType) {
				case FAVORITE_CREATE:
					status = api.createFavorite(id);
					//controller.updateStatus(status);
                   
					controller.showStatus(status);
					break;
				case FAVORITE_DESTROY:
					status = api.destroyFavorite(id);
					//controller.updateStatus(status);
					//controller.showTimeline();
                    controller.showStatus(status);
					break;
//				case TEST:
//					info = api.requestTest();
//					controller.showAlert("Test", info);
//					break;
//				case SCHEDULE_DOWNTIME:
//					info = api.requestScheduleDowntime();
//					controller.showAlert("Schedule Downtime", info);
//					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			controller.showError(e, controller.SCREEN_TIMELINE);
		}
	}
}
