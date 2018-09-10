package com.kathancheema.personal.commandline.runner.model.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigFactory {

	private final File location;
	private Stream<String> lines;
	private Stream<List<String>> splitLines;
	private Stream<Config> configs;

	public ConfigFactory(File location) {
		this.location = location;
		setLines();
		setSplitLines();
		setConfigs();
	}

	private void setLines() {
		try {
			lines = new BufferedReader(new FileReader(location)).lines();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void setSplitLines() {
		splitLines = lines.map(s -> Arrays.asList(s.split(" ")));
	}

	private void setConfigs() {
		configs = splitLines.map(Config::new);
	}

	public Config getConfig(String[] args) {
		var matchingConfigs = configs.filter(config -> config.matchesThisConfig(args)).collect(Collectors.toList());

		if (matchingConfigs.size() == 0) {
			throw new RuntimeException("No configs match " + Arrays.asList(args));
		}

		if (matchingConfigs.size() > 1) {
			throw new RuntimeException("[INTERNAL ERROR] Multiple configs match " + Arrays.asList(args));
		}

		return matchingConfigs.get(0);
	}

}
