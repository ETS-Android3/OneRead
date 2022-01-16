package com.example.oneread.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.example.oneread.Common.Common;
import com.example.oneread.Common.Message;
import com.example.oneread.Common.Utils;
import com.example.oneread.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

@SuppressLint("NonConstantResourceId")
public class RegisterFragment extends Fragment {

    private static RegisterFragment instance;
    private Unbinder unbinder;


    @BindView(R.id.btn_delete)
    Button btn_delete;
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.avatar)
    RoundedImageView avatar;

    @OnClick(R.id.avatar)
    void onAvatarClick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // this line allow to pick multiple file
        intent.setType("image/*");
        startActivityForResult(intent, Common.PICK_FILES);
    }

    @OnClick(R.id.btn_delete)
    void onButtonDeleteClick() {
        StorageReference avatarRef = Common.storageReference.child(Common.avatarFirebasePath + "a.jpg");
        avatarRef.delete().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                Utils.showToast(this.getContext(), Message.delete_success, Toast.LENGTH_SHORT);
            } else {
                Utils.showToast(this.getContext(), Message.delete_fail, Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(e -> {
            Utils.showToast(this.getContext(), "[ERR]: " + e.getMessage(), Toast.LENGTH_SHORT);
        });
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_FILES
            && resultCode == RESULT_OK && data != null) {
            try {
                if (data.getData() != null) {
                    final Uri imageUri = data.getData();
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference().child(Common.avatarFirebasePath + "a.jpg");
                    UploadTask uploadTask = storageReference.putFile(imageUri);
                    uploadTask.continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            Utils.showToast(this.getContext(), Message.upload_fail, Toast.LENGTH_SHORT);
                            return null;
                        } else {
                            Utils.showToast(this.getContext(), Message.upload_success, Toast.LENGTH_SHORT);
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(task -> {
                        String url = task.getResult().toString();
                        Picasso.get().load(url).into(avatar);
                    }).addOnFailureListener(e -> {
                        Utils.showToast(this.getContext(), "[ERR]: " + e.getMessage(), Toast.LENGTH_SHORT);
                        return;
                    });
                } else if (data.getClipData() != null) {
                    ClipData clipData = data.getClipData();
                    for (int i=0; i<clipData.getItemCount(); i++) {
                        System.out.println(clipData.getItemAt(i).getUri().getPath());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utils.showToast(this.getContext(), "[ERR]: " + e.getMessage(), Toast.LENGTH_SHORT);
            }
        } else {
            Utils.showToast(this.getContext(), Message.something_wrong, Toast.LENGTH_SHORT);
        }
    }
}