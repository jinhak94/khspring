package com.kh.spring.demo.model.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Dev {
	
	private int no;
	private String name;
	private int career;
	private String email;
	private String gender;
	private String[] lang;

}
