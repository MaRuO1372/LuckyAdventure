package com.godimago.dima;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class DimaAdventure extends ApplicationAdapter {
    SpriteBatch batch;

    Texture background;
    Texture topPredio;
    Texture predio;
    Texture gameOver;

    int spaceBetweenTubes = 700;
    Random random;
    int predioSpeed = 5;
    int prediosNumber = 40;
    float predioX[] = new float[prediosNumber];
    boolean booleans[] = new boolean[prediosNumber];
    float predioShift[] = new float[prediosNumber];
    float distanceBetweenpredios;

    Texture[] dima;
    int dimaStateDlag = 0;
    float flyHeight;
    float fallingSpeed = 0;
    int gameStateFlag = 0;

    Circle dimaCircle;
    Rectangle[] topTubesRectangles;
    Rectangle[] bottomTubesRectangles;
    int gameScore = 0;
    int passedTubeIndex = 0;
    BitmapFont scoreFont;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("back.jpg");
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.YELLOW);
        scoreFont.getData().setScale(10);

        dimaCircle = new Circle();
        topTubesRectangles = new Rectangle[prediosNumber];
        bottomTubesRectangles = new Rectangle[prediosNumber];

        topPredio = new Texture("predio.jpg");
        predio = new Texture("predio.jpg");
        gameOver = new Texture("game_over.png");
        random = new Random();
        dima = new Texture[2];
        dima[0] = new Texture("w1.png");
        dima[1] = new Texture("w2.png");
        distanceBetweenpredios = Gdx.graphics.getWidth() / 1.5f;
        initGame();
    }

    public void initGame() {
        flyHeight = Gdx.graphics.getHeight() / 2 - dima[0].getHeight() / 2;
        for (int i = 0; i < prediosNumber; i++) {
            predioX[i] = Gdx.graphics.getWidth() * 1.5f - topPredio.getWidth() / 2 + i * distanceBetweenpredios;
            predioShift[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - spaceBetweenTubes * 2);
            booleans[i] = getRandomBoolean();
            topTubesRectangles[i] = new Rectangle();
            bottomTubesRectangles[i] = new Rectangle();
        }
    }

    public void drawTubes() {
        for (int i = 0; i < prediosNumber; i++) {
            if (predioX[i] < -topPredio.getWidth()) {
                predioX[i] = prediosNumber * distanceBetweenpredios;
            } else {
                predioX[i] -= predioSpeed;
            }
            if (booleans[i]){
                batch.draw(topPredio, predioX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + predioShift[i]);
                topTubesRectangles[i] = new Rectangle(predioX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + predioShift[i], topPredio.getWidth(), topPredio.getHeight());

            } else {
                batch.draw(predio, predioX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - predio.getHeight() + predioShift[i]);
                bottomTubesRectangles[i] = new Rectangle(predioX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - predio.getHeight() + predioShift[i], predio.getWidth(), predio.getHeight());

            }

        }
    }
    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        if (gameStateFlag == 1) {
            drawTubes();
            if (predioX[passedTubeIndex] < Gdx.graphics.getWidth() / 2) {
                gameScore++;
                if (passedTubeIndex < prediosNumber - 1) {
                    passedTubeIndex++;
                } else {
                    passedTubeIndex = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                fallingSpeed = -20;
            }
            if (flyHeight > 0 && flyHeight < Gdx.graphics.getHeight()) {
                fallingSpeed++;
                flyHeight -= fallingSpeed;
            } else {
                flyHeight = 0;
                flyHeight -= fallingSpeed;
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


        if (dimaStateDlag == 0) {
            dimaStateDlag = 1;
        } else {
            dimaStateDlag = 0;
        }

        batch.draw(dima[dimaStateDlag],
                Gdx.graphics.getWidth() / 2 - dima[dimaStateDlag].getWidth() / 2, flyHeight);
        scoreFont.draw(batch, String.valueOf(gameScore), 100, 200);
        batch.end();
        dimaCircle.set(Gdx.graphics.getWidth() / 2, flyHeight + dima[dimaStateDlag].getHeight() / 2, dima[dimaStateDlag].getWidth() / 2);
        for (int i = 0; i < prediosNumber; i++) {
            if (Intersector.overlaps(dimaCircle, topTubesRectangles[i]) || Intersector.overlaps(dimaCircle, bottomTubesRectangles[i])) {
                gameStateFlag = 2;
            }
        }


    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
