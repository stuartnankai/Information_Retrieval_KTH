/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  

package ir;

import java.io.Serializable;
import java.util.ArrayList;

public class PostingsEntry implements Comparable<PostingsEntry>, Serializable {
    
    public int docID;
    public double score;
    public ArrayList<Integer> offset=new ArrayList<Integer>();

    public PostingsEntry(int docID, ArrayList<Integer> offset) {
    }

    /**
     *  PostingsEntries are compared by their score (only relevant 
     *  in ranked retrieval).
     *
     *  The comparison is defined so that entries will be put in 
     *  descending order.
     */

    public int compareTo( PostingsEntry other ) {
	return Double.compare( other.score, score );
    }

    //
    //  YOUR CODE HERE
    //


    public PostingsEntry(int id,int off)
    {
        this.docID=id;
        offset.add(off);
    }

    public PostingsEntry(int id)
    {
        this.docID=id;
    }



}

    
