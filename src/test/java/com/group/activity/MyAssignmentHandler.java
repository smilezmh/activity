package com.group.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


public class MyAssignmentHandler implements TaskListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityApplicationTests.class);
	@Override
	public void notify(DelegateTask delegateTask) {
		 // 在此执行自定义的身份查找

		 String eventName = delegateTask.getEventName();

		 if("create".endsWith(eventName)) 
		 {
			 // 接下来，例如调用以下方法：
			 System.out.println(eventName);
			 delegateTask.setAssignee("kermit");
			 //delegateTask.addCandidateUser("fozzie");
//			 ArrayList<String> list=new ArrayList<String>();
//			 list.add("ljl");
//			 list.add("zjl");
			 delegateTask.addCandidateUser("zjl");
			// HashMap<String,String> map=new HashMap<String,String>();
			 //map.put("mykey", "myvalue");
			 delegateTask.addCandidateGroup("management");
		 }
		 
		 }
}
