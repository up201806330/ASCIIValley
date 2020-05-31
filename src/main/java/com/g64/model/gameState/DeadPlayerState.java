package com.g64.model.gameState;

import com.g64.controller.GameController;
import com.g64.controller.Commands.Command;
import com.g64.controller.Commands.NullAction;
import com.g64.controller.Commands.QuitCommand;
import com.g64.view.DeadView;
import com.googlecode.lanterna.input.KeyStroke;

public class DeadPlayerState implements GameState {

    GameController gameController;
    private DeadView deadView;

    public DeadPlayerState(GameController gameController) {
        this.gameController = gameController;
        this.deadView = new DeadView(gameController.getDisplay().getScreen());
    }

    public DeadPlayerState(GameController gameController, DeadView deadView) {
        this.gameController = gameController;
        this.deadView = deadView;
    }

    @Override
    public void execute() {
        deadView.draw();
    }

    @Override
    public Command keyStrokeToActionEvent(KeyStroke key) {
        gameController.getDisplay().getScreen().clear();

        // any key works
        if (key != null) return new QuitCommand(gameController);

        return new NullAction();
    }
}
