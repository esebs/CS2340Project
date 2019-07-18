package com.github.esebs.cs2340project.spacetrader.entities;

public class Pirate extends Encounterable {
    /**
     * This method is called in surrender() in Player
     * The pirates will take all the first resource in your
     * cargo hold. If your cargo hold is completely empty,
     * the pirates will take have your credits.
     *
     * @return Resource the resource the pirates take, null otherwise
     */
    public Resource surrender() {
        //{0,0,0,0,5,...}
        int[] playerItems = player.getVehicle().getCargoHold();
        Resource resourceTaken;

        for (int i = 0; i < playerItems.length; i++) {
            if (playerItems[i] != 0) {
                resourceTaken = Resource.values()[i];
                playerItems[i] = 0;
                player.getVehicle().setCargoHold(playerItems);
                return resourceTaken;
            }
        }

        player.setCredits(player.getCredits() / 2);
        return null;
    }
}
