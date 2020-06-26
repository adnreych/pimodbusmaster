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
import net.lockoil.pimodbusmaster.model.ReadResponse;
import net.lockoil.pimodbusmaster.model.WriteRequest;
import net.lockoil.pimodbusmaster.service.ModbusRequestService;


//@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200", "http://192.168.88.23:3000" })
@CrossOrigin
@RestController
public class ReadWriteController {

  
  @Autowired
  private ModbusRequestService modbusRequestService;
  
  @GetMapping("/modbusread")
  public List<String> getAll() {
	return modbusRequestService.courses;
  }

  @PostMapping("/modbusread")
  public List<ReadResponse> modbusRead(@RequestBody ReadRequest modbusReadRequest) {
	return modbusRequestService.read(modbusReadRequest);
  }
  
  @PostMapping("/api/modbuswrite")
  public void modbusWrite(@RequestBody WriteRequest modbusWriteRequest) {
	modbusRequestService.write(modbusWriteRequest);
  }

}