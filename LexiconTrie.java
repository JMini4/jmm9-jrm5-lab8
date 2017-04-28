/*
Josie Maynard and Julia Mini
4/20/17
Wednesday Afternoon, Park

Thought Question: If we used an OrderedVector instead of a trie, we would have to store each individual word in the vector separately instead of overlaying words that share the same prefix. This would take a up a lot more space, and would take longer to search for words and come up with spelling suggestions. When we search for words and suggest corrections using the trie, we follow the path of the letters down the trie, which eliminates unnecessary checks. If we used an ordered vector, we would have to look at each individual word until we found the target. Even though it helps that the words are in alphabetical order, this would still be much more inefficient. Also, for suggest corrections, in the case that the first letter deviates, every single word would need to be considered using an ordered vector and we would need to check whether each word satisfied the requirements for the target and maxDistance, whereas we can eliminate potential words more efficiently using a trie. We also would need to keep track of the length of the words in an ordered vector for the suggestCorrections method, whereas length is more naturally accounted for using the levels of the trie. Because we consider each letter separately and overlay words that share the same prefix, the trie is more efficient.


Program Description: Constructs a dictionary in the form of a trie. A user can manually add words or scan in a file, remove words, check for containment, suggest spelling corrections based on a target and a maximum distance, and give matches to word patterns using regular expressions.

*/

import structure5.*;
import java.util.Vector;
import java.util.Iterator;
import java.util.Scanner;
import java.io.File;

public class LexiconTrie implements Lexicon {

    //the starter node of the trie 
    private LexiconNode root;

    //number of words in the lexicon
    private int count;
    
    //post: constructs an empty lexicon trie
    public LexiconTrie(){
	root = new LexiconNode(' ', false);
	count = 0;
    }

    // post: adds a lower-case string word to the trie
    // returns false if word was already in the lexicon
    public boolean addWord(String word) {
	
	word = word.toLowerCase();

	LexiconNode currentNode = root;
	LexiconNode nextNode;

	for(int i = 0; i < word.length(); i++) {
	    //searches trie for the next letter
	    nextNode = currentNode.getChild(word.charAt(i));

	    if(nextNode == null){ //create a new node for that letter
		nextNode = new LexiconNode(word.charAt(i), false);
		currentNode.addChild(nextNode);
	    } 
	    currentNode = nextNode;
	}

	if(!currentNode.isWord()){ //if it's not already contained in the lexicon
	    //mark that it is a word and update the count in the lexicon
	    currentNode.setTrue();
	    count++;
	    return true;
	} else {
	    return false;
	}
    }


    //pre: words have to be on separate lines in the text
    //post: adds lower-case words from a file to the lexicon
    //returns the number of words added
    public int addWordsFromFile(String filename) {
	
	Scanner in;

	try {
	    in = new Scanner(new File(filename));
	} catch(Exception E){
	    System.out.println("File not found.");
	    return 0;
	}

	//number of words added from the file to the lexicon
	int numWordsAdded = 0;
	
	while(in.hasNextLine()){
	    this.addWord(in.nextLine());
	    numWordsAdded++;
	}
	
	return numWordsAdded;
    }

    
    // post: removes word from the lexicon by setting isWord of the last node to false
    //returns false if the word is not contained in the lexicon
    public boolean removeWord(String word) {
	word = word.toLowerCase();

	LexiconNode currentNode = root;
	LexiconNode nextNode;

	for(int i = 0; i < word.length(); i++) {
	    nextNode = currentNode.getChild(word.charAt(i));

	    if(nextNode == null){ //word does not appear in lexicon
		return false;
	    }
	    currentNode = nextNode;
	}

	//set that the node is no longer the end of a word and update the count
	currentNode.setFalse();
	count--;
	return true;
    }

    // post: returns the number of words in the lexicon
    public int numWords() {
	return count;
    }

    // post: returns true if the lexicon contains the lower-case word
    public boolean containsWord(String word){

	word = word.toLowerCase();
	
	LexiconNode currentNode = root;
	LexiconNode nextNode;
	    
	for(int i = 0; i < word.length(); i++) {
	    nextNode = currentNode.getChild(word.charAt(i));

	    if(nextNode == null){
		return false;
	    }
	    currentNode = nextNode;
	}
	
	if(currentNode.isWord()){
	    return true;
	} else { //the string is not considered a word
	    return false;
	}
    }
    
    // post: returns true if the lexicon contains the lower-case prefix
    public boolean containsPrefix(String prefix){

	prefix = prefix.toLowerCase();
	
	LexiconNode currentNode = root;
	LexiconNode nextNode;
	
	for(int i = 0; i < prefix.length(); i++) {
	    nextNode = currentNode.getChild(prefix.charAt(i));

	    if(nextNode == null){
		return false;
	    }
	    currentNode= nextNode;
	}
	return true;
    }


    // post: creates an iterator for the vector of words contained in the lexicon
    //traverses them in alphabetical order 
    public Iterator<String> iterator() {
	Vector<String> wordVector = this.wordList(root, "");
	return wordVector.iterator();
    }

