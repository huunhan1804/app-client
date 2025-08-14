package com.example.dietarysupplementshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.requests.ChangePasswordRequest;
import com.example.dietarysupplementshop.requests.UpdateAccountRequest;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.services.OtpService;
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
    //private ImageButton editButton, editPhoneButton, editGenderButton, editBirthdateButton;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private AccountViewModel accountViewModel;
    private IProgressBarController progressBarController;
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
//    private String birthdate;
    private String currentBirthdate;
//    public ProfileFragment() {
//    }
//    public static ProfileFragment newInstance(String param1, String param2) {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
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
            showPhoneDialog();
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
                    UpdateAccountRequest request = new UpdateAccountRequest(fullnameEditText.getText().toString(), newGender, currentBirthdate, phoneEditText.getText().toString());
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

            UpdateAccountRequest request = new UpdateAccountRequest(fullnameEditText.getText().toString(), genderEditText.getText().toString(), newBirthdate, phoneEditText.getText().toString());
            accountViewModel.updateAccountProfile(request);

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
//    private void showBirthdateDialog() {
//        Calendar calendar = Calendar.getInstance();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
//            Calendar birthDate = Calendar.getInstance();
//            birthDate.set(year, month, dayOfMonth);
//            Calendar today = Calendar.getInstance();
//
//            int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
//
//            if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) || (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
//                age--;
//            }
//
//            if (age >= 18) {
//                birthdate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
//                birthdateEditText.setText(birthdate);
//                UpdateAccountRequest accountRequest = new UpdateAccountRequest(fullnameEditText.getText().toString().trim(), genderEditText.getText().toString().trim(), birthdate);
//                accountViewModel.updateAccountProfile(accountRequest).observe(getViewLifecycleOwner(), resource -> {
//                    if (resource != null) {
//                        switch (resource.getStatus()) {
//                            case SUCCESS:
//                                ((HomepageActivity) getActivity()).hideProgressBar();
//                                break;
//                            case ERROR:
//                                ((HomepageActivity) getActivity()).hideProgressBar();
//                                break;
//                            case LOADING:
//                                ((HomepageActivity) getActivity()).showProgressBar();
//                                break;
//                        }
//                    }
//                });
//
//
//            } else {
//                Toast.makeText(getContext(), "You must be at least 18 years old to use this app.", Toast.LENGTH_SHORT).show();
//            }
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.show();
//    }


