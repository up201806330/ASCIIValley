package com.g64.controller.commands;

import com.g64.controller.GameController;
import com.g64.model.entities.EntityModel;

public class MoveLeft implements Command {
    private final GameController controller;
    private final EntityModel entity;

    public MoveLeft(GameController controller, EntityModel entity) {
        this.controller = controller;
        this.entity = entity;
    }

    @Override
    public void execute() {
        EntityModel target = controller.getMapModel().thisChunk().getEntityAt(entity.getPosition().lookLeft());

        if (!target.hasCollision()) entity.getPosition().moveLeft();
        entity.handleBoundaryCrossing(controller.getMapModel());
    }
}
