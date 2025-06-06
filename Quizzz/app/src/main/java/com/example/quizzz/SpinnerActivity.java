package com.example.quizzz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizzz.SpinWheel.LuckyWheelView;
import com.example.quizzz.SpinWheel.model.LuckyItem;
import com.example.quizzz.databinding.ActivitySpinnerBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpinnerActivity extends AppCompatActivity {

    ActivitySpinnerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinnerBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        List<LuckyItem> data = new ArrayList<>();

        LuckyItem item1 = new LuckyItem();
        item1.topText = "5";
        item1.secondaryText = "COINS";
        item1.color = Color.parseColor("#eceff1");
        item1.textColor = Color.parseColor("#212121");
        data.add(item1);

        LuckyItem item2 = new LuckyItem();
        item2.topText = "10";
        item2.secondaryText = "COINS";
        item2.color = Color.parseColor("#00cf00");
        item2.textColor = Color.parseColor("#ffffff");
        data.add(item2);

        LuckyItem item3 = new LuckyItem();
        item3.topText = "15";
        item3.secondaryText = "COINS";
        item3.color = Color.parseColor("#eceff1");
        item3.textColor = Color.parseColor("#212121");
        data.add(item3);

        LuckyItem item4 = new LuckyItem();
        item4.topText = "20";
        item4.secondaryText = "COINS";
        item4.color = Color.parseColor("#7f00d9");
        item4.textColor = Color.parseColor("#ffffff");
        data.add(item4);

        LuckyItem item5 = new LuckyItem();
        item5.topText = "25";
        item5.secondaryText = "COINS";
        item5.color = Color.parseColor("#eceff1");
        item5.textColor = Color.parseColor("#212121");
        data.add(item5);

        LuckyItem item6 = new LuckyItem();
        item6.topText = "30";
        item6.secondaryText = "COINS";
        item6.color = Color.parseColor("#dc0000");
        item6.textColor = Color.parseColor("#ffffff");
        data.add(item6);

        LuckyItem item7 = new LuckyItem();
        item7.topText = "35";
        item7.secondaryText = "COINS";
        item7.color = Color.parseColor("#eceff1");
        item7.textColor = Color.parseColor("#212121");
        data.add(item7);

        LuckyItem item8 = new LuckyItem();
        item8.topText = "0";
        item8.secondaryText = "COINS";
        item8.color = Color.parseColor("#008bff");
        item8.textColor = Color.parseColor("#ffffff");
        data.add(item8);


        binding.wheelview.setData(data);
        binding.wheelview.setRound(5);

        binding.spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                int randomNumber = r.nextInt(8);

                binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber);
            }
        });

        binding.wheelview.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                updateCash(index);
            }
        });
    }

    void updateCash(int index){
        long cash = 0;
        switch (index){

            case 0:
                cash = 5;
                break;

            case 1:
                cash = 10;
                break;

            case 2:
                cash = 15;
                break;

            case 3:
                cash = 20;
                break;

            case 4:
                cash = 25;
                break;

            case 5:
                cash = 30;
                break;

            case 6:
                cash = 35;
                break;

            case 7:
                cash = 0;
                break;
        }

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(cash)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SpinnerActivity.this, "Coins added in account.", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
    }

}