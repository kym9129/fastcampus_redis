package com.example.helloworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ChatService implements MessageListener {

    @Autowired
    private RedisMessageListenerContainer container;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public void enterChatRoom(String chatRoomName) {
        // ChatService가 MessageListener를 구현하고 있으므로 this를 넣음
        container.addMessageListener(this, new ChannelTopic(chatRoomName));

        // 사용자 입력 받음
        Scanner in = new Scanner(System.in);
        while(in.hasNextLine()){
            String line = in.nextLine();
            if(line.equals("q")) {
                System.out.println("Quit...");
                break;
            }

            // 한 줄 입력할 때마다 메시지 전송하기
            redisTemplate.convertAndSend(chatRoomName, line);
        }

        // 종료 시 리스너 제거
        container.removeMessageListener(this);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("Message: " + message.toString());
    }
}
