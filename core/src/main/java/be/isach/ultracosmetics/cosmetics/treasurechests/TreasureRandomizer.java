package be.isach.ultracosmetics.cosmetics.treasurechests;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.api.EconomyType;
import be.isach.ultracosmetics.api.RewardData;
import be.isach.ultracosmetics.api.RewardType;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.emotes.EmoteType;
import be.isach.ultracosmetics.cosmetics.gadgets.GadgetType;
import be.isach.ultracosmetics.cosmetics.hats.Hat;
import be.isach.ultracosmetics.cosmetics.morphs.MorphType;
import be.isach.ultracosmetics.cosmetics.mounts.MountType;
import be.isach.ultracosmetics.cosmetics.particleeffects.ParticleEffectType;
import be.isach.ultracosmetics.cosmetics.pets.PetType;
import be.isach.ultracosmetics.cosmetics.suits.ArmorSlot;
import be.isach.ultracosmetics.cosmetics.suits.SuitType;
import be.isach.ultracosmetics.util.MathUtils;
import be.isach.ultracosmetics.util.SoundUtil;
import be.isach.ultracosmetics.util.Sounds;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by sacha on 19/08/15.
 */
public class TreasureRandomizer {

    Player player;
    public Location loc;
    private ItemStack itemStack;
    private String name;
    private RewardType rewardType;

    public List<GadgetType> gadgetList = new ArrayList<>();
    public List<GadgetType> ammoList = new ArrayList<>();
    public List<ParticleEffectType> particleEffectList = new ArrayList<>();
    public List<MountType> mountList = new ArrayList<>();
    public List<PetType> petList = new ArrayList<>();
    public List<MorphType> morphList = new ArrayList<>();
    public List<Hat> hatList = new ArrayList<>();
    public List<SuitType> helmetList = new ArrayList<>();
    public List<SuitType> chestplateList = new ArrayList<>();
    public List<SuitType> leggingList = new ArrayList<>();
    public List<SuitType> bootList = new ArrayList<>();
    public List<EmoteType> emoteList = new ArrayList<>();

    private Random random = new Random();

    private final List<ResultType> RESULT_TYPES = new ArrayList<>();

    private void setupChance(List<ResultType> resultRef, int percent, ResultType resultType) {
        for (int i = 0; i < percent; i++) {
            resultRef.add(resultType);
        }
    }

