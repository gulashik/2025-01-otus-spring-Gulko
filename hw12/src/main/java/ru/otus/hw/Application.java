package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println(
			"Чтобы перейти на страницу сайта открыть: \nhttp://localhost:8080 \n\n" +
			"Чтобы перейти в H2-condole открыть \nhttp://localhost:8080/h2-console " +
				"\n\turl=jdbc:h2:mem:maindb " +
				"\n\tuser=sa password=<empty>"
		);
	}
}
