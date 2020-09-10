package net.lockoil.pimodbusmaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.lockoil.pimodbusmaster.model.ReadRequest;
import net.lockoil.pimodbusmaster.model.WriteRequest;
import net.lockoil.pimodbusmaster.service.ModbusRequestService;


//@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200", "http://192.168.88.23:3000" })
@CrossOrigin
@RestController
public class ReadWriteController {

  
  @Autowired
  private ModbusRequestService modbusRequestService;
  
  @ApiOperation(value = "Чтение регистров устройства")
  @PostMapping("/api/modbusread")
  public List<Object> modbusRead(@RequestBody ReadRequest modbusReadRequest) {
	return modbusRequestService.read(modbusReadRequest);
  }
  
  @ApiOperation(value = "Запись регистров устройства")
  @PostMapping("/api/modbuswrite")
  public String modbusWrite(@RequestBody WriteRequest modbusWriteRequest) {
	  return modbusRequestService.write(modbusWriteRequest);
  }

}