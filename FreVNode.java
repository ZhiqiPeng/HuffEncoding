
public class FreVNode {
	private FreVNode lchil;
	private FreVNode rchil;
	private FreV lv;
	private FreV rv;
	private Long freSum;
	private String pathCode = "";

	public FreVNode(FreV lv, FreV rv) {
		this.lv = lv;
		this.rv = rv;
		freSum = (long) (lv.getF() + rv.getF());
	}

	public FreVNode(FreV lv, FreVNode rchil) {
		this.lv = lv;
		this.rchil = rchil;
		freSum = lv.getF() + rchil.getFreSum();
	}

	public FreVNode(FreVNode lchil, FreV rv) {
		this.lchil = lchil;
		this.rv = rv;
		freSum = lchil.getFreSum() + rv.getF();
	}

	public FreVNode(FreVNode lchil, FreVNode rchil) {
		this.lchil = lchil;
		this.rchil = rchil;
		freSum = lchil.getFreSum() + rchil.getFreSum();
	}

	public Long getFreSum() {
		return freSum;
	}

	public boolean isLN() {
		return lchil != null;
	}

	public boolean isRN() {
		return rchil != null;
	}

	public Object getL() {
		if (isLN()) {
			return lchil;
		} else {
			return lv;
		}
	}

	public Object getR() {
		if (isRN()) {
			return rchil;
		} else {
			return rv;
		}
	}

	public void addPathCode(String c) {
		pathCode = pathCode + c;
	}

	public String getPathCode() {
		return pathCode;
	}

}
