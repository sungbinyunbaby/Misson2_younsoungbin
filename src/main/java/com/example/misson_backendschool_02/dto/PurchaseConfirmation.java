package com.example.misson_backendschool_02.dto;

import com.example.misson_backendschool_02.entity.PurchaseProposalEntity;
import com.example.misson_backendschool_02.entity.RegisterEntity;
import lombok.*;

import java.util.List;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PurchaseConfirmation {
//    private String proposalStatus;
    private RegisterEntity registerEntity;
    private List<PurchaseProposalEntity> proposalEntityList;
}
