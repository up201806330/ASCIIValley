package com.g64.model.entities.visitors;

import com.g64.controller.GameController;
import com.g64.model.Position;
import com.g64.model.entities.EntityModel;
import com.g64.model.entities.enemy.Enemy;
import com.g64.model.entities.map.NullEntity;
import com.g64.model.entities.map.RockEntity;
import com.g64.model.entities.map.TreeEntity;
import com.g64.model.entities.plant.PlantEntity;
import com.g64.model.entities.plant.SeedEntity;
import com.g64.model.items.Item;
import com.g64.model.items.drops.*;
import com.g64.model.items.tools.*;
import com.g64.model.terrain.GrassTerrain;
import com.g64.model.terrain.MapTerrain;
import com.g64.model.terrain.NullTerrain;
import com.g64.model.terrain.SoilTerrain;

public class ItemVisitor implements Visitor{
    GameController controller;
    Position position;
    EntityModel entity;
    MapTerrain terrain;

    public ItemVisitor(GameController controller, Position position) {
        this.controller = controller;
        this.position = position;
        this.entity = controller.getMapModel().thisChunk().getEntityAt(position);
        this.terrain = controller.getMapModel().thisChunk().getTerrainAt(position);
    }

    @Override
    public Item.usageValue allowUsage(SeedDrop item) {
        if (entity instanceof NullEntity && terrain instanceof SoilTerrain){
            controller.getMapModel().thisChunk().getEntities().add(item.getEntityFromDrop(position));
            return item.decrementValue();
        }
        else return Item.usageValue.UNUSED;
    }

    @Override
    public Item.usageValue allowUsage(ConsumableDrop item) {
        controller.getPlayer().addHealth(item.getHealthUpValue());
        return item.decrementValue();
    }

    @Override
    public Item.usageValue allowUsage(LogDrop item) {
        if  (entity instanceof NullEntity && terrain instanceof GrassTerrain){
            controller.getMapModel().thisChunk().getEntities().add(item.getEntityFromDrop(position));
            return item.decrementValue();
        }
        else return Item.usageValue.UNUSED;
    }

    @Override
    public Item.usageValue allowUsage(RockDrop item) {
        if (entity instanceof NullEntity && !(terrain instanceof NullTerrain)){
            controller.getMapModel().thisChunk().getEntities().add(item.getEntityFromDrop(position));
            return item.decrementValue();
        }
        else return Item.usageValue.UNUSED;
    }

    @Override
    public Item.usageValue allowUsage(TallGrassDrop item) {
        if (entity instanceof NullEntity && terrain instanceof GrassTerrain){
            controller.getMapModel().thisChunk().getEntities().add(item.getEntityFromDrop(position));
            return item.decrementValue();
        }
        else return Item.usageValue.UNUSED;
    }

    @Override
    public Item.usageValue allowUsage(Axe item) {
        if (entity instanceof TreeEntity || entity instanceof Enemy){
            if (entity.reduceHealth(item.getHitValue()) == EntityModel.healthReduction.DIED){
                controller.getInventoryModel().add(entity.getRandomDrop());
                controller.getMapModel().thisChunk().getEntities().remove(entity);
            }
            return item.decrementValue();
        }
        else return Item.usageValue.UNUSED;
    }

    @Override
    public Item.usageValue allowUsage(Hoe item) {
        if (entity instanceof NullEntity && terrain instanceof GrassTerrain){
            controller.getMapModel().thisChunk().getTerrain().remove(terrain);
            controller.getMapModel().thisChunk().getTerrain().add(new SoilTerrain(position, 3));
            return item.decrementValue();
        }
        else return Item.usageValue.UNUSED;
    }

    @Override
    public Item.usageValue allowUsage(Pickaxe item) {
        if (entity instanceof RockEntity){
            if (entity.reduceHealth(item.getHitValue()) == EntityModel.healthReduction.DIED){
                controller.getInventoryModel().add(entity.getRandomDrop());
                controller.getMapModel().thisChunk().getEntities().remove(entity);
            }
            return item.decrementValue();
        }
        else return Item.usageValue.UNUSED;
    }

    @Override
    public Item.usageValue allowUsage(Scythe item) {
        if (entity instanceof PlantEntity){
            if (entity.reduceHealth(item.getHitValue()) == EntityModel.healthReduction.DIED){
                controller.getInventoryModel().add(entity.getRandomDrop());
                controller.getMapModel().thisChunk().getEntities().remove(entity);
            }
            return item.decrementValue();
        }
        else return Item.usageValue.UNUSED;
    }

    @Override
    public Item.usageValue allowUsage(WateringCan item) {
        if (entity instanceof SeedEntity){
            ((SeedEntity)entity).water(item.getHitValue());
            return item.decrementValue();
        }
        else return Item.usageValue.UNUSED;
    }
}
