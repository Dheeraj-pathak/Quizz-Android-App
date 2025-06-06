package com.example.quizzz;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizzz.databinding.ActivityQuizBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;

    ArrayList<Question> questions;
    int index = 0;
    Question question;

    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswers =0;
    private InterstitialAd mInterstitialAd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        //binding.adView3.loadAd(adRequest);


        questions = new ArrayList<>();

        database = FirebaseFirestore.getInstance();

       final String catId = getIntent().getStringExtra("catId");

        Random random = new Random();
        database.collection("categories")
                .document(catId)
                .collection("questions")
                .orderBy("index")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int totalQuestions = queryDocumentSnapshots.size();
                        if (totalQuestions <= 5) {
                            // If the total number of questions is less than or equal to 5, use all questions
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Question question = snapshot.toObject(Question.class);
                                questions.add(question);
                            }
                        } else {
                            // If there are more than 5 questions, generate a random number within the valid range
                            int[] randomIndices = new int[totalQuestions];
                            for (int i = 0; i < totalQuestions; i++) {
                                randomIndices[i] = i;
                            }
                            // Shuffle the indices array
                            for (int i = totalQuestions - 1; i > 0; i--) {
                                int j = random.nextInt(i + 1);
                                int temp = randomIndices[i];
                                randomIndices[i] = randomIndices[j];
                                randomIndices[j] = temp;
                            }
                            // Retrieve the first 5 questions based on the shuffled indices
                            for (int i = 0; i < 5; i++) {
                                int index = randomIndices[i];
                                DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(index);
                                Question question = snapshot.toObject(Question.class);
                                questions.add(question);
                            }
                        }
                        setNextQuestion();

                    }
                });
        resetTimer();
        Button quitButton = findViewById(R.id.quitbtn);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    void resetTimer(){
        timer = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));

            }

            @Override
            public void onFinish() {

                // You can perform any additional actions you want, such as updating the score
                // ...

                // Move to the next question or finish the activity if there are no more questions
                if (index < questions.size() - 1) {
                    index++;
                    setNextQuestion();
                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    finish();

            }}
        };
    }

    void showAnswer(){
        if (question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));


    }

    void setNextQuestion(){

        if(timer != null){
            timer.cancel();
        }

        timer.start();
        if(index < questions.size()){
            binding.questionCounter.setText(String.format("%d/%d",(index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());


        }
    }
    void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())){
            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }



    void reset() {
        binding.option1.setBackground(getResources().getDrawable(R.drawable.options_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.options_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.options_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.options_unselected));
    }
    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.option_1 || viewId == R.id.option_2 || viewId == R.id.option_3 || viewId == R.id.option_4) {
            if (timer != null) {
                timer.cancel();
            }
            TextView selected = (TextView) view;
            checkAnswer(selected);
        } else if (viewId == R.id.nextbtn) {
            reset();
            if (index < questions.size()-1) {
                index++;
                setNextQuestion();
            } else {
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("correct", correctAnswers);
                intent.putExtra("total", questions.size());
                startActivity(intent);
               // Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
            }
        }
    }}