    public TreasureRandomizer(final Player player, Location location, RewardType rewardType) {
        this.loc = location.add(0.5, 0, 0.5);
        this.player = player;
        this.rewardType = rewardType;
        // add ammo.
        if (UltraCosmetics.getInstance().isAmmoEnabled() && ammoList.isEmpty())
            for (GadgetType type : GadgetType.values())
                if (type.isEnabled()
                        && player.hasPermission(type.getPermission())
                        && type.requiresAmmo()
                        && type.canBeFound())
                    ammoList.add(type);
        // Add GADGETS! (Not ammo)
        if (gadgetList.isEmpty())
            for (GadgetType type : GadgetType.values())
                if (type.isEnabled()
                        && !player.hasPermission(type.getPermission())
                        && type.canBeFound())
                    gadgetList.add(type);
        if (petList.isEmpty())
            for (PetType petType : PetType.enabled())
                if (!player.hasPermission(petType.getPermission())
                        && petType.canBeFound())
                    petList.add(petType);
        if (morphList.isEmpty()
                && UltraCosmetics.enabledCategories.contains(Category.MORPHS))
            for (MorphType morph : MorphType.enabled())
                if (!player.hasPermission(morph.getPermission())
                        && morph.canBeFound())
                    morphList.add(morph);
        if (particleEffectList.isEmpty())
            for (ParticleEffectType type : ParticleEffectType.enabled())
                if (!player.hasPermission(type.getPermission())
                        && type.canBeFound())
                    particleEffectList.add(type);
        if (mountList.isEmpty())
            for (MountType type : MountType.enabled())
                if (!player.hasPermission(type.getPermission())
                        && type.canBeFound())
                    mountList.add(type);
        if (hatList.isEmpty())
            for (Hat hat : Hat.enabled())
                if (hat.canBeFound()
                        && !player.hasPermission(hat.getPermission()))
                    hatList.add(hat);
        if (helmetList.isEmpty())
            for (SuitType suit : SuitType.enabled())
                if (suit.canBeFound()
                        && !player.hasPermission(suit.getPermission(ArmorSlot.HELMET)))
                    helmetList.add(suit);
        if (chestplateList.isEmpty())
            for (SuitType suit : SuitType.enabled())
                if (suit.canBeFound()
                        && !player.hasPermission(suit.getPermission(ArmorSlot.CHESTPLATE)))
                    chestplateList.add(suit);
        if (leggingList.isEmpty())
            for (SuitType suit : SuitType.enabled())
                if (suit.canBeFound()
                        && !player.hasPermission(suit.getPermission(ArmorSlot.LEGGINGS)))
                    leggingList.add(suit);
        if (bootList.isEmpty())
            for (SuitType suit : SuitType.enabled())
                if (suit.canBeFound()
                        && !player.hasPermission(suit.getPermission(ArmorSlot.BOOTS)))
                    bootList.add(suit);
        if (emoteList.isEmpty())
            for (EmoteType emoteType : EmoteType.enabled())
                if (emoteType.canBeFound()
                        && !player.hasPermission(emoteType.getPermission()))
                    emoteList.add(emoteType);


        if (!Category.MOUNTS.isEnabled())
            mountList.clear();
        if (!Category.GADGETS.isEnabled()) {
            ammoList.clear();
            gadgetList.clear();
        }
        if (!Category.EFFECTS.isEnabled())
            particleEffectList.clear();
        if (!Category.PETS.isEnabled())
            petList.clear();
        if (!Category.MORPHS.isEnabled())
            morphList.clear();
        if (!Category.HATS.isEnabled())
            hatList.clear();
        if (!Category.SUITS.isEnabled()) {
            helmetList.clear();
            chestplateList.clear();
            leggingList.clear();
            bootList.clear();
        }
        if (!Category.EMOTES.isEnabled())
            emoteList.clear();

        for(RewardData rewardData : rewardType.getData()) {
            setupChance(RESULT_TYPES, rewardData.getPercentage(), rewardData.getCategory());
        }
    }

