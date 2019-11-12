package hop2;

import java.util.List;

public class Label {
	int nodeid;
	int dist;
	List<Integer> traversed;
	
	public Label(int nodeid, int dist) {
		this.nodeid = nodeid;
		this.dist = dist;
	}

	@Override
	public String toString() {
		return "Label: " + nodeid + ", " + dist;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ dist;
		result = prime
				* result
				+ nodeid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Label other = (Label) obj;
		if (dist != other.dist)
			return false;
		if (nodeid != other.nodeid)
			return false;
		return true;
	}
	
	
}
