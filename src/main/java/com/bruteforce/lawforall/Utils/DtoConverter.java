package com.bruteforce.lawforall.Utils;


import com.bruteforce.lawforall.dto.ChatResponseDto;
import com.bruteforce.lawforall.dto.SignUpRequestDto;
import com.bruteforce.lawforall.model.ChatMessage;
import com.bruteforce.lawforall.model.Role;
import com.bruteforce.lawforall.model.User;

public final class DtoConverter {

    public static User convertSignUpRequestDtoToUser(SignUpRequestDto requestDto, String password) {

        return new User.UserBuilder()
                .email(requestDto.getEmail())
                .password(password)
                .fullName(requestDto.getFullName())
                .username(requestDto.getUserName())
                .phoneNumber(requestDto.getPhoneNumber())
                .role(requestDto.getRole() != null ? requestDto.getRole() : Role.PUBLIC)
                .build();
    }

    public static ChatResponseDto convertChatMessageToChatResponseDto(ChatMessage savedChat) {

        return new ChatResponseDto();
    }
}
