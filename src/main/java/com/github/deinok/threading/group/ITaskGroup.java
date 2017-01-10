package com.github.deinok.threading.group;

import com.github.deinok.threading.OnSuccess;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface ITaskGroup<R> {

	@NotNull
	ITaskGroup<R> executeAsync();

	@NotNull
	ITaskGroup<R> executeSync();

	int getPriority();

	@NotNull
	ITaskGroup<R> setPriority(int priority);

	boolean cancel();

	@NotNull
	ITaskGroup<R> await();

	@NotNull
	List<R> getResult();

	@NotNull
	ITaskGroup<R> onSuccess(@NotNull OnSuccess<R> onSuccess);

}
