package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.domain.IntentType;
import com.aistetic.conversationallayer.domain.Message;
import com.aistetic.conversationallayer.domain.MessageType;
import org.springframework.stereotype.Service;
@Service
public class IntentDetectionService {

    public IntentType detectIntent(Message message) {
        if (message == null) {
            return IntentType.UNKNOWN;
        }
        if (message.getMessageType() == MessageType.IMAGE) {
            return IntentType.UPLOAD_IMAGE;
        }
        String content = message.getContent();

        if (content == null) {
            return IntentType.UNKNOWN;
        }
        content = content.trim().toLowerCase();


        if (content.equals("yes")
                || content.equals("approve")
                || content.equals("approved")
                || content.equals("okay")
                || content.equals("ok")) {

            return IntentType.APPROVE;
        }

        if (content.equals("no")
                || content.equals("reject")
                || content.equals("rejected")
                || content.equals("decline")) {

            return IntentType.REJECT;
        }

        if (content.equals("1")
                || content.equals("2")
                || content.equals("3")
                || content.equals("4")) {

            return IntentType.SELECT_MARKETPLACE;
        }
        if (content.equals("cancel")
                || content.equals("/cancel")) {

            return IntentType.CANCEL;
        }
        if (content.equals("help")
                || content.equals("/help")) {

            return IntentType.HELP;
        }
        return IntentType.UNKNOWN;

    }
}
