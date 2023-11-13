package com.shopping.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderColumn;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerOrderMapping {

	@Id
	private Long customerId;
	
	@ElementCollection
	@OrderColumn
	@Cascade({CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Long> orderIds=new ArrayList<>();
}
