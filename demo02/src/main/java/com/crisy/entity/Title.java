package com.crisy.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
//��ָ�������������˵�����ǵ��಻���ڸ��ֹ����ʱ�򣨲�����@Controller��@Services�ȵ�ʱ�򣩣����ǾͿ���ʹ��@Component����ע����ࡣ
public class Title {
	@Value("${com.crisy.title}")
	private String title;
	@Value("${com.crisy.description}")
	private String description;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
