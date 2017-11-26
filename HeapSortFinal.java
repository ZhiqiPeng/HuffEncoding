import java.util.ArrayList;

public class HeapSortFinal {
	private ArrayList<Long> array = new ArrayList<>();
	private ArrayList<FreV> list;

	private void ALAdd(FreV v) {
		list.add(v);
		array.add(v.getF());
	}

	private void ALRemove(int position) {
		list.remove(position);
		array.remove(position);
	}

	private void ALSwap(int p1, int p2) {
		long temp = array.get(p1);
		FreV v = list.get(p1);
		array.set(p1, array.get(p2));
		list.set(p1, list.get(p2));
		array.set(p2, temp);
		list.set(p2, v);
	}

	public void storeList(ArrayList<FreV> list) {
		this.list = list;
		storeArray();
	}

	private void storeArray() {// store array will build heap
		array.clear();
		for (FreV v : list) {
			array.add(v.getF());
		}
		buildHeap();
		// System.out.println("Build");
	}

	private void swap(int p1, int p2) {// swap will swap both list and array.
		ALSwap(p1, p2);
	}

	public FreV removeMin() {
		FreV v = list.get(0);
		ALSwap(0, list.size() - 1);
		ALRemove(list.size() - 1);
		downHeap(0);
		return v;
	}

	public void inSert(FreV v) {// insert a num. Will remain Minheap.
		ALAdd(v);
		int parentP = (int) Math.floor((array.size() - 1 - 1) / 2);// parent
																	// position.
		upHeap(parentP);
	}

	private void buildHeap() {
		for (int i = (int) Math.floor((array.size() - 1) / 2); i >= 0; i--) {
			downHeap(i);
		}
	}

	private void upHeap(int parentP) {
		int newPP = oneDownHeapComparison(parentP);// the parent new Position,
													// the old parentP would be
													// inserted num if swapped.
		if (parentP == 0) {
			return;
		}
		if (newPP != parentP) {
			parentP = (int) Math.floor((parentP - 1) / 2);// calculate new
															// parent position.
			upHeap(parentP);
		}
	}

	public int getHeapSize() {
		return list.size();
	}

	private void downHeap(int position) {
		int p1 = 2 * position + 1;
		int newPosition = position;
		while (p1 <= (array.size() - 1)) {
			newPosition = oneDownHeapComparison(position);
			if (newPosition == position) {
				return;
			}
			position = newPosition;
			p1 = 2 * newPosition + 1;
		}
	}

	private int oneDownHeapComparison(int position) {// input father position
														// output swapped
														// position.
		int child1 = 2 * position + 1;
		int child2 = 2 * position + 2;
		long min;
		int newPosition = position;
		if (array.size() - 1 >= child2) {
			min = Math.min(array.get(position), array.get(child1));
			min = Math.min(min, array.get(child2));
			if (array.get(position) > min) {
				newPosition = swapWithMin(min, position, child1, child2);
				return newPosition;
			}
		} else {
			if (array.size() - 1 >= child1) {
				min = Math.min(array.get(position), array.get(child1));
				if (array.get(position) > min) {
					newPosition = swapWithMin(min, position, child1);
					return newPosition;
				}
			}
		}
		return position;
	}

	private int swapWithMin(long minValue, int p1, int p2, int p3) {
		if (minValue == array.get(p2)) {
			swap(p1, p2);
			return p2;
		} else {
			swap(p1, p3);
			return p3;
		}
	}

	private int swapWithMin(long minValue, int p1, int p2) {
		swap(p1, p2);
		return p2;
	}
}
