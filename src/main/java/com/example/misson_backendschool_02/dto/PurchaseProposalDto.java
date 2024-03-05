package com.example.misson_backendschool_02.dto;

import com.example.misson_backendschool_02.entity.PurchaseProposalEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseProposalDto {
    private Long id;
    private String proposal;
    private String username;
    private String register;
    private String status;
    private String explanation;

    public static PurchaseProposalDto fromEntity(
            String explanation,
            PurchaseProposalEntity entity,
            String username,
            String registerTitle) {
        return PurchaseProposalDto.builder()
                .explanation(explanation)
                .id(entity.getId())
                .proposal(entity.getProposal())
                .status(entity.getStatus())
                .username(username)
                .register(registerTitle)
                .build();
    }

}
