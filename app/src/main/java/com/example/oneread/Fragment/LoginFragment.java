package com.example.oneread.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.example.oneread.Common.Common;
import com.example.oneread.Common.Message;
import com.example.oneread.Common.SharedPrefs;
import com.example.oneread.Common.Utils;
import com.example.oneread.Listener.ILoginListener;
import com.example.oneread.Model.User;
import com.example.oneread.R;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


public class LoginFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private Unbinder unbinder;
    private ILoginListener listener;

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.cardview)
    CardView cardLogin;

    public LoginFragment(ILoginListener listener) {
        this.listener = listener;
    }

    @OnClick({R.id.btn_register})
    void openRegisterPage() {
        listener.register();
    }

    @OnClick(R.id.btn_login)
    void onButtonLoginClick() {
        if (!Utils.checkEmptyComponents(new Object[]{username, password})) {
            return;
        }
        try {
            compositeDisposable.add(Common.iServiceAPI.login(new User(username.getText().toString(),
                            password.getText().toString()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        String accessToken = jsonObject.get("accessToken").getAsString();
                        User user = new Gson().fromJson(jsonObject.get("user").toString(), User.class);
                        SharedPrefs.getInstance(getContext()).put(Common.shareRefKeyUser, user);
                        SharedPrefs.getInstance(getContext()).put(Common.shareRefKeyAccessToken, accessToken);
                        Utils.showToast(getContext(), Message.loginSuccess, Toast.LENGTH_SHORT);
                        listener.onLoginSuccess();
                    }, err -> {
                        if (err instanceof HttpException) {
                            HttpException response = (HttpException) err;
                            String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                            Utils.showToast(getContext(), Message.loginFail + "\n" + message, Toast.LENGTH_SHORT);
                        } else {
                            err.printStackTrace();
                            Utils.showToast(getContext(), Message.loginFail + "\n" + err.getMessage(), Toast.LENGTH_SHORT);
                        }
                    }));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        compositeDisposable.dispose();
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        compositeDisposable = new CompositeDisposable();

        cardLogin.getBackground().setAlpha(200);
    }
}