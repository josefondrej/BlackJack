package maxExpectation;

import java.util.Arrays;
import java.util.stream.IntStream;

public class GameState implements Cloneable{
	int[] dealerCards;				// dealerovy karty
	int dealerSum;					// sou�et hodnot dealerov�ch karet
	boolean dealerSoft;				// sou�et dealerov�ch karet je m�kk�
	int[] myCards;					// moje karty
	int mySum;						// sou�et hodnot m�ch karet 
	boolean iSoft;					// sou�et m�ch karet je m�kk�
	int[] packCards;				// karty v bal��ku 1 = A = 11, 2, 3, ... 10
	int packNr;						// po�et karet v bal��ku
	boolean doubledDown;			// byla situace vytvo�ena zdvojen�m s�zky?
	int splitted;					// kolikr�t byl proveden split, ne� nastala situace
	
	/**
	 * Naklonuje stav hry.
	 */
	public GameState clone(){
		return new GameState(myCards.clone(), dealerCards.clone(), packCards.clone(), doubledDown, splitted);
	}
	
	/**
	 * Vytiskne aktu�ln� stav.
	 */
	public void print(){
		System.out.print("("+mySum+")\t");
		for(int i:myCards) System.out.print(i+"\t");
		System.out.println();
		System.out.print("("+dealerSum+")\t");
		for(int d:dealerCards) System.out.print(d+"\t");
		System.out.println();
		for(int i = 0; i < 10; i++) System.out.print((i+1)+":"+packCards[i]+"/");
	} 
	
	/**
	 * Aktualizuje m�j a dealer�v sou�et podle hodnot karet.
	 */
	public void actCounts(){
		int myHard = IntStream.of(myCards).sum();
		int mySoft = myHard;
		boolean iAce = contains(myCards, 1);
		if(iAce) mySoft += 10;
		
		int dHard = IntStream.of(dealerCards).sum();
		int dSoft = dHard;
		boolean dAce = contains(dealerCards, 1); 
		if(dAce) dSoft += 10;
		
		mySum = myHard;
		iSoft = false;
		
		dealerSum = dHard;
		dealerSoft = false;
		
		if(iAce && mySoft<=21){
			mySum = mySoft;
			iSoft = true;
		}
		
		if(dAce && dSoft<=21){
			dealerSum = dSoft;
			dealerSoft = true;
		}
	}
	
	/**
	 * Generuj nov� stavy po splitu. 
	 * @throws CloneNotSupportedException 
	 */
	public GameState[] split(int c1, int c2) throws CloneNotSupportedException{
		GameState state1 = (GameState) this.clone();
		GameState state2 = (GameState) this.clone();
		
		state1.myCards = new int[]{state1.myCards[0], c1};
		state2.myCards = new int[]{state2.myCards[1], c2};
		
		state1.packCards[c1-1]--;
		state1.packCards[c2-1]--;
		state2.packCards[c1-1]--;
		state2.packCards[c2-1]--;
		
		state1.packNr-=2;
		state2.packNr-=2;
		
		state1.splitted ++;
		state2.splitted ++;
		
		state1.actCounts();
		state2.actCounts();
		
		return new GameState[]{state1, state2};
	} 
	
	/**
	 * Generuj nov� stav. P�idej dealerovi kartu c.  
	 */
	public GameState dealerPlus(int c) throws CloneNotSupportedException {
		 GameState res = (GameState) this.clone();
		 int[] dealerOrig = res.dealerCards.clone();
		 int l = res.dealerCards.length;
		 res.dealerCards = new int[l+1];
		 for(int i = 0; i<l; i++) 
			 res.dealerCards[i] = dealerOrig[i];
		 res.dealerCards[l] = c;
		 res.packCards[c-1]--;
		 res.packNr--;
		 res.actCounts();
		 return(res);
	} 
	
	/**
	 * Generuj nov� stav. P�idej hr��i kartu c. 
	 */
	public GameState iPlus(int c) throws CloneNotSupportedException {
		 GameState res = (GameState) this.clone();
		 int[] myOrig = res.myCards.clone();
		 int l = res.myCards.length;
		 res.myCards = new int[l+1];
		 for(int i = 0; i<l; i++) 
			 res.myCards[i] = myOrig[i];
		 res.myCards[l] = c;
		 res.packCards[c-1]--;
		 res.packNr--;
		 res.actCounts();
		 return(res);
	} 
	
	/**
	 * Obsahuje seznam karet cards kartu c? 
	 */
	public boolean contains(int[] cards, int c){
		boolean res = false;
		for(int i:cards) res = res || (c==i);
		return(res);
	}
	
