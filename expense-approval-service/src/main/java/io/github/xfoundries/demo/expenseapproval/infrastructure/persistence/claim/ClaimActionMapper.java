package io.github.xfoundries.demo.expenseapproval.infrastructure.persistence.claim;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ClaimActionMapper extends BaseMapper<ClaimActionData> {

    default List<ClaimActionData> selectByClaimId(String claimId) {
        return selectList(new QueryWrapper<ClaimActionData>()
                .eq("claim_id", claimId)
                .orderByAsc("sequence_no", "id"));
    }
}
