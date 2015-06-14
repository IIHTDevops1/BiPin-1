package ivye.bipin.fragments;

import android.os.Bundle;
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
import ivye.bipin.util.FragmentFlowUtil;

/**
 * Created by IGA on 9/6/15.
 */
public class MainFragment extends BaseFragment {
    private DBHelper dbh;

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
        dbh = new DBHelper(view.getContext());

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
                if(str.charAt(0) == '0')
                    mEditText.setText(str.substring(1, str.length()));
                if (str.length() > 6) {
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

                if (str.length() < 1) {
                    mEditText.setText("0");
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

                if (mEditText.getText() == null) {
                    tmp = "0";
                } else {
                    tmp = mEditText.getText().toString();
                }
                int totalBudget = Integer.parseInt(tmp);
                if (totalBudget < 10000) {
                    Toast.makeText(view.getContext(), "請輸入大於10000元，10000元以下的電腦你敢用？", Toast.LENGTH_SHORT).show();
                    Log.d("BiPin",  "請輸入大於10000元，10000元以下的電腦你敢用？");
                } else {
                    String selection = ((Spinner)view.findViewById(R.id.fragment_main_spinner_requirement)).getSelectedItem().toString();
                    int cpuPrice, memPrice, mbPrice, gcPrice, powerPrice, hddPrice;
                    if (selection.equals("文書")){
                        memPrice = 1500;
                        cpuPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.25)));
                        mbPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.25)));
                        hddPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.25)));
                        powerPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.25)));
                        gcPrice = 0;
                    }
                    else if(selection.equals("遊戲")){
                        memPrice = 1500;
                        cpuPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.185)));
                        mbPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.106)));
                        hddPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.161)));
                        powerPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.127)));
                        gcPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.417)));
                    }
                    else{
                        memPrice = 1500;
                        cpuPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.3)));
                        mbPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.15)));
                        hddPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.25)));
                        powerPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.10)));
                        gcPrice = Integer.parseInt(String.valueOf(Math.floor((totalBudget - memPrice) * 0.2)));
                    }

                    HashMap<String, String> target = new HashMap<String, String>();
                    HashMap<String, Integer> targetPrice = new HashMap<String, Integer>();
                    HashMap<String, String> itemTemp = ItemCPU.findCPUByPrice(dbh, cpuPrice, 1000);
                    target.put("CPU", itemTemp.get("Company") + " " + itemTemp.get("Name"));
                    targetPrice.put("CPU", Integer.parseInt(itemTemp.get("Price")));

                    itemTemp = ItemMem.findMemByPrice(dbh, memPrice, 1000);
                    target.put("Mem", itemTemp.get("Company") + " " + itemTemp.get("Name"));
                    targetPrice.put("Mem", Integer.parseInt(itemTemp.get("Price")));

                    itemTemp = ItemMB.findMBByPrice(dbh, mbPrice, 1000);
                    target.put("MB", itemTemp.get("Company") + " " + itemTemp.get("Name"));
                    targetPrice.put("MB", Integer.parseInt(itemTemp.get("Price")));

                    itemTemp = ItemDisk.findDiskByPrice(dbh, hddPrice, 1000);
                    target.put("Disk", itemTemp.get("Company") + " " + itemTemp.get("Name"));
                    targetPrice.put("Disk", Integer.parseInt(itemTemp.get("Price")));

                    itemTemp = ItemGC.findGCByPrice(dbh, gcPrice, 1000);
                    target.put("GC", itemTemp.get("Company") + " " + itemTemp.get("Name"));
                    targetPrice.put("GC", Integer.parseInt(itemTemp.get("Price")));

                    itemTemp = ItemPower.findPowerByPrice(dbh, powerPrice, 1000);
                    target.put("Power", itemTemp.get("Company") + " " + itemTemp.get("Name"));
                    targetPrice.put("Power", Integer.parseInt(itemTemp.get("Price")));

                    Bundle args = new Bundle();
                    args.putSerializable("TargetName", target);
                    args.putSerializable("TargetPrice", targetPrice);


                    try {
                        FragmentFlowUtil.commitFragment(getActivity().getSupportFragmentManager(), CompleteFragment.class, args,
                                MyConstant.MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID, true, null, 0);
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return view;
    }
}
