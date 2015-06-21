package  ivye.bipin.activities.launch;

import android.os.Bundle;

import java.io.IOException;

import ivye.bipin.R;
import ivye.bipin.activities.BaseActivity;
import ivye.bipin.database.UpdateHelper;

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
