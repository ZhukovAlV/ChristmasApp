package com.example.christmasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    /**
     * Елка
     */
    ImageView mainImageView;

    /**
     * Снежинка
     */
    ImageView snowflake;

    /**
     * Кнопка "Начало игры"
     */
    ImageView startButton;

    /**
     * Поле для вывода очков
     */
    TextView textViewPoints;

    /**
     * Поле для вывода уровня игры
     */
    TextView textViewLevel;

    /**
     * Количество очков
     */
    int points = 0;

    /**
     * Генератор для вычисления случайных координат снежинки
     */
    Random random;

    /**
     * Таймер для перемещения снежинки
     */
    Timer snowflakeTimer = null;

    /**
     * Начальное значение задержки для перемещения снежинки
     */
    int snowflakeTimerPeriod = 1000;

    /**
     * Начальный уровень игры
     */
    int level = 0;

    /**
     * Начальное количество очков
     */
    int winPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // winPoints = Calendar.getInstance().get(Calendar.YEAR);
        winPoints = 400;
        mainImageView = findViewById(R.id.imageView);
        startButton = findViewById(R.id.imageView1);
        snowflake = findViewById(R.id.imageView2);

        textViewPoints = findViewById(R.id.textViewPoints);
        showPoints();

        textViewLevel = findViewById(R.id.textViewLevel);
        showLevel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startButton.setVisibility(View.VISIBLE);
        snowflake.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        snowflakeTimerDestroy();
        snowflake.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        snowflakeTimerDestroy();
        snowflake.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        super.onDestroy();
    }

    /**
     * Отобразить набранные очки
     */
    void showPoints() {
        textViewPoints.setText("Очки: " + points);
    }

    /**
     * Отобразить уровень игры
     */
    void showLevel() {
        textViewLevel.setText("Уровень: " + level);
    }

    public void snowflakeClick(View view) {
        //System.out.println("Снежинка активирована");
        snowflakeTimerDestroy();
        points += 10;
        if (points > winPoints) {
            points = winPoints;
        }
        showPoints();

        if (points < winPoints) {
            if (points % 100 == 0) {
                if (level < 3) {
                    level++;
                    showLevel();
                    snowflakeTimerPeriod -= 50;
                }
            }
            startSnowflakeTimer();
        }
        else {
            snowflake.setVisibility(View.INVISIBLE);
            textViewLevel.setText("Победа!!!");
        }
    }

    /**
     * Запуск игры при нажатии на кнопку Play
     * @param view View
     */
    public void doStart(View view) {
        System.out.println("Запуск приложения");

        if (points >= winPoints) {
            return;
        }

        random = new Random();

        startButton.setVisibility(View.INVISIBLE);

        snowflake.setVisibility(View.VISIBLE);
        startSnowflakeTimer();
    }


    /**
     * Запуск таймера перемещения снежинки
     */
    void startSnowflakeTimer() {
        snowflakeTimerDestroy();
        snowflakeTimer = new Timer();
        snowflakeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snowflake.setX(getSnowflakeRandomLeft());
                        snowflake.setY(getSnowflakeRandomTop());
                    }
                });
            }
        }, 0, snowflakeTimerPeriod);
    }

    /**
     * Удаление объекта таймера
     */
    void snowflakeTimerDestroy() {
        if (snowflakeTimer != null) {
            snowflakeTimer.cancel();
            snowflakeTimer = null;
        }
    }

    /**
     * Получить случайную x-координату снежинки
     * @return
     */
    int getSnowflakeRandomLeft() {
        return random.nextInt(mainImageView.getWidth() - snowflake.getWidth());
    }

    /**
     * Получить случайную y-координату снежинки
     * @return
     */
    int getSnowflakeRandomTop() {
        return random.nextInt(mainImageView.getHeight() - textViewPoints.getHeight() - snowflake.getHeight()) + textViewPoints.getHeight();
    }
}