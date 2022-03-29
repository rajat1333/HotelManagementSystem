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


	public void run(String... args) {
	}


}
