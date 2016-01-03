package maxExpectation;

public class ResE {
	double expectation;
	char action;	// h = Hit, s = Stand, d = Double, p = sPlit
	
	public void print(){
		System.out.println(expectation+"\t"+action);
	}
	
	public double getExp(){
		return expectation;
	}
	
	public char getAct(){
		return action;
	}
	
	public ResE(double expectation, char action){
		this.expectation = expectation;
		this.action = action;
	}
}
