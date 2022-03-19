/**
 * 
 */
package edu.nbcc.dao;

import java.util.List;

import edu.nbcc.model.Book;

/**
 * @author Maria Ocampo
 *
 */
public interface BookDAO {
	
	/**
	 * delete a book
	 * @param d
	 * @return
	 */
	public int delete(int d);
	
	/**
	 * insert a book
	 * @param book
	 * @return
	 */
	public int insert (Book book);
	
	/**
	 * update a book
	 * @param book
	 * @return
	 */
	public int update (Book book);
	
	/**
	 * find all books
	 * @return
	 */
	public List<Book> findAll();
	
	/**
	 * get book by name
	 * @param name
	 * @return
	 */
	public Book findByName(String name);
	
	public Book findById(int id);
}
