package com.redsponge.sponge.game.outro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.game.WinScene;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.physics.Trigger;
import com.redsponge.sponge.screen.Scene;

public class OutroIcePlayer extends PActor {
    private AnimationComponent drawn;
    private AnimationNodeSystem ans;
    private Vector2 vel;
    private float gravity = 100;
    private AnimationStage animState;

    private TypingLabel label;
    private boolean isTalking;


    private enum AnimationStage {
        FALL_DOWN {
            @Override
            public void update(OutroIcePlayer player, float delta) {
                super.update(player, delta);
                if(elapsedTime > 2) {
                    player.animState = ONTO_PORTAL;
                }
            }
        },
        ONTO_PORTAL {
            @Override
            public void update(OutroIcePlayer player, float delta) {
                super.update(player, delta);
                player.vel.x = -50;
            }
        },
        TALK_1 {
            boolean done;
            @Override
            public void update(OutroIcePlayer player, float delta) {
                if(elapsedTime > 3 && !done) {
                    done = true;
                    player.isTalking = true;
                    player.label.restart("Yeah!{WAIT=0.5} Haven't you read the itch page that RedSponge wrote with much love and care?!{WAIT=1} We're the same person, different worlds..{WAIT=2} ");
                    player.label.setTypingListener(new TypingAdapter() {
                        @Override
                        public void end() {
                            super.end();
                            player.isTalking = false;
                            player.animState = TALK_2;
                        }
                    });
                }
                super.update(player, delta);
                player.vel.x = 0;
            }
        },
        TALK_2 {
            boolean done;
            @Override
            public void update(OutroIcePlayer player, float delta) {
                super.update(player, delta);
                if(elapsedTime > 10 && !done) {
                    System.out.println("NOW");
                    done = true;
                    player.isTalking = true;
                    player.label.restart("{SHAKE}What?!{ENDSHAKE}{WAIT=1} I WISH it was this hot up over here!{WAIT=0.5} I can't feel my face!{WAIT=3} ");
                    player.label.setTypingListener(new TypingAdapter() {
                        @Override
                        public void end() {
                            super.end();
                            player.isTalking = false;
                            player.animState = TALK_3;
                        }
                    });
                }
            }
        },
        SWAP {

        },
        TALK_3 {
            boolean done;
            @Override
            public void update(OutroIcePlayer player, float delta) {
                super.update(player, delta);
                if(elapsedTime > 2 && !done) {
                    done = true;
                    player.isTalking = true;
                    player.label.restart("Yeah!{WAIT=0.5} Let's{WAIT=3}");
                    player.label.setTypingListener(new TypingAdapter() {
                        @Override
                        public void end() {
                            super.end();
                            player.isTalking = false;
                            player.animState = SWAP;
                            ((WinScene)player.getScene()).swap();
                        }
                    });
                }
            }
        }
        ;
        float elapsedTime;
        public void update(OutroIcePlayer player, float delta) {
            elapsedTime += delta;
        }
    }

    public OutroIcePlayer(Vector2 pos) {
        super(pos);
        vel = new Vector2();
        getHitbox().set(0, 0, 16, 16);
        setOnTrigger(this::onTrigger);
    }

    private void onTrigger(Trigger trigger) {
        if(trigger.trigger instanceof StartTalkingTrigger) {
            animState = AnimationStage.TALK_1;
        }
    }

    private BitmapFont font;

    @Override
    public void added(Scene scene) {
        super.added(scene);
        font = new BitmapFont(Gdx.files.internal("fonts/pixelmix/16/pixelmix_16.fnt"));
        label = new TypingLabel("", new Label.LabelStyle(font, Color.WHITE));
        label.setPosition(20, 10);
        label.setSize(480 - 40, 20);
        label.setFontScale(0.5f);
        label.setWrap(true);

        animState = AnimationStage.FALL_DOWN;
        ans = scene.getAssets().getAnimationNodeSystemInstance("player");
        drawn = new AnimationComponent(true, true, ans.getActiveAnimation());
        drawn.setScaleX(1.5f).setScaleY(1.5f);
        drawn.setFlippedX(true).setFlippedY(true);
        add(drawn);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        animState.update(this, delta);
        vel.y += gravity * delta;

        moveX(vel.x * delta, this::collideX);
        moveY(vel.y * delta, this::collideY);

        boolean onGround = groundCheck(-1);

        ans.putValue("is_on_ground", onGround);
        ans.putValue("y_speed", -vel.y);
        ans.putValue("x_speed", vel.x);
        ans.update();

        drawn.setAnimation(ans.getActiveAnimation());
        drawn.update(0);

        if(isTalking) {
            label.act(delta);
        }
    }

    @Override
    public void render() {
        super.render();
        if(isTalking) {
            SpongeGame.i().getShapeDrawer().filledRectangle(10, 5, 460, 30, new Color(0.5f, 0, 0, 1));
            label.draw(SpongeGame.i().getBatch(), 1);
        }
    }

    private void collideX(Collision collision) {

    }

    private void collideY(Collision collision) {
        vel.y = 0;
        zeroRemainderY();
    }

    @Override
    public void removed() {
        super.removed();
        font.dispose();
    }
}
