package com.kathancheema.personal.commandline.runner.model.configuration;

import org.apache.commons.validator.routines.DoubleValidator;
import org.apache.commons.validator.routines.FloatValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.LongValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Config {

	private static final String
			STRING = "STRING",
			VALUE = "VALUE",
			CHARACTER = "CHAR",
			INTEGER = "INT",
			LONG = "LONG",
			FLOAT = "FLOAT",
			DOUBLE = "DOUBLE";

	private final Logger logger = LoggerFactory.getLogger("com.kathancheema.personal.argparser");
	private final String targetClass;
	private final String targetMethod;
	private final List<String> argumentTypes;

	Config(List<String> argumentConfig) {

		var size = argumentConfig.size();
		this.targetClass = argumentConfig.get(size - 2);
		this.targetMethod = argumentConfig.get(size - 1);
		this.argumentTypes = argumentConfig.subList(0, size - 2);

	}

	public String getTargetClass() {
		return targetClass;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

	public Collection<String> getArgumentTypeStrings() {
		return argumentTypes;
	}

	public Collection<Class> getArgumentTypes() {
		var list = new ArrayList<Class>();

		for (String argumentTypeString : getArgumentTypeStrings()) {
			Class type = getClassFromTypeString(argumentTypeString);
			if (type != null) {
				list.add(type);
			}
		}

		return list;
	}

	private Class getClassFromTypeString(String type) {
		switch (type) {
			case VALUE:
				return null;
			case STRING:
				return String.class;
			case INTEGER:
				return int.class;
			case LONG:
				return long.class;
			case CHARACTER:
				return char.class;
			case FLOAT:
				return float.class;
			case DOUBLE:
				return double.class;
			default:
				return null;
		}
	}

	boolean matchesThisConfig(String[] args) {
		if (args.length != argumentTypes.size()) return false;

		for (int i = 0; i < argumentTypes.size(); i++) {
			String arg = argumentTypes.get(i);
			switch (arg) {
				case VALUE:
				case STRING:
					break;

				case INTEGER: {
					if (! matchesInt(args[i])) {
						logger.debug("Argument [" + (i + 1) + "/" + args.length + "] ( = " + args[i] + ") is not type " + INTEGER);
						return false;
					}
					break;
				}

				case LONG: {
					if (! matchesLong(args[i])) {
						logger.debug("Argument [" + (i + 1) + "/" + args.length + "] ( = " + args[i] + ") is not type " + LONG);
						return false;
					}
					break;
				}

				case CHARACTER: {
					if (! matchesChar(args[i])) {
						logger.debug("Argument [" + (i + 1) + "/" + args.length + "] ( = " + args[i] + ") is not type " + CHARACTER);
						return false;
					}
					break;
				}

				case FLOAT: {
					if (! matchesFloat(args[i])) {
						logger.debug("Argument [" + (i + 1) + "/" + args.length + "] ( = " + args[i] + ") is not type " + FLOAT);
						return false;
					}
					break;
				}

				case DOUBLE: {
					if (! matchesDouble(args[i])) {
						logger.debug("Argument [" + (i + 1) + "/" + args.length + "] ( = " + args[i] + ") is not type " + DOUBLE);
						return false;
					}
					break;
				}
			}
		}

		return true;
	}

	private boolean matchesChar(String val) {
		return val.length() == 1;
	}

	private boolean matchesInt(String val) {
		return IntegerValidator.getInstance().isValid(val);
	}

	private boolean matchesLong(String val) {
		return LongValidator.getInstance().isValid(val);

	}

	private boolean matchesFloat(String val) {
		return FloatValidator.getInstance().isValid(val);

	}

	private boolean matchesDouble(String val) {
		return DoubleValidator.getInstance().isValid(val);
	}

}
