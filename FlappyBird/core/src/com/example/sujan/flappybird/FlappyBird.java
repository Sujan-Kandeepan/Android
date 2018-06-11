package com.example.sujan.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import static com.example.sujan.flappybird.FlappyBird.GameState.Active;
import static com.example.sujan.flappybird.FlappyBird.GameState.Over;
import static com.example.sujan.flappybird.FlappyBird.GameState.Start;

public class FlappyBird extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Texture[] birds;
    private Texture topTube;
    private Texture bottomTube;
    private Texture gameover;

    private ShapeRenderer shapeRenderer;
    private Circle birdCircle;
    private Rectangle[] topTubeRectangles;
    private Rectangle[] bottomTubeRectangles;

    private BitmapFont font;

    private int flapState = 0, numberOfTubes = 4, score = 0, scoringTube = 0;
    private float birdY, birdScale = 1.5f, velocity = 0, gravity = 1.5f, gap = 900, maxTubeOffset,
            tubeVelocity = 4, tubeInterval, tubeScale = 1.25f;
    private float[] tubeXs = new float[numberOfTubes], tubeOffsets = new float[numberOfTubes];

    protected enum GameState {Start, Active, Over}
    private GameState gameState = Start;

    public void startGame() {
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
        velocity = 0;
        score = 0;
        scoringTube = 0;
        for (int i = 0; i < numberOfTubes; i++) {
            tubeXs[i] = Gdx.graphics.getWidth() + i * tubeInterval;
            tubeOffsets[i] = (float) ((new Random()).nextFloat() - 0.5) * maxTubeOffset;
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        gameover = new Texture("gameover.png");

        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        tubeInterval = Gdx.graphics.getWidth() / 2;

        for (int i = 0; i < numberOfTubes; i++) {
            tubeXs[i] = Gdx.graphics.getWidth() + i * tubeInterval;
            tubeOffsets[i] = (float) ((new Random()).nextFloat() - 0.5) * maxTubeOffset;
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == Active) {
            velocity += gravity;
            birdY -= velocity;

            if (tubeXs[scoringTube] == Gdx.graphics.getWidth() / 2) {
                score++;
                scoringTube = (scoringTube + 1) % numberOfTubes;
                Gdx.app.log("Score", Integer.toString(score));
            }

            if (Gdx.input.justTouched()) {
                Gdx.app.log("Info", "Flapped wings");
                velocity = -30;
            }

            for (int i = 0; i < numberOfTubes; i++) {
                tubeXs[i] -= tubeVelocity;

                if (tubeXs[i] == -topTube.getWidth() * tubeScale) {
                    tubeXs[i] = tubeXs[(i + numberOfTubes - 1) % numberOfTubes] + tubeInterval;
                    tubeOffsets[i] = (float) ((new Random()).nextFloat() - 0.5) * maxTubeOffset;
                }

                batch.draw(topTube, tubeXs[i], Gdx.graphics.getHeight() / 2 + gap / 2
                                + tubeOffsets[i], topTube.getWidth() * tubeScale,
                        topTube.getHeight() * tubeScale);
                batch.draw(bottomTube, tubeXs[i], Gdx.graphics.getHeight() / 2 - gap / 2
                                - bottomTube.getHeight() + tubeOffsets[i],
                        topTube.getWidth() * tubeScale, topTube.getHeight() * tubeScale);

                topTubeRectangles[i] = new Rectangle(tubeXs[i], Gdx.graphics.getHeight() / 2
                        + gap / 2 + tubeOffsets[i], topTube.getWidth() * tubeScale,
                        topTube.getHeight() * tubeScale);
                bottomTubeRectangles[i] = new Rectangle(tubeXs[i], Gdx.graphics.getHeight() / 2
                        - gap / 2 - bottomTube.getHeight() + tubeOffsets[i],
                        bottomTube.getWidth() * tubeScale,
                        bottomTube.getHeight() * tubeScale);

                if (Intersector.overlaps(birdCircle, topTubeRectangles[i])
                        || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
                    Gdx.app.log("Info", "Collision!");
                    gameState = Over;
                }
            }

            if (birdY <= 0) {
                gameState = Over;
            }
        } else if (gameState == Start) {
            score = 0;
            if (Gdx.input.justTouched()) {
                Gdx.app.log("Info", "Game started");
                gameState = Active;
            }
        } else if (gameState == Over) {
            for (int i = 0; i < numberOfTubes; i++) {
                batch.draw(topTube, tubeXs[i], Gdx.graphics.getHeight() / 2 + gap / 2
                                + tubeOffsets[i], topTube.getWidth() * tubeScale,
                        topTube.getHeight() * tubeScale);
                batch.draw(bottomTube, tubeXs[i], Gdx.graphics.getHeight() / 2 - gap / 2
                                - bottomTube.getHeight() + tubeOffsets[i],
                        topTube.getWidth() * tubeScale, topTube.getHeight() * tubeScale);
            }

            if (Gdx.input.justTouched()) {
                Gdx.app.log("Info", "Returned to start screen");
                gameState = Active;
                startGame();
            }
        }

        flapState = (flapState + 1) % 16;
        if (gameState == Over) {
            flapState = 0;
        }

        batch.draw(birds[flapState / 8], Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2,
                birdY, birds[0].getWidth() * birdScale,
                birds[0].getHeight() * birdScale);
        font.draw(batch, String.valueOf(score), 100, 200);

        if (gameState == Over) {
            batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth(),
                    Gdx.graphics.getHeight() / 2 - gameover.getHeight(),
                    gameover.getWidth() * 2, gameover.getHeight() * 2);
        }

        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2 + 25,
                birdY + birds[0].getHeight() - 25, birds[0].getWidth() - 40);

        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
        for (int i = 0; i < numberOfTubes; i++) {
            shapeRenderer.rect(tubeXs[i], Gdx.graphics.getHeight() / 2
                    + gap / 2 + tubeOffsets[i], topTube.getWidth() * tubeScale,
                    topTube.getHeight() * tubeScale);
            shapeRenderer.rect(tubeXs[i], Gdx.graphics.getHeight() / 2
                            - gap / 2 - bottomTube.getHeight() + tubeOffsets[i],
                    bottomTube.getWidth() * tubeScale,
                    bottomTube.getHeight() * tubeScale);
        }
        shapeRenderer.end();
        */

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
