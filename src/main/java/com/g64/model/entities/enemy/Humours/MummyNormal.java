package com.g64.model.entities.enemy.Humours;

import com.g64.controller.GameController;
import com.g64.controller.action.*;
import com.g64.model.entities.enemy.Enemy;
import com.googlecode.lanterna.TextColor;

import java.util.Random;

public class MummyNormal extends NormalHumour{
    private static final int maxCooldown = 45;
    private static final TextColor color = new TextColor.RGB(182, 172, 101);

    public MummyNormal(Enemy me) {
        super(me, maxCooldown, color);
    }

    @Override
    public ActionEvent enemyAction(GameController controller) {
        return defaultMovement(controller);
    }
}
