package markov;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SecondOrderMarkovModel {

	private ArrayList<String> wordList = new ArrayList<String>();
	private double[][][] probabilityMatrix;
	
	private static void addTo3dArray(ArrayList<ArrayList<ArrayList<Integer>>> list, int defVal) {
		if (list.isEmpty()) {
			list.add(new ArrayList<ArrayList<Integer>>());
			list.get(0).add(new ArrayList<Integer>());
			list.get(0).get(0).add(defVal);
		} else {
			list.add(new ArrayList<ArrayList<Integer>>());
			int curIndex = list.size() - 1;
			for (int i = 0; i < list.size(); i++) {
				list.get(curIndex).add(new ArrayList<Integer>());
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.size(); j++) {
					list.get(curIndex).get(i).add(defVal);
				}
			}
			for (int i = 0; i < curIndex; i++) {
				list.get(i).add(new ArrayList<Integer>());
				for (int j = 0; j < list.size(); j++) {
					list.get(i).get(curIndex).add(defVal);
				}
				for (int j = 0; j < curIndex; j++) {
					list.get(i).get(j).add(defVal);
				}
			}
		}
		System.out.println(list.size());
	}
	
	public SecondOrderMarkovModel(String filename) throws IOException {
		FileReader filereader = new FileReader(filename);
		int characterInt = 0;
		ArrayList<Character> charList = new ArrayList<Character>();
		ArrayList<ArrayList<ArrayList<Integer>>> countMatrix = new ArrayList<ArrayList<ArrayList<Integer>>>();
		String word = "";
		String prevWord = "";
		String secondPrevWord = "";
		while ((characterInt = filereader.read()) != -1) {
			char character = (char) characterInt;
			if (Character.isLetter(character) || character == '\'' || character == '$') {
				charList.add(character);
			} else {
				if (!charList.isEmpty()) {
					Character[] letterArray = charList.toArray(new Character[charList.size()]);
					secondPrevWord = prevWord;
					prevWord = word;
					word = "";
					for (Character letter : letterArray) {
						word += letter.toString();
					}
					if (!wordList.contains(word)) {
						wordList.add(word);
						addTo3dArray(countMatrix, 0);
					}
					if (!prevWord.isEmpty() && !secondPrevWord.isEmpty()) {
						int i = wordList.indexOf(word);
						int iPrev = wordList.indexOf(prevWord);
						int iSecondPrev = wordList.indexOf(secondPrevWord);
						countMatrix.get(iSecondPrev).get(iPrev).set(i, countMatrix.get(iSecondPrev).get(iPrev).get(i) + 1);
					}
				}
				if (character != ' ') {
					secondPrevWord = prevWord;
					prevWord = word;
					word = Character.toString(character);
					if (!wordList.contains(word)) {
						wordList.add(word);
						addTo3dArray(countMatrix, 0);
					}
					if (!prevWord.isEmpty() && !secondPrevWord.isEmpty()) {
						int i = wordList.indexOf(word);
						int iPrev = wordList.indexOf(prevWord);
						int iSecondPrev = wordList.indexOf(secondPrevWord);
						countMatrix.get(iSecondPrev).get(iPrev).set(i, countMatrix.get(iSecondPrev).get(iPrev).get(i) + 1);
					}
				}
				charList.clear();
			}
		}
		filereader.close();
		//populate probability matrix
		//allocate more heap space (tryint to generate a matrix with over 1 billion spaces.
	}
}
