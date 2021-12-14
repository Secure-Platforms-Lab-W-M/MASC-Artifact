package edu.wm.cs.mplus.detectors.ast.locator;

import java.util.ResourceBundle;

import edu.wm.cs.mplus.model.MutationType;

public class LocatorFactory {

	private static LocatorFactory instance = null;
	private static ResourceBundle types = null;
	
	
	public LocatorFactory(){
		types = ResourceBundle.getBundle("locator-types");
	}
	
	public static LocatorFactory getInstance() {
	      if(instance == null) {
	         instance = new LocatorFactory();
	      }
	      return instance;
	}
	
	
	public Locator getLocator(MutationType type){
		
		try{
			return (Locator)Class.forName(types.getString(type.getId()+"")).newInstance();
		}catch(Exception ex){
			return null;
		}
	}
	
	
}
