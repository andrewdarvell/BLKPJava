package ru.darvell.blkp.lastfm.model;

/**
 * Created with IntelliJ IDEA.
 * User: darvell
 * Date: 15.07.14
 * Time: 17:36
 * Class store info auth auth response
 */
public class AuthResp {
	private String name;
	private String key;
	private int subscriber;

	public AuthResp(String name, String key, int subscriber) {
		this.setName(name);
		this.setKey(key);
		this.setSubscriber(subscriber);
	}

	public AuthResp(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(int subscriber) {
		this.subscriber = subscriber;
	}

	@Override
	public String toString() {
		return "AuthResp{" +
				"name='" + getName() + '\'' +
				", key='" + getKey() + '\'' +
				", subscriber=" + getSubscriber() +
				'}';
	}
}
