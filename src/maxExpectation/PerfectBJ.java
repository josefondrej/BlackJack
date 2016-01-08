package maxExpectation;

import java.util.concurrent.ExecutionException;

public class PerfectBJ {

	public static void main(String[] args) throws CloneNotSupportedException, InterruptedException, ExecutionException {

		int[][] hardPairs = new int[][]{
			{2, 3},
			{2, 4}, 
			{2, 5},
			{2, 6}, 
			{2, 7}, 
			{2, 8}, 
			{2, 9}, 
			{2, 10}, 
			{3, 10}, 
			{4, 10},
			{5, 10}, 
			{6, 10}, 
			{7, 10}, 
			{8, 10}, 
			{9, 10}, 
			};
			
		int[][] softPairs = new int[][]{
			{1, 2}, 
			{1, 3}, 
			{1, 4}, 
			{1, 5}, 
			{1, 6}, 
			{1, 7}, 
			{1, 8}, 
			{1, 9}
		};
		
		int[][] splitPairs = new int[][]{
			{1, 1}, 
			{2, 2}, 
			{3, 3}, 
			{4, 4}, 
			{5, 5}, 
			{6, 6}, 
			{7, 7}, 
			{8, 8}, 
			{9, 9}, 
			{10, 10}
		};
		
		int[][] dealerCard = new int[][]{
			{1}, 
			{2}, 
			{3}, 
			{4}, 
			{5}, 
			{6}, 
			{7}, 
			{8}, 
			{9}, 
			{10}
		};
		
		for(int[] p:hardPairs){
			for(int[] d:dealerCard){
				GameState state = new GameState(
						p, 
						d, 
						new int[]{32, 32, 32, 32, 32, 32, 32, 32, 32, 128}, 
						false, 
						0);
				state.parall = true;
				System.out.print(state.E().getAct()+"\t");
			}
			System.out.println();
		}
		
	}

}
