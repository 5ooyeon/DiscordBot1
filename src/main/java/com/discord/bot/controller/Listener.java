package com.discord.bot.controller;

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
public class Listener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message getmsg = event.getMessage();
        String[] msg = getmsg.getContentDisplay().split("! ");
        User user = event.getAuthor();
        if(!msg[0].equals("Chamber") || user.isBot()) {
            return;
        }

        if(msg[1].contains("erase") ) {
            int N = Integer.parseInt(msg[1].replace("erase ", ""));
            int cnt = 0; int pointer1 = 0;
            List<Message> deletemsg = new ArrayList<>();
            // 채널에서 최근 10개의 메시지를 가져온 다음, 본인이 작성한 메시지만 필터링

            while(cnt < N) {
                List<Message> list = event.getChannel().asTextChannel().getHistory().retrievePast(pointer1+1).complete();
                if(list.get(pointer1).getAuthor().equals(user.getId())) {
                    deletemsg.add(list.get(pointer1)); cnt++;
                }
                pointer1++;
            }
            event.getChannel().asTextChannel().purgeMessages(deletemsg);

            event.getChannel().asTextChannel().sendMessage("Chamber erased your "+Integer.toString(N)+" messages!").queue();
        }

    }
}
