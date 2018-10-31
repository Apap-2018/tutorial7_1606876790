package com.apap.tutorial7.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.apap.tutorial7.model.CarModel;
import com.apap.tutorial7.model.DealerModel;
import com.apap.tutorial7.rest.Setting;
import com.apap.tutorial7.service.CarService;
import com.apap.tutorial7.service.DealerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * CarController
 */
@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarService carService;

    @Autowired
    RestTemplate restTemplate;
    
    @Bean
    public RestTemplate rest() {
    	return new RestTemplate();
    }
    
    @GetMapping(value = "/model/{namaProdusen}")
    private String getModel(@PathVariable("namaProdusen") String namaProdusen) throws Exception {
    	String path = Setting.carUrl + "/model?factory=" + namaProdusen;
    	return restTemplate.getForEntity(path, String.class).getBody();
    }
    
    @PutMapping(value = "/{id}")
    private String updateCarSubmit (
    		@PathVariable (value = "id") Long id,
    		@RequestParam("brand") String brand,
    		@RequestParam("type") String type,
    		@RequestParam("price") Long price,
    		@RequestParam("amount") Integer amount,
    		@RequestParam("dealerId") Long dealerId) {
    	CarModel car = (CarModel) carService.getCarDetailById(id).get();
    	
    	if(car.equals(null)) {
    		return "Couldn't find your car";
    	}
    	
    	car.setBrand(brand);
    	car.setType(type);
    	car.setPrice(price);
    	car.setAmount(amount);
    	
    	carService.addCar(car);
    	
    	return "car update success";
    }
    
    @PostMapping(value = "/add")
    private CarModel addCarSubmit(@RequestBody CarModel car) {
    	carService.addCar(car);
    	return car;
    }
    
    @GetMapping(value = "/{carId}")
    private CarModel viewCar(@PathVariable ("carId") long carId, Model model) {
    	return carService.getCarDetailById(carId).get();
    }
    
    @GetMapping()
    private List<CarModel> viewAllCar(Model model) {
    	return carService.getAllCar();
    }
    
    @DeleteMapping(value = "/delete")
    private String deleteCar(@RequestParam("carId") long id, Model Model) {
    	CarModel car = carService.getCarDetailById(id).get();
    	carService.deleteCar(car);
    	return "Car has been deleted";
    }
}