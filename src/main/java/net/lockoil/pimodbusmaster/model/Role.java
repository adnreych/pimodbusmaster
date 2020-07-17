package net.lockoil.pimodbusmaster.model;

import java.util.Set;

import javax.persistence.Entity;
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
    private Long id;
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


    @Override
    public String getAuthority() {
        return getName();
    }
}