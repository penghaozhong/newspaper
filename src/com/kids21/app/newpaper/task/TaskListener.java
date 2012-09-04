package com.kids21.app.newpaper.task;

public interface TaskListener {
	String getName();

	void onPreExecute(GenericTask task);

	void onPostExecute(GenericTask task, TaskResult result);

	void onProgressUpdate(GenericTask task, Object param);

	void onCancelled(GenericTask task);
}