	/**
	 * Konstruktor. 
	 */
	public GameState(int[] my, int[] dealer, int[] pack, boolean doubled, int splitted){
		this.dealerCards = dealer;
		this.myCards = my;
		this.packCards = pack;
		this.doubledDown = doubled;
		this.splitted = splitted;
		packNr = IntStream.of(packCards).sum();
		
		actCounts();
	}
	
	/**
	 * Je kombinace karet blackjack?
	 */
	public boolean isBJ(int[] cards){
		return  (cards[0] == 1 && cards[1] == 10) || (cards[0] == 10 && cards[1] == 1 && splitted == 0);
	}
	
	/**
	 *  Zisk hr��e v aktu�ln� situaci s odkryt�mi kartami.
	 */
	public double evaluate(){
		if(mySum>21) return -1;
		else{
			if(dealerSum>21) return 1;
			else{
				if(mySum>dealerSum) {
					if(isBJ(myCards)) return 1.5;
					return 1;
				}
				if(mySum == dealerSum){
					if(isBJ(myCards) && !isBJ(dealerCards)) return 1.5;
					if(!isBJ(myCards) && isBJ(dealerCards)) return -1;
					return 0;
				}
				if(mySum<dealerSum) return -1;
			}
		}
		throw new RuntimeException("evaluate() nevr�tila v�sledek");
	}
	
	/** 
	 * V��et mo�n�ch akc� hr��e.  
	 */
	public char[] possibleMoves(){
		if(isBJ(myCards)) return new char[]{'s'};
		if(splitted == 0){
			if(myCards.length == 2) {	
				if(myCards[0] == myCards[1]) return new char[]{'s', 'd', 'h', 'p'};
				else return new char[]{'s', 'd', 'h'};
			} else {
				return new char[]{'s', 'h'};
			}
		} else if(splitted == 1){
			if(myCards[0] == 1) return new char[]{};
			if(myCards.length == 2) {
				return new char[]{'s', 'd', 'h'};
			} else {
				return new char[]{'s', 'h'};
			}
		} else {
			return null;
		}
		
	}
	
	/**
	 * 0�ek�van� hodnota zisku hr��e, pokud se rozhodne st�t.
	 */
	public double standE() throws CloneNotSupportedException{
		if(dealerSum<17){
			double E = 0;
			for(int i = 0; i<10; i++){
				if(packCards[i]==0) continue;
				E += packCards[i]*dealerPlus(i+1).standE()/packNr;
			}
			return E;
		} else {
			double eval = evaluate();
			//print();
			//System.out.println(eval);
			return eval;
		}
	}
	
	/**
	 * O�ek�van� hodnota zisku hr��e, pokud se rozhodne vz�t dal�� kartu. 
	 */
	public double hitE() throws CloneNotSupportedException{
		double E = 0;
		for(int i = 0; i<10; i++){
			if(packCards[i]==0) continue;
			E += packCards[i]*iPlus(i+1).E().getExp()/packNr;
		}
		//print();
		return E;
	}
	
	/**
	 * O�ek�van� hodnota zisku hr��e, pokud d� double.
	 * @throws CloneNotSupportedException 
	 */
	public double doubleE() throws CloneNotSupportedException{
		double E = 0;
		for(int i = 0; i<10; i++){
			if(packCards[i]==0) continue;
			E += packCards[i]*iPlus(i+1).standE()/packNr;
		}
		return 2*E;
	}
	
	/**
	 * O�ek�van� hodnota zisku hr��e, pokud provede split.
	 * @throws CloneNotSupportedException 
	 */
	public double splitE() throws CloneNotSupportedException{
		double E = 0;
			for(int i = 0; i<10; i++){
				if(packCards[i]==0) continue;
				for(int j = 0; j<10; j++){
					if(packCards[j]==0) continue;
					if(i==j && packCards[j]<=1) continue;
					GameState[] split = split(i, j);
					E += split[0].E().getExp()+split[1].E().getExp();
				}
			}
		return E;
	}

	/**
	 * O�ek�van� hodnota zisku hr��e za dan� situace.
	 */
	public ResE E() throws CloneNotSupportedException{
		if(mySum>21) return new ResE(-1, 'x');
		char[] moves = possibleMoves();
		double maxVal = Double.NEGATIVE_INFINITY;
		char action = 'x';
		for(int i = 0; i<moves.length; i++){
			char ch = moves[i];
			if(ch=='s' && mySum<=10) continue;
			double val = Double.NEGATIVE_INFINITY;
			switch(ch){
				case 's': val = standE(); break;
				case 'd': val = doubleE(); break;
				case 'h': val = hitE(); break;
				case 'p': val = splitE(); break;
			} 	
			if(val > maxVal){
				maxVal = val;
				action = ch;
			}
		}
		
		return new ResE(maxVal, action);
	}
	
}
