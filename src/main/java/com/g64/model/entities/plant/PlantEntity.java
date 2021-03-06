package com.g64.model.entities.plant;

import com.g64.model.Position;
import com.g64.model.entities.map.MapEntity;
import com.g64.model.items.drops.Drop;
import com.googlecode.lanterna.TextColor;

public abstract class PlantEntity extends MapEntity {
    public PlantEntity(Position position, String string, TextColor color, boolean collision, Drop[] drops, int maxHealth) {
        super(position, string, color, collision, drops, maxHealth);
    }
}
