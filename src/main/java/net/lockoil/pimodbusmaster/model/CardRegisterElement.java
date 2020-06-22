package net.lockoil.pimodbusmaster.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "registers")
public class CardRegisterElement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "device_id")
	private Long deviceId;
	
	@Column(name = "register_metadata") // определить hstoreType
	private Map<String, String> registerMetadata;
	
	@Column(name = "register_name")
	private String name;
	
	@Column(name = "suffix")
	private String suffix;
	
	@Column(name = "multiplier")
	private Long multiplier;
	
	@Column(name = "min_value")
	private Long minValue;
	
	@Column(name = "max_value")
	private Long maxValue;

}
