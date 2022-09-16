package com.example.thriftify.view.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.thriftify.R;
import com.example.thriftify.service.model.login_model.LoginResult;
import com.example.thriftify.utils.InternetUtils;
import com.example.thriftify.view.ui.user_authentication.LoginFormState;
import com.example.thriftify.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // tag
    private static final String TAG = "LoginActivity";

    // variables
    private boolean networkStatus;
    private LoginViewModel loginViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController host = NavHostFragment.findNavController(this);
        // get the viewModel class instance
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        // get views by using view ids
        final TextInputLayout usernameEditText = view.findViewById(R.id.email_textInputLayout);
        final TextInputLayout passwordEditText = view.findViewById(R.id.password_textInputLayout);
        final Button loginButton = view.findViewById(R.id.login);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);

        // get the status of Network connectivity
        InternetUtils.get(getActivity()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    networkStatus = aBoolean;
                } else {
                    networkStatus = aBoolean;
                    Toast.makeText(getActivity(), "No Internet Connection.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // get the states from login form
        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                } else {
                    usernameEditText.setError(null);
                }
                if (loginFormState.getPasswordError() != null) {
                    // passwordEditText.setError(getString(loginFormState.getPasswordError()));
                } else {
                    passwordEditText.setError(null);
                }
            }
        });

        // get the login result by setting up the observer
        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {

            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                Log.e(TAG, "onChanged: " + loginResult.getSuccess());
                if (loginResult == null) {
                    Log.e(TAG, "onChanged: null");
                    return;
                }
                if (loginResult.getLoginError() != null) {
                    Log.e(TAG, "onError: " + loginResult.getLoginError());
                    loadingProgressBar.setVisibility(View.GONE);
                    showLoginFailed(loginResult.getLoginError());
                }
                if (loginResult.getSuccess() != null) {
                    Log.e(TAG, "onChangedSuccess: " + loginResult.getSuccess());
                    loadingProgressBar.setVisibility(View.GONE);
                    updateUiWithUser(loginResult.getSuccess());
                    host.navigate(LoginFragmentDirections.actionLoginFragmentToDashboard().getActionId());
                }

            }
        });

        // set up the textWatcher listener to listen the change of into the Edit field
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getEditText().getText().toString(),
                        passwordEditText.getEditText().getText().toString());
            }
        };

        // add the listener with the Edit field
        usernameEditText.getEditText().addTextChangedListener(afterTextChangedListener);
        passwordEditText.getEditText().addTextChangedListener(afterTextChangedListener);

        // attach the onClick listener to login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkStatus) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loginViewModel.login(usernameEditText.getEditText().getText().toString(),
                            passwordEditText.getEditText().getText().toString());

                } else {
                    Toast.makeText(getActivity(), "Mobile data is disabled. Connect to Wi-Fi network instead, or enable mobile data and try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // call this when successful authentication by the server acceptance
    private void updateUiWithUser(String token) {
        String welcome = token;
        // TODO : initiate successful logged in experience
        Toast.makeText(getActivity(), welcome, Toast.LENGTH_LONG).show();
    }

    //call this when not remember by the server
    private void showLoginFailed(@Nullable String errorString) {
        // TODO : unsccessful logged in experience
        Toast.makeText(getActivity(), errorString, Toast.LENGTH_SHORT).show();

    }
}