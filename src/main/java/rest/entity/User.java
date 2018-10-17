package rest.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import core.entity.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@Entity
@Table()
public class User extends EntityBase{
	private static final long serialVersionUID = 8542522994899388913L;
	
	private String name;
	private String email;
	private String phone;
	private String city;
	private Date birthDate;
	private double salary;
}
