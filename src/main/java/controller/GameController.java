package controller;

import exceptions.CrossedDown;
import exceptions.CrossedLeft;
import exceptions.CrossedRight;
import exceptions.CrossedUp;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import controller.action.*;
import model.InventoryModel;
import model.map.MapModel;
import model.PlayerModel;
import model.Position;
import view.EntityView;
import view.InventoryView;
import view.MapView;

import java.io.IOException;

public class GameController {

    public static final int MAP_WIDTH = 40;
    public static final int MAP_HEIGHT = 15;

    private MapModel mapModel;
    private MapView mapView;
    private PlayerModel playerModel;
    private InventoryModel inventoryModel;
    private EntityView entityView;
    private InventoryView inventoryView;

    private boolean running;

    public GameController() {
        this.playerModel = new PlayerModel(new Position(MAP_WIDTH/2, MAP_HEIGHT/2), "\u263B", TextColor.ANSI.BLACK, true);
        this.inventoryModel = new InventoryModel();
        this.mapModel = new MapModel(1,  "resources/chunks.csv");
        this.mapView = new MapView(MAP_WIDTH, MAP_HEIGHT + 2);
        this.entityView = new EntityView(mapView.getScreen());
        this.inventoryView = new InventoryView(mapView.getScreen());
        this.running = true;
    }

    public void start() {
        while (running){
            mapView.draw(mapModel);
            inventoryView.draw(inventoryModel);
            entityView.draw(playerModel, mapModel.thisChunk());
            try {
                mapView.getScreen().refresh();
                processKey(getActionEvent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void processKey(ActionEvent event){
        if (event == null) return;
        try {
            event.execute();
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (CrossedUp crossedUp) {
            mapModel.moveNorth();
            playerModel.setPosition(new Position(playerModel.getPosition().getX(), MAP_HEIGHT - 1));
        } catch (CrossedLeft crossedLeft) {
            mapModel.moveWest();
            playerModel.setPosition(new Position(MAP_WIDTH - 1, playerModel.getPosition().getY()));
        } catch (CrossedDown crossedDown) {
            mapModel.moveSouth();
            playerModel.setPosition(new Position(playerModel.getPosition().getX(), 0));
        } catch (CrossedRight crossedRight) {
            mapModel.moveEast();
            playerModel.setPosition(new Position(0, playerModel.getPosition().getY()));
        }
    }

    public ActionEvent getActionEvent() throws IOException{
        Screen screen = mapView.getScreen();
        KeyStroke key = screen.readInput();
        if (key.getKeyType() == KeyType.Escape) return new QuitGame(this);
        if (key.getKeyType() == KeyType.ArrowUp) return new InteractUp(this);
        if (key.getKeyType() == KeyType.ArrowDown) return new InteractDown(this);
        if (key.getKeyType() == KeyType.ArrowLeft) return new InteractLeft(this);
        if (key.getKeyType() == KeyType.ArrowRight) return new InteractRight(this);

        //TODO Which is better?? vv  (if its this one, SelectSlot needs fixing)
        //if (key.getCharacter() >= '0' && key.getCharacter() <= '9') return new SelectSlot(this, key.getCharacter());

        if (key.getCharacter() == '1') return new SelectSlot(this, 0);
        if (key.getCharacter() == '2') return new SelectSlot(this, 1);
        if (key.getCharacter() == '3') return new SelectSlot(this, 2);
        if (key.getCharacter() == '4') return new SelectSlot(this, 3);
        if (key.getCharacter() == '5') return new SelectSlot(this, 4);
        if (key.getCharacter() == '6') return new SelectSlot(this, 5);
        if (key.getCharacter() == '7') return new SelectSlot(this, 6);
        if (key.getCharacter() == '8') return new SelectSlot(this, 7);
        if (key.getCharacter() == '9') return new SelectSlot(this, 8);
        if (key.getCharacter() == '0') return new SelectSlot(this, 9);

        if (key.getCharacter() == 'w') return new MoveUp(this);
        if (key.getCharacter() == 'd') return new MoveRight(this);
        if (key.getCharacter() == 's') return new MoveDown(this);
        if (key.getCharacter() == 'a') return new MoveLeft(this);
        return null;
    }

    public void setRunning(boolean running){ this.running = running; }

    public PlayerModel getPlayer(){ return playerModel; }

    public MapModel getMapModel() { return mapModel; }

    public MapView getMapView() { return mapView; }

    public InventoryModel getInventoryModel() { return inventoryModel; }
}
