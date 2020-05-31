package com.g64.model.entities.map;

import com.g64.model.Position;
import com.googlecode.lanterna.TextColor;

public class WaterEntity extends MapEntity{
    public WaterEntity(Position position){
        super(position, "~", new TextColor.RGB(0, 204, 255), true, null, 1);
    }
}
