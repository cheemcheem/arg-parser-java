/**
 * For Parameters like "-t" or "--test"
 */
public class DeclaredParameter {
	private final String paramVal;

	DeclaredParameter(String paramVal) {
		this.paramVal = paramVal;
	}


	@SuppressWarnings("unused")
	public DeclaredParameter valueOf(Object val) {
		return new DeclaredParameter(val.toString());
	}

	@SuppressWarnings("unused")
	public DeclaredParameter valueOf(String val) {
		return new DeclaredParameter(val);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof DeclaredParameter && ((DeclaredParameter) obj).paramVal.equals(this.paramVal);
	}
}