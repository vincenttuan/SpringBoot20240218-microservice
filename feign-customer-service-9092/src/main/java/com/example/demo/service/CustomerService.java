package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CustomerRepository;
import com.example.demo.model.po.Customer;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	
	// 查詢所有客戶
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}
	
	// 查詢單一客戶
	public Customer findById(Integer id) {
		return customerRepository.findById(id);
	}
	
	// 新增客戶
	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}
	
	// 修改客戶
	public Customer update(Integer id, Customer customer) {
		Customer customerInDb = customerRepository.findById(id);
		if (customerInDb == null) {
			return null;
		}
		customer.setId(id);
		return customerRepository.update(customer);
	}
	
	// 更新 name
	public void updateName(Integer id, String name) {
		customerRepository.updateName(id, name);
	}
	
	// 更新 email
	public void updateEmail(Integer id, String email) {
		customerRepository.updateEmail(id, email);
	}
	
	// 刪除客戶
	public boolean delete(Integer id) {
		return customerRepository.delete(id);
	}
	
}
