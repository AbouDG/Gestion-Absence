package sn.uasz.gestionConge.gestionConge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients("sn.uasz.gestionConge.gestionConge.Client")
public class GestionCongeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionCongeApplication.class, args);
	}

}
