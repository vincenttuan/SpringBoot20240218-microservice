package com.example.demo.model.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item { // 訂單明細
	private Integer id; // 訂單明細(項目)編號
	private Integer orderId; // 訂單主檔編號
	private Integer productId; // 產品編號
	private Integer quantity; // 訂購數量
	private Integer subtotal; // 小計 
}
