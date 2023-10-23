package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.services.OtpService;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private EditText emailEditText, phoneEditText, genderEditText, birthdateEditText, fullnameEditText;
    private ImageButton editButton, editPhoneButton, editGenderButton, editBirthdateButton;

    private ImageView avatarImageView;
    private Button myAddressBtn, changePassBtn, signOutBtn;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private AccountViewModel accountViewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
    }

    private static final int REQUEST_IMAGE_PICK = 101;


    public void onAvatarClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        fullnameEditText = view.findViewById(R.id.fullnameText);
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        genderEditText = view.findViewById(R.id.genderEditText);
        birthdateEditText = view.findViewById(R.id.birthdateEditText);
        avatarImageView = view.findViewById(R.id.avatarImageView);

        editButton = view.findViewById(R.id.editButton);
        editPhoneButton = view.findViewById(R.id.editPhoneButton);
        editGenderButton = view.findViewById(R.id.editGenderButton);
        editBirthdateButton = view.findViewById(R.id.editBirthdateButton);
        myAddressBtn = view.findViewById(R.id.myAddressButton);
        changePassBtn = view.findViewById(R.id.changePasswordButton);
        signOutBtn = view.findViewById(R.id.signOutButton);

        accountViewModel.getAccountInfoResource().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        ((HomepageActivity) getActivity()).hideProgressBar();
                        if (resource.getData() != null) {
                            AccountInformation accountInformations = resource.getData();
                            emailEditText.setText(accountInformations.getAccountProfileDTO().getEmail());
                            phoneEditText.setText(accountInformations.getAccountProfileDTO().getPhone());
                            genderEditText.setText(accountInformations.getAccountProfileDTO().getGender());
                            Date date = accountInformations.getAccountProfileDTO().getBirthday();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String birthdateStr = dateFormat.format(date);
                            birthdateEditText.setText(birthdateStr);
                            fullnameEditText.setText(accountInformations.getAccountProfileDTO().getFullname());
                            Picasso.get().load(accountInformations.getAvatar_url()).transform(new CircleTransform()).into(avatarImageView);
                        }
                        break;
                    case ERROR:
                        ((HomepageActivity) getActivity()).hideProgressBar();
                        Toast.makeText(getContext(), resource.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                    case LOADING:
                        ((HomepageActivity) getActivity()).showProgressBar();
                        break;
                }
            }
        });

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri selectedImage = data.getData();
                    ((HomepageActivity) getActivity()).showProgressBar();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference oldImageRef = storage.getReferenceFromUrl(accountViewModel.getAvatar_url());

                    oldImageRef.delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            uploadNewImage(storage, selectedImage);
                        } else if (task.getException() instanceof StorageException) {
                            StorageException storageException = (StorageException) task.getException();
                            if (storageException.getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                                uploadNewImage(storage, selectedImage);
                            } else {
                                Toast.makeText(getContext(), "Failed to delete the previous image", Toast.LENGTH_SHORT).show();
                                ((HomepageActivity) getActivity()).hideProgressBar();
                            }
                        } else {
                            Toast.makeText(getContext(), "An unexpected error occurred", Toast.LENGTH_SHORT).show();
                            ((HomepageActivity) getActivity()).hideProgressBar();
                        }
                    });

                }
            }
        });

        ((HomepageActivity) getActivity()).hideProgressBar();

        FrameLayout frameAvatar = view.findViewById(R.id.frameAvatar);
        frameAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAvatarClick(v);
            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailDialog();
            }
        });

        editPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoneDialog();
            }
        });

        editGenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });

        editBirthdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBirthdateDialog();
            }
        });

        myAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyAddressActivity.class);
                startActivity(intent);
            }
        });

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog();
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getTokenManager().clearTokens();

                GoogleSignIn.getClient(
                        getActivity(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();

                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });


        return view;
    }

    private void uploadNewImage(FirebaseStorage storage, Uri selectedImage) {
        StorageReference storageRef = storage.getReference();
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("images/" + fileName);
        UploadTask uploadTask = imageRef.putFile(selectedImage);
        uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            accountViewModel.updateAvatar(imageUrl);
            Toast.makeText(getContext(), "Change avatar success!", Toast.LENGTH_SHORT).show();
            ((HomepageActivity) getActivity()).hideProgressBar();
        })).addOnFailureListener(exception -> {
            Toast.makeText(getContext(), "Failed to upload the image", Toast.LENGTH_SHORT).show();
            ((HomepageActivity) getActivity()).hideProgressBar();
        });
    }


    private void showEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Email");

        // Use custom layout
        View customView = LayoutInflater.from(getContext()).inflate(R.layout.custom_email_dialog, null);
        final EditText input = customView.findViewById(R.id.email_input);
        input.setText(emailEditText.getText());
        builder.setView(customView);
        OtpService otpService = new OtpService();

        // Thêm sự kiện cho nút "OK", để không cần nút "OK" mà sử dụng Enter
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            ((HomepageActivity) getActivity()).showProgressBar();
            String email = input.getText().toString();
            if (Validation.isValidEmail(email)) {
                otpService.sendOtpRegistration(email, new OtpService.OtpCallback() {
                    @Override
                    public void onSuccess(String successMessage) {
                        ((HomepageActivity) getActivity()).hideProgressBar();
                        showOtpDialogForEmail();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        if (Integer.parseInt(errorMessage) == 409) {
                            ((HomepageActivity) getActivity()).hideProgressBar();
                            Toast.makeText(getContext(), "Email is already exist!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ((HomepageActivity) getActivity()).hideProgressBar();
                Toast.makeText(getContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();

        // Lắng nghe sự kiện khi nhấn Enter trên bàn phím
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                ((HomepageActivity) getActivity()).showProgressBar();
                String email = input.getText().toString();
                if (Validation.isValidEmail(email)) {
                    otpService.sendOtpRegistration(email, new OtpService.OtpCallback() {
                        @Override
                        public void onSuccess(String successMessage) {
                            // Đóng dialog khi API thành công
                            dialog.dismiss();
                            ((HomepageActivity) getActivity()).hideProgressBar();
                            showOtpDialogForEmail();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            if (Integer.parseInt(errorMessage) == 409) {
                                // Báo lỗi lên EditText của dialog
                                input.setError("Email is already exist!");
                                ((HomepageActivity) getActivity()).hideProgressBar();
                            }
                        }
                    });
                } else {
                    ((HomepageActivity) getActivity()).hideProgressBar();
                    Toast.makeText(getContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        dialog.show();
    }


    private void showPhoneDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Phone");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setText(phoneEditText.getText());
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumber = input.getText().toString();
                if (Validation.isValidPhoneNumber(phoneNumber)) {
                    // TODO: Send OTP to the phone number
                    showOtpDialog();
                } else {
                    Toast.makeText(getContext(), "Invalid phone number!", Toast.LENGTH_SHORT).show();
                }
            }

        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showGenderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Gender");

        final String[] genders = {"Male", "Female"};
        int checkedItem = genderEditText.getText().toString().equals("Male") ? 0 : 1;

        builder.setSingleChoiceItems(genders, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                genderEditText.setText(genders[which]);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showBirthdateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar birthDate = Calendar.getInstance();
                birthDate.set(year, month, dayOfMonth);
                Calendar today = Calendar.getInstance();

                int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

                if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) || (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
                    age--;
                }

                if (age >= 18) {
                    String birthdate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    birthdateEditText.setText(birthdate);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date birthDateObject = sdf.parse(birthdate);
                        //TODO
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing date.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "You must be at least 18 years old to use this app.", Toast.LENGTH_SHORT).show();
                }
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showOtpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter OTP sent to your phone");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 10, 50, 10);

        final EditText otpInput = new EditText(getContext());
        otpInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(otpInput);

        final TextView timerTextView = new TextView(getContext());
        timerTextView.setText("Time remaining: 60s");
        layout.addView(timerTextView);

        builder.setView(layout);

        final CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time remaining: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Time's up!");
            }
        }.start();

        builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Verify the OTP
                countDownTimer.cancel();
            }
        });

        builder.setNegativeButton("Resend", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Resend the OTP
                countDownTimer.cancel();
                showOtpDialog();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countDownTimer.cancel();
            }
        });

        builder.show();
    }

    private void showOtpDialogForEmail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter OTP sent to your email");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 10, 50, 10);

        final EditText otpInput = new EditText(getContext());
        otpInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(otpInput);

        final TextView timerTextView = new TextView(getContext());
        timerTextView.setText("Time remaining: 60s");
        layout.addView(timerTextView);

        builder.setView(layout);

        final CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time remaining: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Time's up!");
            }
        }.start();

        builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Verify the OTP
                countDownTimer.cancel();
            }
        });

        builder.setNegativeButton("Resend", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Resend the OTP to email
                countDownTimer.cancel();
                showOtpDialogForEmail();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                countDownTimer.cancel();
            }
        });

        builder.show();
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Password");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 10, 50, 10);

        final EditText oldPasswordInput = new EditText(getContext());
        oldPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        oldPasswordInput.setHint("Old Password");
        layout.addView(oldPasswordInput);

        final EditText newPasswordInput = new EditText(getContext());
        newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPasswordInput.setHint("New Password");
        layout.addView(newPasswordInput);

        final EditText confirmPasswordInput = new EditText(getContext());
        confirmPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPasswordInput.setHint("Confirm New Password");
        layout.addView(confirmPasswordInput);

        builder.setView(layout);

        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldPassword = oldPasswordInput.getText().toString();
                String newPassword = newPasswordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                // TODO: Check if old password is correct
                if (Validation.isValidPassword(newPassword) && Validation.isValidPassword(confirmPassword) && Validation.isValidPasswordMatch(newPassword, confirmPassword)) {
                    // TODO: Change the password in the backend
                    Toast.makeText(getContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Invalid password or passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


}