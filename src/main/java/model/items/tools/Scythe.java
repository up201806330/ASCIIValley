package model.items.tools;

import controller.GameController;
import model.Position;
import model.entities.map.MapEntity;
import model.entities.map.SeedEntity;
import model.entities.map.TallGrassEntity;

public class Scythe extends Tool{
    public Scythe(){
        this.name = "SCYT";
        this.durability = 100;
    }

    @Override
    public void use(GameController controller, Position position){
        MapEntity target = controller.getMapModel().thisChunk().getEntityAt(position);
        if(target.getClass() == SeedEntity.class || target.getClass() == TallGrassEntity.class) { //TODO Ha forma de criar grupo de subclasses "Plant" ????
            this.decrementDurability();
            controller.getInventoryModel().add(target.getRandomDrop());
            controller.getMapModel().thisChunk().getEntities().remove(target);
        }
    }
}
