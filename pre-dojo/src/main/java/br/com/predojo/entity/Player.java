package br.com.predojo.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Player {
	
	private String name;
	private Integer amountMurder = 0;
	private Integer amountDeath = 0;
	private Date firstMurder;
	private Date lastMurder;
	private Date whenDeath;
	private Map<String, Integer> weaponOfChoiceMap = new HashMap<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAmountMurder() {
		return amountMurder;
	}
	
	public void setAmountMurder(Integer amountMurder) {
		this.amountMurder = amountMurder;
	}
	
	public Integer getAmountDeath() {
		return amountDeath;
	}
	
	public void setAmountDeath(Integer amountDeath) {
		this.amountDeath = amountDeath;
	}
	
	public Date getFirstMurder() {
		return firstMurder;
	}
	
	public void setFirstMurder(Date firstMurder) {
		this.firstMurder = firstMurder;
	}
	
	public Date getLastMurder() {
		return lastMurder;
	}
	
	public void setLastMurder(Date lastMurder) {
		this.lastMurder = lastMurder;
	}
	
	public Date getWhenDeath() {
		return whenDeath;
	}
	
	public void setWhenDeath(Date whenDeath) {
		this.whenDeath = whenDeath;
	}

	public Map<String, Integer> getWeaponOfChoiceMap() {
		return weaponOfChoiceMap;
	}

	public void setWeaponOfChoiceMap(Map<String, Integer> weaponOfChoiceMap) {
		this.weaponOfChoiceMap = weaponOfChoiceMap;
	}

	
}
