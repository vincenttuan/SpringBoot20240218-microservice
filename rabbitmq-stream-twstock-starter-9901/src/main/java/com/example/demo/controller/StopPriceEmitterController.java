package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.PriceEmitterService;

@CrossOrigin
@RestController
public class StopPriceEmitterController {
	
	@Autowired
	private PriceEmitterService priceEmitterService;
	
	@GetMapping("/stop")
	public String stop() {
		priceEmitterService.stop();
		return "stop";
	}
	
}
