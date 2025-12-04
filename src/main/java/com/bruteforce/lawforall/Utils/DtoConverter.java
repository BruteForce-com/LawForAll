package com.bruteforce.lawforall.Utils;


import com.bruteforce.lawforall.dto.ChatResponseDto;
import com.bruteforce.lawforall.dto.RagMetaDataInfo;
import com.bruteforce.lawforall.dto.SignUpRequestDto;
import com.bruteforce.lawforall.model.ChatMessage;
import com.bruteforce.lawforall.model.RagMetaData;
import com.bruteforce.lawforall.model.Role;
import com.bruteforce.lawforall.model.User;

public final class DtoConverter {


    // convert sign up request dto to user
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

    // convert chat message to chat response dto
    public static ChatResponseDto convertChatMessageToChatResponseDto(ChatMessage savedChat) {
        return new ChatResponseDto.ChatResponseDtoBuilder()
                .conversationId(savedChat.getConversationId())
                .message(savedChat.getMessage())
                .title(savedChat.getTitle())
                .role(savedChat.getRole())
                .build();
    }

    // convert rag meta data to rag meta data info
    public static RagMetaDataInfo convertRagMetaDataToRagMetaDataInfo(RagMetaData ragMetaData) {
        return new RagMetaDataInfo.RagMetaDataInfoBuilder()
                .fileName(ragMetaData.getFileName())
                .dateOfUpload(ragMetaData.getDateOfUpload())
                .build();
    }
}
