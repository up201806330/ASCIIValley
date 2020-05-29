package com.g64.model.entities.enemy.MovementHumour;

import com.g64.controller.GameController;
import com.g64.controller.action.*;
import com.g64.model.Position;
import com.g64.model.entities.enemy.Enemy;

public class MummyMovementAggroed extends EnemyMovementHumour {
    public MummyMovementAggroed(Enemy me) {
        super(me);
    }

    @Override
    public ActionEvent move(GameController controller) {
        Position mummyPosition = me.getPosition();
        Position playerPosition = controller.getPlayer().getPosition();
        double verticalDifference = mummyPosition.verticalDifference(controller.getPlayer().getPosition());
        double horizontalDifference = mummyPosition.horizontalDifference(controller.getPlayer().getPosition());

        if      (mummyPosition.adjacent(playerPosition)) return new AttackPlayer(controller, me.getAttackValue());
        if      (verticalDifference   < 0) return new MoveDown(controller, me);
        else if (verticalDifference   > 0) return new MoveUp(controller, me);
        if      (horizontalDifference < 0) return new MoveRight(controller, me);
        else if (horizontalDifference > 0) return new MoveLeft(controller, me);
        return new NullAction();
    }
}
