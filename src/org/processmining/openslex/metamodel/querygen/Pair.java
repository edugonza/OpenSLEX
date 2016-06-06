package org.processmining.openslex.metamodel.querygen;

public class Pair<T> {
	
	T a,b = null;
	
	public Pair(T a, T b) {
		this.a = a;
		this.b = b;
	}
}