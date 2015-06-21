package ivye.bipin.fragments;

import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;
import ivye.bipin.MyConstant;
import ivye.bipin.R;
import ivye.bipin.database.DBHelper;
import ivye.bipin.database.ItemCPU;
import ivye.bipin.database.ItemDisk;
import ivye.bipin.database.ItemGC;
import ivye.bipin.database.ItemMB;
import ivye.bipin.database.ItemMem;
import ivye.bipin.database.ItemPower;
import ivye.bipin.database.UpdateHelper;
import ivye.bipin.util.FragmentFlowUtil;

/**
 * Created by IGA on 9/6/15.
 */
public class MainFragment extends BaseFragment {
    private DBHelper dbh = null;

    private Handler mHandler = new Handler() {
        @Override
        public void close() {}

        @Override
        public void flush() {}

        @Override
        public void publish(LogRecord record) {}
    };
    private SweetAlertDialog dialogPriceError;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (dbh == null && ((new File(Environment.getExternalStorageDirectory() + "/BiPin/BiPin.db")).exists())) {
            dbh = new DBHelper(view.getContext());
        }

        MaterialSpinner spinner_requirement = (MaterialSpinner) view.findViewById(R.id.fragment_main_spinner_requirement);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_spinner_item,getActivity().getResources()
                .getStringArray(R.array.fragment_main_spinner_name));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_requirement.setAdapter(adapter);

        final MaterialEditText mEditText = (MaterialEditText) view.findViewById(R.id.fragment_main_editText_price);
        mEditText.setCursorVisible(false);
        mEditText.setText("0");

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String str = mEditText.getText().toString();

                try {
                    if (str.charAt(0) == '0') {
                        mEditText.setText(str.substring(1, str.length()));
                        mEditText.setSelection(mEditText.getText().length());
                    } else if (str.length() > 5) {
                        mEditText.post(new Runnable() {
                            @Override
                            public void run() {
                                mEditText.setText(str.substring(0, str.length() - 1));
                                onDestroy();
                            }
                        });
                        dialogPriceError = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                        dialogPriceError.setTitleText(getString(R.string.fragment_main_editText_overNumberCount))
                                .setConfirmText(getString(R.string.fragment_main_editText_confirm))
                                .show();
                    }
                } catch (Exception e) {

                }
            }
        });

        ButtonRectangle startButton = (ButtonRectangle) view.findViewById(R.id.fragment_main_button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                                        檢查預算
                                 */
                String tmp = "";

                if ((new File(Environment.getExternalStorageDirectory() + "/BiPin/BiPin.db")).exists()) {
                    if (mEditText.getText() == null) {
                        tmp = "0";
                    } else {
                        tmp = mEditText.getText().toString();
                    }
                    int totalBudget = Integer.parseInt(tmp);
                    String selection = ((Spinner) view.findViewById(R.id.fragment_main_spinner_requirement)).getSelectedItem().toString();
                    int cpuPrice, memPrice, mbPrice, gcPrice, powerPrice, hddPrice;
                    if (selection.equals("文書") || selection.equals("寫程式")) {
                        memPrice = 1500;
                        cpuPrice = (int) Math.round(((totalBudget - memPrice) * 0.25));
                        mbPrice = (int) Math.round(((totalBudget - memPrice) * 0.25));
                        hddPrice = (int) Math.round(((totalBudget - memPrice) * 0.25));
                        powerPrice = (int) Math.round(((totalBudget - memPrice) * 0.25));
                        gcPrice = 0;
                    } else if (selection.equals("遊戲")) {
                        memPrice = 2000;
                        cpuPrice = (int) Math.round(((totalBudget - memPrice) * 0.185));
                        mbPrice = (int) Math.round(((totalBudget - memPrice) * 0.106));
                        hddPrice = (int) Math.round(((totalBudget - memPrice) * 0.161));
                        powerPrice = (int) Math.round(((totalBudget - memPrice) * 0.127));
                        gcPrice = (int) Math.round(((totalBudget - memPrice) * 0.417));
                    } else {
                        memPrice = 1500;
                        cpuPrice = (int) Math.round(((totalBudget - memPrice) * 0.3));
                        mbPrice = (int) Math.round(((totalBudget - memPrice) * 0.13));
                        hddPrice = (int) Math.round(((totalBudget - memPrice) * 0.22));
                        powerPrice = (int) Math.round(((totalBudget - memPrice) * 0.13));
                        gcPrice = (int) Math.round(((totalBudget - memPrice) * 0.23));
                        Log.d("BiPinPrice", String.valueOf(cpuPrice) + "/" + String.valueOf(mbPrice) + "/" + String.valueOf(hddPrice) + "/" + String.valueOf(powerPrice) + "/" + String.valueOf(gcPrice));
                    }


                    try {
                        HashMap<String, String> target = new HashMap<String, String>();
                        HashMap<String, Integer> targetPrice = new HashMap<String, Integer>();

                        HashMap<String, String> itemTemp = ItemCPU.findCPUByPrice(dbh, cpuPrice);
                        if (itemTemp == null)
                            throw new NullPointerException();
                        target.put("CPU", itemTemp.get("Company") + " " + itemTemp.get("Name"));
                        targetPrice.put("CPU", Integer.parseInt(itemTemp.get("Price")));

                        itemTemp = ItemMem.findMemByPrice(dbh, memPrice);
                        if (itemTemp == null)
                            throw new NullPointerException();
                        target.put("Mem", itemTemp.get("Company") + " " + itemTemp.get("Name") + "-" + itemTemp.get("Clock"));
                        targetPrice.put("Mem", Integer.parseInt(itemTemp.get("Price")));

                        itemTemp = ItemMB.findMBByPrice(dbh, mbPrice);
                        if (itemTemp == null)
                            throw new NullPointerException();
                        target.put("MB", itemTemp.get("Company") + " " + itemTemp.get("Name"));
                        targetPrice.put("MB", Integer.parseInt(itemTemp.get("Price")));

                        itemTemp = ItemDisk.findDiskByPrice(dbh, hddPrice);
                        if (itemTemp == null)
                            throw new NullPointerException();
                        target.put("Disk", itemTemp.get("Company") + " " + itemTemp.get("Name") + " " + itemTemp.get("Size") + "GB");
                        targetPrice.put("Disk", Integer.parseInt(itemTemp.get("Price")));

                        if (gcPrice > 0) {
                            itemTemp = ItemGC.findGCByPrice(dbh, gcPrice);
                            if (itemTemp == null)
                                throw new NullPointerException();
                            target.put("GC", itemTemp.get("Company") + " " + itemTemp.get("Name"));
                            targetPrice.put("GC", Integer.parseInt(itemTemp.get("Price")));
                        }

                        itemTemp = ItemPower.findPowerByPrice(dbh, powerPrice);
                        if (itemTemp == null)
                            throw new NullPointerException();
                        target.put("Power", itemTemp.get("Company") + " " + itemTemp.get("Name"));
                        targetPrice.put("Power", Integer.parseInt(itemTemp.get("Price")));

                        // 把所有東西塞進bundle
                        Bundle args = new Bundle();
                        args.putSerializable("TargetName", target);
                        args.putSerializable("TargetPrice", targetPrice);

                        // 切換Fragment
                        FragmentFlowUtil.commitFragment(getActivity().getSupportFragmentManager(), CompleteFragment.class, args,
                                MyConstant.MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID, true, null, 0);
                    } catch (NullPointerException e) {
                        Toast.makeText(view.getContext(), "配對失敗，可能是預算過低，請調整預算後重新開始估價！", Toast.LENGTH_LONG).show();
                    } catch (CursorIndexOutOfBoundsException e) {
                        Toast.makeText(view.getContext(), "配對失敗，可能是預算過低，請調整預算後重新開始估價！", Toast.LENGTH_LONG).show();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(view.getContext(), "資料庫不存在，請開啟網路並更新資料褲！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
