package com.kathancheema.personal.commandline.runner.view;

import org.slf4j.Logger;

import java.util.Arrays;

public class LoggingUtils {

	private final Logger logger;

	public LoggingUtils(Logger logger) {
		this.logger = logger;
	}

	public void error(Throwable e) {
		Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).forEach(logger::error);
	}

	public void debug(Throwable e) {
		Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).forEach(logger::debug);
	}
}
