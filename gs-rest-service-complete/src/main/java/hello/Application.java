package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.net.HttpURLConnection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws MalformedURLException, IOException {
		SpringApplication.run(Application.class, args);

		HttpURLConnection connect;

		String line;
		String buf, buf2;
		BufferedReader br;

		while (true) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Для продолжения введите номер пункта и нажмите кнопку Enter");
			System.out.println("1) Просмотреть расписание сеансов");
			System.out.println("2) Просмотреть имеющиеся брони");
			System.out.println("3) Добавление бронирование место");
			System.out.println("4) Поиск бронирования");
			System.out.println("5) Отменить бронирование");
			System.out.println("6) Выход");

			switch (sc.nextLine()) {
			
			case "1":
				
				connect = (HttpURLConnection) new URL(HTTPquery.VIEW_SCHEDULE).openConnection();
				br = new BufferedReader(new InputStreamReader(connect.getInputStream()));

				while ((line = br.readLine()) != null) 
				{
					System.out.println(line);
				}
				sc.nextLine();

				break;
				
			case "2":
								
				connect = (HttpURLConnection) new URL(HTTPquery.VIEW_RESERVATION).openConnection();
				br = new BufferedReader(new InputStreamReader(connect.getInputStream()));

				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				sc.nextLine();
				
				break;
				
			case "3":		
				
				System.out.println("Введите ID фильма и бронируемое место:");
				buf = sc.nextLine();
				buf2 = sc.nextLine();

				connect = (HttpURLConnection) new URL(HTTPquery.ADD_RESERVATION + "?film=" + buf + "&&place=" + buf2)
						.openConnection();
				br = new BufferedReader(new InputStreamReader(connect.getInputStream()));

				while ((line = br.readLine()) != null) 
				{
					System.out.println(line);
				}
				sc.nextLine();
				
				break;
				
			case "4":
				
				System.out.println("Введите номер бронирования:");
				buf = sc.nextLine();

				connect = (HttpURLConnection) new URL(HTTPquery.SEARCH_RESERVATION + "?number=" + buf).openConnection();
				br = new BufferedReader(new InputStreamReader(connect.getInputStream()));

				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				sc.nextLine();
				
				break;
				
			case "5":
				
				System.out.println("Введите номер брони для его отмены:");
				buf = sc.nextLine();

				connect = (HttpURLConnection) new URL(HTTPquery.DELETE_RESERVATION + "?number=" + buf).openConnection();
				br = new BufferedReader(new InputStreamReader(connect.getInputStream()));

				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				sc.nextLine();
				
				break;
				
			case "6":
				sc.close();
				System.exit(1);
				break;
				
			default:
				System.out.println("Неверный ввод, повторите заново!");
			}

		}

	}
}
