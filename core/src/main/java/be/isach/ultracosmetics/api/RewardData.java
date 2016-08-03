package be.isach.ultracosmetics.api;

import be.isach.ultracosmetics.cosmetics.treasurechests.ResultType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RewardData {

    private ResultType category;
    private int percentage;
    private int startRange;
    private int endRange;
    private ItemStack itemStack;

    public RewardData() {}

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

    public void setCategory(ResultType category){
        this.category = category;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setStartRange(int startRange) {
        this.startRange = startRange;
    }

    public void setEndRange(int endRange) {
        this.endRange = endRange;
    }

    public void setItemStack(String itemStack) {
        this.itemStack = getItemStack(itemStack);
    }

    public ItemStack getItemStack(final String string) {
        if (string == null) {
            return null;
        }

        ItemStack itemStack;

        if (string.contains(":") && string.contains(",")) {
            final String[] data = string.split(":");
            final String[] data2 = data[1].split(",");
            final Material material = Material.matchMaterial(data[0]);

            if (material == null) {
                System.err.println("MATERIAL: " + data[0] + " IS INVALID!");
                return null;
            }

            short damage;
            int amount;

            try {
                damage = Short.parseShort(data2[0]);
            } catch (NumberFormatException e) {
                System.err.println("DATA: " + data2[0] + " IS NOT A NUNBER!");
                return null;
            }

            try {
                amount = Integer.parseInt(data2[1]);
            } catch (NumberFormatException e) {
                System.err.println("AMOUNT: " + data2[1] + " IS NOT A NUNBER!");
                return null;
            }

            itemStack = new ItemStack(material, amount, damage);
        } else if (string.contains(":")) {
            final String[] data = string.split(":");
            final Material material2 = Material.matchMaterial(data[0]);

            if (material2 == null) {
                System.err.println("MATERIAL: " + data[0] + " IS INVALID!");
                return null;
            }

            short damage2;

            try {
                damage2 = Short.parseShort(data[1]);
            } catch (NumberFormatException e2) {
                System.err.println("DATA: " + data[1] + " IS NOT A NUNBER!");
                return null;
            }

            itemStack = new ItemStack(material2, 1, damage2);
        } else if (string.contains(",")) {
            final String[] data = string.split(",");
            final Material material2 = Material.matchMaterial(data[0]);

            if (material2 == null) {
                System.err.println("MATERIAL: " + data[0] + " IS INVALID!");
                return null;
            }

            int amount2;

            try {
                amount2 = Integer.parseInt(data[1]);
            } catch (NumberFormatException e2) {
                System.err.println("AMOUNT: " + data[1] + " IS NOT A NUNBER!");
                return null;
            }

            itemStack = new ItemStack(material2, amount2);
        } else {
            final Material material3 = Material.matchMaterial(string);

            if (material3 == null) {
                System.err.println("MATERIAL: " + string + " IS INVALID!");
                return null;
            }

            itemStack = new ItemStack(material3, 1);
        }

        return itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
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
