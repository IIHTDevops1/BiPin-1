package ivye.bipin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import ivye.bipin.R;
/**
 * Created by IGA on 10/6/15.
 */
public class CompleteFragment extends BaseFragment {
    ArrayList<String> mStrings = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_complete, container, false);

        ListView complete = (ListView) view.findViewById(R.id.fragemnt_complete_listView);
        /*
            抓 Bundle
        */
        Bundle args = getArguments();
        HashMap<String, String> target = (HashMap<String, String>) args.getSerializable("TargetName");
        HashMap<String, Integer> targetPrice = (HashMap<String, Integer>) args.getSerializable("TargetPrice");
        Object[] target_key_obj = target.keySet().toArray();
        String[] target_key = Arrays.copyOf(target_key_obj, target_key_obj.length, String[].class);
        int totalPrice = 0;
        // 清除mStrings
        mStrings.clear();

        mStrings.add("中央處理器：\n    " + target.get("CPU") +  " (NT$ " + targetPrice.get("CPU").toString() + " )");
        totalPrice += targetPrice.get("CPU");
        mStrings.add("主機板：\n    " + target.get("MB") +  " (NT$ " + targetPrice.get("MB").toString() + " )");
        totalPrice += targetPrice.get("MB");

        mStrings.add("記憶體：\n    " + target.get("Mem") +  " (NT$ " + targetPrice.get("Mem").toString() + " )");
        totalPrice += targetPrice.get("Mem");

        mStrings.add("硬碟：\n    " + target.get("Disk") +  " (NT$ " + targetPrice.get("Disk").toString() + " )");
        totalPrice += targetPrice.get("Disk");

        if (target.get("GC")!=null) {
            mStrings.add("顯示卡：\n    " + target.get("GC") + " (NT$ " + targetPrice.get("GC").toString() + " )");
            totalPrice += targetPrice.get("GC");
        }
        mStrings.add("電源供應器：\n    " + target.get("Power") +  " (NT$ " + targetPrice.get("Power").toString() + " )");
        totalPrice += targetPrice.get("Power");

//
//
//        for(int i=0;i<target.size();i++){
//            mStrings.add(target.get(target_key[i]) + " (NT$ " + targetPrice.get(target_key[i]).toString() + " )");
//            totalPrice += targetPrice.get(target_key[i]);
//        }
        mStrings.add("總額： NT$" + String.valueOf(totalPrice));

        complete.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, mStrings));
        return view;
    }
}