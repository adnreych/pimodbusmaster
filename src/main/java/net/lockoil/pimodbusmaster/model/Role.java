package net.lockoil.pimodbusmaster.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
@Entity
@Table(name = "t_role")
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
    private Long id;
	
	@Column(name = "r_name")
    private String name;
    
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    
    public Role() {
    }

    public Role(Long id) {
        this.id = id;
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Role(String name) {
        this.name = name;
    }


    @Override
    public String getAuthority() {
        return getName();
    }
}