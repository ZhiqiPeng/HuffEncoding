public class FreV {
	private long frequency;
	private byte value;
	private String huffCode;
	private char charValue;
	private FreVNode pointer;

	public FreV(byte value) {
		this.value = value;
		this.frequency = 1;
		charValue = (char) value;
	}

	public FreV(char charValue) {
		this.charValue = charValue;
		value = (byte) charValue;
		this.frequency = 1;
	}

	public FreV(String value) {// String is the ASCII code for value, not A, B,
								// C....
		int i = Integer.parseInt(value);
		this.value = (byte) i;
		this.charValue = (char) this.value;
		this.frequency = 1;
	}

	public FreV(FreVNode pointer) {
		this.pointer = pointer;
	}

	public void setF(long newFre) {
		frequency = newFre;
	}

	public long getF() {
		if (pointer == null) {
			return frequency;
		} else {
			return pointer.getFreSum();
		}
	}

	public void setHuff(String code) {
		huffCode = code;
	}

	public String getHuff() {
		return huffCode;
	}

	public byte getV() {
		return value;
	}

	public char getCharV() {
		return charValue;
	}
	public boolean isPointer(){
		return pointer!=null;
	}

	public FreVNode getNode() {
		return pointer;
	}
}
