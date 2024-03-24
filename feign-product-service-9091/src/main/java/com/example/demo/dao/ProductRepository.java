package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.po.Product;

@Repository
public class ProductRepository {
	// 利用 ArrayList 儲存商品資料
	private static List<Product> products = new ArrayList<>();
	
	static {
		// 初始化商品資料
		products.add(new Product(1, "Apple", 10, 5, 100));
		products.add(new Product(2, "Banana", 20, 8, 200));
		products.add(new Product(3, "Cherry", 30, 12, 300));
	}
	
	// 查詢全部
	public List<Product> findAll() {
		return products;
	}
	
	// 查詢單筆
	public Optional<Product> findById(Integer id) {
		return products.stream().filter(product -> product.getId().equals(id)).findFirst();
	}
	
	// 商品儲存
	public Product save(Product product) {
		// 建立商品 id
		// 找到當前最大的ID並加1設置為新產品的ID
		int maxId = products.stream()
								.mapToInt(Product::getId) // 獲取所有商品的 id
								.max()
								.orElse(0); // 若沒找到最大值就返回 0
		// 設定商品 id
		product.setId(maxId + 1);	
		// 存檔
		products.add(product);
		return product;
	}
	
	// 商品修改
	public Product update(Product product) {
		for(Product p : products) {
			if(p.getId().equals(product.getId())) {
				p.setName(product.getName());
				p.setPrice(product.getPrice());
				p.setQuantity(product.getQuantity());
				return product;
			}
		}
		return null;
	}
	
	// 庫存置換
	public Boolean replaceQuantity(Integer id, Integer quantity) {
		for(Product p : products) {
			if(p.getId().equals(id)) {
				p.setQuantity(quantity);
				return true;
			}
		}
		return false;
	}
	
	// 庫存調整:增/減
	// amount: 會有 + -
	public boolean adjustQuantity(Integer id, Integer amount) {
		for(Product p : products) {
			if(p.getId().equals(id)) {
				// 調整後數量
				int lastQuantity = p.getQuantity() + amount;
				if(lastQuantity < 0) {
					return false;
				}
				p.setQuantity(lastQuantity);
				return true;
			}
		}
		return false;
	}
	
	// 刪除商品
	public Boolean delete(Integer id) {
		Optional<Product> productOpt = findById(id);
		if(productOpt.isPresent()) {
			return products.remove(productOpt.get());
		}
		return false;
	}
}










