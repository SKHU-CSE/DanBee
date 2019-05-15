package danbee.com.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import danbee.com.R;

public class FindPwFragment extends Fragment {

    EditText findpw_id;
    EditText findpw_name;
    EditText findpw_birth;
    EditText findpw_phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView2 = (ViewGroup) inflater.inflate(R.layout.fragment_findpw,container,false);
        findpw_id = rootView2.findViewById(R.id.findpw_user_et_id);
        findpw_name = rootView2.findViewById(R.id.findpw_user_et_name);
        findpw_birth = rootView2.findViewById(R.id.findpw_user_et_birth);
        findpw_phone = rootView2.findViewById(R.id.findpw_user_et_phone);
        return rootView2;
    }
}
