package be.isach.ultracosmetics.api;

import be.isach.ultracosmetics.cosmetics.treasurechests.ResultType;

import static be.isach.ultracosmetics.cosmetics.treasurechests.ResultType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RewardType {

    NORMAL(new RewardData(AMMO, 50, 10, 50), new RewardData(HUESITOS, 45, 1000, 2000), new RewardData(ALMAS, 5, 1000, 2000)),
    RARE(new RewardData(AMMO, 40, 20, 100), new RewardData(ALMAS, 15, 1000, 3000), new RewardData(HUESITOS, 30, 1000, 3000), new RewardData(GADGET, 10), new RewardData(HELMET, 1), new RewardData(CHESTPLATE, 1), new RewardData(LEGGINGS, 1), new RewardData(BOOTS, 2)),
    EPIC(new RewardData(AMMO, 30, 20, 10), new RewardData(ALMAS, 20, 2000, 4000), new RewardData(HUESITOS, 20, 2000, 4000), new RewardData(GADGET, 15), new RewardData(HELMET, 2), new RewardData(CHESTPLATE, 2), new RewardData(LEGGINGS, 3), new RewardData(BOOTS, 3), new RewardData(HAT, 5)),
    LEGENDARY(new RewardData(ALMAS, 20, 3000, 5000), new RewardData(HUESITOS, 20, 3000, 5000), new RewardData(GADGET, 15), new RewardData(HELMET, 2), new RewardData(CHESTPLATE, 2), new RewardData(LEGGINGS, 3), new RewardData(BOOTS, 3), new RewardData(HAT, 5), new RewardData(EMOTE, 5)),
    VOTE(new RewardData(AMMO, 60, 5, 25), new RewardData(HUESITOS, 40, 500, 1000));

    private List<RewardData> data;

    RewardType(RewardData... data) {
        this.data = new ArrayList<>(Arrays.asList(data));
    }

    public List<RewardData> getData() {
        return data;
    }

    public int getStartRange(ResultType type) {
        for(RewardData rewardData : data) {
            if(rewardData.getCategory() == type) {
                return rewardData.getStartRange();
            }
        }

        return 0;
    }

    public int getEndRange(ResultType type) {
        for(RewardData rewardData : data) {
            if(rewardData.getCategory() == type) {
                return rewardData.getEndRange();
            }
        }

        return 0;
    }

}
