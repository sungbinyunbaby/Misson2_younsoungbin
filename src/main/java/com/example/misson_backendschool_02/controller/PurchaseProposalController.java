package com.example.misson_backendschool_02.controller;

import com.example.misson_backendschool_02.dto.PurchaseConfirmation;
import com.example.misson_backendschool_02.dto.PurchaseProposalDto;
import com.example.misson_backendschool_02.entity.PurchaseProposalEntity;
import com.example.misson_backendschool_02.service.PurchaseProposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proposal")
@Slf4j
@RequiredArgsConstructor
public class PurchaseProposalController {
    private final PurchaseProposalService service;

    @PostMapping("/purchase")
    public String proposal(
            @RequestParam("registerId") Long registerId
    ) {
        log.info("*** 구매 제안을 실행합니다. ***");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        PurchaseProposalDto dto
                = service.proposal(username, registerId);
        return dto.toString();
    }

    // selectUserRegisterProposals: 판매자는 자신의 모든 물품의 구매제안 상태를 볼 수 있다.
    // 거래 제안 상태 보기:
    // 1. 이 상품의 판매자라면 이 상품의 모든 제안을 볼 수 있다.
    // 2. 이 상품에 제안을 한 사용자라면 이 상품의 제안 목록에서 자신의 제안만 확인 할 수 있다.

    // 거래 제안 상태 보기
    @GetMapping("/selectProposals")
    public String selectProposal(
            @RequestParam("registerId") Long registerId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<PurchaseProposalDto> dto
                = service.selectProposal(username, registerId);

        return dto.toString();
    }

//    @GetMapping("/findAll")
//    public String selectProposal123(
//            @RequestParam("registerId") Long registerId
//    ) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        List<String> dto
//                = service.selectProposal123(username, registerId);
//
//        return dto.toString();
//    }
      // 사용자는 자신이 판매 중인 모든 물품의 구매 제안 상태를 볼 수 있다.
        @GetMapping("/selectReceiveAllProposals")
    public String selectReceiveAllProposal() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<PurchaseProposalDto> dto
                = service.selectReceiveAllProposal(username);

        return dto.toString();
    }

    // 판매자는 구매제안을 한 사용자의 제안을 수락한다.
    @PostMapping("/acceptProposal")
    public String acceptProposal(
            @RequestParam("consumer") Long consumerId,
            @RequestParam("registerId") Long registerId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String decision = "수락";

        PurchaseProposalDto dto = service.decisionProposal(username, consumerId, registerId, decision);

        return dto.toString();
    }

    // 판매자는 구매제안을 한 사용자의 제안을 거절한다.
    @PostMapping("/rejectProposal")
    public String rejectProposal(
            @RequestParam("consumer") Long consumerId,
            @RequestParam("registerId") Long registerId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String decision = "거절";

        PurchaseProposalDto dto = service.decisionProposal(username, consumerId, registerId, decision);

        return dto.toString();
    }

    // 소비자는 자신이 제안한 물품의 상태가 수락이면 구매 확정을 할 수 있다.
    // 구매 확정을 하면 대상 물품의 상태는 판매완료가 된다.
    // 구매 확정이 되면 해당 물품의 다른 제안의 상태는 거절이 된다.
    @PostMapping("/purchaseConfirmation")
    public String confirmation(
            @RequestParam("registerId") Long registerId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PurchaseConfirmation dto = service.confirmation(username, registerId);

        return dto.toString();
    }

}
