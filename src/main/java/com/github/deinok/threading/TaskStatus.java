package com.github.deinok.threading;

/**
 * Represents the current stage in the lifecycle of a Task
 */
public enum TaskStatus {
	Canceled,
	Created,
	Faulted,
	RanToCompletion,
	Running,
	WaitingForActivation,
	WaitingForChildrenToComplete,
	WaitingToRun
}
