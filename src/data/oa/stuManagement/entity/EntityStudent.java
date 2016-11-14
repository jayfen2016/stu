package data.oa.stuManagement.entity ;

import data.framework.support.AbstractEntity ;

/**
 * 学生实体
 */
public class EntityStudent extends AbstractEntity {
    
	private String id;
	private String name;
	private String phone;
	private int sex;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	
	
	
	
}