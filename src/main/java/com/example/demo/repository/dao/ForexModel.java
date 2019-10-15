package com.example.demo.repository.dao;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Table(name = "rates")
@Entity
public class ForexModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private int id;
	
	@NotNull
	@Column(name = "baseCurrency")
	private String base;
	
	@NotNull
	@Column(name = "dateLastUpdated")
	private String date;
	
	@NotNull
	@Column(name = "timeLastUpdated")
	private String time_last_updated;
	
	@NotNull
	@Column(name = "ratesJson")
	@ElementCollection
	private Map<String, BigDecimal> rates;
	
	public ForexModel() {
		super();
	}
	
	public ForexModel(@NotNull String base, @NotNull String date, @NotNull String time_last_updated,
			@NotNull Map<String, BigDecimal> rates) {
		super();
		this.base = base;
		this.date = date;
		this.time_last_updated = time_last_updated;
		this.rates = rates;
	}

	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime_last_updated() {
		return time_last_updated;
	}
	public void setTime_last_updated(String time_last_updated) {
		this.time_last_updated = time_last_updated;
	}
	public Map<String, BigDecimal> getRates() {
		return rates;
	}
	public void setRates(Map<String, BigDecimal> rates) {
		this.rates = rates;
	}

	@Override
	public String toString() {
		return "ForexModel [id=" + id + ", base=" + base + ", date=" + date + ", time_last_updated=" + time_last_updated
				+ ", rates=" + rates + "]";
	}
}