    private String getMessage(String s) {
        try {
            return ChatColor.translateAlternateColorCodes('&', ((String) SettingsManager.getConfig().get(s)).replace("%prefix%", MessageManager.getMessage("Prefix")));
        } catch (Exception exc) {
            return "§c§lError";
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    List<ResultType> types = new ArrayList();

    public void giveRandomThing() {
        try {
            if (types.isEmpty()) {
                types = new ArrayList(RESULT_TYPES);
                Collections.shuffle(types);
            }

            ResultType type = types.get(0);

            types = new ArrayList();

            switch (type) {
                case HUESITOS:
                    giveHuesitos();
                    break;
                case ALMAS:
                    giveAlmas();
                    break;
                case AMMO:
                    if (!UltraCosmetics.getInstance().isAmmoEnabled()) {
                        giveRandomThing();
                        break;
                    }
                    giveAmmo();
                    break;
                case MOUNT:
                    giveRandomMount();
                    break;
                case MORPH:
                    giveRandomMorph();
                    break;
                case PET:
                    giveRandomPet();
                    break;
                case EFFECT:
                    giveRandomEffect();
                    break;
                case HAT:
                    giveRandomHat();
                    break;
                case GADGET:
                    giveRandomGadget();
                    break;
                case HELMET:
                    giveRandomSuit(ArmorSlot.HELMET);
                    break;
                case CHESTPLATE:
                    giveRandomSuit(ArmorSlot.CHESTPLATE);
                    break;
                case LEGGINGS:
                    giveRandomSuit(ArmorSlot.LEGGINGS);
                    break;
                case BOOTS:
                    giveRandomSuit(ArmorSlot.BOOTS);
                    break;
                case EMOTE:
                    giveRandomEmote();
                    break;
            }

        } catch (IndexOutOfBoundsException exception) {
            if ((!d("Gadgets") || gadgetList.isEmpty())
                    && (!d("Gadgets-Ammo") || ammoList.isEmpty())
                    && (!d("Pets") || petList.isEmpty())
                    && (!d("Morphs") || morphList.isEmpty())
                    && (!d("Mounts") || mountList.isEmpty())
                    && (!d("Hats") || hatList.isEmpty())
                    && (!d("Effects") || particleEffectList.isEmpty())
                    || RESULT_TYPES.isEmpty())
                giveNothing();
            else
                giveRandomThing();
        } catch (IllegalArgumentException exception) {
            if ((!d("Gadgets") || gadgetList.isEmpty())
                    && (!d("Gadgets-Ammo") || ammoList.isEmpty())
                    && (!d("Pets") || petList.isEmpty())
                    && (!d("Morphs") || morphList.isEmpty())
                    && (!d("Mounts") || mountList.isEmpty())
                    && (!d("Hats") || hatList.isEmpty())
                    && (!d("Effects") || particleEffectList.isEmpty())
                    || RESULT_TYPES.isEmpty())
                giveNothing();
            else
                giveRandomThing();
        }
        SoundUtil.playSound(loc, Sounds.CHEST_OPEN, 1.4f, 1.5f);
    }

    private boolean d(String s) {
        return (boolean) SettingsManager.getConfig().get("TreasureChests.Loots." + s + ".Enabled");
    }

    public String getName() {
        return name;
    }

    public void clear() {
        petList.clear();
        ammoList.clear();
        gadgetList.clear();
        particleEffectList.clear();
        mountList.clear();
        morphList.clear();
        hatList.clear();
        helmetList.clear();
        chestplateList.clear();
        leggingList.clear();
        bootList.clear();
        emoteList.clear();
        RESULT_TYPES.clear();
        types.clear();
    }

    public void giveNothing() {
        if(UltraCosmetics.getInstance().isVaultLoaded()) {
            try {
                giveHuesitos();
            } catch (Exception e) {
                name = MessageManager.getMessage("Treasure-Chests-Loot.Nothing");
                itemStack = new ItemStack(Material.BARRIER);
            }
        } else {
            name = MessageManager.getMessage("Treasure-Chests-Loot.Nothing");
            itemStack = new ItemStack(Material.BARRIER);
        }
    }
    
    public void giveHuesitos() {
        if (!UltraCosmetics.getInstance().isVaultLoaded()) {
            giveNothing();
            return;
        }
        int money = MathUtils.randomRangeInt(rewardType.getStartRange(ResultType.HUESITOS), rewardType.getEndRange(ResultType.HUESITOS));
        name = MessageManager.getMessage("Treasure-Chests-Loot.Money").replace("%money%", money + "");
        UltraCosmetics.economy.depositPlayer(EconomyType.HUESITOS, player, money);
        itemStack = new ItemStack(Material.BONE);
        if (money > 3 * (int) SettingsManager.getConfig().get("TreasureChests.Loots.Money.Max") / 4)
            spawnRandomFirework(loc);
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Money.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Money.Message.message")).replace("%name%", player.getName()).replace("%money%", money + ""));
    }
    
    public void giveAlmas() {
        if (!UltraCosmetics.getInstance().isVaultLoaded()) {
            giveNothing();
            return;
        }
        int money = MathUtils.randomRangeInt(rewardType.getStartRange(ResultType.ALMAS), rewardType.getEndRange(ResultType.ALMAS));
        name = MessageManager.getMessage("Treasure-Chests-Loot.Money").replace("%money%", money + "").replace("Huesitos", "Almas");
        UltraCosmetics.economy.depositPlayer(EconomyType.ALMAS, player, money);
        itemStack = new ItemStack(Material.NETHER_STAR);
        if (money > 3 * (int) SettingsManager.getConfig().get("TreasureChests.Loots.Money.Max") / 4)
            spawnRandomFirework(loc);
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Money.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Money.Message.message")).replace("%name%", player.getName()).replace("%money%", money + ""));
    }

    public void giveAmmo() {
        int i = random.nextInt(ammoList.size());
        GadgetType g = ammoList.get(i);
        int ammo = MathUtils.randomRangeInt(rewardType.getStartRange(ResultType.AMMO), rewardType.getEndRange(ResultType.AMMO));
        name = MessageManager.getMessage("Treasure-Chests-Loot.Ammo").replace("%name%", g.getName()).replace("%ammo%", ammo + "");
        ammoList.remove(i);
        UltraCosmetics.getCustomPlayer(player).addAmmo(g.toString().toLowerCase(), ammo);
        itemStack = new MaterialData(g.getMaterial(), g.getData()).toItemStack(1);
        if (ammo > 50) {
            spawnRandomFirework(loc);
        }
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Gadgets-Ammo.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Gadgets-Ammo.Message.message")).replace("%name%", player.getName()).replace("%ammo%", ammo + "").replace("%gadget%", (UltraCosmetics.getInstance().placeholdersHaveColor()) ? g.getName() : UltraCosmetics.filterColor(g.getName())));

    }

    public void giveRandomSuit(ArmorSlot armorSlot) {
        List<SuitType> list = null;
        switch (armorSlot) {
            case HELMET:
                list = helmetList;
                break;
            case CHESTPLATE:
                list = chestplateList;
                break;
            case LEGGINGS:
                list = leggingList;
                break;
            case BOOTS:
                list = bootList;
                break;
        }
        int i = random.nextInt(list.size());
        SuitType suitType = list.get(i);
        name = MessageManager.getMessage("Treasure-Chests-Loot.Suit").replace("%suit%", suitType.getName(armorSlot));
        list.remove(i);
        givePermission(suitType.getPermission(armorSlot));
        itemStack = new ItemStack(suitType.getMaterial(armorSlot));
        spawnRandomFirework(loc);
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Suits.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Suits.Message.message")).replace("%name%", player.getName())
                    .replace("%suit%", (UltraCosmetics.getInstance().placeholdersHaveColor()) ? suitType.getName(armorSlot) : UltraCosmetics.filterColor(suitType.getName(armorSlot))));
    }

    public void giveRandomGadget() {
        int i = random.nextInt(gadgetList.size());
        GadgetType gadget = gadgetList.get(i);
        name = MessageManager.getMessage("Treasure-Chests-Loot.gadget").replace("%gadget%", gadget.getName());
        gadgetList.remove(i);
        givePermission(gadget.getPermission());
        itemStack = new ItemStack(gadget.getMaterial());
        spawnRandomFirework(loc);
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Gadgets.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Gadgets.Message.message")).replace("%name%", player.getName())
                    .replace("%gadget%", (UltraCosmetics.getInstance().placeholdersHaveColor()) ? gadget.getName() : UltraCosmetics.filterColor(gadget.getName())));
    }

