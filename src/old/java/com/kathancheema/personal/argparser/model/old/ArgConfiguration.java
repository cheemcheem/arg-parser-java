package java.com.kathancheema.personal.argparser.model.old;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Tells you whether the args given match the arg configuration.
 */
public class ArgConfiguration {
	private final Object[] objects;
	private final Class[] types;
	private final Method[] valueOfMethods;

	public ArgConfiguration(Class[] types, Object[] objects) throws NoSuchMethodException {
		checkArgs(types, objects);
		this.valueOfMethods = getValueMethods(types);
		this.types = types;
		this.objects = objects;
	}

	/**
	 * Method to create array of the "valueOf" methods from each class type.
	 *
	 * @param types Class array containing classes which should have a public "valueOf" method.
	 * @return Method array of each class's "valueOf" methods.
	 * @throws NoSuchMethodException When a given class does not have a "valueOf" method.
	 */
	@SuppressWarnings("unchecked")
	private static Method[] getValueMethods(Class[] types) throws NoSuchMethodException {
		Method[] valueOfMethods = new Method[types.length];
		for (int i = 0; i < types.length; i++) {
			if (types[i] == String.class) continue;
			valueOfMethods[i] = types[i].getDeclaredMethod("valueOf", String.class);
		}
		return valueOfMethods;
	}

	/**
	 * Ensures parameters are not null, different lengths and each @param objects is an instance of the equivalent @param types.
	 *
	 * @param types   Class array.
	 * @param objects Object array.
	 */
	private static void checkArgs(Class[] types, Object[] objects) {

		// Check parameters are not null
		if (types == null || objects == null) {
			throw new IllegalArgumentException("Objects and Types arrays must not be null.");
		}

		// Check parameters are the same length
		if (types.length != objects.length) {
			throw new IllegalArgumentException("Objects and Types must be same length.");
		}

		// Check all objects are an instance of their equivalent type, if they are not null
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null && ! types[i].isInstance(objects[i])) {
				throw new IllegalArgumentException("Object " + objects[i].toString() + " is not an instance of " + types[i].toString() + ".");
			}
		}

	}


	/**
	 * Creates a list of arguments that are not DeclaredParameters.
	 * Will return null if the @param args is not applicable to this ArgConfiguration.
	 * This was done instead of throwing an exception because a failed result is expected here quite a lot as correct
	 * behaviour.
	 *
	 * @param args String array of args handed to the program.
	 * @return Null if args don't match or a non null array of Objects (depending on each type).
	 */
	Object[] format(String[] args) {

		// Return null if args length not equal to this configuration length
		if (valueOfMethods.length != args.length) return null;

		// Instantiate object array of same length as args
		Object[] parsedArgs = new Object[args.length];

		// Try invoking "valueOf" method for each of the types
		try {
			for (int i = 0; i < valueOfMethods.length; i++) {
				if (types[i].equals(DeclaredParameter.class) && ! objects[i].equals(new DeclaredParameter(args[i]))) {
					return null;
				}

				if (types[i] == String.class) {
					parsedArgs[i] = args[i];
					continue;
				}
				parsedArgs[i] = valueOfMethods[i].invoke(objects[i], args[i]);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			// Return null if "valueOf" method throws an exception
			return null;

		}

		// Count number of DeclaredParameter objects in parsedArgs
		int declaredParameterCount = 0;
		for (Object parsedArg : parsedArgs) {
			declaredParameterCount += parsedArg instanceof DeclaredParameter ? 1 : 0;
		}

		// Create formattedArgs array to hold parsedArgs that are not DeclaredParameter objects
		int formattedArgsIndex = 0;
		Object[] formattedArgs = new Object[parsedArgs.length - declaredParameterCount];
		for (Object parsedArg : parsedArgs) {
			if (! (parsedArg instanceof DeclaredParameter)) {
				formattedArgs[formattedArgsIndex++] = parsedArg;

			}
		}

		// Return non DeclaredObject array if all types are matching
		return formattedArgs;
	}


	@Override
	public boolean equals(Object obj) {
		// Return false if not this object type
		if (! (obj instanceof ArgConfiguration)) return false;

		ArgConfiguration actualObj = (ArgConfiguration) obj;

		// Return false if not same amount of args
		if (this.types.length != actualObj.types.length) return false;

		for (int i = 0; i < this.types.length; i++) {
			if (this.types[i] != actualObj.types[i]) {
				// Return false if any args are not matching types
				return false;

			}
			if (this.types[i] == DeclaredParameter.class
					&& ! this.objects[i].equals(actualObj.objects[i])) {
				// Return false if both args are DeclaredParameters and are different
				return false;
			}
		}


		return false;
	}

	@Override
	public String toString() {
		return "ArgConfiguration{" +
				"types=" + (types == null ? null : Arrays.asList(types)) +
				'}';
	}
}
