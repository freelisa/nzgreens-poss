package com.nzgreens.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei on 2017/5/3.
 */
public class CollectionUtil {

	public static  <T> List<List<T>> splitList(List<T> list, int pageSize) {
		int listSize = list.size();
		int page = (listSize + (pageSize - 1)) / pageSize;
		List<List<T>> listArray = new ArrayList<>();
		for (int i = 0; i < page; i++) {
			List<T> subList = new ArrayList<>();
			for (int j = 0; j < listSize; j++) {
				int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
				if (pageIndex == (i + 1)) {
					subList.add(list.get(j));
				}
				if ((j + 1) == ((j + 1) * pageSize)) {
					break;
				}
			}
			listArray.add(subList);
		}
		return listArray;
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		List<List<String>> list2 = splitList(list,2);
		System.out.println(list2);
	}
}
