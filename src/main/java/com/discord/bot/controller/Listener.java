package com.discord.bot.controller;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.scheduledevent.ScheduledEventDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Controller
public class Listener extends ListenerAdapter implements AudioEventListener {

    private int cnt;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);

        String getmsg = event.getMessage().getContentDisplay();
        User user = event.getAuthor();

        if(getmsg.equals("Ping")) {
            event.getChannel().asTextChannel().sendMessage("Pong").queue();
        }

        if(!getmsg.contains("c!") || user.isBot()) {
            return;
        }

        String msg = getmsg.replace("c!","");

        if(msg.equals("delete") ) {

            // 채널에서 최근 10개의 메시지를 가져오기
            List<Message> messages = event.getChannel().getHistory().retrievePast(100).complete();

            // 본인이 작성한 메시지만 필터링하여 가져오기
            List<Message> ownMessages = messages.stream()
                    .filter(message -> message.getAuthor().getId().equals(user.getId()))
                    .collect(Collectors.toList());

            event.getChannel().asTextChannel().purgeMessages(ownMessages);

            cnt = ownMessages.size();

            event.getChannel().asTextChannel().sendMessage("Chamber erased your " + cnt+ " messages!").queue();
        } else if(msg.equals("delete all")) {
            // 채널에서 최근 10개의 메시지를 가져오기
            List<Message> messages = event.getChannel().getHistory().retrievePast(100).complete();
            cnt = messages.size();

            event.getChannel().asTextChannel().purgeMessages(messages);

            event.getChannel().asTextChannel().sendMessage(cnt+" messages cleared!").queue();
        } else if(msg.contains("play")) {
            AudioPlayer player = playerManager.createPlayer();


        }

    }

    @Override
    public void onEvent(AudioEvent event) {

    }
}
