package com.redsponge.sponge.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.redsponge.sponge.SpongeGame;
import com.redsponge.sponge.entity.Entity;
import com.redsponge.sponge.event.EventBus;
import com.redsponge.sponge.event.EventHandler;
import com.redsponge.sponge.screen.Scene;

public class GuiRenderer extends Entity {

    private Texture bg, buttonTex;
    private Texture arrowPX, arrowPY, arrowNX, arrowNY;
    private TextureRegion jumpTile;

    private Array<GameButton> buttons;
    private GameButton playPauseButton, resetButton;

    private Vector2 mousePosition;

    private int[] arrowCounts;

    private BitmapFont fnt;
    private IsometricTileMapRenderer mapRenderer;
    private int selectedIndex;
    private Level level;

    private Texture begin, stop, reset;
    private boolean playing;

    public GuiRenderer() {
        super(new Vector2(0, 0));
    }

    @Override
    public void added(Scene scene) {
        super.added(scene);
        bg = scene.getAssets().get("gui_bg.png");
        buttonTex = scene.getAssets().get("button.png");


        arrowPX = scene.getAssets().get("arrow_px.png");
        arrowNX = scene.getAssets().get("arrow_nx.png");
        arrowPY = scene.getAssets().get("arrow_py.png");
        arrowNY = scene.getAssets().get("arrow_ny.png");
        jumpTile = scene.getAssets().<TextureAtlas>get("world.atlas").findRegion("trampoline");

        buttons = new Array<>();
        buttons.add(createSelectableButton(4, 30 + 120, new TextureRegion(arrowPX)));
        buttons.add(createSelectableButton(4, 30 +  90, new TextureRegion(arrowPY)));
        buttons.add(createSelectableButton(4, 30 +  60, new TextureRegion(arrowNX)));
        buttons.add(createSelectableButton(4, 30 +  30, new TextureRegion(arrowNY)));
        buttons.add(createSelectableButton(4, 30 , jumpTile));
        mousePosition = new Vector2();

        begin = scene.getAssets().get("begin.png");
        reset = scene.getAssets().get("clear.png");
        stop = scene.getAssets().get("pause.png");

        fnt = new BitmapFont(Gdx.files.internal("fonts/pixel.fnt"));

        mapRenderer = getScene().first(IsometricTileMapRenderer.class);
        selectedIndex = -1;

        playPauseButton = new GameButton(300, 30 + 120, this::togglePausePlay, 16, 16, new TextureRegion(begin), null);
        buttons.add(playPauseButton);

        EventBus.getInstance().registerListener(this);
    }

    private void togglePausePlay() {
        if(playing) {
            pause();
        } else {
            play();
        }
    }

    private void play() {
        if(playing) return;
        playPauseButton.getDrawn().setTexture(stop);
        playing = true;
        EventBus.getInstance().dispatch(new BeginMoveEvent());
    }

    private void pause() {
        if(!playing) return;
        playPauseButton.getDrawn().setTexture(begin);
        playing = false;
        EventBus.getInstance().dispatch(new ResetMoveEvent());
    }

    public GameButton createSelectableButton(int x, int y, TextureRegion innerTex) {
        GameButton gb = new GameButton(x, y, null, buttonTex.getWidth(), buttonTex.getHeight(), new TextureRegion(buttonTex), innerTex);
        gb.setOnClick(() -> {
            EventBus.getInstance().dispatch(new SelectButtonEvent(gb));
        });
        return gb;
    }

    public void loadLevel(Level level) {
        selectedIndex = -1;
        System.out.println(level);
        arrowCounts = new int[] {
                level.getPxCount(), level.getPyCount(), level.getNxCount(), level.getNyCount(), level.getSpringCount()
        };
        this.level = level;
    }

    @Override
    public void update(float delta) {
        mousePosition = getScene().getRenderingPipeline().getToScreenViewport().unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 ps = mapRenderer.getIndexByPoint(mousePosition, new Vector2());
            if(ps.x >= 0 && ps.y >= 0 && ps.x < 7 && ps.y < 7 && selectedIndex != -1 && level.getTiles()[(int) ps.y][(int) ps.x] instanceof EmptyTile) {
                System.out.println(ps);
                switch (selectedIndex) {
                    case 0: mapRenderer.getLevel().setRoomObject(new DirectionSetTile(new Vector2(1, 0)), (int) ps.x, (int) ps.y); break;
                    case 1: mapRenderer.getLevel().setRoomObject(new DirectionSetTile(new Vector2(0, 1)), (int) ps.x, (int) ps.y); break;
                    case 2: mapRenderer.getLevel().setRoomObject(new DirectionSetTile(new Vector2(-1, 0)), (int) ps.x, (int) ps.y); break;
                    case 3: mapRenderer.getLevel().setRoomObject(new DirectionSetTile(new Vector2(0, -1)), (int) ps.x, (int) ps.y); break;
                    case 4: mapRenderer.getLevel().setRoomObject(new SkipTile(), (int) ps.x, (int) ps.y); break;
                }
                arrowCounts[selectedIndex]--;
                if(arrowCounts[selectedIndex] <= 0) {
                    selectedIndex = -1;
                }
            } else {
                selectedIndex = -1;
                Rectangle rct = new Rectangle(0, 0, buttonTex.getWidth(), buttonTex.getHeight());
                for (int i = 0; i < buttons.size; i++) {
                    GameButton button = buttons.get(i);
                    if (rct.set(button.getX(), button.getY(), buttonTex.getWidth(), buttonTex.getHeight()).contains(mousePosition) && (i >= arrowCounts.length || arrowCounts[i] > 0)) {
                        button.getOnClick().run();
                        if(i < arrowCounts.length) selectedIndex = i;
                    }
                }
            }
        }
        updateButtonDisplays();
    }



    private void updateButtonDisplays() {
        for (int i = 0; i < buttons.size; i++) {
            GameButton button = buttons.get(i);
            if(i < arrowCounts.length) {
                button.getBgColor().set(playing ? Color.GRAY : selectedIndex == i ? Color.YELLOW : arrowCounts[i] > 0 ? Color.WHITE : Color.GRAY);
            }
        }
    }

    @Override
    public void render() {
        super.render();
        SpriteBatch batch = SpongeGame.i().getBatch();
        batch.draw(bg, 0, 0, 320, 180);
        for (int i = 0; i < buttons.size; i++) {
            GameButton gameButton = buttons.get(i);
            gameButton.render(batch);
            if(i < arrowCounts.length) {
                fnt.draw(batch, "" + arrowCounts[i], gameButton.getX() + 23, gameButton.getY() + 15);
            }
        }
    }

    @EventHandler
    public void onDie(ResetMoveEvent event) {
        pause();
    }
}
