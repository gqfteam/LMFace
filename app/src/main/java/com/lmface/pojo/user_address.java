package com.lmface.pojo;

import java.sql.Timestamp;

public class user_address {
    private Integer addressId;

    private Integer userId;

    private String countries;

    private String provinces;

    private String city;

    private String county;

    private String street;

    private String addressTxt;

    private Timestamp addressTime;

    private String name;

    private String phone;

    public user_address(){
    	
    }
    
    
    public user_address(Integer userId, String countries, String provinces,
			String city, String county, String street, String addressTxt,
			Timestamp addressTime, String name, String phone) {
		super();
		this.userId = userId;
		this.countries = countries;
		this.provinces = provinces;
		this.city = city;
		this.county = county;
		this.street = street;
		this.addressTxt = addressTxt;
		this.addressTime = addressTime;
		this.name = name;
		this.phone = phone;
	}

	public user_address(Integer addressId, Integer userId, String countries,
			String provinces, String city, String county, String street,
			String addressTxt, Timestamp addressTime, String name, String phone) {
		super();
		this.addressId = addressId;
		this.userId = userId;
		this.countries = countries;
		this.provinces = provinces;
		this.city = city;
		this.county = county;
		this.street = street;
		this.addressTxt = addressTxt;
		this.addressTime = addressTime;
		this.name = name;
		this.phone = phone;
	}


	public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries == null ? null : countries.trim();
    }

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces == null ? null : provinces.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street == null ? null : street.trim();
    }

    public String getAddressTxt() {
        return addressTxt;
    }

    public void setAddressTxt(String addressTxt) {
        this.addressTxt = addressTxt == null ? null : addressTxt.trim();
    }

    public Timestamp getAddressTime() {
        return addressTime;
    }

    public void setAddressTime(Timestamp addressTime) {
        this.addressTime = addressTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }
}