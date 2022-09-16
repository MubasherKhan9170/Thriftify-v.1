package com.example.thriftify.view.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.thriftify.R;
import com.example.thriftify.utils.SharedPreferencesUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LanuchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LanuchFragment extends Fragment {
    private static final String TAG = "LanuchFragment";

    Animation animation;
    //views
    TextView logo_text;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LanuchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LanuchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LanuchFragment newInstance(String param1, String param2) {
        LanuchFragment fragment = new LanuchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lanuch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get the animations
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.top_splash_animation);

        // get the view and hooks
        logo_text = view.findViewById(R.id.logo_textView);
        logo_text.setAnimation(animation);
        NavController host = NavHostFragment.findNavController(this);

        // adding the handler thread to set up the post delay for required time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (SharedPreferencesUtils.get(getActivity()).getTokenSharedPreferences() == null) {
                   // host.navigate(R.id.action_lanuchFragment_to_loginFragment);

                    host.navigate(LanuchFragmentDirections.actionLanuchFragmentToLoginFragment().getActionId());

                } else {
                    host.navigate(LanuchFragmentDirections.actionLanuchFragmentToDashboard().getActionId());
                    Log.e(TAG, "run: " + SharedPreferencesUtils.get(getActivity()).getTokenSharedPreferences());
                }
            }
        }, 1500);
    }
}