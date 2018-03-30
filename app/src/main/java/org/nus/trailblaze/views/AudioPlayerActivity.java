package org.nus.trailblaze.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.nus.trailblaze.R;
import org.nus.trailblaze.models.ContributedItem;

import java.io.IOException;

public class AudioPlayerActivity extends AppCompatActivity
        implements MediaPlayer.OnCompletionListener {

    private static ProgressDialog progressDialog;
    private Button play;
    private Button pause;
    private Button stop;
    private MediaPlayer mp;
    private SeekBar seekBar;
    private Runnable mRunnable ;
    private Handler mSeekbarUpdateHandler;

    private TextView mPass;
    private TextView mDuration;
    private String audioUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        stop = (Button) findViewById(R.id.stop);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        mPass = findViewById(R.id.tv_pass);
        mDuration = findViewById(R.id.tv_duration);

        ContributedItem item = (ContributedItem) getIntent().getParcelableExtra("Item");

        audioUrl = item.getFile().getUrl();

        mSeekbarUpdateHandler = new Handler();

        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                play();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pause();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stop();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mp!=null && b){
                    mp.seekTo(i*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setup();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(AudioPlayerActivity.this, TrailBlazaFeedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (stop.isEnabled()) {
            stop();
        }
    }

    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    private void play() {
        Log.d("play", "reached");
        mp.start();

        getAudioStats();

        initializeSeekBar();

        play.setEnabled(false);
        pause.setEnabled(true);
        stop.setEnabled(true);
    }

    private void stop() {
        Log.d("stop", "reached");
        mp.stop();
        pause.setEnabled(false);
        stop.setEnabled(false);

        if(mSeekbarUpdateHandler!=null){
            mSeekbarUpdateHandler.removeCallbacks(mRunnable);
        }

        try {
            setup();
        } catch (Throwable t) {
            goBlooey(t);
        }
    }

    private void pause() {
        Log.d("pause", "reached");
        mp.pause();

        if(mSeekbarUpdateHandler!=null){
            mSeekbarUpdateHandler.removeCallbacks(mRunnable);
        }

        play.setEnabled(true);
        pause.setEnabled(false);
        stop.setEnabled(true);
    }

    private void setup() {
        try {
            progressDialog = ProgressDialog.show(this, "",
                    "Buffering audio...", true);
            progressDialog.setCancelable(true);
            mp = new MediaPlayer();
            mp.setDataSource(audioUrl);
            mp.prepareAsync();

            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d("first", "reached");
                    // mp.start();
                    progressDialog.dismiss();
                }
            });

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        play.setEnabled(true);
        pause.setEnabled(false);
        stop.setEnabled(false);
    }

    private void goBlooey(Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exception!").setMessage(t.toString())
                .setPositiveButton("OK", null).show();
    }

    private String prependZero(int time) {
        return (time < 10 ? "0" : "") + time;
    }

    protected void getAudioStats(){
        int duration  = (mp.getDuration()/1000);
        int durationSecs  = ((mp.getDuration()/1000)%60);
        int durationMin  = ((mp.getDuration()/1000)/60);
        int due = (mp.getDuration() - mp.getCurrentPosition())/1000;
        int passSecs = ((duration - due) % 60);
        int passMin = ((duration - due)/60);

        mPass.setText(prependZero(passMin) + ":" +  prependZero(passSecs));
        mDuration.setText(prependZero(durationMin) + ":" + prependZero(durationSecs));
    }

    protected void initializeSeekBar(){
        seekBar.setMax(mp.getDuration()/1000);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(mp!=null){
                    int mCurrentPosition = mp.getCurrentPosition()/1000; // In milliseconds
                    seekBar.setProgress(mCurrentPosition);
                    getAudioStats();
                }
                mSeekbarUpdateHandler.postDelayed(mRunnable,1000);
            }
        };
        mSeekbarUpdateHandler.postDelayed(mRunnable,1000);
    }

}
