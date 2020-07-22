package net.lockoil.pimodbusmaster;

import net.lockoil.pimodbusmaster.model.modbustypes.*;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.util.Pair;

public class ModbusTypesTest {
	
	SignedInt signedInt;
	UnsignedInt unsignedInt;
	FloatModbus floatModbus;
	BitTypeModbus bitTypeModbus;
	VarTypeModbus varTypeModbus;
	BitTypeLegend bitTypeLegend;
	
	@Before
	public void setValues() {
		signedInt = new SignedInt(-5000);
		unsignedInt = new UnsignedInt(-5000);
		floatModbus = new FloatModbus(Pair.of(4, 5));
		
		List<VarTypeLegend> legend= new ArrayList<>();
		
		legend.add(new VarTypeLegend(100, "Нормальная работа"));
		legend.add(new VarTypeLegend(200, "Ошибка"));
		legend.add(new VarTypeLegend(300, "Принудительно включен"));
		varTypeModbus = new VarTypeModbus(legend, 200);
		
		List<BitTypeLegend> bitTypeLegends = new ArrayList<>();
		
		BitTypeLegend bitTypeLegend = new BitTypeLegend();
		bitTypeLegend.setStartBit(0);
		bitTypeLegend.setBitQuantity(2);
		bitTypeLegend.setDescription("Питание");
		List<String> possibleValues = new ArrayList<String>();
		possibleValues.add("Вкл");
		possibleValues.add("Выкл");
		possibleValues.add("Ошибка");
		bitTypeLegend.setPossibleValues(possibleValues);
		
		BitTypeLegend bitTypeLegend2 = new BitTypeLegend();
		bitTypeLegend2.setStartBit(2);
		bitTypeLegend2.setBitQuantity(1);
		bitTypeLegend2.setDescription("Был перезагружен");
		List<String> possibleValues2 = new ArrayList<String>();
		possibleValues2.add("Нет");
		possibleValues2.add("Да");	
		bitTypeLegend2.setPossibleValues(possibleValues2);
		
		bitTypeLegends.add(bitTypeLegend);
		bitTypeLegends.add(bitTypeLegend2);
		bitTypeModbus = new BitTypeModbus(bitTypeLegends, 5);
	}
	
	@Test
	public void testReadSignedInt() {		
		assertEquals(new Integer(-5000), signedInt.readValue());
	}
	
	@Test
	public void testReadUnsignedInt() {		
		assertEquals(new Integer(60536), unsignedInt.readValue());
	}
	
	@Test
	public void testReadFloatModbus() {		
		assertEquals(new Double(3.67349e-40), floatModbus.readValue());
	}
	
	@Test
	public void testWriteFloatModbus() {	
		floatModbus.writeValue(1.8367);	
		assertEquals(Pair.of(0x18FC, 0x3FEB), floatModbus.getValue());
	}
	
	@Test
	public void testReadVarModbus() {
		assertEquals("Ошибка", varTypeModbus.readValue());
	}
	
	@Test
	public void testReadAfterWriteVarModbus() {
		varTypeModbus.writeValue(300);
		assertEquals("Принудительно включен", varTypeModbus.readValue());
	}
	
	@Test 
	public void testBitTypeRead() {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("Питание", "Ошибка");
		valuesMap.put("Был перезагружен", "Да");
		assertEquals(valuesMap, bitTypeModbus.readValue());
	}
	
	@Test 
	public void testBitTypeReadAfterWrite() {
		Map<String, String> valuesMap = new HashMap<>();
		bitTypeModbus.writeValue(2);
		valuesMap.put("Питание", "Выкл");
		valuesMap.put("Был перезагружен", "Нет");
		assertEquals(valuesMap, bitTypeModbus.readValue());
	}

	
	
	
}
