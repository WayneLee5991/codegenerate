/**
 * 
 */
package com.yuyi.model;

/**
* <p>Title:Table </p>
* <p>Description: </p>
* <p>Company: </p> 
* @author liwenq
* @date 2018年4月4日下午11:14:23
*/
public class Table {
	
	private String panName;
	
	private String author;
	
	private String poName;
	
	private String daoName;
	
	private String serviceName;
	
	private String serviceImplName;
	
	private String controllerName;

	public String getPanName() {
		return panName;
	}

	public void setPanName(String panName) {
		this.panName = panName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPoName() {
		return poName;
	}

	public void setPoName(String poName) {
		this.poName = poName;
	}

	public String getDaoName() {
		return daoName;
	}

	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceImplName() {
		return serviceImplName;
	}

	public void setServiceImplName(String serviceImplName) {
		this.serviceImplName = serviceImplName;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	@Override
	public String toString() {
		return "Table [panName=" + panName + ", author=" + author + ", poName=" + poName + ", daoName=" + daoName
				+ ", serviceName=" + serviceName + ", serviceImplName=" + serviceImplName + ", controllerName="
				+ controllerName + "]";
	}

}
