package com.zyk.xml.bean;

/**
 * @author 凯少
 * @create 2022-09-23 10:13
 */
public class User {
	private String userName;
	private Integer age;
	private Address address;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

//	@Override
//	public String toString() {
//		return "User{" +
//				"userName='" + userName + '\'' +
//				", age=" + age +
//				", address=" + address +
//				'}';
//	}
}
