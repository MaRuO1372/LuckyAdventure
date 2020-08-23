package com.nikorych.nikkybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class NikkyBird extends ApplicationAdapter {
    SpriteBatch batch;

    Texture background;
    Texture topTube;
    Texture bottomTube;
    Texture gameOver;

    int spaceBetweenTubes = 700;
    Random random;
    int tubeSpeed = 5;
    int tubesNumber = 5;
    float tubeX[] = new float[tubesNumber];
    float tubeShift[] = new float[tubesNumber];
    float distanceBetweenTubes;

    Texture[] bird;
    int birdStateFlag = 0;
    float flyHeight;
    float fallingSpeed = 0;
    int gameStateFlag = 0;

    Circle birdCircle;
    Rectangle[] topTubesRectangles;
    Rectangle[] bottomTubesRectangles;
    int gameScore = 0;
    int passedTubeIndex = 0;
    BitmapFont scoreFont;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.YELLOW);
        scoreFont.getData().setScale(10);

        birdCircle = new Circle();
        topTubesRectangles = new Rectangle[tubesNumber];
        bottomTubesRectangles = new Rectangle[tubesNumber];

        topTube = new Texture("top_tube.png");
        bottomTube = new Texture("bottom_tube.png");
        gameOver = new Texture("game_over.png");
        random = new Random();
        bird = new Texture[2];
        bird[0] = new Texture("bird_wings_up.png");
        bird[1] = new Texture("bird_wings_down.png");
        distanceBetweenTubes = Gdx.graphics.getWidth() / 1.5f;
        initGame();
    }

    public void initGame() {
        flyHeight = Gdx.graphics.getHeight() / 2 - bird[0].getHeight() / 2;
        for (int i = 0; i < tubesNumber; i++) {
            tubeX[i] = Gdx.graphics.getWidth() * 1.5f - topTube.getWidth() / 2 + i * distanceBetweenTubes;
            tubeShift[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - spaceBetweenTubes * 2);

            topTubesRectangles[i] = new Rectangle();
            bottomTubesRectangles[i] = new Rectangle();
        }
    }

    public void drawTubes() {
        for (int i = 0; i < tubesNumber; i++) {
            if (tubeX[i] < -topTube.getWidth()) {
                tubeX[i] = tubesNumber * distanceBetweenTubes;
            } else {
                tubeX[i] -= tubeSpeed;
            }
            batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i]);
            batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i]);
            topTubesRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i], topTube.getWidth(), topTube.getHeight());
            bottomTubesRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i], bottomTube.getWidth(), bottomTube.getHeight());

        }
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        if (gameStateFlag == 1) {
            drawTubes();
            if (tubeX[passedTubeIndex] < Gdx.graphics.getWidth() / 2) {
                gameScore++;
                if (passedTubeIndex < tubesNumber - 1) {
                    passedTubeIndex++;
                } else {
                    passedTubeIndex = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                fallingSpeed = -30;
            }
            if (flyHeight > 0 && flyHeight < Gdx.graphics.getHeight()) {
                fallingSpeed++;
                flyHeight -= fallingSpeed;
            } else {
                gameStateFlag = 2;
            }

        } else if (gameStateFlag == 0) {
            if (Gdx.input.justTouched()) {
                gameStateFlag = 1;
            }
        } else if (gameStateFlag == 2) {
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                gameStateFlag = 1;
                initGame();
                gameScore = 0;
                passedTubeIndex = 0;
                fallingSpeed = 0;
            }
        }


        if (birdStateFlag == 0) {
            birdStateFlag = 1;
        } else {
            birdStateFlag = 0;
        }

        batch.draw(bird[birdStateFlag],
                Gdx.graphics.getWidth() / 2 - bird[birdStateFlag].getWidth() / 2, flyHeight);
        scoreFont.draw(batch, String.valueOf(gameScore), 100, 200);
        batch.end();
        birdCircle.set(Gdx.graphics.getWidth() / 2, flyHeight + bird[birdStateFlag].getHeight() / 2, bird[birdStateFlag].getWidth() / 2);
        for (int i = 0; i < tubesNumber; i++) {
            if (Intersector.overlaps(birdCircle, topTubesRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubesRectangles[i])) {
                gameStateFlag = 2;
            }
        }


    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
