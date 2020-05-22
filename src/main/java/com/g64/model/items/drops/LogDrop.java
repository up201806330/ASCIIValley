package com.g64.model.items.drops;

import com.g64.exceptions.RemoveFromInventory;
import com.g64.model.Position;
import com.g64.model.entities.EntityModel;
import com.g64.model.entities.map.TreeEntity;
import com.g64.model.entities.target.Target;
public class LogDrop extends Drop {

    public LogDrop(){
        super("LOG");
    }

    @Override
    public EntityModel getEntityFromDrop(Position position) {
        return new TreeEntity(position);
    }

    @Override
    public void accept(Target target) throws RemoveFromInventory {
        target.allowUsage(this);
    }
}
