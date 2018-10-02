package markov;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FirstOrderMarkovModel {

	private ArrayList<String> wordList = new ArrayList<String>();
	private double[][] probabilityMatrix;

	private static void addTo2DArrayList(ArrayList<ArrayList<Integer>> list, int defVal) {
		if (list.isEmpty()) {
			list.add(new ArrayList<Integer>());
			list.get(0).add(defVal);
		} else {
			list.add(new ArrayList<Integer>());
			int curRowIndex = list.size() - 1;
			int rowLength = list.get(curRowIndex - 1).size();
			for (int i = 0; i < rowLength + 1; i++) {
				list.get(curRowIndex).add(defVal);
			}
			for (int i = 0; i < curRowIndex; i++) {
				list.get(i).add(defVal);
			}
		}
	}

	public FirstOrderMarkovModel(String filename) throws IOException {
		FileReader filereader = new FileReader(filename);
		int characterInt = 0;
		ArrayList<Character> charList = new ArrayList<Character>();
		ArrayList<ArrayList<Integer>> countMatrix = new ArrayList<ArrayList<Integer>>();
		String word = "";
		String prevWord = "";
		while ((characterInt = filereader.read()) != -1) {
			char character = (char) characterInt;
			if (Character.isLetter(character) || character == '\'' || character == '$') {
				charList.add(character);
			} else {
				if (!charList.isEmpty()) {
					Character[] letterArray = charList.toArray(new Character[charList.size()]);
					prevWord = word;
					word = "";
					for (Character letter : letterArray) {
						word += letter.toString();
					}
					if (!wordList.contains(word)) {
						wordList.add(word);
						addTo2DArrayList(countMatrix, 0);
					}
					if (!prevWord.isEmpty()) {
						int i = wordList.indexOf(word);
						int iPrev = wordList.indexOf(prevWord);
						countMatrix.get(iPrev).set(i, countMatrix.get(iPrev).get(i) + 1);
					}
				}
				if (character != ' ') {
					prevWord = word;
					word = Character.toString(character);
					if (!wordList.contains(word)) {
						wordList.add(word);
						addTo2DArrayList(countMatrix, 0);
					}
					if (!prevWord.isEmpty()) {
						int i = wordList.indexOf(word);
						int iPrev = wordList.indexOf(prevWord);
						countMatrix.get(iPrev).set(i, countMatrix.get(iPrev).get(i) + 1);
					}
				}
				charList.clear();
			}
		}
		filereader.close();
		probabilityMatrix = new double[wordList.size()][wordList.size()];
		for (int i = 0; i < countMatrix.size(); i++) {
			double rowTotal = 0.0;
			for (int count : countMatrix.get(i)) {
				rowTotal += count;
			}
			if (rowTotal != 0) {
				for (int j = 0; j < countMatrix.get(i).size(); j++) {
					probabilityMatrix[i][j] = countMatrix.get(i).get(j) / rowTotal;
				}
			}
		}
	}
	
	public String generatePassage(int length, int maxLineLength) {
		StringBuilder s = new StringBuilder();
		Random rand = new Random();
		String firstWord = " ";
		while (!Character.isUpperCase(firstWord.toCharArray()[0])) {
			firstWord = wordList.get(rand.nextInt(wordList.size()));
		}
		int prevWordIndex = wordList.indexOf(firstWord);
		s.append(firstWord);
		int lineStartLength = 0;
		for (int i = 0; i < length; i++) {
			double choice = rand.nextDouble();
			double totalProb = 0.0;
			int iOfChoice = -1;
			for (int j = 0; j < probabilityMatrix[prevWordIndex].length; j++) {
				totalProb += probabilityMatrix[prevWordIndex][j];
				if (choice < totalProb && iOfChoice == -1) {
					iOfChoice = j;
				}
			}
			String wordOfChoice = "";
			if (iOfChoice == -1) {
				wordOfChoice = wordList.get(rand.nextInt(wordList.size()));
			} else {
				wordOfChoice = wordList.get(iOfChoice);
			}
			boolean newline = false;
			if (s.length() - lineStartLength > maxLineLength) {
				s.append("\n");
				lineStartLength = s.length();
				newline = true;
			}
			if (Character.isLetter(wordOfChoice.toCharArray()[0]) && newline == false) {
				s.append(" " + wordOfChoice);
			} else if (Character.isDigit(wordOfChoice.toCharArray()[0]) && newline == false) {
				s.append(" " + wordOfChoice);
			} else {
				s.append(wordOfChoice);
			}
			prevWordIndex = wordList.indexOf(wordOfChoice);
		}
		return s.toString();
	}
	
	public String generatePassage(int length) {
		return generatePassage(length, 80);
	}
	
	public String generatePassage() {
		return generatePassage(100);
	}
}
