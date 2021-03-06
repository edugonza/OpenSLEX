package org.processmining.openslex.utils;

import java.util.Collection;

public class MMUtils {

	public static final int INFINITE_FUTURE = -1;
	public static final int INFINITE_PAST = -2;
	
	public static String intern(String str) {
		if (str == null) {
			return null;
		} else {
			return str.intern();
		}
	}
	
	public static String str(String str) {
		if (str == null) {
			return "".intern();
		} else {
			return str;
		}
	}
	
	public static boolean before(long a, long b) {
		return (a != b && beforeOrEqual(a,b));
	}
	
	public static boolean after(long a, long b) {
		return (a != b && afterOrEqual(a,b));
	}
	
	public static boolean beforeOrEqual(long a, long b) {
		return ((a <= b && a != INFINITE_FUTURE && b != INFINITE_PAST) ||
				 a == INFINITE_PAST ||
				 b == INFINITE_FUTURE ||
				 a == b);
	}
	
	public static boolean afterOrEqual(long a, long b) {
		return beforeOrEqual(b,a);
	}
	
	public static long latest(long a, long b) {
		if (a == INFINITE_FUTURE || b == INFINITE_FUTURE) {
			return INFINITE_FUTURE;
		} else {
			return Math.max(a,b);
		}
	}
	
	public static long earliest(long a, long b) {
		if (a == INFINITE_PAST || b == INFINITE_PAST) {
			return INFINITE_PAST;
		} else if (a == INFINITE_FUTURE || b == INFINITE_FUTURE) {
			return Math.max(a,b);
		} else {
			return Math.min(a,b);
		}
	}
	
	public static int[] colAsArrayInt(Collection<Integer> list) {
		return list.stream().filter(i -> i != null).mapToInt(i -> i).toArray();
	}
}
