package ivye.bipin.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android4devs.navigationdrawer.MyAdapter;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import ivye.bipin.MyConstant;
import ivye.bipin.R;
import ivye.bipin.database.UpdateHelper;
import ivye.bipin.fragments.MainFragment;
import ivye.bipin.listener.RecyclerItemClickListener;
import ivye.bipin.util.FragmentFlowUtil;

/**
 * Created by IGA on 4/6/15.
 */

public class MainActivity extends BaseActivity {
    private Handler mHandler = new Handler() {
        @Override
        public void close() {}

        @Override
        public void flush() {}

        @Override
        public void publish(LogRecord record) {}
    };
    String TITLES[] = {"新檢索","更新資料庫", "關於我們"};
    int ICONS[] = {R.drawable.ic_action_new, R.drawable.ic_action_refresh, R.drawable.ic_action_about};

    String NAME = "BiPin";
    String EMAIL = "BiPin@homework.tw";
    int PROFILE = R.drawable.ic_arrow_drop_down_white_24dp;

    private Toolbar toolbar;

    private RecyclerView mRecyclerView;                           // Declaring RecyclerView
    private RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    private RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    private DrawerLayout Drawer;                                  // Declaring DrawerLayout
    private ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(R.color.white);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mRecyclerView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws IllegalAccessException, InstantiationException {
                switch (position) {
                    case 1:
                        // 新檢索
                        FragmentFlowUtil.commitFragment(getSupportFragmentManager(), MainFragment.class, null,
                                MyConstant.MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID, false, null, 0);
                        break;
                    case 2:
                        // 更新資料庫
                        try {
                            UpdateHelper.checkUpdate();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        // 關於我們
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                        alertDialogBuilder.setTitle("關於我們");
                        // set dialog message
                        alertDialogBuilder.setMessage("比拼(BiPin)是由逢甲大學資訊工程學系四位同學製作：\n" +
                                "  - 前端：林柏丞\n" +
                                "  - 後端：侯均靜\n" +
                                "  - 偵錯：孫右錠\n" +
                                "  - 美工：林倚婕\n" +
                                "本APP目前仍在開發中，評估結果僅供參考。").setCancelable(true);

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        break;
                    default:
                        Toast.makeText(view.getContext(), "這不可能發生啊，你對這個APP做了什麼？！", Toast.LENGTH_LONG).show();
                }

            }
        }));

        try {
            UpdateHelper.checkUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        try {
            FragmentFlowUtil.commitFragment(getSupportFragmentManager(), MainFragment.class, null,
                    MyConstant.MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID, false, null, 0);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
