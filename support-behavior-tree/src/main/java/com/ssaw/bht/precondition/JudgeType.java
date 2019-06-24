package com.ssaw.bht.precondition;

/**
 * @author HuSen
 * create on 2019/6/21 14:26
 */
public enum JudgeType {
    // and运算
    AND("and", preConditions -> {
        boolean result = true;
        for (IsTrue pre : preConditions) {
            result = result && pre.isTrue();
        }
        return result;
    }),
    // or运算
    OR("or", preConditions -> {
        boolean result = false;
        for (IsTrue pre : preConditions) {
            result = result || pre.isTrue();
        }
        return result;
    }),
    // not运算
    NOT("not", preConditions -> {
        if (preConditions.length > 1) {
            throw new IllegalArgumentException("not arg number must have one");
        }
        return !preConditions[0].isTrue();
    }),
    // xor运算
    XOR("xor", preConditions -> {
        int length = preConditions.length;
        if (length > 1) {
            boolean result = preConditions[0].isTrue();
            for (int i = 1; i < length; i++) {
                result = result ^ preConditions[i].isTrue();
            }
            return result;
        } else {
            throw new IllegalArgumentException("arg number must lg one");
        }
    });

    private String desc;
    private Judge judge;

    JudgeType(String desc, Judge judge) {
        this.desc = desc;
        this.judge = judge;
    }

    public String getDesc() {
        return desc;
    }

    public Judge getJudge() {
        return judge;
    }

    public boolean judge(IsTrue... isTrues) {
        return this.judge.satisfy(isTrues);
    }
}
