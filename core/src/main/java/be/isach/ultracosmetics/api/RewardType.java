package be.isach.ultracosmetics.api;

import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.treasurechests.ResultType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum RewardType {

    /*
    NORMAL(new RewardData(AMMO, 50, 5, 10), new RewardData(HUESITOS, 45, 250, 500), new RewardData(ALMAS, 5, 250, 500)),
    RARE(new RewardData(AMMO, 40, 5, 25), new RewardData(ALMAS, 15, 250, 750), new RewardData(HUESITOS, 30, 250, 750), new RewardData(GADGET, 10), new RewardData(HELMET, 1), new RewardData(CHESTPLATE, 1), new RewardData(LEGGINGS, 1), new RewardData(BOOTS, 2)),
    EPIC(new RewardData(AMMO, 30, 5, 25), new RewardData(ALMAS, 20, 500, 1000), new RewardData(HUESITOS, 20, 500, 1000), new RewardData(GADGET, 15), new RewardData(HELMET, 2), new RewardData(CHESTPLATE, 2), new RewardData(LEGGINGS, 3), new RewardData(BOOTS, 3), new RewardData(HAT, 5)),
    LEGENDARY(new RewardData(ALMAS, 20, 750, 1250), new RewardData(HUESITOS, 20, 750, 1250), new RewardData(GADGET, 15), new RewardData(HELMET, 2), new RewardData(CHESTPLATE, 2), new RewardData(LEGGINGS, 3), new RewardData(BOOTS, 3), new RewardData(HAT, 5), new RewardData(EMOTE, 5)),
    VOTE(new RewardData(AMMO, 60, 1, 5), new RewardData(HUESITOS, 40, 50, 250));
    */

    NORMAL,
    RARE,
    EPIC,
    LEGENDARY,
    VOTE;

    private List<RewardData> data;
    private Map<ItemStack, Integer> items;

    RewardType() {
        this.data = new ArrayList<>();
        this.items = new HashMap<>();

        ConfigurationSection section = SettingsManager.cofres.get("COFRES." + name() + ".REWARDS");

        for(String s1 : section.getKeys(false)) {
            String s = s1.replaceAll("\\d+.*", "");
            RewardData data = new RewardData();

            ResultType category;

            try {
                category = ResultType.valueOf(s);
            } catch (IllegalArgumentException e) {
                System.err.println("UNKNOWN CATEGORY " + s);
                continue;
            }

            data.setCategory(category);

            if(!validate(section, s1 + ".PERCENTAGE")) {
                System.err.println("PERCENTAGE NOT SET ON CHEST " + name() + " AND CATEGORY " + category.name());
                continue;
            }

            data.setPercentage(section.getInt(s1 + ".PERCENTAGE"));

            switch(category) {
                case AMMO:
                case HUESITOS:
                case ALMAS:
                    if(!validate(section, s1 + ".MIN")) {
                        System.err.println("MIN NOT SET ON CHEST " + name() + " AND CATEGORY " + category.name());
                        continue;
                    }

                    if(!validate(section, s1 + ".MAX")) {
                        System.err.println("MAX NOT SET ON CHEST " + name() + " AND CATEGORY " + category.name());
                        continue;
                    }

                    data.setStartRange(section.getInt(s1 + ".MIN"));
                    data.setEndRange(section.getInt(s1 + ".MAX"));
                    break;
                case ITEM:
                    if(!validate(section, s1 + ".ITEMSTACK")) {
                        System.err.println("ITEMSTACK NOT SET ON CHEST " + name() + " AND CATEGORY " + category.name());
                        continue;
                    }

                    data.setItemStack(section.getString(s1 + ".ITEMSTACK"));
                    items.put(data.getItemStack(), data.getPercentage());
                    continue;
                case GADGET:
                case MORPH:
                case MOUNT:
                case EFFECT:
                case PET:
                case HAT:
                case HELMET:
                case CHESTPLATE:
                case LEGGINGS:
                case BOOTS:
                case EMOTE:
                    break;
            }

            this.data.add(data);
        }
    }

    private boolean validate(ConfigurationSection section, String type) {
        return section.get(type) != null;
    }

    public List<RewardData> getData() {
        return data;
    }

    public Map<ItemStack, Integer> getItems() {
        return items;
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
