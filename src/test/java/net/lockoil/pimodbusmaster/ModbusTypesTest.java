package net.lockoil.pimodbusmaster;

import net.lockoil.pimodbusmaster.model.modbustypes.*;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
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
	BitTypeModbus bitTypeModbusZero;
	VarTypeModbus varTypeModbus;
	BitTypeLegend bitTypeLegend;
	BoxTypeModbus boxTypeModbus;
	MultipleTypeModbus multipleTypeModbusOneArg;
	MultipleTypeModbus multipleTypeModbusThreeArgs;
	MultipleTypeModbus multipleTypeModbusThreeArgsFloat;
	
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
		List<BitTypeLegend> bitTypeLegendsZero = new ArrayList<>();
		
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
		
		BitTypeLegend bitTypeLegendZero1 = new BitTypeLegend();
		bitTypeLegendZero1.setStartBit(0);
		bitTypeLegendZero1.setBitQuantity(1);
		bitTypeLegendZero1.setDescription("Состояние входа 1");
		List<String> possibleValuesZero1 = new ArrayList<String>();
		possibleValuesZero1.add("Выкл");
		possibleValuesZero1.add("Вкл");	
		bitTypeLegendZero1.setPossibleValues(possibleValuesZero1);
		
		BitTypeLegend bitTypeLegendZero2 = new BitTypeLegend();
		bitTypeLegendZero2.setStartBit(1);
		bitTypeLegendZero2.setBitQuantity(1);
		bitTypeLegendZero2.setDescription("Состояние входа 2");
		List<String> possibleValuesZero2 = new ArrayList<String>();
		possibleValuesZero2.add("Выкл");
		possibleValuesZero2.add("Вкл");	
		bitTypeLegendZero2.setPossibleValues(possibleValuesZero2);
		
		bitTypeLegendsZero.add(bitTypeLegendZero1);
		bitTypeLegendsZero.add(bitTypeLegendZero2);
		bitTypeModbusZero = new BitTypeModbus(bitTypeLegendsZero, 0);
		
		bitTypeLegends.add(bitTypeLegend);
		bitTypeLegends.add(bitTypeLegend2);
		bitTypeModbus = new BitTypeModbus(bitTypeLegends, 5);
		
		boxTypeModbus = new BoxTypeModbus(Pair.of(bitTypeModbus, new SignedInt(5)));
		
		List<Integer> hexStringArrayList = Arrays.asList(76, 90, 13, 84);  // LZqT
		multipleTypeModbusOneArg = new MultipleTypeModbus(hexStringArrayList);
		
		String single = "UnsignedInt";
		multipleTypeModbusThreeArgs = new MultipleTypeModbus(hexStringArrayList, single, "");
		
		single = "Float";
		multipleTypeModbusThreeArgsFloat = new MultipleTypeModbus(hexStringArrayList, single, "");
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
		assertEquals(new Integer(200), varTypeModbus.readValue());
	}
	
	@Test
	public void testReadAfterWriteVarModbus() {
		varTypeModbus.writeValue(300);
		assertEquals(new Integer(300), varTypeModbus.readValue());
	}
	
	@Test 
	public void testBitTypeRead() {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("Питание", "Ошибка");
		valuesMap.put("Был перезагружен", "Да");
		assertEquals(valuesMap, bitTypeModbus.readValue());
	}
	
	@Test 
	public void testBitTypeZeroRead() {
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put("Состояние входа 1", "Выкл");
		valuesMap.put("Состояние входа 2", "Выкл");
		assertEquals(valuesMap, bitTypeModbusZero.readValue());
	}
	
	@Test 
	public void testBitTypeReadZeroAfterWrite() {
		Map<String, String> valuesMap = new HashMap<>();
		bitTypeModbus.writeValue(0);
		valuesMap.put("Состояние входа 1", "Выкл");
		valuesMap.put("Состояние входа 2", "Выкл");
		assertEquals(valuesMap, bitTypeModbusZero.readValue());
	}
	
	@Test 
	public void testBitTypeReadAfterWrite() {
		Map<String, String> valuesMap = new HashMap<>();
		bitTypeModbus.writeValue(2);
		valuesMap.put("Питание", "Выкл");
		valuesMap.put("Был перезагружен", "Нет");
		assertEquals(valuesMap, bitTypeModbus.readValue());
	}
	
	@Test 
	public void testBoxTypeReadFirst() {
		assertEquals(bitTypeModbus.readValue(), boxTypeModbus.readValue().getFirst());
	}
	
	@Test 
	public void testBoxTypeReadSecond() {
		assertEquals(5, boxTypeModbus.readValue().getSecond());
	}
	
	@Test 
	public void testMultipleOneArgRead() {
		assertEquals("LZqT", multipleTypeModbusOneArg.readValue().get(0));
	}
	
	@Test 
	public void testMultipleThreeArgsIntRead() {
		assertEquals(Arrays.asList(76, 90, 13, 84), multipleTypeModbusThreeArgs.readValue().get(0));
	}
	
	@Test 
	public void testMultipleThreeArgsFloatRead() {
		assertEquals(Arrays.asList(4.25322, 7.00089), multipleTypeModbusThreeArgsFloat.readValue().get(0));
	}

	
	
	
}
