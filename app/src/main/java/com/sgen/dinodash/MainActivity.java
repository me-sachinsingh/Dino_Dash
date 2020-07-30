package com.sgen.dinodash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class MainActivity extends AppCompatActivity {

    Button store, leaderboard, sound, share;
    Button new_game, setting, high_score, quit;

    SharedPreferences sharedPreferences;

    Boolean sound_on;

    PopupWindow popupWindow;

    GoogleSignInAccount signInAccount;

    final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        store = findViewById(R.id.button_store);
        leaderboard = findViewById(R.id.button_leaderboard);
        sound = findViewById(R.id.button_sound);
        share = findViewById(R.id.button_share);

        new_game = findViewById(R.id.button_new_game);
        setting = findViewById(R.id.button_setting);
        high_score = findViewById(R.id.button_high_score);
        quit = findViewById(R.id.button_quit);

        String prefName = "Setting";
        sharedPreferences = getSharedPreferences(prefName, Context.MODE_PRIVATE);

        sound_on = sharedPreferences.getBoolean("sound_state",true);

        if(sound_on) {
            sound.setBackground(getDrawable(R.drawable.sound_on));
        }
        else {
            sound.setBackground(getDrawable(R.drawable.sound_off));
        }

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rating = new Intent(Intent.ACTION_VIEW);
                rating.setData(Uri.parse("market://details?id=com.sgen.flasher"));
                startActivity(rating);
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInSilently();
            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(sound_on) {
                    editor.putBoolean("sound_state",false);
                    sound.setBackground(getDrawable(R.drawable.sound_off));
                    sound_on = false;
                }
                else {
                    editor.putBoolean("sound_state",true);
                    sound.setBackground(getDrawable(R.drawable.sound_on));
                    sound_on = true;
                }
                editor.apply();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT,getString(R.string.share_text));
                startActivity(share);
            }
        });

        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") final View view1 = inflater.inflate(R.layout.settings, null);

                CheckBox controls;
                final TextView screen_left, screen_right;

                popupWindow = new PopupWindow(view1, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, false);

                popupWindow.showAtLocation(view, Gravity.CENTER,0,0);


                controls = view1.findViewById(R.id.invert_controls);
                screen_left = view1.findViewById(R.id.screen_left);
                screen_right = view1.findViewById(R.id.screen_right);

                if(sharedPreferences.getBoolean("invert_controls",false)) {
                    screen_left.setText("Jump");
                    screen_right.setText("Crouch");
                    controls.setChecked(true);
                }
                else {
                    screen_left.setText("Crouch");
                    screen_right.setText("Jump");
                    controls.setChecked(false);
                }

                view1.findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                controls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        if(b) {
                            editor.putBoolean("invert_controls",true);
                            screen_left.setText("Jump");
                            screen_right.setText("Crouch");
                        }
                        else {
                            editor.putBoolean("invert_controls",false);
                            screen_left.setText("Crouch");
                            screen_right.setText("Jump");
                        }
                        editor.apply();
                    }
                });
            }
        });

        high_score.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") final View view2 = inflater.inflate(R.layout.high_score, null);

                TextView user_score;

                popupWindow = new PopupWindow(view2, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

                popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

                user_score = view2.findViewById(R.id.user_high_score);
                user_score.setText(Integer.toString(sharedPreferences.getInt("Score",0)));
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void signInSilently() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build();
        GoogleSignIn.getClient(MainActivity.this, signInOptions);

        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            signInAccount = account;
        }
        else {
            GoogleSignInClient signInClient = GoogleSignIn.getClient(this, signInOptions);
            signInClient
                .silentSignIn()
                .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if(task.isSuccessful()){
                            signInAccount = task.getResult();
                        }
                        else {
                            //Toast.makeText(MainActivity.this,"Unable to signIn", Toast.LENGTH_SHORT).show();
                            Log.d("MAIN","Exception : "+task.getException());
                            startSignInIntent();
                        }
                    }
                });
        }
    }

    public void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                signInAccount = result.getSignInAccount();
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error);
                }
                new AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        }
    }
}