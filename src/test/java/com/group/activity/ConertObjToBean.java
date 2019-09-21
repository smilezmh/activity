package com.group.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;



public  class ConertObjToBean<T>
{
	private String className;
	private  T t;
	
	public ConertObjToBean(T t) 
	{
		className=t.getClass().getName();
		this.t=t;
	}
	
	
	public  T GetBean(Object obj) throws ReflectiveOperationException, JSONException
	{
	JSONObject jb= new JSONObject(obj.toString());
	
	//  通过反射获取字段
    Class<?> fclass=Class.forName(className);// 获取类
    Field[] fs= fclass.getDeclaredFields();// 获取字段
    
    for(Field f :  fs) 
    {
    	f.setAccessible(true);
        f.set(this.t, jb.get(f.getName()));
    }

    return t;
	}
}
