package io.github.xfoundries.demo.expenseapproval.adapter.out.persistence.claim;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ExpenseItemMapper extends BaseMapper<ExpenseItemData> {

    default List<ExpenseItemData> selectByClaimId(String claimId) {
        return selectList(new QueryWrapper<ExpenseItemData>()
                .eq("claim_id", claimId)
                .orderByAsc("item_order", "id"));
    }

    default int deleteByClaimId(String claimId) {
        return delete(new QueryWrapper<ExpenseItemData>().eq("claim_id", claimId));
    }
}
