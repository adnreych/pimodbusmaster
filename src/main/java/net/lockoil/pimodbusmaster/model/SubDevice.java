package net.lockoil.pimodbusmaster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * Дочернее устройство
 */
@Entity
@Data
@Table(name = "sub_device")
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
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
	private Device device;
	
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
