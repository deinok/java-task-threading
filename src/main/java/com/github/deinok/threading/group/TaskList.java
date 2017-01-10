package com.github.deinok.threading.group;

import com.github.deinok.threading.OnSuccess;
import com.github.deinok.threading.Task;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class TaskList<R> extends LinkedList<Task<R>> implements ITaskGroup<R> {

	@NotNull
	public ITaskGroup<R> executeAsync() {
		for (Task<R> task : this) {
			task.executeAsync();
		}
		return this;
	}

	@NotNull
	public ITaskGroup<R> executeSync() {
		for (Task<R> task : this) {
			task.executeSync();
		}
		return this;
	}

	public int getPriority() {
		int result = 0;
		for (Task<R> task : this) {
			result += task.getPriority();
		}
		return result / this.size();
	}

	@NotNull
	public ITaskGroup<R> setPriority(int priority) {
		for (Task<R> task : this) {
			task.setPriority(priority);
		}
		return this;
	}

	public boolean cancel() {
		for (Task<R> task : this) {
			boolean cancelled = task.cancel();
			if (!cancelled) {
				return false;
			}
		}
		return true;
	}

	@NotNull
	public ITaskGroup<R> await() {
		for (Task<R> task : this) {
			task.await();
		}
		return this;
	}

	@NotNull
	public List<R> getResult() {
		final List<R> results = new ArrayList<R>(this.size());
		for (Task<R> task : this) {
			results.add(task.getResult());
		}
		return results;
	}

	@NotNull
	public ITaskGroup<R> onSuccess(@NotNull OnSuccess<R> onSuccess) {
		for (Task<R> task : this) {
			task.onSuccess(onSuccess);
		}
		return this;
	}
}

