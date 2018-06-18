import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Parser {

	public static ArgHandler parseInputFromFile(String location) throws Exception {

		// Read file from scanner
		Scanner scanner = new Scanner(new File(location));

		// Instantiate configuration to method map
		Map<ArgConfiguration, Method> configurationMethodMap = new HashMap<>();

		// Loop through each line of input and parse
		String line;
		while (scanner.hasNextLine() && (line = scanner.nextLine()) != null) {

			// Allow comments in parser
			if (line.startsWith("//")) continue;

			String[] argsInLine = line.split(" ");

			// Check if invalid number of args (need class and method name at least)
			if (argsInLine.length < 2)
				throw new IllegalArgumentException("Invalid line " + configurationMethodMap.size());

			// Create configuration and method objects
			ArgConfiguration configuration = getConfiguration(argsInLine);
			Method method = getMethod(argsInLine);

			// Add configuration and method objects to map
			configurationMethodMap.put(configuration, method);

		}

		scanner.close();
		return new ArgHandler(configurationMethodMap);
	}


	/**
	 * Gets the method object from the line in.
	 * Only allows single public static method.
	 *
	 * @param argsInLine String arguments passed to the program.
	 * @return Method to call with these arguments.
	 * @throws Exception When arguments are invalid (like class or method doesn't exist).
	 */
	private static Method getMethod(String[] argsInLine) throws Exception {
		// Specify here for easy access later
		String className = argsInLine[argsInLine.length - 2];
		String methodName = argsInLine[argsInLine.length - 1];

		// Get class if exists, else throws ClassNotFoundException
		Class classToRunOn = Class.forName(className);

		// Get methods in the class
		Method[] classToRunOnMethods = classToRunOn.getMethods();

		// Find the method whose name matches "methodName"
		Method methodToRun = null;
		for (Method classToRunOnMethod : classToRunOnMethods) {
			if (classToRunOnMethod.getName().equals(methodName)) {
				// Check to make sure only one method matches "methodName", else throw Exception todo pick better exception for duplicate method
				if (methodToRun != null) {
					throw new Exception("Public Method is duplicate " + methodToRun.getName() + " in Class " + classToRunOn.getName() + ".");
				}
				methodToRun = classToRunOnMethod;

			}
		}

		// If no methods match "methodName" then throw Exception todo pick better exception for missing method
		if (methodToRun == null) {
			throw new Exception("Method " + methodName + " in Class " + classToRunOn.getName() + " does not exist.");

		}

		return methodToRun;
	}

	private static ArgConfiguration getConfiguration(String[] argsInLine) throws ClassNotFoundException, NoSuchMethodException {
		// Minus two because last two should be class name and method name
		int numberOfArgs = argsInLine.length - 2;

		Object[] argumentObjects = new Object[numberOfArgs];
		Class[] argumentObjectTypes = new Class[numberOfArgs];

		for (int i = 0; i < numberOfArgs; i++) {
			switch (argsInLine[i]) {
				case "String":
				case "Character":
				case "Integer":
				case "Double":
				case "Float":
					argumentObjects[i] = null;
					argumentObjectTypes[i] = Class.forName("java.lang." + argsInLine[i]);
					break;
				default:
					argumentObjects[i] = new DeclaredParameter(argsInLine[i]);
					argumentObjectTypes[i] = DeclaredParameter.class;
					break;
			}
		}
		return new ArgConfiguration(argumentObjectTypes, argumentObjects);
	}
}
