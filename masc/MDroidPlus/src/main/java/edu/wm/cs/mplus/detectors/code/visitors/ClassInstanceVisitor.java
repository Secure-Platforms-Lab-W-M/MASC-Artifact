package edu.wm.cs.mplus.detectors.code.visitors;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;

public class ClassInstanceVisitor extends ASTVisitor{

	private HashSet<String> targetCalls;
	private HashSet<MethodCallVO> calls;

	public ClassInstanceVisitor(){
		calls = new HashSet<MethodCallVO>();
		targetCalls = new HashSet<>();
	}
	
 public boolean visit(ClassInstanceCreation call){
		
	
		
		IMethodBinding bind = null;		
		String className = null;
		
		String methodName = "<init>";

	 	System.out.println("debug-classinstance: "+call.getType().toString());
		try{
			if(call.getType() != null && call.getType().toString().equals("View.OnClickListener")){
				className = "View";
				methodName = "OnClickListener"; 
				
			}else{
				bind = call.resolveConstructorBinding();
				className = bind.getName();
			}
		}catch(Exception ex){
			bind = null;
		}
		
		StringBuilder targetCall = new StringBuilder();
		
		if(className != null ){
			targetCall.append(className).append(".").append(methodName);
			
			if(targetCalls.contains(targetCall.toString())){
				calls.add(new MethodCallVO(className, methodName, call.getStartPosition(), call.getLength()));
			}
			//work around for anonymous hostname instance.
			else{
				if(call.getType().toString().compareTo("HostnameVerifier")==0){
					if(targetCall.toString().compareTo(".<init>")==0){
						calls.add(new MethodCallVO("HostnameVerifier", methodName,call.getStartPosition(), call.getLength()));
					}
				}
			}
		}
		return true;
	}

	public HashSet<String> getTargetCalls() {
		return targetCalls;
	}

	public void setTargetCalls(HashSet<String> targetCalls) {
		this.targetCalls = targetCalls;
	}
	
	public HashSet<MethodCallVO> getCalls() {
		return calls;
	}
	
	public void setCalls(HashSet<MethodCallVO> calls) {
		this.calls = calls;
	}
	 
 
 	
}
