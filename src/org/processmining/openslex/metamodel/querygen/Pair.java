package org.processmining.openslex.metamodel.querygen;

public class Pair<T> {
	
	public T a,b = null;
	
	public Pair(T a, T b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		} else if (obj instanceof Pair<?>) {
			return this.a.equals(((Pair<T>) obj).a) && this.b.equals(((Pair<T>) obj).b);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		String str = a.hashCode() + "#" + b.hashCode();
		return str.hashCode();
	}
}