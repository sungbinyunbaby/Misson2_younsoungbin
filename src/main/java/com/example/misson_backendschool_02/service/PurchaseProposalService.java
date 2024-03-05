package com.example.misson_backendschool_02.service;

import com.example.misson_backendschool_02.dto.PurchaseConfirmation;
import com.example.misson_backendschool_02.dto.PurchaseProposalDto;
import com.example.misson_backendschool_02.entity.PurchaseProposalEntity;
import com.example.misson_backendschool_02.entity.RegisterEntity;
import com.example.misson_backendschool_02.entity.User;
import com.example.misson_backendschool_02.repo.PurchaseProposalRepository;
import com.example.misson_backendschool_02.repo.RegisterRepository;
import com.example.misson_backendschool_02.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseProposalService {
    private final PurchaseProposalRepository proposalRepository;
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;

    // 물품을 등록한 사용자와 비활성 사용자 제외, 등록된 물품에 대하여 구매 제안을 등록할 수 있다.
    public PurchaseProposalDto proposal(String token, Long registerId) {
        // 작성자 인지 아닌지 확인.
        Boolean userCheck = userCheck(token, registerId);

        // 작성한 사용자가 아니라면 구매 제안 허용.
        if (userCheck) {
            log.info("*** 작성자가 아니므로 구매 제안이 가능합니다. ***");
            // user을 확인하고
            User username = user(token);
            // 물품을 확인하고
            RegisterEntity registerEntity = registerEntity(registerId);

            String status = "구매 제안";

            // 지정한 물품에 어느 user가 구매를 제안했는지 저장.
            PurchaseProposalEntity proposalEntity = PurchaseProposalEntity.builder()
                    .proposal(status)
                    .user(username)
                    .registerEntity(registerEntity)
                    .build();

            proposalRepository.save(proposalEntity);
            log.info("*** 구매 제안을 성공하였습니다. ***");

            log.info("*** 구매제안: {} ***", proposalEntity.getProposal());
            log.info("*** 구매제안: {} ***", proposalEntity.getUser().getUsername());
            log.info("*** 구매제안: {} ***", proposalEntity.getRegisterEntity().getUser().getUsername());
            log.info(
                    "***  \"{}\"님이 \"{}\"의 \"{}\"을 \"{}\"했습니다.***",
                    username.getUsername(), // 본인이
                    proposalEntity.getRegisterEntity().getUser().getUsername(), // 다른 누군가의
                    proposalEntity.getRegisterEntity().getTitle(), // 물건을
                    proposalEntity.getProposal() // 구매 제안 했습니다.
            );

            // Dto에 저장.
            return PurchaseProposalDto.builder()
                    .proposal(proposalEntity.getProposal())
                    .username(username.getUsername())
                    .register(registerEntity.getUser().getUsername())
                    .build();
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    // 등록된 구매 제안은 물품을 등록한 사용자와 제안을 등록한 사용자만 조회가 가능하다.

    // 구매 제안을 등록한 사용자는 자신의 제안만 확인 가능하다.
    public List<PurchaseProposalDto> selectProposal(String token, Long registerId) {
        // 물품을 등록한 사용자인지 확인.
      Boolean checkUser = userCheck(token, registerId);

      // 구매 제안한 사용자인지 확인.
      User username = user(token);
      // 있는 물건인지 확인.
      RegisterEntity registerEntity = registerEntity(registerId);

      // 이 판매 상품의 주인이 아니고 이 상품에 거래 제안을 한 사람이라면
        // proposal.findByUsername()을 하면 내가 등록한 거래 제안이 다 나올거고
        // registerId가 파라미터와 같은 것의 판매상태를 검색.
//         select p from PurchaseProposal p where p.getUser.username = ? and p.getRegister.getId() = ?

        if (!checkUser) {
            log.info("*** 물품을 등록한 사용자 입니다. 이 상품의 모든 구매 제안을 확인 합니다.***");

            // select p
            // from PurchaseProposalEntity p
            // where p.registerEntity.user.username = ?1
            // 판매자: 이 물건을 판매하는 사람이라면 이 물건의 모든 구매 제안을 보여준다.
            List<PurchaseProposalEntity> listProposals
                    = proposalRepository
                    .findUserRegisterProposals(username.getUsername());

            String explanation = username.getUsername() + "님의 " + registerEntity.getTitle() + " 상품의 제안 리스트입니다.";

            List<PurchaseProposalDto> listDto = new ArrayList<>();
            for (PurchaseProposalEntity p : listProposals) {
                listDto.add(PurchaseProposalDto.fromEntity(
                        explanation,
                        p,
                        p.getUser().getUsername(), // 거래 제안한 유저.
                        registerEntity.getTitle()) // 거래 제안을 요청 받은 아이템.
                );
                log.info("물품 판매자인 \"{}\"님의 \"{}\"을 \"{}\"한 사용자는 \"{}\"입니다.",
                        username.getUsername(),
                        registerEntity.getTitle(),
                        p.getProposal(),
                        p.getUser().getUsername()
                        );
            }
            return listDto;
        }
        // 이 상품에 제안을 등록한 사용자라면 자신의 제안만 조회 가능.
        else {

            // PurchaseProposalEntity의 user.username()에 현재 접속한 사용자의 이름이 있다면 list에 data가 들어갈 것이다.
            // 그렇다면 이 사용자는 이 상품에 제안을 등록한 사용자이다.
            // 현재 접속한 사용자의 이름이 없다면 null일 것이다.
            // ("SELECT p FROM PurchaseProposalEntity p WHERE p.user.username = ?")
            List <PurchaseProposalEntity> listProposals
                    = proposalRepository
                    .findUserRegisterProposal(username.getUsername(), registerId);

            // 구매 제안을 등록한 사용자라면 자신의 제안을 확인할 수 있다.
            if (listProposals != null) {
                log.info("*** 구매 제안을 등록한 사용자 입니다. 자신의 제안만 확인 가능합니다.");
                List<PurchaseProposalDto> listDto = new ArrayList<>();

                String explanation = username.getUsername() + " 님은 " + registerEntity.getTitle() + " 상품에 구매제안을 하였습니다.";

                for (PurchaseProposalEntity p : listProposals) {
                    listDto.add(PurchaseProposalDto.fromEntity(
                            explanation,
                            p,
                            username.getUsername(),
                            registerEntity.getTitle())
                    );
                    log.info("\"{}\"님이 \"{}\"한 물품은 \"{}\"님의 판매 상품인 \"{}\"입니다.",
                            username.getUsername(), // 님이
                            p.getProposal(), // 구매제안한 것은
                            p.getRegisterEntity().getUser().getUsername(), // 이 사람의
                            registerEntity.getTitle() // 판매 상품 입니다.
                    );
                }
                return listDto;

            } else {
                log.info("*** 물품의 판매자도 아니고 구매 제안을 한 사용자도 아닙니다. ***");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

    }

    // 유저는 자신이 판매 중인 모든 물품의 구매 제안을 볼 수 있다.
    public List<PurchaseProposalDto> selectReceiveAllProposal(String token) {
        // 사용자는
        User username = user(token);
        // 자신이 판매 중인 모든 물품의 구매 제안 상태를 볼 수 있다.
        log.info("*** 물품을 등록한 사용자 입니다. 구매 제안 전부를 확인 합니다.***");

        List<PurchaseProposalEntity> listProposals
                = proposalRepository
                .findReceivedAllProposals(username.getUsername());

        List<PurchaseProposalDto> listDto = new ArrayList<>();

        String explanation = username.getUsername() + " 님이 등록한 물품들의 구재 제안 문의 입니다.";

        for (PurchaseProposalEntity p : listProposals) {
            listDto.add(PurchaseProposalDto.fromEntity(
                    explanation,
                    p,
                    p.getUser().getUsername(),
                    p.getRegisterEntity().getTitle())
            );
            log.info("\"{}\"님의 \"{}\"을 \"{}\"한 사용자는 \"{}\"입니다.",
                    username.getUsername(), // 사용자의
                    p.getRegisterEntity().getTitle(), // 물건을
                    p.getProposal(), // 구매 제안 한 사람은
                    p.getUser().getUsername() // 이 사람 입니다.
            );
        }
        return listDto;
    }


    // 물품을 등록한 사용자는 등록된 구매 제안을 수락 또는 거절 할 수 있다.
    // 이때 구매 제안의 상태는 수락 또는 거절이 된다.
    public PurchaseProposalDto decisionProposal(String token, Long consumerId ,Long registerId, String decision) {
        User user = user(token); // 토큰 사용자가 존재하는 사용자인지 확인.
        Optional<User> findConsumer = userRepository.findById(consumerId);
        User consumer = findConsumer.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        RegisterEntity register = registerEntity(registerId); // 판매 물품이 있는지 확인.
        Boolean seller = userCheck(token, registerId);// 판매 물품의 사용자 인지 확인 false가 판매자이다.

        if (!seller) {
            // 거래 제안을 수락한다.
            // 거래 제안 엔티티의 이 메서드 파라미터인 레지스터아이디와 같은 것의 상태를 수라갛낟.
            // 판매자는 자신의 물건에 거래제안을 한 이 사람의 거래제안을 수락할 수 있다.
            // 자신의 거래제안에서 소비자가 거래제안한 물품이 있다면 확인한다.
            Optional<PurchaseProposalEntity> proposalEntity
                    = proposalRepository.findEqualsConsumerRegister(
                    user.getUsername(),
                    consumer.getId(),
                    registerId
            );

            PurchaseProposalEntity entity =
                    proposalEntity.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

            // 거래제안을한 아이디라면 수락한다.

            entity.setStatus(decision);

            proposalRepository.save(entity);

            String explanation = "구매를 " + decision + "한다.";
            return PurchaseProposalDto.fromEntity(explanation, entity, user.getUsername(), register.getTitle());
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // 제안을 등록한 사용자는 자신이 등록한 제안이 수락 상태일 경우, 구매 확정을 할 수 있다.
    // 이때 구매 제안ㅇ의 상태는 확정 상태가 된다.
    // 구매 ㅔ장ㄴ이 확정될 경우, 대상 물품의 상태는 판매완료가 된다.
    // 구매 제안이 확정될 경우, 확정되지 않은 다른 구매 제안의 상태는 모두 거절이 된다.

    // 사용자는 구매제안을 한 사람이다.
    public PurchaseConfirmation confirmation(
            String tokeUsername,
            Long registerId
    ) {
        User user = user(tokeUsername); // 존재하는 사용자인이 확인.
        RegisterEntity register = registerEntity(registerId); // 판매하는 상품인지 확인.
//        Boolean seller = userCheck(tokeUsername, registerId); // 판매자인지 아닌지 확인. 현재는 아니어야 한다.
        // proposal.user.username이 proposal.register을 구매제안한 proposal.status가 수락이라면
        // proposal.proposal을 구매제안->구매확정으로 할 수 있다.

        // 자신이 구매제안한 물품인지 확인한다.
        Optional<PurchaseProposalEntity> proposalEntity =
                proposalRepository.findConsumerRegisterProposal(user.getUsername(), registerId);
        // 자신이 구매 제안한 물품이 맞다면 proposal.status가 수락인지 거절인지 확인
        if (proposalEntity.isPresent()) {
            // proposal.status가 수락이라면 proposal.proposal을 구매확정으로 한다.
            log.info("*** 판매자가 구매 제안을 수락하였습니다. ***");
            if (proposalEntity.get().getStatus().equals("수락")) {
                proposalEntity.get().setProposal("구매 확정");
                proposalRepository.save(proposalEntity.get());
                // 구매 확정될 경우, registerEntity.status는 판매 완료가 된다.
                register.setStatus("판매 완료");
                registerRepository.save(register);
                log.info("*** 구매를 제안한 사용자가 구매 확정을 하였습니다. ***");

                // 구매 확정이 될 경우, 확정되지 않은 다른 구매 제안의 상태는 모두 거절이 된다.
                // proposal.register과 관련된 구매 제안을 확인하고 그 결과의 proposal.status는 거절이 된다.
                List<PurchaseProposalEntity> otherRegisterProposal =
                        proposalRepository.findOtherRegisterProposal(registerId);
                for (PurchaseProposalEntity p : otherRegisterProposal) {
                    p.setStatus("거절");
                    if (p.getUser().getUsername().equals(proposalEntity.get().getUser().getUsername())){
                        p.setStatus("수락");
                    }
                    proposalRepository.save(p);
                }
                return PurchaseConfirmation.builder()
//                        .proposalStatus(proposalEntity.get().getProposal())
                        .registerEntity(register)
                        .proposalEntityList(otherRegisterProposal)
                        .build();
            } else {
                log.info("*** 판매자가 구매 제안을 거절하였습니다. ***");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
            log.info("*** " +user.getUsername() + "님이 거래 제안을 한 물품이 아닙니다. ***");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }








    public Boolean userCheck(String token, Long id) {
       RegisterEntity registerEntity = registerEntity(id);
       User username = user(token);

        // 사용자와 게시글의 작성자가 같은지 같지 않은지 확인.
        log.info("*** register.getUser().getUsername(): {} ***", registerEntity.getUser().getUsername());
        log.info("*** username.getUsername(): {}", username.getUsername());

        // 게시글의 작성자인지 아닌지 확인한다.
        if (!registerEntity.getUser().getUsername()
                .equals(username.getUsername())){
            log.info("*** 게시글을 작성한 username이 아니므로 구매 제안을 할 수 있습니다. ***");
            return true;
        }
        log.info("*** 게시글을 작성한 user이므로 구매 제안을 할 수 없습니다. ***");
        return false;
    }

    // User이 맞는지 확인한다.
    public User user(String token) {
        Optional<User> tokenUsername = userRepository.findByUsername(token);
        return tokenUsername.orElseThrow(()-> new UsernameNotFoundException(token));
    }

    // 물품이 있는지 확인.
    public RegisterEntity registerEntity(Long id) {
        Optional<RegisterEntity> findRegisterEntity = registerRepository.findById(id);
        return findRegisterEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}

//    public PurchaseProposalDto selectProposal(String token, Long registerId, Long proposalId) {
//        // 물품을 등록한 사용자인지 확인.
//        Boolean checkUser = userCheck(token, registerId);
//        // 구매 제안이 있는지 확인.
//        Optional<PurchaseProposalEntity> proposal = proposalRepository.findById(proposalId);
//        PurchaseProposalEntity proposalEntity
//                = proposal.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        // 구매 제안한 사용자인지 확인.
//        User username = user(token);
//
//        // 물품을 등록한 사용자라면 모든 제안 조회 가능
//        if (!checkUser) {
//            log.info("*** 물품을 등록한 사용자가 맞습니다. 구매 제안 전부를 확인합니다.***");
//            List<PurchaseProposalEntity> allProposalEntity = proposalRepository.findAll();
//            return (PurchaseProposalDto) allProposalEntity;
//        }
//        // 제안을 등록한 사용자라면 자신의 제안만 조회 가능.
//        else if (username.getUsername()
//                .equals(proposalEntity.getUser().getUsername())) {
//            log.info("*** 구매 제안을 등록한 사용자 입니다. 자신의 제안만 확인 가능합니다.");
//            return PurchaseProposalDto.builder()
//                    .proposal(proposalEntity.getProposal())
//                    .username(username.getUsername())
//                    .build();
//        }
//        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//    }


// 물품을 등록한 사용자는 모든 제안이 확인가능하다. => sql을 사용 안 하면 이렇게 뻘짓을 하게 된다.ㅎ
//    public List<String> selectProposal123 (String token, Long registerId) {
//        // 물품을 등록한 사용자인지 확인.
//        Boolean checkUser = userCheck(token, registerId);
//
//        // 구매 제안한 사용자인지 확인.
//        User username = user(token);
//
//        // 물품을 등록한 사용자라면 모든 제안 조회 가능
//        if (!checkUser) {
//            log.info("*** 물품을 등록한 사용자가 맞습니다. 구매 제안 전부를 확인합니다.***");
//            List<PurchaseProposalEntity> list = proposalRepository.findAll();
//            List<String> proposalUsername = new ArrayList<>();
//            proposalUsername.add("물품을 등록한 사용자 " + username.getUsername() + " 입니다. 거래 제안을 모두 조회합니다.\n");
//
//            for (PurchaseProposalEntity a : list) {
//                // 내 물품에 제안이 온 것만 조회.
//                if (a.getRegisterEntity().getUser().getUsername()
//                        .equals(username.getUsername())) {
//                    proposalUsername.add("*** 내가 올린 물품의 구매 제안만 조회 *** \n");
////                    proposalUsername.add("proposal id: " + a.getId() + "\n");
////                    proposalUsername.add("구매 제안 한 id: " + a.getUser().getId() + "\n");
////                    proposalUsername.add("구매 제안 한 username: " + a.getUser().getUsername() + "\n");
//                    proposalUsername.add(
//                            "\"" + a.getRegisterEntity().getTitle() + "\""
//                            + "은 " + a.getRegisterEntity().getUser().getUsername()
//                            +  "의 물품입니다." + "\n"
//                    );
//                    proposalUsername.add(
//                            a.getRegisterEntity().getUser().getUsername() + "님의 "
//                            + "\"" + a.getRegisterEntity().getTitle() + "\"" + "의 판매 물품은 "
//                            + a.getUser().getUsername() + "님이 " + "\"" + a.getProposal() + "\"" + " 하였습니다.\n"
//                    );
//                }else log.info("*** {}는 {}가 올린 물품이 아니어서 조회를 하지 못합니다.", a.getRegisterEntity().getTitle(), username.getUsername());
//            }
//            return proposalUsername;
//        }
//        else{
//            log.info("*** 물품을 등록한 사용자가 아닙니다. 구매 제안을 확인할 수 없습니다. ***");
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
//
//    }