package edu.wm.cs.masc.similarity.selector;

import edu.wm.cs.masc.similarity.model.MutationType;

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
