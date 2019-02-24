package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gamerepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {

			Player player1 = new Player("cobrian@ctu.gov", passwordEncoder().encode("42") );
			Player player2 = new Player("jbauer@ctu.gov", passwordEncoder().encode("24"));
			Player player3 = new Player ("kim_bauer@gmail.com", passwordEncoder().encode("kb"));
			Player player4 = new Player ("t.almeida@ctu.gov", passwordEncoder().encode("mole"));
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);

			Date date = new Date();
			Date date1 = date.from(date.toInstant().plusSeconds(3600));
			Date date2 = date1.from(date1.toInstant().plusSeconds(3600));
			Date date3 = date2.from(date1.toInstant().plusSeconds(3600));
			Date date4 = date3.from(date1.toInstant().plusSeconds(3600));
			Date date5 = date4.from(date1.toInstant().plusSeconds(3600));
			Date date6 = date5.from(date1.toInstant().plusSeconds(3600));
			Date date7 = date6.from(date1.toInstant().plusSeconds(3600));

			Game game1 = new Game(date);
			Game game2 = new Game (date1);
			Game game3 = new Game (date2);
			Game game4 = new Game (date3);
			Game game5 = new Game (date4);
			Game game6 = new Game (date5);
			Game game7 = new Game (date6);
			Game game8 = new Game (date7);

			gamerepository.save(game1);
			gamerepository.save(game2);
			gamerepository.save(game3);
			gamerepository.save(game4);
			gamerepository.save(game5);
			gamerepository.save(game6);
			gamerepository.save(game7);
			gamerepository.save(game8);

			GamePlayer gamePlayer1 = new GamePlayer(game1,player1, date);
			GamePlayer gamePlayer2 = new GamePlayer(game1,player2, date);

			GamePlayer gamePlayer3 = new GamePlayer(game2,player1, date1);
			GamePlayer gamePlayer4 = new GamePlayer(game2,player2, date1);

            GamePlayer gamePlayer5 = new GamePlayer(game3,player1, date2);
            GamePlayer gamePlayer6 = new GamePlayer(game3,player4, date2);

			GamePlayer gamePlayer7 = new GamePlayer(game4,player1, date3);
			GamePlayer gamePlayer8 = new GamePlayer(game4,player2, date3);

			GamePlayer gamePlayer9 = new GamePlayer(game5,player3, date4);
			GamePlayer gamePlayer10 = new GamePlayer(game5,player4, date4);

			GamePlayer gamePlayer11 = new GamePlayer(game6,player3, date5);


			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
            gamePlayerRepository.save(gamePlayer6);
			gamePlayerRepository.save(gamePlayer7);
			gamePlayerRepository.save(gamePlayer8);
			gamePlayerRepository.save(gamePlayer9);
			gamePlayerRepository.save(gamePlayer10);
			gamePlayerRepository.save(gamePlayer11);

			List<String> locations1 = new ArrayList<>();
			locations1.add("H2");
			locations1.add("H3");
			locations1.add("H4");

			List<String> locations2 = new ArrayList<>();
			locations2.add("A1");
			locations2.add("A2");
			locations2.add("A3");
			locations2.add("A4");

			List<String> locations3 = new ArrayList<>();
			locations3.add("C4");
			locations3.add("C5");

			Ship ship1 = new Ship ("Carrier", gamePlayer1, locations1);
			Ship ship2 = new Ship ("Battleship", gamePlayer2, locations3);
			Ship ship3 = new Ship ("Submarine", gamePlayer3, locations2);
			Ship ship4 = new Ship ("Destroyer", gamePlayer4, locations1);
			Ship ship5 = new Ship ("PatrolBoat", gamePlayer5, locations2);

			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);


			List<String> salvoLocations1 = new ArrayList<>();
			salvoLocations1.add("G9");
			salvoLocations1.add("H9");
			salvoLocations1.add("I9");

			List<String> salvoLocations2 = new ArrayList<>();
			salvoLocations2.add("J5");
			salvoLocations2.add("J6");
			salvoLocations2.add("J7");
			salvoLocations2.add("J8");

			List<String> salvoLocations3 = new ArrayList<>();
			salvoLocations3.add("D4");
			salvoLocations3.add("E4");

			List<String> salvoLocations4 = new ArrayList<>();
			salvoLocations4.add("H2");
			salvoLocations4.add("H3");


			Salvo salvo1 = new Salvo(1, gamePlayer1, salvoLocations1);
			Salvo salvo2 = new Salvo(1, gamePlayer2, salvoLocations4);
			Salvo salvo3 = new Salvo(2, gamePlayer1, salvoLocations3);
			Salvo salvo4 = new Salvo(2, gamePlayer2, salvoLocations4);
			Salvo salvo5 = new Salvo(3, gamePlayer6, salvoLocations1);
			Salvo salvo6 = new Salvo(3, gamePlayer4, salvoLocations3);

			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);
			salvoRepository.save(salvo4);
			salvoRepository.save(salvo5);
			salvoRepository.save(salvo6);


			Date finishDate1 = date.from(date.toInstant().plusSeconds(1800));
			Date finishDate2 = date1.from(date.toInstant().plusSeconds(1800));
			Date finishDate3 = date2.from(date1.toInstant().plusSeconds(1800));
			Date finishDate4 = date3.from(date1.toInstant().plusSeconds(1800));
/*
			Score score1 = new Score(1, game1, player1, finishDate1);
			Score score2 = new Score(0.5d, game1, player2, finishDate1);
			Score score3 = new Score(0.5d, game2, player1, finishDate2);
			Score score4 = new Score(0.5d, game2, player2, finishDate2);
			Score score5 = new Score(1, game3, player1, finishDate3);
			Score score6 = new Score(0.5d, game3, player4, finishDate3);
            Score score7 = new Score(1, game4, player1, finishDate4);
			Score score8 = new Score(1, game4, player2, finishDate4);
*/

			Score score1 = new Score(1, game1, player1, finishDate1);
			Score score3 = new Score(0.5, game2, player1, finishDate2);
			Score score4 = new Score(0.5, game2, player2, finishDate2);
			Score score5 = new Score(1, game3, player1, finishDate3);
            Score score7 = new Score(0.5, game4, player1, finishDate4);
			Score score8 = new Score(0.5, game4, player2, finishDate4);

			scoreRepository.save(score1);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
            scoreRepository.save(score7);
			scoreRepository.save(score8);

		};
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/web/games.html").permitAll()
				.antMatchers("/web/**").permitAll()
				.antMatchers("/api/games").permitAll()
				.antMatchers("/api/players").permitAll()
				.antMatchers("/api/game_view/*").hasAnyAuthority("USER")
				.antMatchers("/rest/*").denyAll()
				.anyRequest().permitAll();

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
  		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
 		 http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
  		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
  		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
  		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		}

		private void clearAuthenticationAttributes(HttpServletRequest request) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			}
		}


}