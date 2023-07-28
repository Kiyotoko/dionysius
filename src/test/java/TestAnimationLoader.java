import com.google.gson.Gson;
import org.dionysius.content.AnimationLoader;
import org.dionysius.game.Creature;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestAnimationLoader {

    @Test
    void printAnimations() {
        assertDoesNotThrow(() -> {
            AnimationLoader.BundleSource source = new AnimationLoader.BundleSource(
                    Map.of(Creature.ANIMATION_IDLE,
                            new AnimationLoader.AnimationSource("art/creature/zapper/AnimationIdle.png", new AnimationLoader.FrameSource(0, 0, 90, 90, 10.0))));
            System.out.println(new Gson().toJson(source));
        });
    }
}
