package model.terrain;

import com.googlecode.lanterna.TextColor;
import model.Position;

public class WaterTerrain extends MapTerrain {
    public WaterTerrain(Position position){
        super(position, new TextColor.RGB(0, 153, 255));
    }
}