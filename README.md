# arg-parser
### What is is
This library help you to easily map command line argument configurations to Java Public Static methods. 

This is done by adding mappings in a separate configuration file.



### Benefits
* Does not require modification or recompilation of existing code to change functionality of application.
* Easy abstraction of application funcationality.


### Usage
#### Compile:
```bash
jar cf ArgParser.jar src
```

#### Use:
```java
import ArgParser.Parser;

class Main {
	public static void java(String[] args){
		// Read 'inputs/Arguments.args' and run configuration that matches 'args'
		Parser.parseInputFromFile("inputs/Arguments.args").run(args);
	}
}
```


### Limitations



### License
See `LICENSE.txt