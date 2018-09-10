package com.kathancheema.personal.commandline.runner.model.runner;

import com.kathancheema.personal.commandline.runner.model.configuration.Config;

public class RunnerFactory {

	public static Runner getRunnerFor(Config config, String[] args) {
		return new Runner(config, args);
	}
}
