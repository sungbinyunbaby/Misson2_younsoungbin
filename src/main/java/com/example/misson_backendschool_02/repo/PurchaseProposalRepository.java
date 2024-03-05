package com.example.misson_backendschool_02.repo;

import com.example.misson_backendschool_02.dto.PurchaseProposalDto;
import com.example.misson_backendschool_02.entity.PurchaseProposalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PurchaseProposalRepository extends JpaRepository<PurchaseProposalEntity, Long> {
    // 구매 제안한 사용자: 자신이 구매 제안한 모든 상품을 조회.
    @Query("SELECT p FROM PurchaseProposalEntity p WHERE p.user.username = ?1")
    List<PurchaseProposalEntity> findProposalEntityUsernameEqualsTokenUsername(String tokenUsername);
//    나의 물품의 구매제안 들어온 것을 다 확인한다.
    // p 테이블에 존재하는 p.user_id가 내 이름과 동일한 것


    // 구매 제안한 사용자: 자신이 구매 제안한 물품이면 이 상품의 제안에서 자신의 제안 문의만 볼 수 있다.
    @Query("select p from PurchaseProposalEntity p where p.user.username = ?1 and p.registerEntity.id = ?2")
    List<PurchaseProposalEntity> findUserRegisterProposal(String tokenUsername, Long registerId);

    // 판매자: 이 물건을 판매하는 사람이라면 이 물건의 모든 구매 제안을 보여준다.
    @Query("select p from PurchaseProposalEntity p where p.registerEntity.user.username = ?1")
    List<PurchaseProposalEntity> findUserRegisterProposals(String tokenUsername);

    // 판매자: 자신이 판매하는 모든 물건의 제안 상태를 확인할 수 있다.
    @Query("select p from PurchaseProposalEntity p, User u where u.username = ?1")
    List<PurchaseProposalEntity> findReceivedAllProposals(String username);

    // 판매자: 자신이 판매하는 물품중 소비자가 거래제안한 물품이 있다면 수락.
    @Query("select p " +
            "from PurchaseProposalEntity p, User u " +
            "where " +
            "u.username = ?1 and p.user.id = ?2 and p.registerEntity.id = ?3")
    Optional<PurchaseProposalEntity> findEqualsConsumerRegister(String tokenUsername, Long consumer, Long registerId);

    // 나중에 제안한 물건에 또 제안을 걸지 못하게 할거기 때문에 이걸 만든다.
    // 나중에 위처럼 고치면 이걸로 바꿔야 한다.
    @Query("select p from PurchaseProposalEntity p where p.user.username = ?1 and p.registerEntity.id = ?2")
    Optional<PurchaseProposalEntity> findConsumerRegisterProposal(String tokenUsername, Long registerId);

    // 구매 확정이 될 경우, 해당 물품의 확정 되지 않은 다른 구매 제안의 상태는 모두 거절이 된다.
    @Query("select p from PurchaseProposalEntity p where p.registerEntity.id = ?1")
    List<PurchaseProposalEntity> findOtherRegisterProposal(Long registerId);

}
