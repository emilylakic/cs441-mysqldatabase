package com.example.myapplication.newgame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button mainButton;
    private Button other;
    public static String firstName, hobby;
    private TextView scoreView;
    private TextView timeView;
    private TextView gameOver;
    private int score = 0;
    private boolean playing = false;
    EditText editName, editGame, scoreField;
    String nameHolder, gameHolder, scoreHolder;
    private static final String URLline = "http://cs.binghamton.edu/~pmadden/courses/441score/postscore.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainButton = (Button) findViewById(R.id.main_button);
        scoreField = (EditText) findViewById(R.id.scoreField);
        scoreView = (TextView) findViewById(R.id.score_view);
        timeView = (TextView) findViewById(R.id.time_view);
        editName = (EditText) findViewById(R.id.editName);
        editGame = (EditText) findViewById(R.id.editGame);
        other = (Button) findViewById(R.id.other);
        gameOver = (TextView) findViewById(R.id.gameOver);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playing) {
                    // The first click
                    playing = true;
                    mainButton.setText("Keep Clicking");

                    // Initialize CountDownTimer to 60 seconds
                    new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timeView.setText("Time remaining: " + millisUntilFinished / 1000);
                        }

                        @Override
                        public void onFinish() {
                            playing = false;
                            gameOver.setText("Game Over");
                            mainButton.setVisibility(View.GONE);
                        }
                    }.start();  // Start the timer
                } else {
                    // Subsequent clicks
                    score++;
                    scoreView.setText("Score: " + score + " points");
                }
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }
    private void loginUser(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URLline = "http://cs.binghamton.edu/~pmadden/courses/441score/postscore.php?player="+editName.getText()+"&game="+editGame.getText()+"&score="+scoreField.getText();

        //final String player = name.getText().toString().trim();
        // final String game = enterGame.getText().toString().trim();
        // final String newScore = score.getText().toString().trim();

        nameHolder = editName.getText().toString().trim();
        gameHolder = editGame.getText().toString().trim();
        scoreHolder = scoreField.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("player",nameHolder);
                params.put("game",gameHolder);
                params.put("score",scoreHolder);
                return params;
            }

        };
        requestQueue.add(stringRequest);
        //requestQueue = Volley.newRequestQueue(this);
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
    }


    public void parseData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);
                    firstName = dataobj.getString("name");
                    hobby = dataobj.getString("hobby");
                }

                Intent intent = new Intent(MainActivity.this,HobbyActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}