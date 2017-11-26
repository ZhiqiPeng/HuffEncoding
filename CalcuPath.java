import java.util.ArrayList;

public class CalcuPath {
	private ArrayList<FreV> list;

	public CalcuPath(ArrayList<FreV> list) {
		this.list = list;
	}

	public void buildTree() {
		HeapSortFinal hs = new HeapSortFinal();
		hs.storeList(list);
		while (hs.getHeapSize() != 1) {
			formANode(hs);
		}

		goThroughTree(list.get(0).getNode());
	}

	private void goThroughTree(FreVNode root) {
		list.clear();
		goThroughEachNode(root);
	}

	private void goThroughEachNode(FreVNode node) {// 0 left 1 right
		if (node.isLN()) {
			FreVNode ln = (FreVNode) node.getL();
			ln.addPathCode(node.getPathCode() + '0');
			goThroughEachNode(ln);
		} else {
			FreV lv = (FreV) node.getL();
			lv.setHuff(node.getPathCode() + '0');
			list.add(lv);
		}

		if (node.isRN()) {
			FreVNode rn = (FreVNode) node.getR();
			rn.addPathCode(node.getPathCode() + '1');
			goThroughEachNode(rn);
		} else {
			FreV rv = (FreV) node.getR();
			rv.setHuff(node.getPathCode() + '1');
			list.add(rv);
		}
	}

	private void formANode(HeapSortFinal hs) {
		FreV v2 = hs.removeMin();
		FreV v1 = hs.removeMin();
		FreVNode node;
		if (v1.isPointer()) {
			if (v2.isPointer()) {
				node = new FreVNode(v1.getNode(), v2.getNode());
			} else {
				node = new FreVNode(v1.getNode(), v2);
			}
		} else {
			if (v2.isPointer()) {
				node = new FreVNode(v1, v2.getNode());
			} else {
				node = new FreVNode(v1, v2);
			}
		}
		FreV v = new FreV(node);
		hs.inSert(v);
	}
}
