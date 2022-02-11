package edu.wm.cs.masc.exhaustive.mdroid;



import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodCallVisitor extends ASTVisitor{
	

	private HashSet<String> targetCalls;
	private HashSet<MethodCallVO> calls;

	public MethodCallVisitor(){
		calls = new HashSet<MethodCallVO>();
		targetCalls = new HashSet<>();
	}
	
	public boolean visit(MethodInvocation call){
		
	
		
		ITypeBinding bind = null;		
		String className = null;
		
		String methodName = call.getName().getFullyQualifiedName();
		
		
		try{
			bind = ((MethodInvocation)call).getExpression().resolveTypeBinding();
			className = bind.getName();
		}catch(Exception ex){
			bind = null;
		}
		
		StringBuilder targetCall = new StringBuilder();
		
		if(className != null ){
			targetCall.append(className).append(".").append(methodName);
			//For TESTING ONLY
			//System.out.println("SC CALL: "+targetCall);
			//for(String tc : targetCalls){
			//	System.out.println("T CALL: "+tc);
			//}
			
			if(targetCalls.contains(targetCall.toString())){
				calls.add(new MethodCallVO(className, methodName, call.getStartPosition(), call.getLength()));
			}
			else if(targetCall.toString().contains("findViewById")){
//				System.out.println(className +","+ methodName +","+ call.getStartPosition() +","+ call.getLength());
//				System.exit(0);
				calls.add(new MethodCallVO("Activity", methodName, call.getStartPosition(), call.getLength()));
			}
		}
		return true;
	}

	public HashSet<MethodCallVO> getCalls() {
		return calls;
	}

	public HashSet<String> getTargetCalls() {
		return targetCalls;
	}

	public void setTargetCalls(HashSet<String> targetCalls) {
		this.targetCalls = targetCalls;
	}
	
	

}
