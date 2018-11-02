package com.anbang.qipai.fangpaomajiang.plan.dao;

import com.anbang.qipai.fangpaomajiang.plan.bean.MemberGoldBalance;

public interface MemberGoldBalanceDao {

	void save(MemberGoldBalance memberGoldBalance);

	MemberGoldBalance findByMemberId(String memberId);
}
