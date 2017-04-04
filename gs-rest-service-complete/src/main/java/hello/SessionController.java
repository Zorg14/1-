package hello;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream; 
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

	private final AtomicLong counter = new AtomicLong();
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private List<Session> sessions;
	private List<Reservation> reservetions;

	@SuppressWarnings("unchecked")
	SessionController() throws IOException, ClassNotFoundException
	{
		sessions = new LinkedList<>();
		reservetions = new LinkedList<>();
		in = new ObjectInputStream(new FileInputStream("schedule.dat"));
		sessions = (List<Session>) in.readObject();

		File file = new File("reservation.dat");

		if (file.exists()) {
			in = new ObjectInputStream(new FileInputStream(file));
			reservetions = (List<Reservation>) in.readObject();
			counter.set(reservetions.get(reservetions.size()-1).getNumber());
		}

		in.close();
	}
	
	@RequestMapping(value = "/reservation", method = RequestMethod.GET)
	public String viewReservation() throws IOException {

		String result = new String();

		for (int index = 0; index < reservetions.size(); index++) {
			result += " " + reservetions.get(index).getNumber() + " " + reservetions.get(index).getPlace() + " "
					+ reservetions.get(index).getSession().getName() + "\n";
		}

		return result;

	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addReservation(@RequestParam(value = "film", defaultValue = "0") int film,
			@RequestParam(value = "place", defaultValue = "0") int place) throws IOException {
		try {

			Session session = sessions.get(film);
			Reservation reserv = new Reservation(counter.incrementAndGet(), place, session);
			Reservation temp;
			boolean Free = true;
			
            for(int index = 0; index < reservetions.size(); index++)
            {            
            	temp = reservetions.get(index); 
                 if(temp.getPlace() == place && temp.getSession().equals(session))
                 {
                	 System.out.println("Это место уже занято");
                	 Free = false;
                     break;
                 }                
            }
			if(Free)
			{
				reservetions.add(reserv);

			out = new ObjectOutputStream(new FileOutputStream("reservation.dat"));
			out.writeObject(reservetions);

			return "Забронировано место " + reserv.getPlace() + " с номером бронирования " + reserv.getNumber()
					+ " На фильм :" + reserv.getSession().getName();
			}
			else
				return " место " + reserv.getPlace() +  " На фильм :" + reserv.getSession().getName() 
						+ "нельзя забронировать"; 
		} catch (Exception e) {
			return "Ошибка бронирования ";
		}

		finally {
			in.close();
			out.close();
		}
	}
	
	@RequestMapping(value = "/schedule", method = RequestMethod.GET)
	public String viewSchedule() throws IOException {

		String result = new String();

		for (int index = 0; index < sessions.size(); index++) {
			result += index + " " + sessions.get(index).getName() + " " + sessions.get(index).getDate().getTime() + " "
					+ sessions.get(index).getCost() + " р\n";
		}

		return result;

	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String viewReservation(@RequestParam(value = "number", defaultValue = "0") int number) throws IOException {
		try {
			Reservation reserv;
			for (int index = 0; index < reservetions.size(); index++) {
				reserv = reservetions.get(index);
				if (reserv.getNumber() == number)

					return "По номеру " + number + " заказано место " + reserv.getPlace() + " на фильм "
							+ reserv.getSession().getName() + " Время " + reserv.getSession().getDate().getTime();

			}

			return "Броней нет";

		} catch (Exception ex) {
			return "Невозможно выполнить";
		}

	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public String cancelReservation(@RequestParam(value = "number", defaultValue = "0") int number) throws IOException {
		try {
			
			Reservation reserv;
			for (int index = 0; index < reservetions.size(); index++) {
				reserv = reservetions.get(index);
				if (reserv.getNumber() == number) {

					reservetions.remove(index);

					out = new ObjectOutputStream(new FileOutputStream("reservation.dat"));
					out.writeObject(reservetions);
					out.close();

					return "Бронирование по номеру " + number + "отменено.";
				}
			}
			return "Нет забронированных мест.";
		} catch (Exception ex) {
			return "Невозможно выполнить";
		}

	}

}
