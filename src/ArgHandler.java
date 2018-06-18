import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Tells you which valid arg configuration is given.
 */
public class ArgHandler {
	private final Map<ArgConfiguration, Method> configurations;

	ArgHandler(Map<ArgConfiguration, Method> configurations) throws IllegalArgumentException {
		ArgConfiguration[] argConfigurations = new ArgConfiguration[configurations.size()];
		checkArgConfigurations(configurations.keySet().toArray(argConfigurations));
		this.configurations = configurations;
	}

	private static void checkArgConfigurations(ArgConfiguration[] configurations) throws IllegalArgumentException {
		for (int i = 0; i < configurations.length; i++) {
			for (int j = i + 1; j < configurations.length; j++) {
				if (configurations[i].equals(configurations[j])) {
					throw new IllegalArgumentException("Duplicate configurations found."
							+ System.lineSeparator()
							+ configurations[i].toString() + " [" + i + "]"
							+ System.lineSeparator()
							+ configurations[j].toString() + " [" + j + "]"
					);
				}
			}
		}
	}

	public void run(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		for (Map.Entry<ArgConfiguration, Method> entry : this.configurations.entrySet()) {
			Method method = entry.getValue();

			Object[] formattedArgs = entry.getKey().format(args);

			if (formattedArgs != null) {

				if (method.isVarArgs()) {
					method.invoke(null, new Object[]{formattedArgs});
					return;
				}
				method.invoke(null, formattedArgs);
				return;
			}
		}

		throw new NoSuchMethodException("Method for " + Arrays.toString(args) + " not found.");
	}

}
