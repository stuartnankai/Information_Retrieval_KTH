/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  

package ir;

import java.util.LinkedList;
import java.io.Serializable;

/**
 *   A list of postings for a given word.
 */
public class PostingsList implements Serializable {
    
    /** The postings list as a linked list. */
    public LinkedList<PostingsEntry> list = new LinkedList<PostingsEntry>();

    public PostingsList()
    {

    }

    public PostingsList(int id,int off)
    {
        PostingsEntry e=new PostingsEntry(id,off);
        this.list.add(e);
    }


    /**  Number of postings in this list  */
    public int size() {
	return list.size();
    }

    /**  Returns the ith posting */
    public PostingsEntry get( int i ) {
	return list.get( i );
    }

    //
    //  YOUR CODE HERE
    //
    public void insertInto(String token,int id,int off)
    {
        //int getNum;

        int lastID = list.getLast().docID; //find the last docID

        if (id>lastID){ //compare with the id, if it is bigger than the last docID, put it into the list
            PostingsEntry p=new PostingsEntry(id,off);
            this.list.add(p);

        } else { // have the id, just add the new offset.
            this.list.getLast().offset.add(off);

        }
    }

}
	

			   