//private void showPhoneDialog() {
//    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//    builder.setTitle("Edit Phone");
//
//    final EditText input = new EditText(getContext());
//    input.setInputType(InputType.TYPE_CLASS_PHONE);
//    input.setText(phoneEditText.getText());
//    builder.setView(input);
//
//    builder.setPositiveButton("OK", (dialog, which) -> {
//        String phoneNumber = input.getText().toString();
//        if (Validation.isValidPhoneNumber(phoneNumber)) {
//            accountViewModel.addLoginId(phoneNumber).observe(getViewLifecycleOwner(), resource -> {
//                if (resource != null) {
//                    switch (resource.getStatus()) {
//                        case SUCCESS:
//                        case ERROR:
//                            ((HomepageActivity) getActivity()).hideProgressBar();
//                            break;
//                        case LOADING:
//                            ((HomepageActivity) getActivity()).showProgressBar();
//                            break;
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(getContext(), "Invalid phone number!", Toast.LENGTH_SHORT).show();
//        }
//    });
//    builder.setNegativeButton("Cancel", null);
//    builder.show();
//}
    private void showPhoneDialog() {
        Context context = requireContext();
        final EditText input = new EditText(context);
        input.setHint("Nhập số điện thoại mới");
        input.setText(phoneEditText.getText().toString());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(context)
                .setTitle("Cập nhật số điện thoại")
                .setView(input)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String newPhone = input.getText().toString().trim();
                    if (!newPhone.isEmpty()) {
                        phoneEditText.setText(newPhone);

                        UpdateAccountRequest request = new UpdateAccountRequest(
                                fullnameEditText.getText().toString(),
                                genderEditText.getText().toString(),
                                currentBirthdate,
                                newPhone
                        );
                        accountViewModel.updateAccountProfile(request);
                    } else {
                        Toast.makeText(context, "Vui lòng nhập số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
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
//
//    private void showEmailDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Edit Email");
//
//        // Use custom layout
//        View customView = LayoutInflater.from(getContext()).inflate(R.layout.custom_email_dialog, null);
//        final EditText input = customView.findViewById(R.id.email_input);
//        input.setText(emailEditText.getText());
//        builder.setView(customView);
//        OtpService otpService = new OtpService();
//        String originalEmail = emailEditText.getText().toString();
//
//        builder.setPositiveButton("OK", (dialogInterface, i) -> {
//            if (!originalEmail.equals(input.getText().toString())) {
//                ((HomepageActivity) getActivity()).showProgressBar();
//                String email = input.getText().toString();
//                if (Validation.isValidEmail(email)) {
//                    otpService.sendOtpRegistration(email, new OtpService.OtpCallback() {
//                        @Override
//                        public void onSuccess(String successMessage) {
//                            ((HomepageActivity) getActivity()).hideProgressBar();
//                            showOtpDialogForEmail(otpService, email);
//                        }
//
//                        @Override
//                        public void onError(String errorMessage) {
//                            if (Integer.parseInt(errorMessage) == 409) {
//                                ((HomepageActivity) getActivity()).hideProgressBar();
//                                Toast.makeText(getContext(), "Email is already exist!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                } else {
//                    ((HomepageActivity) getActivity()).hideProgressBar();
//                    Toast.makeText(getContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        builder.setNegativeButton("Cancel", null);
//
//        final AlertDialog dialog = builder.create();
//        input.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
//                if (!originalEmail.equals(input.getText().toString())) {
//
//                    ((HomepageActivity) getActivity()).showProgressBar();
//                    String email = input.getText().toString();
//                    if (Validation.isValidEmail(email)) {
//                        otpService.sendOtpRegistration(email, new OtpService.OtpCallback() {
//                            @Override
//                            public void onSuccess(String successMessage) {
//                                // Đóng dialog khi API thành công
//                                dialog.dismiss();
//                                ((HomepageActivity) getActivity()).hideProgressBar();
//                                showOtpDialogForEmail(otpService, email);
//                            }
//
//                            @Override
//                            public void onError(String errorMessage) {
//                                if (Integer.parseInt(errorMessage) == 409) {
//                                    // Báo lỗi lên EditText của dialog
//                                    input.setError("Email is already exist!");
//                                    ((HomepageActivity) getActivity()).hideProgressBar();
//                                }
//                            }
//                        });
//                    } else {
//                        ((HomepageActivity) getActivity()).hideProgressBar();
//                        Toast.makeText(getContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
//                    }
//                    return true;
//                }
//
//            }
//            return false;
//        });
//
//        dialog.show();
//    }
//    private void showOtpDialogForEmail(OtpService otpService, String email) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Enter OTP sent to your email");
//
//        LinearLayout layout = new LinearLayout(getContext());
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setPadding(50, 10, 50, 10);
//
//        final EditText otpInput = new EditText(getContext());
//        otpInput.setInputType(InputType.TYPE_CLASS_NUMBER);
//        layout.addView(otpInput);
//
//        final TextView timerTextView = new TextView(getContext());
//        timerTextView.setText("Time remaining: 60s");
//        layout.addView(timerTextView);
//
//        builder.setView(layout);
//
//        final CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timerTextView.setText("Time remaining: " + millisUntilFinished / 1000 + "s");
//            }
//
//            @Override
//            public void onFinish() {
//                timerTextView.setText("Time's up!");
//            }
//        }.start();
//
//        builder.setPositiveButton("Verify", (dialog, which) -> {
//            countDownTimer.cancel();
//            accountViewModel.addLoginId(email).observe(getViewLifecycleOwner(), resource -> {
//                if (resource != null) {
//                    switch (resource.getStatus()) {
//                        case SUCCESS:
//                            ((HomepageActivity) getActivity()).hideProgressBar();
//                            break;
//                        case ERROR:
//                            ((HomepageActivity) getActivity()).hideProgressBar();
//                            break;
//                        case LOADING:
//                            ((HomepageActivity) getActivity()).showProgressBar();
//                            break;
//                    }
//                }
//            });
//        });
//
//        builder.setNegativeButton("Resend", (dialog, which) -> {
//            ((HomepageActivity) getActivity()).showProgressBar();
//            otpService.sendOtpRegistration(email, new OtpService.OtpCallback() {
//                @Override
//                public void onSuccess(String successMessage) {
//                    ((HomepageActivity) getActivity()).hideProgressBar();
//                    countDownTimer.cancel();
//                    showOtpDialogForEmail(otpService, email);
//                }
//
//                @Override
//                public void onError(String errorMessage) {
//                    ((HomepageActivity) getActivity()).hideProgressBar();
//                    if (Integer.parseInt(errorMessage) == 409) {
//                        Toast.makeText(getContext(), "Email is already exist!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        });
//
//        builder.setOnCancelListener(dialog -> countDownTimer.cancel());
//
//        builder.show();
//    }
//
//
//    private void showChangePasswordDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Change Password");
//
//        LinearLayout layout = new LinearLayout(getContext());
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setPadding(50, 10, 50, 10);
//
//        final EditText oldPasswordInput = new EditText(getContext());
//        oldPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        oldPasswordInput.setHint("Old Password");
//        layout.addView(oldPasswordInput);
//
//        final EditText newPasswordInput = new EditText(getContext());
//        newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        newPasswordInput.setHint("New Password");
//        layout.addView(newPasswordInput);
//
//        final EditText confirmPasswordInput = new EditText(getContext());
//        confirmPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        confirmPasswordInput.setHint("Confirm New Password");
//        layout.addView(confirmPasswordInput);
//
//        builder.setView(layout);
//
//        builder.setPositiveButton("Change", (dialog, which) -> {
//            String oldPassword = oldPasswordInput.getText().toString();
//            String newPassword = newPasswordInput.getText().toString();
//            String confirmPassword = confirmPasswordInput.getText().toString();
//
//            if (Validation.isValidPassword(newPassword) && Validation.isValidPassword(confirmPassword) && Validation.isValidPasswordMatch(newPassword, confirmPassword)) {
//                ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(oldPassword, confirmPassword);
//                accountViewModel.changePassword(changePasswordRequest).observe(getViewLifecycleOwner(), resource -> {
//                    if (resource != null) {
//                        switch (resource.getStatus()) {
//                            case SUCCESS:
//                                ((HomepageActivity) getActivity()).hideProgressBar();
//                                if (resource.getData() != null) {
//                                    Toast.makeText(getContext(), resource.getData(), Toast.LENGTH_SHORT).show();
//                                }
//                                break;
//                            case ERROR:
//                                ((HomepageActivity) getActivity()).hideProgressBar();
//                                Toast.makeText(getContext(), resource.getMessage(), Toast.LENGTH_LONG).show();
//                                break;
//                            case LOADING:
//                                ((HomepageActivity) getActivity()).showProgressBar();
//                                break;
//                        }
//                    }
//                });
//            } else {
//                Toast.makeText(getContext(), "Invalid password or passwords do not match!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", null);
//        builder.show();
//    }
}