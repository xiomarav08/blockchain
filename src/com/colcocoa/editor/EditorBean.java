package com.colcocoa.editor;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "editor")
public class EditorBean {
	private String value = "";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
