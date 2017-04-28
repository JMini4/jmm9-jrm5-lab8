/*
Names: Josie Maynard and Julia Mini
Lab Section: Wednesday Afternoon, Park
Lab 8

Program Description: Creates a node that contains a letter and indicates whether or not the letter marks the end of a word.
Nodes can be compared and its children can be added, removed, and iterated through.
 */

import structure5.*;
import java.util.Iterator;

class LexiconNode implements Comparable<LexiconNode> {

    // stores the children of a node
    private Vector<LexiconNode> children;

    //the letter contained in a node
    public char letter;

    //whether or not the node marks the end of a word
    private boolean completeWord;

    //post: constructs a node and a vector to store its children
    public LexiconNode(char letter, boolean isWord) {
	this.letter = letter;
	completeWord = isWord;
	children = new Vector<LexiconNode>();
    }

    //pre: o is not null
    // post: compares this LexiconNode to another by comparing their characters
    public int compareTo(LexiconNode o) {
	assert(o != null):("o cannot be null");
	
	return letter - o.letter;
    } 

    // post: returns the letter stored by this LexiconNode
    public char letter() {
	return letter;
    }

    // pre: ln is not null
    // post: adds a LexiconNode child to the alphabetically correct position in child data structure
    // if ln is already a child, does nothing 
    public void addChild(LexiconNode ln) {
	assert(ln != null):("node cannot be null");

	//index to insert the child
	int i;
	
	for (i = 0; i < children.size(); i++){
	    if (children.get(i).compareTo(ln) == 0) { //ln is already a child of the node, exit the method
		return;
	    } else if (children.get(i).compareTo(ln) > 0) { //found the index
		break;
	    }
	}
       
	children.add(i, ln);
    }

    
    // post: returns the LexiconNode child that corresponds to the letter 'ch'
    // returns null if the letter 'ch' node is not a child of this node
    public LexiconNode getChild(char ch) {
	
	for(int i = 0; i < children.size(); i++){
	    if (children.get(i).letter - ch == 0) {
		return children.get(i);
	    }
	}
	return null;
    }

    // post: removes the LexiconNode child for 'ch' from the child vector
    // will only ever remove 1 child (if for some reason there were two nodes for the same letter)
    public void removeChild(char ch) {
	
	for(int i = 0; i < children.size(); i++){
	    if (children.get(i).letter - ch == 0) {
		children.remove(i);
		break;
	    }
	}
    }

    
    // post: creates an Iterator that iterates over children in alphabetical order
     public Iterator<LexiconNode> iterator() {
	return children.iterator();
    }

    //post: returns whether or not the node is the end of a word
    public boolean isWord(){
	return completeWord;
    }

    // post: makes it so that the node does not indicate the end of a word 
    public void setFalse(){
	completeWord = false;
    }

    // post: makes it so that the node indicates the end of a word 
    public void setTrue(){
	completeWord = true;
    }
}
