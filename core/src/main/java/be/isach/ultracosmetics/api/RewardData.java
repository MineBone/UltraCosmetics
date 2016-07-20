package be.isach.ultracosmetics.api;

import be.isach.ultracosmetics.cosmetics.treasurechests.ResultType;

public class RewardData {

    private ResultType category;
    private int percentage;

    public RewardData(ResultType category, int percentage) {
        this.category = category;
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }

    public ResultType getCategory() {
        return category;
    }
}
