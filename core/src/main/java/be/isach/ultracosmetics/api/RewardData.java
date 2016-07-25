package be.isach.ultracosmetics.api;

import be.isach.ultracosmetics.cosmetics.treasurechests.ResultType;

public class RewardData {

    private ResultType category;
    private int percentage;
    private int startRange;
    private int endRange;

    public RewardData(ResultType category, int percentage) {
        this.category = category;
        this.percentage = percentage;
    }

    public RewardData(ResultType category, int percentage, int startRange, int endRange) {
        this.category = category;
        this.percentage = percentage;
        this.startRange = startRange;
        this.endRange = endRange;
    }

    public int getPercentage() {
        return percentage;
    }

    public ResultType getCategory() {
        return category;
    }

    public int getStartRange() {
        return startRange;
    }

    public int getEndRange() {
        return endRange;
    }
}
