package kr.or.komca.foundation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KomcaJwtVerificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(KomcaJwtVerificationApplication.class, args);
	}
}
