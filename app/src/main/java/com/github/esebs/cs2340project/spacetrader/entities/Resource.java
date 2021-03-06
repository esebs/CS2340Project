package com.github.esebs.cs2340project.spacetrader.entities;
import java.util.Random;

/**
 * Enum holding all info for building resources
 */
public enum Resource {
    WATER(TechLevel.PRE_AGRICULTURE, TechLevel.PRE_AGRICULTURE,30,3,4),
    FURS(TechLevel.PRE_AGRICULTURE,TechLevel.PRE_AGRICULTURE,250,10,10),
    FOOD(TechLevel.AGRICULTURE, TechLevel.PRE_AGRICULTURE, 100, 5, 5),
    ORE(TechLevel.MEDIEVAL, TechLevel.MEDIEVAL, 350, 20, 10),
    GAMES(TechLevel.RENAISSANCE, TechLevel.AGRICULTURE, 250, -10, 5),
    FIREARMS(TechLevel.RENAISSANCE, TechLevel.AGRICULTURE, 1250, -75, 100),
    MEDICINE(TechLevel.EARLY_INDUSTRIAL, TechLevel.AGRICULTURE, 650, -30, 5),
    MACHINES(TechLevel.EARLY_INDUSTRIAL, TechLevel.RENAISSANCE, 900, -30, 5),
    NARCOTICS(TechLevel.INDUSTRIAL, TechLevel.PRE_AGRICULTURE, 3500, -125, 150),
    ROBOTS(TechLevel.POST_INDUSTRIAL, TechLevel.EARLY_INDUSTRIAL, 5000, -150, 100);

    private final TechLevel mtlp;
    private final TechLevel mtlu;
    private final int basePrice;
    private final int incPerLevel;
    private final int variance;

    Resource(TechLevel mtlp, TechLevel mtlu, int basePrice, int incPerLevel, int variance) {
        this.mtlp = mtlp;
        this.mtlu = mtlu;
        this.basePrice = basePrice;
        this.incPerLevel = incPerLevel;
        this.variance = variance;
    }

    private float varCalc() {
        Random random = new Random();
        int varKey = random.nextInt(2);
        varKey = (varKey > 0) ? 1 : -1;
        final double percentToDecimal = 0.01;
        float var = (float)(random.nextInt(variance + 1) * percentToDecimal) * basePrice;
        return var * varKey;
    }

    /**
     * Calculates a semi-random buy price for a resource based on its base price and the tech
     * level of its building.
     * @param tech tech level of this resource's building
     * @return buy price
     */
    public int buyPriceCalc(TechLevel tech) {
        int techDiff = tech.ordinal() - mtlp.ordinal();
        return Math.max((int)(basePrice + varCalc() + (techDiff*incPerLevel)),10);
    }

    /**
     * Calculates a semi-random sell price for a resource based on its base price and the tech
     * level of its building.
     * @param tech tech level of this resource's building
     * @return sell price
     */
    public int sellPriceCalc(TechLevel tech) {
        int techDiff = tech.ordinal() - mtlu.ordinal();
        return Math.max((int)(basePrice + varCalc() + (techDiff*incPerLevel)), 10);
    }

    /**
     * Gets the minimum tech level required to produce this Resource
     * @return the resource's mltp
     */
    public TechLevel getMtlp() {
        return mtlp;
    }

    /**
     * Gets the minimum tech level required to use this Resource
     * @return the resource's mltu
     */
    public TechLevel getMtlu() {
        return mtlu;
    }

    /**
     * Gets the resource's base price (for use in price calculation)
     *
     * @return base price of this resource
     */
    public int getBasePrice() {
        return basePrice;
    }

}
