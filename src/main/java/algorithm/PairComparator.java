package algorithm;

import java.util.Comparator;

public class PairComparator implements Comparator<Pair> {

	public int compare(Pair o1, Pair o2) {
		// TODO Auto-generated method stub
		if(o1.size>o2.size)return -1;
		else if(o1.size<o2.size)return 1;
		return 0;
	}

}