    public void giveRandomHat() {
        int i = random.nextInt(hatList.size());
        Hat hat = hatList.get(i);
        name = MessageManager.getMessage("Treasure-Chests-Loot.Hat").replace("%hat%", hat.getName());
        hatList.remove(i);
        givePermission(hat.getPermission());
        itemStack = hat.getItemStack().clone();
        spawnRandomFirework(loc);
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Hats.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Hats.Message.message")).replace("%name%", player.getName()).replace("%hat%", (UltraCosmetics.getInstance().placeholdersHaveColor()) ? hat.getName() : UltraCosmetics.filterColor(hat.getName())));
    }

    public void giveRandomPet() {
        int i = random.nextInt(petList.size());
        PetType pet = petList.get(i);
        name = MessageManager.getMessage("Treasure-Chests-Loot.Pet").replace("%pet%", pet.getMenuName());
        petList.remove(i);
        givePermission(pet.getPermission());
        itemStack = new ItemStack(pet.getMaterial());
        spawnRandomFirework(loc);
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Pets.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Pets.Message.message")).replace("%name%", player.getName())
                    .replace("%pet%", (UltraCosmetics.getInstance().placeholdersHaveColor()) ? pet.getMenuName() : UltraCosmetics.filterColor(pet.getMenuName())));
    }

    public void giveRandomEmote() {
        int i = random.nextInt(emoteList.size());
        EmoteType emoteType = emoteList.get(i);
        name = MessageManager.getMessage("Treasure-Chests-Loot.Emote").replace("%emote%", emoteType.getName());
        emoteList.remove(i);
        givePermission(emoteType.getPermission());
        itemStack = new ItemStack(emoteType.getFrames().get(emoteType.getMaxFrames() - 1));
        spawnRandomFirework(loc);
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Emotes.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Emotes.Message.message")).replace("%name%", player.getName())
                    .replace("%emote%", (UltraCosmetics.getInstance().placeholdersHaveColor()) ? emoteType.getName() : UltraCosmetics.filterColor(emoteType.getName())));
    }

    public void giveRandomMount() {
        int i = random.nextInt(mountList.size());
        MountType mount = mountList.get(i);
        name = MessageManager.getMessage("Treasure-Chests-Loot.Mount").replace("%mount%", mount.getMenuName());
        mountList.remove(i);
        itemStack = new ItemStack(mount.getMaterial());
        givePermission(mount.getPermission());
        spawnRandomFirework(loc);
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Mounts.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Mounts.Message.message"))
                    .replace("%name%", player.getName()).replace("%mount%", (UltraCosmetics.getInstance().placeholdersHaveColor())
                            ? mount.getMenuName() : UltraCosmetics.filterColor(mount.getMenuName())));
    }

    public void giveRandomEffect() {
        int i = random.nextInt(particleEffectList.size());
        ParticleEffectType particleEffect = particleEffectList.get(i);
        name = MessageManager.getMessage("Treasure-Chests-Loot.Effect").replace("%effect%", particleEffect.getName());
        particleEffectList.remove(i);
        itemStack = new ItemStack(particleEffect.getMaterial());
        givePermission(particleEffect.getPermission());
        spawnRandomFirework(loc);
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Effects.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Effects.Message.message")).replace("%name%", player.getName()).replace("%effect%", (UltraCosmetics.getInstance().placeholdersHaveColor()) ? particleEffect.getName() : UltraCosmetics.filterColor(particleEffect.getName())));
    }

    public void giveRandomMorph() {
        int i = random.nextInt(morphList.size());
        MorphType morph = morphList.get(i);
        name = MessageManager.getMessage("Treasure-Chests-Loot.Morph").replace("%morph%", morph.getName());
        morphList.remove(morph);
        itemStack = new ItemStack(morph.getMaterial());
        givePermission(morph.getPermission());
        spawnRandomFirework(loc);
        if (SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Morphs.Message.enabled"))
            Bukkit.broadcastMessage((getMessage("TreasureChests.Loots.Morphs.Message.message"))
                    .replace("%name%", player.getName()).replace("%morph%", (UltraCosmetics.getInstance().placeholdersHaveColor()) ? morph.getName() : UltraCosmetics.filterColor(morph.getName())));
    }


    public static FireworkEffect getRandomFireworkEffect() {
        if (!UltraCosmetics.getInstance().isEnabled())
            return null;
        Random r = new Random();
        FireworkEffect.Builder builder = FireworkEffect.builder();
        FireworkEffect effect = builder.flicker(false).trail(false).with(FireworkEffect.Type.BALL).withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255))).withFade(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255))).build();
        return effect;
    }

    public void givePermission(String permission) {
        String command = (getMessage("TreasureChests.Permission-Add-Command")).replace("%name%", player.getName()).replace("%permission%", permission);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public void spawnRandomFirework(Location location) {
        if (!UltraCosmetics.getInstance().isEnabled())
            return;
        final ArrayList<Firework> fireworks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            final Firework f = player.getWorld().spawn(location.clone().add(0.5, 0, 0.5), Firework.class);

            FireworkMeta fm = f.getFireworkMeta();
            fm.addEffect(getRandomFireworkEffect());
            f.setFireworkMeta(fm);
            fireworks.add(f);
        }
        Bukkit.getScheduler().runTaskLater(UltraCosmetics.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Firework f : fireworks)
                    f.detonate();
            }
        }, 2);
    }

}
