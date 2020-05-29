package com.g64.controller;

import com.g64.exceptions.*;
import com.g64.model.MenuModel;
import com.g64.view.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.g64.controller.action.*;
import com.g64.model.InventoryModel;
import com.g64.model.MapModel;
import com.g64.model.entities.Player;
import com.g64.model.Position;

import java.io.IOException;

public class GameController {

    public enum gameStates {
        IN_GAME, CONTROLS, MAIN_MENU, DEAD;
    }

    private static final int frameRate = 60;

    private MapModel mapModel;
    private MapView mapView;
    private Player player;
    private InventoryModel inventoryModel;
    private EntityView entityView;
    private InventoryView inventoryView;

    private boolean running;
    private Display display;
    private gameStates gameState;
    private MenuModel menuModel;
    private MenuView menuView;
    private ControlsView controlsView;
    private DeadView deadView;

    public GameController() {
        this.mapModel = new MapModel(6,  "resources/chunks.csv");
        this.display = new Display(mapModel.thisChunk().getWidth(), mapModel.thisChunk().getHeight() + 3);
        this.mapView = new MapView(display.getScreen());
        this.player = new Player(new Position(mapModel.thisChunk().getWidth()/2, mapModel.thisChunk().getHeight()/2), "\u263B", TextColor.ANSI.BLACK);
        this.inventoryModel = new InventoryModel();
        this.entityView = new EntityView(mapView.getScreen());
        this.inventoryView = new InventoryView(mapView.getScreen());
        this.running = true;
        this.gameState = gameStates.MAIN_MENU;
        this.menuModel = new MenuModel();
        this.menuView = new MenuView(display.getScreen());
        this.controlsView = new ControlsView(display.getScreen());
        this.deadView = new DeadView(display.getScreen());
    }

    public GameController(Player player, MapModel mapModel, MapView mapView, EntityView entityView,  InventoryModel inventoryModel, InventoryView inventoryView){
        this.player = player;
        this.mapModel = mapModel;
        this.mapView = mapView;
        this.entityView = entityView;
        this.inventoryModel = inventoryModel;
        this.inventoryView = inventoryView;
    }

    public void start() {
        while (running) {

            switch (gameState) {
                case MAIN_MENU:
                    menuView.draw(menuModel);

                    try {
                        processAction(getActionEventFromKeyboard());
                        mapView.getScreen().refresh();
                        Thread.sleep(1000/ frameRate);
                    }
                    catch (IOException | InterruptedException e) { e.printStackTrace(); }
                    break;

                case CONTROLS:
                    controlsView.draw();
                    try {
                        processAction(getActionEventFromKeyboard());
                        mapView.getScreen().refresh();
                        Thread.sleep(1000/ frameRate);
                    }
                    catch (IOException | InterruptedException e) { e.printStackTrace(); }
                    break;

                case IN_GAME:
                    mapView.draw(mapModel);
                    inventoryView.draw(inventoryModel, player.getCurrentHealth(), mapModel.thisChunk().getHeight());
                    entityView.draw(player, mapModel.thisChunk());
                    try {
                        processAction(getActionEventFromKeyboard());
                        for (ActionEvent event: mapModel.updateEntities(this)) processAction(event);
                        mapView.getScreen().refresh();
                        Thread.sleep(1000/ frameRate);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                case DEAD:
                    deadView.draw();
                    try {
                        processAction(getActionEventFromKeyboard());
                        mapView.getScreen().refresh();
                        Thread.sleep(1000/ frameRate);
                    }
                    catch (IOException | InterruptedException e) { e.printStackTrace(); }
                    break;
            }
        }
    }

    public void processAction(ActionEvent event){
        if (event==null) return;
        try {
            event.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Died died) {
            // GAME OVER
            getMapView().getScreen().clear();
            gameState = gameStates.DEAD;
        }
    }

    public ActionEvent getActionEventFromKeyboard() throws IOException{
        Screen screen = mapView.getScreen();
        KeyStroke key = screen.pollInput();
        if (key == null) return null;
        if (gameState == gameStates.DEAD) return new QuitGame(this);
        if (gameState == gameStates.CONTROLS) return new ExitControls(this);
        if (key.getKeyType() == KeyType.Escape) return new QuitGame(this);
        if (key.getKeyType() == KeyType.ArrowUp) {
            if (gameState == gameStates.MAIN_MENU) return new MenuUp(this.menuModel);
            else return new InteractUp(this);
        }
        if (key.getKeyType() == KeyType.ArrowDown) {
            if (gameState == gameStates.MAIN_MENU) return new MenuDown(this.menuModel);
            else return new InteractDown(this);
        }
        if (key.getKeyType() == KeyType.ArrowLeft) return new InteractLeft(this);
        if (key.getKeyType() == KeyType.ArrowRight) return new InteractRight(this);

        if (key.getKeyType() == KeyType.Enter && gameState == gameStates.MAIN_MENU) return new SelectMenuOption(this.menuModel, this);

        if (key.getKeyType() == KeyType.Character) {
            if (key.getCharacter() >= '0' && key.getCharacter() <= '9') return new SelectSlot(this, (Character.getNumericValue(key.getCharacter()) - 1) % 10);
            if (key.getCharacter() == 'w') return new MoveUp(this, player);
            if (key.getCharacter() == 'd') return new MoveRight(this, player);
            if (key.getCharacter() == 's') return new MoveDown(this, player);
            if (key.getCharacter() == 'a') return new MoveLeft(this, player);
        }
        return new NullAction();
    }

    public void setRunning(boolean running){ this.running = running; }

    public Player getPlayer(){ return player; }

    public MapModel getMapModel() { return mapModel; }

    public MapView getMapView() { return mapView; }

    public InventoryModel getInventoryModel() { return inventoryModel; }

    public void setGameState(gameStates gameState) {  this.gameState = gameState; }
}
