package org.dionysius.content;

import org.dionysius.game.*;

import javax.annotation.Nonnull;
import java.io.File;

import javafx.geometry.Point2D;

public class Merchant extends Creature implements Interactable {

    private static final AnimationLoader animation = new AnimationLoader(new File("src/main/resources/text/animation/Merchant.json"));
    private static final DialogLoader dialog = new DialogLoader(new File("src/main/resources/text/dialog/Merchant.json"));
    static {
        dialog.getLoaded().get("5").setOnAction((dialog, hero) -> hero.getGame().loadNextLevel());
    }

    private final DialogBox box = new DialogBox();
    {
        box.setLayoutX( 50 );
        box.setLayoutY( 50 );
    }

    public Merchant(@Nonnull Game game) {
        super(game, new Point2D(500, 0));
        getHitbox().setWidth(64);
        getHitbox().setHeight(50);

        getAnimations().putAll(animation.getLoaded());
        getDialogBox().getDialogs().putAll(dialog.getLoaded());

        flip(DIRECTION_LEFT);
        idle();
    }

    public DialogBox getDialogBox() {
        return box;
    }

    @Override
    public void interact() {
        getGame().setDialog(getDialogBox());
        getDialogBox().show();
    }
}
