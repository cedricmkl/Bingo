package de.coolepizza.bingo.utils;



import de.coolepizza.bingo.manager.BingoSettings;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Random;

public class ItemSelector {
    private static ArrayList<Material> base = new ArrayList<>();
    private static ArrayList<Material> normal = new ArrayList<>();
    private static ArrayList<Material> hard = new ArrayList<>();
    static {
        addBaseItem("IRON");
        addBaseItem("REDSTONE");
        addBaseItem("GOLD");
        removeBaseItem(Material.ENCHANTED_GOLDEN_APPLE);
        addBaseItem("FURNACE");
        addBaseItem("RAIL");
        addBaseItem(Material.SUGAR_CANE);
        addBaseItem(Material.PUMPKIN);
        addBaseItem(Material.JUKEBOX);
        addBaseItem("EMERALD");
        removeBaseItem(Material.DIAMOND_HORSE_ARMOR);
        removeBaseItem(Material.DIAMOND_BLOCK);
        removeBaseItem(Material.DIAMOND);
        addBaseItem(Material.FISHING_ROD);
        addBaseItem(Material.OBSERVER);
        addBaseItem(Material.BOW);
        addBaseItem(Material.FEATHER);
        addBaseItem(Material.COMPASS);
        addBaseItem(Material.CLOCK);
        addBaseItem(Material.SHIELD);
        addBaseItem(Material.SHEARS);
        addBaseItem(Material.WATER_BUCKET);
        addBaseItem(Material.MILK_BUCKET);
        addBaseItem(Material.LAVA_BUCKET);
        addBaseItem(Material.BUCKET);
        addBaseItem(Material.SUGAR_CANE);
        addBaseItem(Material.FLINT_AND_STEEL);



        addNormalItem(Material.NETHERRACK);
        addNormalItem("SOUL");
        addNormalItem("DIAMOND");
        addNormalItem("WART");
        removeNormalItem(Material.SOUL_FIRE);
        addNormalItem(Material.CRYING_OBSIDIAN);
        addNormalItem(Material.NAME_TAG);
        removeNormalItem(Material.DIAMOND_HORSE_ARMOR);
        removeNormalItem(Material.DIAMOND_ORE);
        addNormalItem(Material.COBWEB);
        addNormalItem(Material.SUGAR_CANE);
        addNormalItem("GLOWSTONE");

        addHardItem(Material.NETHERITE_HOE);
        addHardItem(Material.NETHERITE_INGOT);
        addHardItem(Material.NETHERITE_SCRAP);
        addHardItem(Material.OBSIDIAN);
        addHardItem(Material.ANCIENT_DEBRIS);
        addHardItem(Material.ENDER_PEARL);
        addHardItem("BLAZE");
        removeHardItem(Material.BLAZE_SPAWN_EGG);

    }
    public static void addBaseItem(Material material){
        base.add(material);
    }
    public static void removeBaseItem(Material material){
        if (base.contains(material)){
            base.remove(material);
        }
    }
    public static void addBaseItem(String material){
        for (Material value : Material.values()) {
            if (value.name().contains(material)){
                base.add(value);
            }
        }
    }
    public static void addNormalItem(Material material){
        normal.add(material);
    }
    public static void removeNormalItem(Material material){
        if (normal.contains(material)){
            normal.remove(material);
        }
    }
    public static void addNormalItem(String material){
        for (Material value : Material.values()) {
            if (value.name().contains(material)){
                normal.add(value);
            }
        }
    }

    public static ArrayList<Material> getBase() {
        return base;
    }

    public static void addHardItem(Material material){
        hard.add(material);
    }
    public static void removeHardItem(Material material){
        if (hard.contains(material)){
            hard.remove(material);
        }
    }
    public static void addHardItem(String material){
        for (Material value : Material.values()) {
            if (value.name().contains(material)){
                hard.add(value);
            }
        }
    }

    public static ArrayList<Material> getHard() {
        return hard;
    }

    public static ArrayList<Material> getNormal() {
        return normal;
    }

    public static ArrayList<Material> getItems(BingoSettings.BingoDifficulty bingoDifficulty , int itemsize) {
        ArrayList<Material> items = new ArrayList<>();
        if (bingoDifficulty == BingoSettings.BingoDifficulty.EASY){
            for (int i = 0; i < itemsize  ; i++) {
                Material material = base.get(new Random().nextInt(base.size()));

                while (items.contains(material)){
                    material = base.get(new Random().nextInt(base.size()));
                }
                items.add(material);
            }
        }else if (bingoDifficulty == BingoSettings.BingoDifficulty.NORMAl){
            int baseitems = (itemsize/3)*2;
            int normalitems = itemsize - baseitems;
            for (int i = 0; i < baseitems  ; i++) {
                Material material = base.get(new Random().nextInt(base.size()));
                while (items.contains(material)){
                      material = base.get(new Random().nextInt(base.size()));
                }
                items.add(material);
            }

            for (int i = 0; i < normalitems  ; i++) {
                Material material = normal.get(new Random().nextInt(normal.size()));
                while (items.contains(material)){
                    material = normal.get(new Random().nextInt(normal.size()));
                }
                items.add(material);
            }

        }else if (bingoDifficulty == BingoSettings.BingoDifficulty.HARD){
            int itemsizes = itemsize/3;

            for (int i = 0; i < itemsizes  ; i++) {
                Material material = base.get(new Random().nextInt(base.size()));
                while (items.contains(material)){
                    material = base.get(new Random().nextInt(base.size()));
                }
                items.add(material);
            }

            for (int i = 0; i < itemsizes  ; i++) {
                Material material = normal.get(new Random().nextInt(normal.size()));
                while (items.contains(material)){
                    material = normal.get(new Random().nextInt(normal.size()));
                }
                items.add(material);
            }
            for (int i = 0; i < itemsizes  ; i++) {
                Material material = hard.get(new Random().nextInt(hard.size()));
                while (items.contains(material)){
                    material = hard.get(new Random().nextInt(hard.size()));
                }
                items.add(material);
            }
        }
        return items;
    }

}
