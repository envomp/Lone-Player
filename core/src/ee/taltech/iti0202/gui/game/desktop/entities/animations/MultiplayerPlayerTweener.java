package ee.taltech.iti0202.gui.game.desktop.entities.animations;

import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.PlayerTweener;

import java.util.Stack;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;

public class MultiplayerPlayerTweener extends PlayerTweener {

    private float influence;
    private float changeSpeed;
    private Stack<MultiplayerAnimation> animations = new Stack<>();

    public MultiplayerPlayerTweener(Entity entity) {
        super(entity);
        getSecondPlayer().addListener(new MyPlayerListener(() -> {
            while (!animations.isEmpty() && animations.peek().isToPlayOnce()) {
                animations.pop();
            }
            if (!animations.isEmpty())
                setAnimation(animations.pop());  //Should be first to default to...
        }));
    }

    public void update(float dt) {
        if (influence < 1) {
            influence += dt * changeSpeed / B2DVars.PLAYER_ANIMATION_CHANGE_SPEED;
            if (influence > 1) influence = 1;
            setWeight(influence);
        }
        super.update(dt);
    }

    public void setAnimation(MultiplayerAnimation animation) {
        if (animations.isEmpty() || animations.peek() != animation)animations.push(animation);
        influence = 0;
        getFirstPlayer().setAnimation(getSecondPlayer().getAnimation());
        getSecondPlayer().setAnimation(animation.getName());

        changeSpeed = animation.isToPlayOnce() || animations.peek().isToPlayOnce() ? 2f : 1f;
    }

    public MultiplayerAnimation getCurrentAnimation() {
        return !animations.isEmpty() ? animations.peek() : null;
    }
}