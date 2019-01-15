package testgame.com.testgame2d.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import testgame.com.testgame2d.GameSurface;

public class ChibiCharacter extends GameObject {
    private static final int ROW_TOP_TO_BOTTOM = 0;
    private static final int ROW_RIGHT_TO_LEFT = 1;
    private static final int ROW_LEFT_TO_RIGHT = 2;
    private static final int ROW_BOTTOM_TO_TOP = 3;
    // Dòng ảnh đang được sử dụng.
    private int rowUsing = ROW_LEFT_TO_RIGHT;
    private int colUsing;
    private Bitmap[] leftToRights;
    private Bitmap[] rightToLefts;
    private Bitmap[] topToBottoms;
    private Bitmap[] bottomToTops;

    // van toc di chuyen cua nhan vat
    public static final float VELOCITY = 0.1f;
    private int movingVectorX = 10;
    private int movingVectorY = 5;

    private long lastDrawNanoTime = -1;
    private GameSurface gameSurface;

    public ChibiCharacter(GameSurface gameSurface, Bitmap image, int x, int y) {
        super(image, 4, 3, x, y);
        this.gameSurface = gameSurface;

        this.topToBottoms = new Bitmap[colCount];//3
        this.rightToLefts = new Bitmap[colCount]; // 3
        this.leftToRights = new Bitmap[colCount]; // 3
        this.bottomToTops = new Bitmap[colCount]; // 3

        for (int col = 0; col < this.colCount; col++) {
            this.topToBottoms[col] = this.createSubImageAt(ROW_TOP_TO_BOTTOM, col);
            this.rightToLefts[col] = this.createSubImageAt(ROW_RIGHT_TO_LEFT, col);
            this.leftToRights[col] = this.createSubImageAt(ROW_LEFT_TO_RIGHT, col);
            this.bottomToTops[col] = this.createSubImageAt(ROW_BOTTOM_TO_TOP, col);
        }
    }

    public Bitmap[] getMoveBitmaps() {
        switch (rowUsing) {
            case ROW_BOTTOM_TO_TOP:
                return this.bottomToTops;
            case ROW_LEFT_TO_RIGHT:
                return this.leftToRights;
            case ROW_TOP_TO_BOTTOM:
                return this.topToBottoms;
            case ROW_RIGHT_TO_LEFT:
                return this.rightToLefts;
            default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap() {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.colUsing];
    }

    public void update() {
//        Log.e("tusinh", "chibi update");
        this.colUsing++;
        if (colUsing >= this.colCount) {
            this.colUsing = 0;
        }
        // thoi diem hien tai theo nano giay
        long now = System.nanoTime();
        // Chưa vẽ lần nào.
        if (lastDrawNanoTime == -1) {
            lastDrawNanoTime = now;
        }
        // Đổi nano giây ra mili giây (1 nanosecond = 1000000 millisecond).
        int deltaTime = (int) ((now - lastDrawNanoTime) / 1000000);
        // quãng đường mà nhân vật đi được
        float distance = VELOCITY * deltaTime;
        double movingVectorLength = Math.sqrt(movingVectorX * movingVectorX + movingVectorY * movingVectorY);

        // Tính toán vị trí mới của nhân vật. ???
        this.x = x + (int) (distance * movingVectorX / movingVectorLength);
        this.y = y + (int) (distance * movingVectorY / movingVectorLength);

        // Khi nhân vật của game chạm vào cạnh của màn hình thì đổi hướng.
        if (this.x < 0) {
            this.x = 0;
            this.movingVectorX = -this.movingVectorX;
        } else if (this.x > this.gameSurface.getWidth() - width) {
            this.x = this.gameSurface.getWidth() - width;
            this.movingVectorX = -this.movingVectorX;
        }

        if (this.y < 0) {
            this.y = 0;
            this.movingVectorY = -this.movingVectorY;
        } else if (this.y > this.gameSurface.getHeight() - height) {
            this.y = this.gameSurface.getHeight() - height;
            this.movingVectorY = -this.movingVectorY;
        }

        // Tính toán rowUsing.
        if (movingVectorX > 0) {
            if (movingVectorY > 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_TOP_TO_BOTTOM;
            } else if (movingVectorY < 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_BOTTOM_TO_TOP;
            } else {
                this.rowUsing = ROW_LEFT_TO_RIGHT;
            }
        } else {
            if (movingVectorY > 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_TOP_TO_BOTTOM;
            } else if (movingVectorY < 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_BOTTOM_TO_TOP;
            } else {
                this.rowUsing = ROW_RIGHT_TO_LEFT;
            }
        }

    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap, x, y, null);

        // Thời điểm vẽ cuối cùng (Nano giây).
        this.lastDrawNanoTime = System.nanoTime();
    }

    public void setMovingVector(int movingVectorX, int movingVectorY) {
        this.movingVectorX = movingVectorX;
        this.movingVectorY = movingVectorY;
    }
}
