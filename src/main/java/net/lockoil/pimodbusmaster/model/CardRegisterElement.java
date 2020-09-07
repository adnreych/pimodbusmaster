package net.lockoil.pimodbusmaster.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import net.lockoil.pimodbusmaster.model.modbustypes.TypeSupportable;

/**
 * Описание регистра
 */
@Entity
@Data
@Table(name = "registers")
public class CardRegisterElement {
	
	/**
	 * id регистра в БД
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, insertable = false)
	private Long id;
	
	/**
	 * Устройство {@link Device} связанное с регистром
	 */
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
	private Device device;
	
	/**
	 * Описание регистра
	 */
	@Column(name = "register_name")
	private String name;
	
	/**
	 * Адрес регистра
	 */
	@Column(name = "address")
	private Integer address;
	
	/**
	 * Количество регистров, следующих после адреса, включая сам адрес
	 */
	@Column(name = "register_count")
	private Integer count;
	
	/**
	 * Возможность чтения регистра
	 */
	@Column(name = "is_read")
	private Boolean isRead;
	
	/**
	 * Возможность записи регистра
	 */
	@Column(name = "is_write")
	private Boolean isWrite;
	
	/**
	 * Тип {@link AbstractModbusType} регистра
	 */
	@Column(name = "register_type")
	private String type;
	
	/**
	 * Описание, которое показывать после значения регистра (например величины кг, сек и т.п.)
	 */
	@Column(name = "suffix")
	private String suffix;
	
	/**
	 * Множитель, на который умножать полученное значение регистра
	 */
	@Column(name = "multiplier")
	private Long multiplier;
	
	/**
	 * Минимальное значение регистра
	 */
	@Column(name = "min_value")
	private Long minValue;
	
	/**
	 * Максимальное значение регистра
	 */
	@Column(name = "max_value")
	private Long maxValue;
	
	/**
	 * Название группы, в которой состоит регистр
	 */
	@Column(name = "register_group")
	private String group;
	
	/**
	 * JSON, в котором хранится более подробная информация о регистре, если в ней есть необходимость
	 */
	@Column(name = "legends", columnDefinition="json")
	private String legends;

}
