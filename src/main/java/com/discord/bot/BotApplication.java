package com.discord.bot;

import com.discord.bot.controller.Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@SpringBootApplication
public class BotApplication {

	public static void main(String[] args) throws IOException {
		ApplicationContext context = SpringApplication.run(BotApplication.class, args);
		BotToken bot1TokenEntity = context.getBean(BotToken.class);
		String botToken = bot1TokenEntity.getBotToken();

		JDA jda = JDABuilder.createDefault(botToken)
				.setActivity(Activity.playing("in test"))
				.setStatus(OnlineStatus.ONLINE)
				.enableIntents(GatewayIntent.MESSAGE_CONTENT)
				.addEventListeners(new Listener())
				.build();
	}
}

@Component
class BotToken {
	@Value("${discord.bot.token}")
	private String botToken;

	public String getBotToken() {
		return botToken;
	}
}