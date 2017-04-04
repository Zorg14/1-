package hello;

import java.io.Serializable;

public class Reservation implements Serializable{
 
	private static final long serialVersionUID = 314666154476175010L;
	private long number;
	private int place;
	private Session session;
	
	
	Reservation(long number,int place,Session session)
	{
		this.number = number;
		this.place = place;
		this.session = session;
	}
	
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	public int getPlace() {
		return place;
	}
	public void setPlace(int place) {
		this.place = place;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}

}
