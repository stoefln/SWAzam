package server;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import lib.entities.Request;
import lib.entities.SearchRequest;
import lib.entities.User;
import lib.dao.RequestDAO;
import lib.dao.UserDAO;

public class ServerStorageTester {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserDAO userDAO = new UserDAO();
		RequestDAO requestDAO = new RequestDAO();
		/*
		//Create new user
		User user = new User();
		user.setUsername("Test_user");
		user.setPassword("test_password");
		user.setToken("12345");
		System.out.println("User created");
		
		//Store user
		userDAO.persist(user);
		System.out.println("User stored");
		
		SearchRequest sr = new SearchRequest();
		sr.setAcessToken("12345");
		
		//Testing getUserBySenderId() method
		User requestingUser = (User) userDAO.findByToken(sr.getAccessToken()).get(0);
		System.out.println("\nfindByToken-test: Username " + requestingUser.getUsername());
		
		//Adding to the requestSet
		Request r = new Request(requestingUser, new Date());
		requestDAO.persist(r);
		Request r2 = new Request(requestingUser, new Date());
		requestDAO.persist(r2);
		
		Set<Request> requestingSet = requestingUser.getRequestsForSenderId();
		
		requestingSet.add(r);
		requestingSet.add(r2);
		
		requestingUser.setRequestsForSenderId(requestingSet);
		userDAO.persist(user);
		
		//Getting the user from DB to secure requests have been added to the set
		User u = (User) userDAO.findByToken(sr.getAccessToken()).get(0);
		Set<Request> set = u.getRequestsForSenderId();
		System.out.println("Set of requests made by user: " + set);
		Iterator<Request> i = set.iterator();
		Request req = (Request) i.next();
		
		System.out.println("ID of the first request of the set: " + req.getId());
		*/
		
		User user = new User();
		user.setUsername("Test_user");
		user.setPassword("test_password");
		user.setToken("12345");
		System.out.println("User created");
		userDAO.persist(user);
		
		//Testing code from ServerAgent
		SearchRequest request = new SearchRequest();
		request.setAcessToken("12345");
	
		User requestingUser;
		Request requestDB;
		
		requestingUser = (User) userDAO.findByToken(request.getAccessToken()).get(0);
		requestDB = new Request(requestingUser, new Date());
		requestDAO.persist(requestDB);
		
		Set<Request> requestingSet = requestingUser.getRequestsForSenderId();
		requestingSet.add(requestDB);
		requestingUser.setRequestsForSenderId(requestingSet);
		userDAO.persist(requestingUser);
		
		request.setId(requestDB.getId());
		
		//Test
		User u = userDAO.findByToken(request.getAccessToken()).get(0);
		Set<Request> requestSet = u.getRequestsForSenderId();
		System.out.println("RequestingSet: " + requestingSet);
		System.out.println("ID of requestDB: " + requestDB.getId());		
		System.out.println("SearchRequest ID: " + request.getId());
	}
}