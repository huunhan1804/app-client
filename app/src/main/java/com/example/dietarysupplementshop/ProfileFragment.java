package com.example.dietarysupplementshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietarysupplementshop.requests.UpdateAccountRequest;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private EditText emailEditText, phoneEditText, genderEditText, birthdateEditText, fullnameEditText;
    private ImageView avatarImageView;
    private Button myAddressBtn, changePassBtn, signOutBtn;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private AccountViewModel accountViewModel;
    private IProgressBarController progressBarController;

    private String currentBirthdate;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IProgressBarController) {
            progressBarController = (IProgressBarController) context;
        } else {
            throw new RuntimeException(context + " must implement IProgressBarController");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        bindViews(view);
        setupClickListeners(view);
        setupImagePicker();
        observeViewModel();
        return view;
    }

    private void bindViews(View view) {
        fullnameEditText = view.findViewById(R.id.fullnameText);
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        genderEditText = view.findViewById(R.id.genderEditText);
        birthdateEditText = view.findViewById(R.id.birthdateEditText);
        avatarImageView = view.findViewById(R.id.avatarImageView);
        myAddressBtn = view.findViewById(R.id.myAddressButton);
        changePassBtn = view.findViewById(R.id.changePasswordButton);
        signOutBtn = view.findViewById(R.id.signOutButton);
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.frameAvatar).setOnClickListener(this);
        view.findViewById(R.id.editButton).setOnClickListener(this);
        view.findViewById(R.id.editPhoneButton).setOnClickListener(this);
        view.findViewById(R.id.editGenderButton).setOnClickListener(this);
        view.findViewById(R.id.editBirthdateButton).setOnClickListener(this);
        myAddressBtn.setOnClickListener(this);
        changePassBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.frameAvatar) {
            openImagePicker();
        } else if (id == R.id.editButton) {
            Toast.makeText(getContext(), "Email không thể thay đổi.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.editPhoneButton) {
        } else if (id == R.id.editGenderButton) {
            showGenderDialog();
        } else if (id == R.id.editBirthdateButton) {
            showBirthdateDialog();
        } else if (id == R.id.myAddressButton) {
            startActivity(new Intent(getContext(), MyAddressActivity.class));
        } else if (id == R.id.changePasswordButton) {
        } else if (id == R.id.signOutButton) {
            signOut();
        }
    }

    private void observeViewModel() {
        accountViewModel.getAccountInfo().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;
            switch (resource.getStatus()) {
                case SUCCESS:
                    progressBarController.hideProgressBar();
                    if (resource.getData() != null) {
                        updateUI(resource.getData());
                    }
                    break;
                case ERROR:
                    progressBarController.hideProgressBar();
                    Toast.makeText(getContext(), resource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                case LOADING:
                    progressBarController.showProgressBar();
                    break;
            }
        });
    }

    private void updateUI(AccountInformation accountInfo) {
        if (accountInfo.getAccountProfileDTO() != null) {
            fullnameEditText.setText(accountInfo.getAccountProfileDTO().getFullname());
            emailEditText.setText(accountInfo.getAccountProfileDTO().getEmail());
            phoneEditText.setText(accountInfo.getAccountProfileDTO().getPhone());
            genderEditText.setText(accountInfo.getAccountProfileDTO().getGender());

            Date date = accountInfo.getAccountProfileDTO().getBirthday();
            if (date != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                currentBirthdate = dateFormat.format(date);
                birthdateEditText.setText(currentBirthdate);
            }
        }
        if (getContext() != null) {
            Picasso.get().load(accountInfo.getAvatar_url()).transform(new CircleTransform()).into(avatarImageView);
        }
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri selectedImage = result.getData().getData();
                if (selectedImage != null) {
                    accountViewModel.updateAvatar(selectedImage);
                }
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void showGenderDialog() {
        final String[] genders = {"Male", "Female", "Other"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Select Gender")
                .setItems(genders, (dialog, which) -> {
                    String newGender = genders[which];
                    genderEditText.setText(newGender);

                    UpdateAccountRequest request = new UpdateAccountRequest(fullnameEditText.getText().toString(), newGender, currentBirthdate);
                    accountViewModel.updateAccountProfile(request);
                })
                .show();
    }

    private void showBirthdateDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String newBirthdate = dateFormat.format(calendar.getTime());

            birthdateEditText.setText(newBirthdate);
            currentBirthdate = newBirthdate;

            UpdateAccountRequest request = new UpdateAccountRequest(fullnameEditText.getText().toString(), genderEditText.getText().toString(), newBirthdate);
            accountViewModel.updateAccountProfile(request);

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void signOut() {
        if (getActivity() == null) return;
        MyApplication.getInstance().getTokenManager().clearTokens();
        GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut();

        Intent intent = new Intent(getActivity(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}