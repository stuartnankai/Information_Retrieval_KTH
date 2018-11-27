/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 *   Additions: Hedvig Kjellström, 2012-14
 */


package ir;

import java.util.*;


/**
 * Implements an inverted index as a Hashtable from words to PostingsLists.
 */
public class HashedIndex implements Index {

    /**
     * The index as a hashtable.
     */
    private HashMap<String, PostingsList> index = new HashMap<String, PostingsList>();


    /**
     * Inserts this token in the index.
     */
    public void insert(String token, int docID, int offset) {  // Try to find the token from the postinglist, if not, add a new list.
        //
        //  YOUR CODE HERE
        //  try to find the token

        if (index.containsKey(token)) {  // if have the same token
            this.index.get(token).insertInto(token, docID, offset); //

        } else {// no token put the new token inside
            PostingsList r = new PostingsList(docID, offset);
            this.index.put(token, r);

            //this.index.get(token).newList(token,docID,offset);
            //PostingsList newPost = new PostingsList(docID,offset)
        }
    }


    /**
     * Returns all the words in the index.
     */
    public Iterator<String> getDictionary() {
        //
        //  REPLACE THE STATEMENT BELOW WITH YOUR CODE
        //
        return null;
    }


    /**
     * Returns the postings for a specific term, or null
     * if the term is not in the index.
     */
    public PostingsList getPostings(String token) {
        //
        //  REPLACE THE STATEMENT BELOW WITH YOUR CODE
        //
        return null;
    }


    /**
     * Searches the index for postings matching the query.
     */
    public PostingsList search(Query query, int queryType, int rankingType, int structureType) {
        //
        //  REPLACE THE STATEMENT BELOW WITH YOUR CODE
        //


        if (isHaveAllToken(query)) { // make sure that all tokens are in the index

            int theSizeQuery = query.terms.size();

            PostingsList answer = new PostingsList();


            if (theSizeQuery == 1) { // only one token

                return index.get(query.terms.getFirst());

            } else if (theSizeQuery > 1) {

                String key1 = query.terms.getFirst();  // get the first token
                String key2 = query.terms.get(1); // get the second token

                int key1Size = index.get(key1).list.size();
                int key2Size = index.get(key2).list.size();

                for (int i = 0, j = 0; i < key1Size && j < key2Size; ) { //compare the first and  the second
                    if (index.get(key1).list.get(i).docID == index.get(key2).list.get(j).docID) {
                        PostingsEntry docIDBoth = new PostingsEntry(index.get(key1).list.get(i).docID);
                        docIDBoth.offset = index.get(key1).list.get(i).offset;
                        answer.list.add(docIDBoth);
                        i++;
                        j++;
                    } else if (index.get(key1).list.get(i).docID < index.get(key2).list.get(j).docID) {
                        i++;
                    } else {
                        j++;
                    }
                }
                if (theSizeQuery > 2) { // Compare with the previous result
                    for (int m = 2; m < theSizeQuery; m++) {
                        PostingsList newAnswer2 = new PostingsList(); //Using new postinglist to save temp result
                        for (int d = 0, e = 0; d < answer.list.size() && e < index.get(query.terms.get(m)).list.size(); ) {
                            if (answer.list.get(d).docID == index.get(query.terms.get(m)).list.get(e).docID) {
                                PostingsEntry docIDAll = new PostingsEntry(answer.list.get(d).docID);
                                docIDAll.offset = answer.list.get(d).offset;
                                newAnswer2.list.add(docIDAll);

                                d++;
                                e++;
                            } else if (answer.list.get(d).docID < index.get(query.terms.get(m)).list.get(e).docID) {
                                d++;
                            } else {
                                e++;
                            }
                        }
                        answer = newAnswer2;

                    }

                }

            }

            if (queryType == Index.PHRASE_QUERY) { //If using phrase query

                if (theSizeQuery == 1) { //the same as interaction query
                    return index.get(query.terms.getFirst());

                } else if (theSizeQuery > 1) {

                    for (int theOrderOfToken = 0; theOrderOfToken < (query.terms.size() - 1); theOrderOfToken++) { //compare with the first and the second token

                        PostingsList newAnswer4 = new PostingsList(); //Using new postinglist to save temp result

                        for (int theNumOfdocID = 0; theNumOfdocID < answer.list.size(); theNumOfdocID++) { // Find how many docID in the answer.list

                            int tempdocID = answer.list.get(theNumOfdocID).docID; // From the first docID


                            ArrayList<Integer> temp1 = getdocIDOffset(tempdocID, theOrderOfToken, query);// find the offset from the index with the same docID (tempdocID)

                            ArrayList<Integer> temp2 = getdocIDOffset(tempdocID, theOrderOfToken + 1, query); // find the next token's offset

                            for (int i = 0, j = 0; i < temp1.size() && j < temp2.size(); ) { //compare the two tokens' offset, if they stay together, the difference of offset should be 1 in the same docID.
                                if ((temp1.get(i) + 1) == temp2.get(j)) { // two tokens are together with the same docID
                                    //docIDForAll.add(tempdocID);
                                    PostingsEntry docIDTemp = new PostingsEntry(tempdocID);
                                    newAnswer4.list.add(docIDTemp);// save this docID
                                    //System.out.println(docIDForAll);
                                    /*PostingsEntry docIDCorrect = new PostingsEntry(tempdocID);
                                    newAnswer3.list.add(docIDCorrect);*/
                                    break;
                                } else if ((temp1.get(i) + 1) < temp2.get(j)) {
                                    i++;
                                } else {
                                    j++;
                                }
                            }
                        }
                        answer = newAnswer4;
                    }
                }

            }
            return answer;

        } else
            return null;
    }

    public ArrayList<Integer> getdocIDOffset(int newTempdocID, int theOrderOfToken, Query query) { // method to get the offset

        ArrayList<Integer> tempSaveOffset = new ArrayList<Integer>();

        for (int temp = 0; temp < index.get(query.terms.get(theOrderOfToken)).list.size(); temp++)
            if (newTempdocID == index.get(query.terms.get(theOrderOfToken)).list.get(temp).docID) { //find the docID
                tempSaveOffset = index.get(query.terms.get(theOrderOfToken)).list.get(temp).offset; //get the offset
                break;
            } else continue;
        return tempSaveOffset;
    }


    public boolean isHaveAllToken(Query query) { // index has all tokens or not
        boolean haveToken = true;
        for (int i = 0; i < query.terms.size(); i++) {
            haveToken = index.containsKey(query.terms.get(i));

        }
        return haveToken;
    }


    /**
     * No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
    }
}
