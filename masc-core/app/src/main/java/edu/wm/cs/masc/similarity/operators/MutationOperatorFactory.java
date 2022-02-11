package edu.wm.cs.masc.similarity.operators;

import java.util.ResourceBundle;

public class MutationOperatorFactory {

	private static MutationOperatorFactory instance = null;
	private static ResourceBundle types = null;
	
	protected  MutationOperatorFactory(){
		types = ResourceBundle.getBundle("operator-types");
	}
	
	public static MutationOperatorFactory getInstance() {
	      if(instance == null) {
	         instance = new MutationOperatorFactory();
	      }
	      return instance;
	}
	
	
	public MutationOperator getOperator(int code){
		
		try{
			return (MutationOperator)Class.forName(types.getString(code+"")).newInstance();
		}catch(Exception ex){
			return null;
		}
		
		
	}
	
	
	/*public static void main(String[] args){
		MutationOperator operator = null;
		for(int i = 1; i <= 36; i++){
			operator =  MutationOperatorFactory.getInstance().getOperator(i);
			System.out.println(operator.getClass().getName());
		}
	}
	*/
}
