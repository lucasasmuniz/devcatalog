package com.lucasasmuniz.devcatalog.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lucasasmuniz.devcatalog.entities.Product;
import com.lucasasmuniz.devcatalog.projections.IdProjection;
import com.lucasasmuniz.devcatalog.projections.ProductProjection;

public class Utils {

	public static <ID> List<? extends IdProjection<ID>> orderListByReference(List<? extends IdProjection<ID>> ordered, List<? extends IdProjection<ID>> unordered){
		
		List<IdProjection<ID>> result = new ArrayList<>();
		
		Map<ID, IdProjection<ID>> map = new HashMap<>();
		for(IdProjection<ID> obj : unordered) {
			map.put(obj.getId(), obj);
		}
		
		for(IdProjection<ID> obj:ordered) {
			result.add(map.get(obj.getId()));
		}
		
		return result;
	}
}
