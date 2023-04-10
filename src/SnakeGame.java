import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame {
    private static final int BLOCK_SIZE = 50;
    private static final int BOARD_LENGTH = 20;
    private static final int DRAW_DELAY = 20;
    private static final int FRAME_SKIP = 8;
    private static final Random rand = new Random();

    private static int foodX;
    private static int foodY;

    private static int snakeHeadX;
    private static int snakeHeadY;
    private static int speedX;
    private static int speedY;
    
    private static final int[] snakeBodyX = new int[BOARD_LENGTH * BOARD_LENGTH];
    private static final int[] snakeBodyY = new int[BOARD_LENGTH * BOARD_LENGTH];
    private static int snakeBodySize = 0;

    public static void main(String[] args) {
        setUpCanvas();
        placeFood();
        placeSnake();
        if (snakeHeadX == foodX && snakeHeadY == foodY) {
            placeSnake();
        }

        // Game loop
        int frameCount = 0;
        while(!isGameOver()) {
            StdDraw.clear(StdDraw.BLACK);

            drawFood();
            changeDirection();
            if (frameCount < FRAME_SKIP) {
                frameCount++;
            } else {
                moveSnake();
                if (eatFood()) {
                    growSnake();
                    placeFood();
                }
                frameCount = 0;
            }

            drawSnake();

            StdDraw.show();
            StdDraw.pause(DRAW_DELAY);
        }

        displayGameOver();
    }

    private static void displayGameOver() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.RED);
        double mid = BOARD_LENGTH * BLOCK_SIZE / 2.0;
        StdDraw.text(mid, mid, "Game Over!");
        StdDraw.show();
    }

    private static boolean isGameOver() {
        if (snakeHeadX < 0
                || snakeHeadX > BOARD_LENGTH * BLOCK_SIZE
                || snakeHeadY < 0
                || snakeHeadY > BOARD_LENGTH * BLOCK_SIZE) {
            // ran to the edge of the board
            return true;
        }

        for (int i = 0; i < snakeBodySize; i++) {
            if (snakeHeadX == snakeBodyX[i]
                    && snakeHeadY == snakeBodyY[i]) {
                // Snake eats own body
                return true;
            }
        }

        return false;
    }

    private static void growSnake() {
        // if else statement to handle the wrong index issue
        // when you start, an index of -1 does not exist because snakeBodySize starts as 0
        if (snakeBodySize == 0) {
            snakeBodySize++;
            snakeBodyX[snakeBodySize] = snakeBodyX[snakeBodySize -1];
            snakeBodyY[snakeBodySize] = snakeBodyY[snakeBodySize -1];
        } else {
            snakeBodyX[snakeBodySize] = snakeBodyX[snakeBodySize -1];
            snakeBodyY[snakeBodySize] = snakeBodyY[snakeBodySize -1];
            snakeBodySize++;
        }
    }

    private static boolean eatFood() {
        // As long as less than Double.MIN_VALUE, it's considered overlap
        return Math.abs(snakeHeadX - foodX) < Double.MIN_VALUE
                && Math.abs(snakeHeadY - foodY) < Double.MIN_VALUE;
    }

    private static void changeDirection() {
        // Arrow keys change speedX, or speedY
        if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
            speedX = 0;
            speedY = 1;
        } else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
            speedX = 0;
            speedY = -1;
        } else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
            speedX = -1;
            speedY = 0;
        } else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
            speedX = 1;
            speedY = 0;
        }
    }

    private static void moveSnake() {
        if (speedX == 0 && speedY == 0) { // edge case
            return; // skip it
        }

        for (int i = snakeBodySize - 1; i > 0; i--) {
            snakeBodyX[i] = snakeBodyX[i - 1];
            snakeBodyY[i] = snakeBodyY[i - 1];
        }

        snakeBodyX[0] = snakeHeadX;
        snakeBodyY[0] = snakeHeadY;

        snakeHeadX += speedX * BLOCK_SIZE;
        snakeHeadY += speedY * BLOCK_SIZE;
    }

    private static void drawSnake() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledSquare(snakeHeadX, snakeHeadY, BLOCK_SIZE / 2.0);

        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        for (int i = 0; i < snakeBodySize; i++) {
            StdDraw.filledSquare(snakeBodyX[i], snakeBodyY[i], BLOCK_SIZE / 2.0);
        }
    }

    private static void placeSnake() {
        snakeHeadX = getRandomCell();
        snakeHeadY = getRandomCell();
    }

    private static void drawFood() {
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.filledSquare(foodX, foodY, BLOCK_SIZE / 2.0);
    }

    private static void placeFood() {
        foodX = getRandomCell();
        foodY = getRandomCell();
    }

    // return the center point of a random cell on vertical/horizontal axis.
    private static int getRandomCell() {
        return BLOCK_SIZE / 2 + rand.nextInt(BOARD_LENGTH) * BLOCK_SIZE;
    }

    private static void setUpCanvas() {
        int canvasLength = BLOCK_SIZE * BOARD_LENGTH;
        StdDraw.setCanvasSize(canvasLength, canvasLength);
        StdDraw.setXscale(0, canvasLength);
        StdDraw.setYscale(0, canvasLength);
        StdDraw.enableDoubleBuffering();
    }
}