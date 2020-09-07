package net.lockoil.pimodbusmaster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Устройство
 */
@Entity
@Data
@Table(name = "device")
public class Device {

	/**
	 * id устройства
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	/**
	 * Название устройства
	 */
	@Column(name = "name")
	private String name;
	
	/**
	 * Modbus-адрес устройства
	 */
	@Column(name = "address")
	private Integer address;
	
}
