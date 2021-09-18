package edu.wm.cs.mplus.selector;

import edu.wm.cs.mplus.model.MutationType;

public class SelectorLocationFactory {

	private static SelectorLocationFactory instance = null;
	
	public static SelectorLocationFactory getInstance() {
	      if(instance == null) {
	         instance = new SelectorLocationFactory();
	      }
	      return instance;
	}
	
	
	public Selector getSelector(MutationType type){
		
		if(type.equals(MutationType.WRONG_MAIN_ACTIVITY)){
			//TODO specific selector
		} 
		
		return new GeneralRandomSelector();
	}
	
	
}
