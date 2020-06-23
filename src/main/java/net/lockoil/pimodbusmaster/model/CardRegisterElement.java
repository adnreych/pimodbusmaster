package net.lockoil.pimodbusmaster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "registers")
public class CardRegisterElement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "device_id")
	private Long deviceId;
	
	@Column(name = "register_name")
	private String name;
	
	@Column(name = "address")
	private Integer address;
	
	@Column(name = "register_count")
	private Integer count;
	
	@Column(name = "is_read")
	private Boolean isRead;
	
	@Column(name = "is_write")
	private Boolean isWrite;
	
	@Column(name = "register_type")
	private String type;
	
	@Column(name = "suffix")
	private String suffix;
	
	@Column(name = "multiplier")
	private Long multiplier;
	
	@Column(name = "min_value")
	private Long minValue;
	
	@Column(name = "max_value")
	private Long maxValue;

}
