package common.utils;

import java.util.ArrayList;
import java.util.List;

public class ListAndArray {
	public static <T> List<T> arrayToList(T[] tArray) {
		ArrayList<T> tList = new ArrayList<T>();
		for (T t : tArray) {
			tList.add(t);
		}
		return tList;
	}

	public static Long[] longListToArray(List<Long> longList) {
		int size = longList.size();
		Long[] longArray = new Long[size];

		for (int i = 0; i < size; i++)
			longArray[i] = longList.get(i);

		return longArray;
	}
	
	public static Integer[] integerListToArray(List<Integer> integerList) {
		int size = integerList.size();
		Integer[] integerArray = new Integer[size];

		for (int i = 0; i < size; i++)
			integerArray[i] = integerList.get(i);

		return integerArray;
	}
	
	public static String[] stringListToArray(List<String> stringList) {
		int size = stringList.size();
		String[] stringArray = new String[size];

		for (int i = 0; i < size; i++)
			stringArray[i] = stringList.get(i);

		return stringArray;
	}
}
