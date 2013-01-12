package at.ac.tuwien.soar.swazam.server;

import jade.util.leap.Iterator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.ac.tuwien.soar.swazam.dao.UserDao;
import at.ac.tuwien.soar.swazam.entity.Request;
import at.ac.tuwien.soar.swazam.entity.User;
import at.tuwien.sa.model.entities.Fingerprint;
import net.microtrash.SearchRequest;
import net.microtrash.SearchResponse;
import net.microtrash.ServerAgent;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Server {
	
	private static ClassPathXmlApplicationContext c;
	private UserDao userDao;
	
	private Request r;
	private User requestingUser;
	private User respondingUser;
	private Set<Request> requestSet;
	private Set<Request> respondentSet;

	public static void main(String[] args) {
		
		c = new ClassPathXmlApplicationContext("config.xml", Server.class);
		Server s = new Server();
		s.run();
		
	}
	
	private void run() {
		ServerAgent sAgent = new ServerAgent();
		sAgent.setup();
		sAgent.setServerClass(this);
		
		userDao = (UserDao) c.getBean("userDao");

	}
	
	public void newRequest(SearchRequest sr) {
		requestingUser = (User) userDao.findByToken(sr.getAccessToken()).first();
		requestSet = requestingUser.getRequestsForSenderId();
		r = new Request();
		
		r.setFingerprint(sr.getFingerPrint());
		r.setUserBySenderId(requestingUser);
		r.setCreated(new Date());
		
		requestSet.add(r);
		requestingUser.setRequestsForSenderId(requestSet);
		
		log("New request from user " + requestingUser.getToken() + " added");
	}
	
	public void requestResent(SearchRequest sr) {
		requestingUser = (User) userDao.findByToken(sr.getAccessToken()).first();
		requestSet = requestingUser.getRequestsForSenderId();
		
		Iterator i = requestSet.iterator();
		while (i.hasNext()) {
			r = i.next();
			if (r.getFingerprint().equals(sr.getFingerPrint())) {
				r.setResent(new Date());
				requestSet.add(r);
				requestingUser.setRequestsForSenderId(requestSet);
			}
		}
	}
	
	public void newResponse(SearchResponse sr) {
		SearchRequest origRequest = sr.getSearchRequest();
		requestingUser = (User) userDao.findByToken(origRequest.getAccessToken()).first();
		requestSet = requestingUser.getRequestsForSenderId();
		respondingUser = (User) userDao.findByToken(sr.getRespondentToken()).first();
		respondentSet = respondingUser.getRequestsForSolverId();
		
		Iterator i = requestSet.iterator();
		while (i.hasNext()) {
			r = i.next();
			if (r.getFingerprint().equals(sr.getFingerPrint())) {
				r.setSolved(new Date());
				r.setUserBySolverId(respondingUser);
				r.setResponse(sr);
				
				requestSet.add(r);
				requestingUser.setRequestsForSenderId(requestSet);
				
				respondentSet.add(r);
				respondingUser.setRequestsForSolverId(respondentSet);
				
				log("Request from " + requestingUser.getToken() + " answered by " + respondingUser.getToken());
			}
		}
 
		
	}
}
