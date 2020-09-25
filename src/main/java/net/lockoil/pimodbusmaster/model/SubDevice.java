package net.lockoil.pimodbusmaster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Дочернее устройство
 */
@Entity
@Data
@Table(name = "device")
public class SubDevice {

	/**
	 * id устройства
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	/**
	 * id основного устройства
	 */
	@Column(name = "device_id")
	private Long deviceId;
	
	/**
	 * Название устройства
	 */
	@Column(name = "name")
	private String name;
	
	/**
	 * Адрес устройства
	 */
	@Column(name = "address")
	private Integer address;
	
	/**
	 * Wrapper-функция устройства (Modbus)
	 */
	@Column(name = "function")
	private Integer function;
}
