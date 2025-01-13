package kr.or.komca.foundation.jwt.security.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("kr.or.komca.foundation.jwt.mapper")  // Mapper 인터페이스들이 있는 패키지 경로
@ComponentScan(basePackages = "kr.or.komca.foundation.jwt")
public class MapperConfig {
}
