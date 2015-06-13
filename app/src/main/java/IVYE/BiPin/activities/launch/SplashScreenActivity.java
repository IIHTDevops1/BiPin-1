package  IVYE.BiPin.activities.launch;

import android.os.Bundle;

import IVYE.BiPin.R;
import IVYE.BiPin.activities.BaseActivity;

/**
 * Created by IGA on 4/6/15.
 */
public class SplashScreenActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Workaround for the bug in Android OS, preventing go back to the previous activity by tapping the app icon in launchers
        //Detail: https://code.google.com/p/android/issues/detail?id=26658
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash_screen_page);
    }
}
