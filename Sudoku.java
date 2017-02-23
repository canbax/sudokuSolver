package sudokuNorvig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.TreeSet;

public class Sudoku {
	
	String digits = "123456789";
	String rows = "ABCDEFGHI";
	String cols = "123456789";
	char[][] board;
	String[] squares;
	HashMap<String, ArrayList<TreeSet<String>>> units;
	HashMap<String, TreeSet<String>> peers;
	
	public Sudoku(String s1){
		
		squares = new String[s1.length()];
		units = new HashMap<>();
		peers = new HashMap<>();
		board = new char[9][9];
		
		
		for (int i = 0; i < rows.length(); i++) {
			for (int j = 0; j < cols.length(); j++){
				squares[i*9 + j] = "" + rows.charAt(i) + cols.charAt(j);
				board[i][j] = s1.charAt(i*9 + j);
			}
		}
		
		for (int i = 0; i < rows.length(); i++) {
			for (int j = 0; j < cols.length(); j++){
				String curr = squares[i*9 + j];
				
				// row neighbors
				TreeSet<String> l1 = new TreeSet<>();
				
				// col neighbors
				TreeSet<String> l2 = new TreeSet<>();
				for (int k = 0; k < 9; k++){
					l1.add(squares[i*9 + k]);
					l2.add(squares[j + k*9]);
				}
				
				// block neighbors
				TreeSet<String> l3 = new TreeSet<>();
				int top = i / 3;
				int left = j / 3;
				for (int a = 0; a < 3; a++){
					int ind1 = top*3 + a;
					for (int b = 0; b < 3; b++){
						int ind2 = left*3 + b;
						l3.add(squares[ind1*9 + ind2]);
					}
				}
				
				ArrayList<TreeSet<String>> bigset = new ArrayList<>();
				
				bigset.add(l1);bigset.add(l2);bigset.add(l3);
				
				units.put(curr, bigset);
				TreeSet<String> united = new TreeSet<>();
				united.addAll(l1); united.addAll(l2); united.addAll(l3);
				
				united.remove(curr);
				peers.put(curr, united);
				
			}
		}
				
	}
	
	public HashMap<String, String> eliminate(HashMap<String, String> values, String s, String d){
		
		// already eliminated
		if (!values.get(s).contains(d))
			return values;
		
		String newVal = values.get(s).replace(d, "");
		values.put(s, newVal);
		
		// nothing exist to put here
		if (values.get(s).length() == 0)
			return null;
		else if (values.get(s).length() == 1){	// only one possibility to put here
			String d2 = values.get(s).charAt(0) + "";
			for (String peer : peers.get(s)) {
				if (eliminate(values, peer, d2) == null){
					return null; // could not eliminated from peers, sth is wrong
				}
			}
		}
		
		// look possible values in my block
		for (TreeSet<String> unit : units.get(s) ) {
			TreeSet<String> dplaces = new TreeSet<>();
			for (String s2 : unit){
				if (values.get(s2).contains(d)){
					dplaces.add(s2);
				}
			}
			if (dplaces.size() == 0){
				return null;
			}else if (dplaces.size() == 1){ // we found the right place for d
				if (assign(values, dplaces.first(), d) == null){
					return null;
				}
			}
		}
		
		return values;
	}
	
	public HashMap<String, String> assign(HashMap<String, String> values, String s, String d){

		String otherVals = values.get(s).replace(d, "");
				
		for (int i = 0; i < otherVals.length(); i++){
			String d2 = otherVals.charAt(i) + "";
			if (eliminate(values, s, d2) == null)
				return null;
		}
		
		return values;
	}
	
	public void printBoard(){
		System.out.println();
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void printPossibles(HashMap<String, String> values){
		System.out.println();
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				System.out.print(values.get(squares[i*9 + j]) + " ");
			}
			System.out.println();
		}
	}
	
	public HashMap<String, String> parseBoard() {
		
		HashMap<String, String> values = new HashMap<>();
		
		for (int i = 0; i < squares.length; i++){
			String s = squares[i];
			values.put(s, "123456789");
		}
		
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				if (board[i][j] != '.' &&  board[i][j] != '0'){
					if (assign(values, squares[i*9 + j], board[i][j] + "") == null){
						return null;
					}
				}
			}
		}
		return values;
	}
	
	public HashMap<String, String> search(HashMap<String, String> values){
		if (values == null)
			return null;
		
		boolean allDone = true;
		for (String t : values.values()){
			if (t.length() != 1){
				allDone = false;
				break;
			}
		}
		
		if (allDone)
			return values;
		
		String minPossible = "";
		int minPosssibleCnt = 10;
		for (String s : squares){
			int size = values.get(s).length();
			if (size > 1 && size < minPosssibleCnt){
				minPossible = s;
				minPosssibleCnt = size;
			}
		}
				
		String possibleArr = values.get(minPossible);
		for (int ind = 0; ind < possibleArr.length(); ind++){
			String d = possibleArr.charAt(ind) + "";
			HashMap<String, String> ves = new HashMap<>(values);
			HashMap<String, String> res = search(assign(ves, minPossible, d));
			if (res != null)
				return res;
		}
		return null;
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "E:\\yusuf\\projects\\eclipseJava\\sudokuNorvig\\src\\sudokuNorvig\\top95.txt";
				
		ArrayList<String> strs = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			
			String line;
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				strs.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
				
		int norvig = 0, backtracking = 0;
		long nTime = 0, bTime = 0;
		for (int i = 0; i < strs.size(); i++){
			String s = strs.get(i);
			Sudoku sudo1 = new Sudoku(s);
			SudokuBacktracker sudo2 = new SudokuBacktracker(s);
			
			long time1 = System.currentTimeMillis();
			HashMap<String, String> res = sudo1.search(sudo1.parseBoard());
			//sudo1.printPossibles(res);
			long time2 = System.currentTimeMillis();
			long deltaT1 = time2 - time1;
			nTime += deltaT1;
			
			time1 = System.currentTimeMillis();
			boolean b1 = sudo2.solve();
			time2 = System.currentTimeMillis();
			long deltaT2 = time2 - time1;
			bTime += deltaT2;
			
			if (res == null){
				System.out.println("norvik fail");
			}
			if (!b1){
				System.out.println("backtracking failed fail");
			}
			
			if (deltaT1 < deltaT2){
				norvig++;
			}else if (deltaT2 < deltaT1){
				backtracking++;
			}
		}
		
		System.out.println("norvig:" + norvig + " backtracking: " + backtracking);
		System.out.println("total time (ms):" + nTime + "  vs   " + bTime);

	}

}
