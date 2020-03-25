package com.example.demo.repository.dao;

import java.util.List;

public class ForexModel {

	private int id;

	private String base;

	private String date;

	private String time_last_updated;

	private List<ForexRates> rates;

	public ForexModel() {
		super();
	}

	public ForexModel(String base, String date, String time_last_updated, List<ForexRates> rates) {
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

	public List<ForexRates> getRates() {
		return rates;
	}

	public void setRates(List<ForexRates> rates) {
		this.rates = rates;
	}

	@Override
	public String toString() {
		return "ForexModel [id=" + id + ", base=" + base + ", date=" + date + ", time_last_updated=" + time_last_updated
				+ ", rates=" + rates + "]";
	}
}
