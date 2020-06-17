package net.lockoil.pimodbusmaster.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.log4j.Log4j2;
import net.lockoil.pimodbusmaster.model.ReadRequest;
import net.lockoil.pimodbusmaster.service.ModbusRequest;


//@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200", "http://192.168.88.23:3000" })
@CrossOrigin
@RestController
public class CourseResource {

  
  @Autowired
  private ModbusRequest modbusRequest;
  
  @GetMapping("/modbusread")
  public List<String> getAll() {
	return modbusRequest.courses;
  }
  
  /*@GetMapping("/index.html")
  public String getMain() {
	return "index";
  } */
  
  @PostMapping("/modbusread")
  public ResponseEntity<String> getResponse(@RequestBody ReadRequest modbusReadRequest) {
	String response = modbusRequest.read(modbusReadRequest).toString();
	String result = response != null ? response : "Ошибка чтения данных";
	modbusRequest.courses.add(result);
	URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/").buildAndExpand(result)
	        .toUri();
	return ResponseEntity.created(uri).build();
  }

}