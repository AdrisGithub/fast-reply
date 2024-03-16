package de.wzrd;

import net.minecraft.client.network.ClientPlayerEntity;

public class Responder {
    private String lastName;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean executeMessage(ClientPlayerEntity player, String message) {
        if (null != lastName) {
            player.networkHandler.sendChatCommand("msg " + lastName + " " + message);
            return true;
        } else {
            return false;
        }
    }
}
