package com.kathancheema.personal.commandline.runner.controller;

import com.kathancheema.personal.commandline.runner.model.configuration.Config;
import com.kathancheema.personal.commandline.runner.model.configuration.ConfigFactory;
import com.kathancheema.personal.commandline.runner.model.runner.Runner;
import com.kathancheema.personal.commandline.runner.model.runner.RunnerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

public class CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger("com.kathancheema.personal.commandline.runner");
	private ConfigFactory configFactory;


	public CommandLineRunner(File location) {

		logger.info("Reading configs file `" + location + "`.");
		parseArgumentConfigs(location);

	}

	public void run(final String[] args) {

		try {

			logger.info("Finding config for args `" + Arrays.toString(args) + "`.");
			var matching = getConfigFor(args);

			logger.info("Finding method to run.");
			var runner = getRunner(matching, args);

			logger.info("Starting application.");
			invokeMethod(runner);
		} catch (Exception ignored) {

		}

	}

	private void parseArgumentConfigs(File location) {
		configFactory = new ConfigFactory(location);
	}

	private Config getConfigFor(String[] args) {
		return configFactory.getConfig(args);
	}

	private Runner getRunner(Config matching, String[] args) {
		return RunnerFactory.getRunnerFor(matching, args);
	}

	private void invokeMethod(Runner runner) {
		var runnerThread = new Thread(runner);
		runnerThread.setName("runner");
		runnerThread.start();
	}

}