    //pre: current node is not null
    // post: helper function for iterator that creates a vector of words in the lexicon
    private Vector<String> wordList(LexiconNode current, String soFar){

	assert(current != null):("Current node must exist");
	
	//vector to contain the words
	Vector<String> wordVector = new Vector<String>();

	//iterator to traverse the children of a node
	Iterator<LexiconNode> childIterator = current.iterator();

	while(childIterator.hasNext()){
	    LexiconNode nextNode = childIterator.next();

	    //adds letter at the node to soFar
	    String soFarNew = soFar + Character.toString(nextNode.letter);

	    if(nextNode.isWord()){
		wordVector.add(soFarNew);
	    }
	    
	    wordVector.addAll(wordList(nextNode, soFarNew));
	}
	
	return wordVector;
    }

    // pre: maxDistance is not negative
    // post: returns a set of words contained in the lexicon that are similar in spelling to the lower-case target word
    public Set<String> suggestCorrections(String target, int maxDistance) {
	assert(maxDistance >= 0):("The max distance cannot be negative");
	
	return suggestCorrectionsHelper(root, target.toLowerCase(), maxDistance, "", new SetVector<String>());
    }

    // pre: current node is not null, maxDistance is not negative, suggestionList is not null
    // post: recursive helper for suggestCorrections 
    private Set<String> suggestCorrectionsHelper(LexiconNode current, String target, int maxDistance, String soFar, Set<String> suggestionList){

	assert(current != null && suggestionList != null):("current node and suggestionList cannot be null");
	assert(maxDistance >= 0):("The max distance cannot be negative");

	//base case
	if(target.equals("")){ //reached the end of the original target string
	    if(current.isWord()){
		suggestionList.add(soFar);
	    }
	    
	} else {
	    //iterator to traverse the children of the node
	    Iterator<LexiconNode> childIterator = current.iterator();
	    
	    while(childIterator.hasNext()){
		LexiconNode nextNode = childIterator.next();

		if(nextNode.letter == target.charAt(0)){ //the letters match, don't alter maxDistance
		    String soFarNew = soFar + nextNode.letter;
		    suggestCorrectionsHelper(nextNode, target.substring(1), maxDistance, soFarNew, suggestionList);
		} else if(maxDistance > 0){ // the letters don't match, make sure maxDistance isn't zero
		    String soFarNew = soFar + nextNode.letter;
		    suggestCorrectionsHelper(nextNode, target.substring(1), maxDistance-1, soFarNew, suggestionList);
		} //if letters don't match and maxDistance is zero, stop going along this path
	    }
	}
	return suggestionList;
    }

    // post: returns a set of words that meet the specifications of the pattern
    public Set<String> matchRegex(String pattern){
	return matchRegexHelper(root, pattern, "", new SetVector<String>());
    }

    // pre: current node is not null, regexMatches is not null
    // post: recursive helper method for matchRegex 
    private Set<String> matchRegexHelper(LexiconNode current, String pattern, String soFar, Set<String> regexMatches){

	assert(current != null && regexMatches != null):("current node and regexMatches cannot be null");

	//base case
	if(pattern.equals("")){ //reached the end of the given pattern
	    if(current.isWord()){
		regexMatches.add(soFar);
	    }
	    
	} else {
	    if(pattern.charAt(0) == '?'){
		//if the "?" stands for 0 characters
		matchRegexHelper(current, pattern.substring(1), soFar, regexMatches);

		//if the "?" stands for 1 character, try out possible letters
		Iterator<LexiconNode> childIterator = current.iterator();

		while(childIterator.hasNext()){
		    LexiconNode nextNode = childIterator.next();
		    String soFarNew = soFar + nextNode.letter;
		    matchRegexHelper(nextNode, pattern.substring(1), soFarNew, regexMatches);
		}
		
	    } else if(pattern.charAt(0) == '*'){
		// if the "*" stands for 0 characters
		matchRegexHelper(current, pattern.substring(1), soFar, regexMatches);

		// if the "*" stands for 1 or more characters, try out possible letters
		Iterator<LexiconNode> childIterator = current.iterator();
		
		while(childIterator.hasNext()){
		    LexiconNode nextNode = childIterator.next();
		    String soFarNew = soFar + nextNode.letter;

		    //get rid of the "*"- stood for 1 character
		    matchRegexHelper(nextNode, pattern.substring(1), soFarNew, regexMatches);

		    //keep the "*"- stands for more than 1 character
		    matchRegexHelper(nextNode, pattern, soFarNew, regexMatches);
		}
		
	    } else { //specific character
		LexiconNode nextNode = current.getChild(pattern.charAt(0));
		if(nextNode != null) {
		    String soFarNew = soFar + nextNode.letter;
		    matchRegexHelper(nextNode, pattern.substring(1), soFarNew, regexMatches);
		}
	    }
	}
	return regexMatches;
    }
}
