package cloud.sgrc.terraform.controllers;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracker for keeping track of running JobExecutions in this application.
 * @author Sergiu Bodiu
 *
 */
public class RunningExecutionTracker {

	private Map<Long, String> runningExecutions = new ConcurrentHashMap<>();

	public void addRunningExecution(String jobName, Long executionId) {
		runningExecutions.put(executionId, jobName);
	}

	public void removeRunningExecution(Long executionId) {
		runningExecutions.remove(executionId);
	}

	public Set<Long> getAllRunningExecutionIds() {
		return new HashSet<>(runningExecutions.keySet());
	}

	public Set<Long> getRunningExecutionIdsForJobName(String jobName) {
		Set<Long> runningExecutionIds = new HashSet<>();
		for (Entry<Long, String> entry : runningExecutions.entrySet()) {
			if (entry.getValue().equals(jobName)) {
				runningExecutionIds.add(entry.getKey());
			}
		}
		return runningExecutionIds;
	}

}