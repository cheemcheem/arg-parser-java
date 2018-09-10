package com.kathancheema.personal.commandline.runner.model.runner;

import com.kathancheema.personal.commandline.runner.model.configuration.Config;
import com.kathancheema.personal.commandline.runner.view.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Runner implements Runnable {
	private final Logger logger = LoggerFactory.getLogger("com.kathancheema.personal.commandline.runner");
	private final LoggingUtils loggingHelper = new LoggingUtils(logger);
	private final Config config;
	private final String[] args;

	public Runner(Config config, String[] args) {
		this.config = config;
		this.args = args;
	}

	@Override
	public void run() {
		try {
			final Class thisClass = getClassFromString(config.getTargetClass());

			var classCollection = config.getArgumentTypes();
			var classArray = new Class[classCollection.size()];
			classCollection.toArray(classArray);

			final Method method = getMethodFromString(config.getTargetMethod(), thisClass, classArray);

			invokeMethodWithArgs(args, method);
		} catch (Exception ignored) {

		}
	}


	private void invokeMethodWithArgs(String[] args, Method method) {
		try {
			logger.debug("Invoking Method [" + method + "] with Arguments [" + Arrays.toString(args) + "].");
			method.invoke(null, (Object[]) args);
		} catch (IllegalAccessException e) {
			logger.error("Method [" + method.getName() + "] in Class [" + method.getClass() + "] is not accessible from " + this.getClass().getPackageName() + ".");
			loggingHelper.error(e);
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			logger.error("Method [" + method.getName() + "] in Class [" + method.getClass() + "] threw Exception [" + e.getCause() + "] when invoked.");
			loggingHelper.error(e);
			throw new RuntimeException(e);
		}
	}

	private Method getMethodFromString(String name, Class thisClass, Class[] argTypes) {
		try {
			logger.debug("Finding Method [" + name + "] in Class [" + thisClass + "] with Parameter Types " + Arrays.toString(argTypes) + ".");
			return thisClass.getMethod(name, argTypes);
		} catch (NoSuchMethodException e) {
			logger.error("Class [" + thisClass + "]" + " does not contain Method [" + name + "] with Parameter Types " + Arrays.toString(argTypes) + ".");
			loggingHelper.error(e);
			throw new RuntimeException(e);
		}
	}

	private Class getClassFromString(String declaringClass) {
		try {
			logger.debug("Finding Class [" + declaringClass + "]");
			return Class.forName(declaringClass);
		} catch (ClassNotFoundException e) {
			logger.error("Class [" + declaringClass + "]" + " does not exist in path.");
			loggingHelper.error(e);
			throw new RuntimeException(e);
		}
	}

}
