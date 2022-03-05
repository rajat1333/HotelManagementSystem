package spartanbots.v01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import spartanbots.v01.entity.Hotel;
import spartanbots.v01.repository.HotelRepository;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableMongoRepositories
public class V01Application implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(V01Application.class, args);
	}

//	void createHotelData() {
//		System.out.println("Data creation started...");
//		hotelRepo.save(new Hotel("1001", "Marriott", "New York"));
//		hotelRepo.save(new Hotel("1002", "Ritz Carlton", "San Jose"));
//		hotelRepo.save(new Hotel("1003", "Radisson", "Los Angeles"));
//		hotelRepo.save(new Hotel("1004", "Oberoi", "Chicago"));
//		hotelRepo.save(new Hotel("1005", "Marriott", "SFO"));
//		System.out.println("Data creation complete...");
//	}

	public void run(String... args) {

//		System.out.println("-------------CREATE HOTEL DATA-------------------------------\n");
//
//		createHotelData();

	}


}
