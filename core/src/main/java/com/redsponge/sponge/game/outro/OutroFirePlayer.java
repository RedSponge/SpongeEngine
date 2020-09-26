package com.redsponge.sponge.game.outro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.animation.AnimationNodeSystem;
import com.redsponge.sponge.components.AnimationComponent;
import com.redsponge.sponge.physics.Collision;
import com.redsponge.sponge.physics.PActor;
import com.redsponge.sponge.physics.Trigger;
import com.redsponge.sponge.screen.Scene;

public class OutroFirePlayer extends PActor {

    private AnimationComponent drawn;
    private AnimationNodeSystem ans;
    private Vector2 vel;
    private float gravity = -100;
    private AnimationStage animState;

    private TypingLabel label;
    private boolean isTalking;


    private enum AnimationStage {
        FALL_DOWN {
            @Override
            public void update(OutroFirePlayer player, float delta) {
                super.update(player, delta);
                if(elapsedTime > 2) {
                    player.animState = ONTO_PORTAL;
                }
            }
        },
        ONTO_PORTAL {
            @Override
            public void update(OutroFirePlayer player, float delta) {
                super.update(player, delta);
                player.vel.x = 50;
            }
        },
        TALK_1 {
            @Override
            public void update(OutroFirePlayer player, float delta) {
                if(elapsedTime == 0) {
                    player.isTalking = true;
                    player.label.restart("{SLOW}Hey!{WAIT} Um, do you know what's going on here?{WAIT} ");
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
            public void update(OutroFirePlayer player, float delta) {
                super.update(player, delta);
                if(elapsedTime > 10 && !done) {
                    System.out.println("NOW");
                    done = true;
                    player.isTalking = true;
                    player.label.restart("Oh yeah,{WAIT=0.5} That was a thing.{WAIT=2} By the way, if I may ask,{WAIT=1} {COLOR=red}{WIND}How can you live in this boiling place?!{ENDWIND}{WAIT=2} ");
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
            public void update(OutroFirePlayer player, float delta) {
                super.update(player, delta);
                if(elapsedTime > 7 && !done) {
                    done = true;
                    player.isTalking = true;
                    player.label.restart("Anyways,{WAIT} can we please swap back?{WAIT=2} ");
                    player.label.setTypingListener(new TypingAdapter() {
                        @Override
                        public void end() {
                            super.end();
                            player.isTalking = false;
                            player.animState = SWAP;
                        }
                    });
                }
            }
        }
        ;
        float elapsedTime;
        public void update(OutroFirePlayer player, float delta) {
            elapsedTime += delta;
        }
    }

    public OutroFirePlayer(Vector2 pos) {
        super(pos);
        vel = new Vector2();
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
        label.setPosition(20, 240);
        label.setSize(480 - 40, 20);
        label.setFontScale(0.5f);
        label.setWrap(true);

        animState = AnimationStage.FALL_DOWN;
        ans = scene.getAssets().getAnimationNodeSystemInstance("ice_player");
        drawn = new AnimationComponent(true, true, ans.getActiveAnimation());
        drawn.setScaleX(1.5f).setScaleY(1.5f);
        add(drawn);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        animState.update(this, delta);
        vel.y += gravity * delta;

        moveX(vel.x * delta, this::collideX);
        moveY(vel.y * delta, this::collideY);

        boolean onGround = groundCheck();

        ans.putValue("is_on_ground", onGround);
        ans.putValue("y_speed", vel.y);
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
            SpongeGame.i().getShapeDrawer().filledRectangle(10, 240, 460, 30, new Color(0, 0.4f, 0.7f, 1));
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
