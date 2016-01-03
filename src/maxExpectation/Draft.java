package maxExpectation;

public class Draft {

	public static void main(String[] args) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		GameState state = new GameState(
				new int[]{5, 5}, 
				new int[]{1}, 
				new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 16}, 
				false, 
				0);
		
		System.out.print(state.E().getExp()+"\t");
	}

}
