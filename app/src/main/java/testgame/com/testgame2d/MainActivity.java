package testgame.com.testgame2d;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/*reference:
https://o7planning.org/vi/10521/huong-dan-lap-trinh-android-game-2d-cho-nguoi-moi-bat-dau#a1815703*/
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Loại bỏ tiêu đề.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(new GameSurface(this));
    }
}
