package edu.nbcc.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import edu.nbcc.dao.BookDAO;
import edu.nbcc.dao.BookDAOImpl;
import edu.nbcc.model.Book;
import edu.nbcc.model.BookModel;
import edu.nbcc.model.ErrorModel;
import edu.nbcc.util.ValidationUtil;

/**
 * Servlet implementation class test
 */
public class BookController extends jakarta.servlet.http.HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String BOOK_VIEW = "/books.jsp";
	private static final String CREATE_BOOK = "/book.jsp";
	private static final String BOOK_SUMMARY = "/bookSummary.jsp";
	private RequestDispatcher view;
	
	public RequestDispatcher getView() {
		return view;
	}
	
	public void setView(HttpServletRequest request, String viewPath) {
		view = request.getRequestDispatcher(viewPath);
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("test");
		
		Book book = new Book();
		String path = request.getPathInfo();
		BookDAO dao = new BookDAOImpl();
		
		if (path==null) {
			//request.setAttribute("vm", book.getBooks());
				
			List<Book> list = dao.findAll();
			request.setAttribute("vm", list);
			setView(request, BOOK_VIEW);
		}else {
			String[] parts = path.split("/");
			
			if (parts[1].equalsIgnoreCase("create") || ValidationUtil.isNumeric(parts[1])) {
				int id = ValidationUtil.getInteger(parts[1]);
				if (id != 0) {
					//Book bk = book.getBook(id);
					Book bk = dao.findById(id);
					if (bk != null) {
						BookModel vm = new BookModel();
						vm.setBook(bk);
						request.setAttribute("vm", vm);
					}else {
						request.setAttribute("error", new ErrorModel("Book not found"));
					}
				}else {
					//create book
					request.setAttribute("vm", new BookModel());
				}
			}else {
				//edit book
				request.setAttribute("vm", new BookModel());
			}
			setView(request,CREATE_BOOK);
		}
		getView().forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List <String> errors = new ArrayList<String>();
		BookDAO dao = new BookDAOImpl();
		Book book = new Book();
		setView(request, BOOK_SUMMARY);
		
		try {
			if (request.getParameter("bookName") == null || (request.getParameter("bookName").trim().length() == 0)) {
				errors.add("Book name is null");
			}
			
			if (request.getParameter("bookPrice") == null || (request.getParameter("bookPrice").trim().length() == 0)) {
				errors.add("Book price is null");
			}
			
			if (request.getParameter("bookTerm") == null) {
				errors.add("Book term is null");
			}
			
			int id = ValidationUtil.getInteger(request,"hdnBookId");
			String name = request.getParameter("bookName");
			double price = ValidationUtil.getDouble(request,"bookPrice",errors);
			
			int term = ValidationUtil.getTerm(request,"bookTerm",errors);
			
			if (price==0.0) {
				errors.add("Price cannot be 0");
			}
			
			if (errors.size() == 0) {
				book.setId(id);
				book.setName(name);
				book.setPrice(price);
				book.setTerm(term);
				
				String action = request.getParameter("action").toLowerCase();
				
				switch(action) {
				case "create":
					//book = book.create();
					dao.insert(book);
					request.setAttribute("createdBook", book);
					break;
				
				case "save":
					int saveId = dao.update(book);
					if (saveId > 0) {
						request.setAttribute("savedBook", book);
					}else {
						BookModel vm = new BookModel();
						vm.setBook(book);
						request.setAttribute("vm",vm);
						request.setAttribute("error",new ErrorModel("Book does not exist to create"));
						setView(request,CREATE_BOOK);
					}
					break;
					
				case "delete":
					int deleteId = dao.delete(id);
					if (deleteId > 0) {
						request.setAttribute("deletedBook", book);
					}else {
						BookModel vm = new BookModel();
						vm.setBook(book);
						request.setAttribute("vm",vm);
						request.setAttribute("error",new ErrorModel("Book does not exist to delete"));
						setView(request,CREATE_BOOK);
					}
					break;
				}
			}else {
				setView(request,CREATE_BOOK);
				ErrorModel errorModel = new ErrorModel();
				errorModel.setErrors(errors);
				request.setAttribute("error", errorModel);
				request.setAttribute("vm", new BookModel());
			}
		}catch(Exception ex) {
			setView(request,CREATE_BOOK);
			request.setAttribute("error", new ErrorModel("An error occurred, please try again"));
		}
		getView().forward(request, response);
	}
}
