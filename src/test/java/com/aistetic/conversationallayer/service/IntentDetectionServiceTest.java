package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.domain.IntentType;
import com.aistetic.conversationallayer.domain.Message;
import com.aistetic.conversationallayer.domain.MessageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntentDetectionServiceTest {

    private final IntentDetectionService intentDetectionService =
            new IntentDetectionService();

    @Test
    void shouldDetectApproveIntent() {

        Message message = new Message();
        message.setMessageType(MessageType.TEXT);
        message.setContent("yes");

        IntentType result =
                intentDetectionService.detectIntent(message);

        assertEquals(IntentType.APPROVE, result);
    }

    @Test
    void shouldDetectRejectIntent() {

        Message message = new Message();
        message.setMessageType(MessageType.TEXT);
        message.setContent("no");

        IntentType result =
                intentDetectionService.detectIntent(message);

        assertEquals(IntentType.REJECT, result);
    }

    @Test
    void shouldDetectMarketplaceIntent() {

        Message message = new Message();
        message.setMessageType(MessageType.TEXT);
        message.setContent("4");

        IntentType result =
                intentDetectionService.detectIntent(message);

        assertEquals(IntentType.SELECT_MARKETPLACE, result);
    }

    @Test
    void shouldDetectImageIntent() {

        Message message = new Message();
        message.setMessageType(MessageType.IMAGE);

        IntentType result =
                intentDetectionService.detectIntent(message);

        assertEquals(IntentType.UPLOAD_IMAGE, result);
    }

    @Test
    void shouldDetectCancelIntent() {

        Message message = new Message();
        message.setMessageType(MessageType.TEXT);
        message.setContent("cancel");

        IntentType result =
                intentDetectionService.detectIntent(message);

        assertEquals(IntentType.CANCEL, result);
    }

    @Test
    void shouldDetectHelpIntent() {

        Message message = new Message();
        message.setMessageType(MessageType.TEXT);
        message.setContent("help");

        IntentType result =
                intentDetectionService.detectIntent(message);

        assertEquals(IntentType.HELP, result);
    }

    @Test
    void shouldReturnUnknownForUnknownMessage() {

        Message message = new Message();
        message.setMessageType(MessageType.TEXT);
        message.setContent("hello there");

        IntentType result =
                intentDetectionService.detectIntent(message);

        assertEquals(IntentType.UNKNOWN, result);
    }
}