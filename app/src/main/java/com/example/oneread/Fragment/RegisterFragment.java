package com.example.oneread.Fragment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.example.oneread.Common.Common;
import com.example.oneread.Common.Message;
import com.example.oneread.Common.Utils;
import com.example.oneread.Listener.ILoginListener;
import com.example.oneread.Model.User;
import com.example.oneread.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.*;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static android.app.Activity.RESULT_OK;


@SuppressLint("NonConstantResourceId")
public class RegisterFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private Unbinder unbinder;
    private ILoginListener listener;

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.cardview)
    CardView cardRegister;

    @OnClick(R.id.btn_login)
    void openLoginPage() {
        listener.login();
    }


    @OnClick(R.id.btn_register)
    void onButtonRegisterClick() {
        if (!Utils.checkEmptyComponents(new Object[]{username, password, email})
            || !Utils.isValidEmailAddress(email)
            || !Utils.isValidPasswordAddress(password)) {
            return;
        }
        try {
            compositeDisposable.add(Common.iServiceAPI.registerAccount(new User(username.getText().toString(),
                            password.getText().toString(), email.getText().toString()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        Utils.showToast(getContext(), Message.registerSuccess, Toast.LENGTH_SHORT);
                        Utils.resetObjects(new Object[]{username, password, email});
                        listener.onRegisterSuccess();
                    }, err -> {
                        // FIXED: get response body onError
                        //  https://github.com/square/retrofit/issues/1218
                        if (err instanceof HttpException) {
                            HttpException response = (HttpException) err;
                            String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                            Utils.showToast(getContext(), Message.registerFail + "\n" + message, Toast.LENGTH_SHORT);
                        } else {
                            err.printStackTrace();
                            Utils.showToast(getContext(), Message.registerFail + "\n" + err.getMessage(), Toast.LENGTH_SHORT);
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

    public RegisterFragment(ILoginListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        compositeDisposable = new CompositeDisposable();

        cardRegister.getBackground().setAlpha(200);
    }

}