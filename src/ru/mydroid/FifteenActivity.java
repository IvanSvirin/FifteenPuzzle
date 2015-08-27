package ru.mydroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class FifteenActivity extends Activity {
    private Button b11;
    private Button b12;
    private Button b13;
    private Button b14;

    private Button b21;
    private Button b22;
    private Button b23;
    private Button b24;

    private Button b31;
    private Button b32;
    private Button b33;
    private Button b34;

    private Button b41;
    private Button b42;
    private Button b43;
    private Button b44;

    private Button[][] buttons = new Button[4][4];
    private int[][] array = new int[4][4];
    private Point emptySpace = new Point();
    int stepsQtyCounter = 0;
    public TextView stepsQty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        stepsQty = (TextView) findViewById(R.id.stepQty);
        initArray();
        generateArray();
        paintTable();
        setListenersOnButtons();
    }

    private void setListenersOnButtons() {
        OnClickListener listener = new OnClickListener() {
            public void onClick(View myView) {
                Button clickedButton = (Button) myView;
                Point clickedPoint = getClickedPoint(clickedButton);
                if (clickedPoint != null && canMove(clickedPoint)) {
                    stepsQtyCounter++;
                    stepsQty.setText("Steps Qty: " + stepsQtyCounter);
                    clickedButton.setVisibility(View.INVISIBLE);
                    String numberStr = clickedButton.getText().toString();
                    clickedButton.setText(" ");

                    Button button = buttons[emptySpace.x][emptySpace.y];
                    button.setVisibility(View.VISIBLE);
                    button.setText(numberStr);

                    emptySpace.x = clickedPoint.x;
                    emptySpace.y = clickedPoint.y;
                }
                checkWin();
            }
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button button = buttons[i][j];
                button.setOnClickListener(listener);
            }
        }
        Button mixButton = (Button) findViewById(R.id.buttonMix);
        mixButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mix();
            }
        });
    }

    private void checkWin() {
        boolean isWin = true;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != emptySpace.x && j != emptySpace.y) {
                    if (Integer.parseInt(buttons[i][j].getText().toString()) != (i * 4 + j + 1)) {
                        isWin = false;
                    }
                }
            }
        }
        if (isWin && emptySpace.x == 3 && emptySpace.y == 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FifteenActivity.this);
            builder.setMessage("Вы выиграли!!!")
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    stepsQtyCounter = 0;
                                    stepsQty.setText("Steps Qty: " + stepsQtyCounter);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void mix() {
        int[] mixedArray = new int[15];
        boolean isNotNew;
        for (int i = 0; i < 15; i++) {
            do {
                mixedArray[i] = (new Random()).nextInt(15) + 1;
                isNotNew = false;
                for (int j = 0; j < i; j++) {
                    if (mixedArray[i] == mixedArray[j]) isNotNew = true;
                }
            } while (isNotNew);
        }
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == emptySpace.x && j == emptySpace.y) array[i][j] = -1;
                else {
                    array[i][j] = mixedArray[k];
                    k++;
                }
            }
        }
        stepsQtyCounter = 0;
        stepsQty.setText("Steps Qty: " + stepsQtyCounter);
        paintTable();
    }

    private Point getClickedPoint(Button clickedButton) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (clickedButton == buttons[i][j]) {
                    Point point = new Point();
                    point.x = i;
                    point.y = j;
                    return point;
                }
            }
        }
        return null;
    }

    private boolean canMove(Point clicked) {
        if (clicked.equals(emptySpace)) {
            return false;
        }

        if (clicked.x == emptySpace.x) {
            int diff = Math.abs(clicked.y - emptySpace.y);
            if (diff == 1) {
                return true;
            }
        } else if (clicked.y == emptySpace.y) {
            int diff = Math.abs(clicked.x - emptySpace.x);
            if (diff == 1) {
                return true;
            }
        }

        return false;
    }

    private void generateArray() {
        int k = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (k >= 16) {
                    emptySpace.x = i;
                    emptySpace.y = j;
                    array[i][j] = -1;
                } else {
                    array[i][j] = k;
//                    array[i][j] = i * 4 + j + 1;
                }
                k++;
            }
        }
    }

    private void paintTable() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button button = buttons[i][j];
                int number = array[i][j];
                if (number > -1) {
//					button.setText(R.string.app_name); // приложение будет падать
//					button.setText(String.valueOf(number));
                    button.setText("" + number);
                } else {
                    button.setText(" ");
                    button.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void initArray() {
        b11 = (Button) findViewById(R.id.button11);
        buttons[0][0] = b11;
        b12 = (Button) findViewById(R.id.button12);
        buttons[0][1] = b12;
        b13 = (Button) findViewById(R.id.button13);
        buttons[0][2] = b13;
        b14 = (Button) findViewById(R.id.button14);
        buttons[0][3] = b14;

        b21 = (Button) findViewById(R.id.button21);
        buttons[1][0] = b21;
        b22 = (Button) findViewById(R.id.button22);
        buttons[1][1] = b22;
        b23 = (Button) findViewById(R.id.button23);
        buttons[1][2] = b23;
        b24 = (Button) findViewById(R.id.button24);
        buttons[1][3] = b24;

        b31 = (Button) findViewById(R.id.button31);
        buttons[2][0] = b31;
        b32 = (Button) findViewById(R.id.button32);
        buttons[2][1] = b32;
        b33 = (Button) findViewById(R.id.button33);
        buttons[2][2] = b33;
        b34 = (Button) findViewById(R.id.button34);
        buttons[2][3] = b34;

        b41 = (Button) findViewById(R.id.button41);
        buttons[3][0] = b41;
        b42 = (Button) findViewById(R.id.button42);
        buttons[3][1] = b42;
        b43 = (Button) findViewById(R.id.button43);
        buttons[3][2] = b43;
        b44 = (Button) findViewById(R.id.button44);
        buttons[3][3] = b44;
    }
}