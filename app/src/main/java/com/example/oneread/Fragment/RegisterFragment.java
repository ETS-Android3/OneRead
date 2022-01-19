package com.example.oneread.Fragment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
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
    private static RegisterFragment instance;
    private Unbinder unbinder;
    private String avatarUrl;
    private Uri avatarUri;



    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.avatar)
    RoundedImageView avatar;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.email)
    EditText email;


    @OnClick(R.id.avatar)
    void onAvatarClick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // this line allow to pick multiple file
        intent.setType("image/*");
        startActivityForResult(intent, Common.PICK_FILES);
    }


    @OnClick(R.id.btn_register)
    void onButtonRegisterClick() {
        if (!Utils.checkEmptyComponents(new Object[]{username, password, email})
            || !Utils.isValidEmailAddress(email)
            || !Utils.isValidPasswordAddress(password)) {
            return;
        }
        try {
//            compositeDisposable.add(Common.iServiceAPI.getBooks()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(jsonObject -> {
//                        JsonArray jsonArray = jsonObject.get("books").getAsJsonArray();
//                        for (int i=0; i<jsonArray.size(); i++) {
//                            System.out.println(jsonArray.get(i).toString());
//                            String json = jsonArray.get(i).toString();
//                            System.out.println(json);
//                            Book book = new Gson().fromJson(json, Book.class);
//                            System.out.println(book.getTitle());
//                        }
//                    }));

            compositeDisposable.add(Common.iServiceAPI.registerAccount(new User(username.getText().toString(),
                            password.getText().toString(), email.getText().toString()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        Utils.showToast(getContext(), Message.registerSuccess, Toast.LENGTH_SHORT);
                        reset();
                        ((ViewPager2) getActivity().findViewById(R.id.viewpager)).setCurrentItem(0);
                    }, err -> {
                        HttpException response = (HttpException) err;
                        err.printStackTrace();
                        // FIXED: get response body onError
                        //  https://github.com/square/retrofit/issues/1218
                        String message = String.valueOf(JsonParser.parseString(response.response().errorBody().string()).getAsJsonObject().get("message"));
                        Utils.showToast(getContext(), Message.registerFail + "\n" + message, Toast.LENGTH_SHORT);
                    }));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        compositeDisposable.dispose();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        compositeDisposable.dispose();
        super.onDestroyView();
    }

    public RegisterFragment() {
    }

    public static RegisterFragment getInstance() {
        if (instance == null) {
            return instance = new RegisterFragment();
        } else return instance;
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_FILES
            && resultCode == RESULT_OK && data != null) {
            try {
                if (data.getData() != null) {
                    avatarUri = data.getData();
                    uploadImage(avatarUri, Common.avatarFirebasePath +
                            Utils.getFileName(getActivity().getContentResolver(), avatarUri) + System.currentTimeMillis());
                } else if (data.getClipData() != null) {
                    ClipData clipData = data.getClipData();
                    for (int i=0; i<clipData.getItemCount(); i++) {
                        System.out.println(clipData.getItemAt(i).getUri().getPath());
                    }
                } else avatarUri = null;
            } catch (Exception e) {
                e.printStackTrace();
                Utils.showToast(this.getContext(), "[ERR]: " + e.getMessage(), Toast.LENGTH_SHORT);
            }
        } else {
            Utils.showToast(this.getContext(), Message.somethingWrong, Toast.LENGTH_SHORT);
        }
    }

    private void uploadImage(Uri imageUri, String path) {
        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child(path);
        UploadTask uploadTask = storageReference.putFile(imageUri);
        uploadTask.continueWithTask(task -> {
            if (task.isSuccessful()) {
                Utils.showToast(this.getContext(), Message.uploadSuccess, Toast.LENGTH_SHORT);
                return storageReference.getDownloadUrl();

            } else {
                Utils.showToast(this.getContext(), Message.uploadFail, Toast.LENGTH_SHORT);
                return null;
            }
        }).addOnCompleteListener(task -> {
            avatarUrl = task.getResult().toString();
            Picasso.get().load(avatarUrl).into(avatar);
        }).addOnFailureListener(e -> {
            Utils.showToast(this.getContext(), "[ERR]: " + e.getMessage(), Toast.LENGTH_SHORT);
            return;
        });
    }

    private void deleteImage(String path) {
        StorageReference avatarRef = Common.storageReference.child(path);
        avatarRef.delete().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                Utils.showToast(this.getContext(), Message.deleteSuccess, Toast.LENGTH_SHORT);
            } else {
                Utils.showToast(this.getContext(), Message.deleteFail, Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(e -> {
            Utils.showToast(this.getContext(), "[ERR]: " + e.getMessage(), Toast.LENGTH_SHORT);
        });
    }

    private void reset() {
        Utils.resetObjects(new Object[]{username, password, email});
    }
}