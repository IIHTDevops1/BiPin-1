package ivye.bipin.helper;

import ivye.bipin.util.LogUtil;

public class ActivityMonitorHelper {
    private static final ActivityMonitorHelper instance = new ActivityMonitorHelper();

    private ActivityMonitorHelper() {
    }

    public static synchronized ActivityMonitorHelper getInstance() {
        return instance;
    }

    // TODO
    private volatile boolean activityVisible;

    public boolean isActivityVisible() {
        return activityVisible;
    }

    public void activityResumed() {
        activityVisible = true;
        LogUtil.log("ActivityMonitor activityResumed " + activityVisible);
    }

    public void activityPaused() {
        activityVisible = false;
        LogUtil.log("ActivityMonitor activityPaused " + activityVisible);
    }
}
