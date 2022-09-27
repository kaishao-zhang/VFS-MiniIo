package com.zyk.xml.bean;

/**
 * @author 凯少
 * @create 2022-09-23 15:33
 */
public class Address {
	private String address;
	private Integer doorId;
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getDoorId() {
		return doorId;
	}

	public void setDoorId(Integer doorId) {
		this.doorId = doorId;
	}

//	@Override
//	public String toString() {
//		return "Address{" +
//				"address='" + address + '\'' +
//				", doorId=" + doorId +
//				", user=" + user +
//				'}';
//	}
}
