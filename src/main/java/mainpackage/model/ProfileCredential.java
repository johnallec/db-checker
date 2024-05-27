package mainpackage.model;

import java.util.Objects;

public class ProfileCredential {
	
	private String name;
	private String url;
	private String username;
	private String password;
	
	public ProfileCredential() {}
	
	public ProfileCredential(String name, String url, String username, String password) {
		super();
		this.name = name;
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String toString() {
		return this.name + "%" + this.url + "%" + this.username + "%" + this.password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, password, url, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		return this.name.equals(((ProfileCredential) obj).name);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}
