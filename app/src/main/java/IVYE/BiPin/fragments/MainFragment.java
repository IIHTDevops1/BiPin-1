package IVYE.BiPin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gc.materialdesign.views.ButtonRectangle;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import IVYE.BiPin.MyConstant;
import IVYE.BiPin.R;
import IVYE.BiPin.util.FragmentFlowUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by IGA on 9/6/15.
 */
public class MainFragment extends BaseFragment {
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

        MaterialSpinner spinner_requirement = (MaterialSpinner) view.findViewById(R.id.fragment_main_spinner_requirement);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_spinner_item,getActivity().getResources()
                .getStringArray(R.array.fragment_main_spinner_name));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_requirement.setAdapter(adapter);

        final MaterialEditText mEditText = (MaterialEditText) view.findViewById(R.id.fragment_main_editText_price);
        mEditText.setCursorVisible(false);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                final String str = mEditText.getText().toString();
                if(str.length() > 5) {
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
            }
        });

        ButtonRectangle startButton = (ButtonRectangle) view.findViewById(R.id.fragment_main_button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentFlowUtil.commitFragment(getActivity().getSupportFragmentManager(), CompleteFragment.class, null,
                            MyConstant.MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID, true, null, 0);
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
            }
        });
        return view;
    }
}
