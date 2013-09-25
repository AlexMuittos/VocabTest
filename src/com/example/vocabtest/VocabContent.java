package com.example.vocabtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class VocabContent {
	/**
	 * An instance of node contains a word, its meanings in a queue, and the entry 
	 * in the text file.
	 */
	protected class node {
		String word;
		Queue<String> meanings;
		String dictentry;	
		private node(String word,Queue<String> meanings, String dictentry){
			this.word = word;
			this.meanings = meanings;
			this.dictentry = dictentry;
		}
	}
	
	/**
	 * The method parses a text file of words and meanings and returns an ArrayList of nodes.
	 * @param filename The text file to be parsed, formatted similar to Barron.txt (included)
	 * @return ArrayList of nodes, each node corresponds to a particular word.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ArrayList<node> makeList(InputStream words_stream){
		ArrayList<node> store = new ArrayList<node>();
		try {		
			
			BufferedReader in = new BufferedReader(new InputStreamReader(words_stream));
			String entry;
			while ((entry = in.readLine()) != null){
				//Get the word from one entry in the file
				String word;
				if(entry.startsWith("h ") || !entry.contains("\t")){
					continue;
				}
				String[] arr = entry.split("\t");
				if(arr[0].trim().startsWith("n ") ||
						arr[0].trim().startsWith("s ") || 
						arr[0].trim().startsWith("v ")){
					
					word = arr[0].substring(2).trim();
				}
				else word = arr[0].trim();
				//Get the meanings from one entry in the file
				String[] mng = arr[1].trim().split(";");
				//Create a queue for the meanings
				Queue<String> meanings = new LinkedList<String>();
				for(int i=0;i<mng.length;i++){
					if(mng[i].trim().startsWith("N.") ||
							mng[i].trim().startsWith("V.") ||
							mng[i].trim().startsWith("ADJ.") ||
							mng[i].trim().startsWith("Ex.") ||
							mng[i].trim().startsWith("CF.") ||
							mng[i].trim().startsWith("OP.") ||
							mng[i].trim().startsWith("Cf."))
						continue;
					else meanings.add(mng[i].trim());
				}
				//Create a new entry in the ArrayList
				store.add(new node(word,meanings,entry));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return store;
	}
	
	public String[] getTestEntry(ArrayList<node> words){
		Random r = new Random();
		String[] entry = new String[8]; //hack to send back answer
		int word_index = r.nextInt(words.size());
		entry[0] = words.get(word_index).word;
		entry[7] = words.get(word_index).dictentry;
		
		String answer = words.get(word_index).meanings.remove();
		words.get(word_index).meanings.add(answer);
		
		int answer_index = 1 + r.nextInt(5);
		entry[answer_index] = answer;
		entry[6] = Integer.toString(answer_index);
		
		//Get the other options for the meaning
		for(int k=1;k<6;k++){
			if(k==answer_index) continue;
			else entry[k] = words.get(r.nextInt(words.size())).meanings.peek();
		}		
		
		return entry;
	}
	
	/**
	 * Creates a vocabulary test given a dictionary as an ArrayList of nodes. Tracks 
	 * score during the test and writes entries for mistaken words in mistakes.txt. 
	 * @param words ArrayList of nodes, each node corresponds to a particular word.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void makeTest(ArrayList<node> words){
		try {
			ArrayList<node> store = words;
			Random r = new Random();
			int i,j,k,score=0,answer_index;
			String query,answer;
			Scanner input = new Scanner(System.in);
			//Create a Buffered Writer to write the mistaken entries to file
			BufferedWriter out = new BufferedWriter(new FileWriter("mistakes.txt",true));			
			for(i=1;i<=15;i++)
			{
				String[] one = new String[5];
				//Randomly choose a word and get its meaning 
				j = r.nextInt(store.size());				
				query = store.get(j).word;
				System.out.println(i + "." + query + "\n");
				//The meaning is dequeued and then enqueued back ensuring that
				//it will appear only after all the other meanings have been 
				//rotated in case the same word appears multiple times
				answer = store.get(j).meanings.remove();
				store.get(j).meanings.add(answer);
				//Place the meaning in a random position among the five options
				answer_index = r.nextInt(5);
				one[answer_index] = answer;
				//Get the other options for the meaning
				for(k=0;k<5;k++){
					if(k==answer_index) continue;
					else one[k] = store.get(r.nextInt(store.size())).meanings.peek();
				}
				//Print out the options
				for(k=0;k<5;k++){
					System.out.println((char)(k+65) + ". " + one[k]);
				}
				//Check if the user input is correct. If correct, increment and print score. 
				//Else print out the current score and print the entry as well.
				char test = input.nextLine().toUpperCase().charAt(0);
				if((test - 65) == answer_index) {
					score++;
					System.out.println("Correct! Score : " + score + "/" + i + "\n");
				}
				else {
					out.write(store.get(j).dictentry + "\n");
					System.out.println("Wrong. Correct answer is " + answer + ". Score : " +
							score + "/" + i + "\n\n" + store.get(j).dictentry + "\n");
				}
			}
			score = 0;
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("StringIndexOutOfBoundsException: " + e.getMessage() +
					"Please enter a valid answer.\n");
			e.printStackTrace();
		}
		
	}

}
 