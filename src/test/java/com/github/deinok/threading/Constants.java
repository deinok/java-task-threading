package com.github.deinok.threading;

public final class Constants {

	public static final int DefaultTimeOut = 1000;
	public static final int Default125PercentTimeOut = Double.valueOf(DefaultTimeOut * 1.25).intValue();
	public static final int Default90PercentTimeOut = Double.valueOf(DefaultTimeOut * 0.9).intValue();
	public static final int Default75PercentTimeOut = Double.valueOf(DefaultTimeOut * 0.75).intValue();
	public static final int Default50PercentTimeOut = Double.valueOf(DefaultTimeOut * 0.5).intValue();
	public static final int Default25PercentTimeOut = Double.valueOf(DefaultTimeOut * 0.25).intValue();
}
