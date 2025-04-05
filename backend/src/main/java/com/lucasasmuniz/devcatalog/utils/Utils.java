package com.lucasasmuniz.devcatalog.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lucasasmuniz.devcatalog.entities.Product;
import com.lucasasmuniz.devcatalog.projections.ProductProjection;

public class Utils {

	public static List<Product> orderListByReference(List<ProductProjection> ordered, List<Product> unordered){
		
		List<Product> result = new ArrayList<>();
		
		Map<Long, Product> map = new HashMap<>();
		for(Product obj : unordered) {
			map.put(obj.getId(), obj);
		}
		
		for(ProductProjection obj:ordered) {
			result.add(map.get(obj.getId()));
		}
		
		return result;
	}
}
