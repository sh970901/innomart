package com.hun.market.member.event;

import com.hun.market.message.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class MemberEventListener {

    private final MailService naverMailService;

    @EventListener
    public void handleOrderCompletedEvent(MbDeductCoinEvent event) {
        event.process();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleMbRestPassword(MbResetRandomPasswordEvent event) throws Exception {
        event.process(naverMailService);
    }

}
