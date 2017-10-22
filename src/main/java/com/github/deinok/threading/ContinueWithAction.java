package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;

public interface ContinueWithAction<TResult> {

	void continueAction(@NotNull Task<TResult> antecedentTask) throws Exception;

}
