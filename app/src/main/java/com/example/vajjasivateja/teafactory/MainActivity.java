package com.example.vajjasivateja.teafactory;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vajjasivateja.teafactory.Model.CheckUserResponse;
import com.example.vajjasivateja.teafactory.Model.User;
import com.example.vajjasivateja.teafactory.Retrofit.TeaFactoryAPI;
import com.example.vajjasivateja.teafactory.Utils.Common;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1000;
    Button btn_continue;
    TeaFactoryAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = Common.getAPI();

            btn_continue=findViewById(R.id.btn_continue);
            btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
            }
        });
    }

    private void startLoginPage(LoginType loginType) {
        Intent loginIntent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder=
                                        new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,
                                                AccountKitActivity.ResponseType.TOKEN);
        loginIntent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,builder.build());
        startActivityForResult(loginIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE)
        {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null)
            {
                Toast.makeText(this, ""+result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            }
            else  if (result.wasCancelled())
            {
                Toast.makeText(this, "Login cancelled", Toast.LENGTH_SHORT).show();
            }
            else {
                if (result.getAccessToken() != null)
                {
                    final android.app.AlertDialog alertDialog = new SpotsDialog(MainActivity.this);
                    alertDialog.show();
                    alertDialog.setMessage("Please wait while loading");

                    //Get User phone and Check exists on server.
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            mService.checkUserExists(account.getPhoneNumber().toString())
                                    .enqueue(new Callback<CheckUserResponse>() {
                                        @Override
                                        public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                            CheckUserResponse checkUserResponse = response.body();
                                            if (checkUserResponse.isExists())
                                            {
                                                //if user already exists, just start new Activity
                                                alertDialog.dismiss();
                                            }
                                            else {
                                                alertDialog.dismiss();

                                                showRegisterDialog(account.getPhoneNumber().toString());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {

                                        }
                                    });
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("ERROR", accountKitError.getErrorType().getMessage());

                        }
                    });

                }
            }
        }
    }

    private void showRegisterDialog(final String Phone) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("REGISTER");
//        final AlertDialog dialog = null;
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.register_layout, null);


        final EditText etName = view.findViewById(R.id.etName);
        final EditText etBirthday = view.findViewById(R.id.etBirthday);
        final EditText etAddress = view.findViewById(R.id.etAddress);
        Button btnRegister = view.findViewById(R.id.btnRegister);

        etBirthday.addTextChangedListener(new PatternedTextWatcher("####-##-##"));
        builder.setView(view);
//        builder.show();
        final AlertDialog dialog = builder.create();

        final AlertDialog finalDialog = dialog;
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final android.app.AlertDialog waitDialog = new SpotsDialog(MainActivity.this);
                waitDialog.show();
                waitDialog.setMessage("Please wait while loading");

                finalDialog.dismiss();


                if (TextUtils.isEmpty(etName.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etBirthday.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Please enter your birthday.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etAddress.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Please enter your address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mService.registerNewUser(Phone,
                        etName.getText().toString(),
                        etBirthday.getText().toString(),
                        etAddress.getText().toString())
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                waitDialog.dismiss();
                                User user = response.body();
                                if (TextUtils.isEmpty(user.getError_msg()))
                                {
                                    Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                waitDialog.dismiss();
                            }
                        });
            }
        });
        dialog.show();


    }
}
