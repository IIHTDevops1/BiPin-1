package ivye.bipin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

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
        Bundle args = savedInstanceState;
        HashMap<String, String> target = (HashMap<String, String>) args.getSerializable("TargetName");
        HashMap<String, Integer> targetPrice = (HashMap<String, Integer>) args.getSerializable("TargetPrice");
        String[] target_key = (String[]) target.keySet().toArray();

        for(int i=0;i<target.size();i++){
            mStrings.add(target.get(target_key[i]) + " (NT$ " + targetPrice.get(target_key[i]).toString() + " )");
        }

        complete.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, mStrings));
        return view;
    }
}