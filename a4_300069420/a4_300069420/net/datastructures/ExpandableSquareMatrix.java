package net.datastructures;

import java.util.ArrayList;

/**
 * Represents and Extendable Square Matrix. The matrix always maintains an n X n order. To increase the size of the matrix, the method
 * increase order should be called. This method will add a row and a column to preserve the property of the matrix as an n X n square.
 * 
 * @author Hussein Al Osman
 * @param <T> type of element stored in the Extendable Square Matrix
 */
public class ExpandableSquareMatrix <T> {

	/**
	 * Private array list that will refer to all the rows in the matrix
	 */
	private ArrayList<ArrayList<T>> rows;
	
	/**
	 * The dimension of the matrix. For an n X n square matrix, dimension is equal to n.
	 */
	private int order;
	
	public ExpandableSquareMatrix(){
		rows = new ArrayList <ArrayList<T>>();
		
		order = 0;
	}
	
	/**
	 * Increase the dimension of the square matrix. That is take an n X n matrix and make it (n+1) X (n+1) matrix
	 */
	public void increaseOrder(){
		ArrayList <T> row = new ArrayList <T>();
		
		rows.add(order, row);
		
		order++;
		
		for (ArrayList<T> r : rows){
			for (int i = r.size(); i < order; i++){
				r.add(i, null);
			}
		}
	}
	
	/**
	 *  Returns the order of the matrix
	 * @return the order of the matrix
	 */
	public int getOrder(){
		return order;
	}
	
	/**
	 * Sets an element in the matrix
	 * @param c column index
	 * @param r row index
	 * @param t element to insert
	 * @throws IndexOutOfBoundsException
	 */
	public void set (int r, int c, T t) throws IndexOutOfBoundsException{		
		checkRange(r, c);
		
		ArrayList <T> row = rows.get(r);
		row.set(c, t);
	}
	
	/**
	 * Get an element from the matrix
	 * @param c column index
	 * @param r row index
	 * @return the element at (r,c)
	 * @throws IndexOutOfBoundsException
	 */
	public T get (int r, int c) throws IndexOutOfBoundsException{
		checkRange(r,c);
		
		return rows.get(r).get(c);
	}
	
	/**
	 * Removes row i and column i
	 * @param i the row and column to be removed
	 */
	public void removeColumnRow(int i){
		checkRange(i,i);
		
		for (ArrayList<T> r : rows){
			r.remove(i);
		}
		
		rows.remove(i);
		
		order--;
	}
	
	private void checkRange(int r, int c){
		if (r < 0 || r > order)
			throw new IndexOutOfBoundsException(outOfBoundsMsg(r));
		
		if (c < 0 || c > order)
			throw new IndexOutOfBoundsException(outOfBoundsMsg(c));
	}
	
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Order: "+order;
    }
	
	
    /**
     * Print the matrix (primarily will be needed during implementation)
     */
	public void printMatrix(){
		for (ArrayList<T> row : rows){
			for (T t : row){
				System.out.print((t==null? "NULL": t.toString())+"\t");
			}
			System.out.println();
		}
		
		System.out.println();
	}
	
	public static void main (String [] args){
		ExpandableSquareMatrix<String> esm = new ExpandableSquareMatrix<String>();
		esm.increaseOrder();
		esm.increaseOrder();
		esm.increaseOrder();
		
		esm.set(0,0, "A");
		esm.set(1,1, "B");
		
		esm.increaseOrder();
		
		esm.set(3,3, "C");
		
		esm.increaseOrder();
		esm.increaseOrder();

		esm.increaseOrder();
		
		esm.set(6,6, "D");
		
		esm.removeColumnRow(1);
		
		esm.printMatrix();
		
	}
	
	
}
