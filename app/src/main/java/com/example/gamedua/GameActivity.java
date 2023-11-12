package com.example.gamedua;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamedua.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private MediaPlayer loginMusicPlayer;
    private MediaPlayer raceMusicPlayer ;
    private SeekBar seekBarPet1, seekBarPet2, seekBarPet3;
    private RadioButton radioButtonPet1, radioButtonPet2, radioButtonPet3;

    private ImageView vit1, vit2, vit3;
    private EditText editText;
    private Button startButton;
    private TextView moneyTextView;
    private TextView tienVit1;
    private TextView tienVit2;
    private TextView tienVit3;
    private TextView resultTextView;
    private int money = 100000;
    private Handler handler;
    private int initialBetAmountPet1 = 0;
    private int initialBetAmountPet2 = 0;
    private int initialBetAmountPet3 = 0;

    private boolean raceStarted = false;
    private boolean win = false;
    private List<Bet> bets;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        loginMusicPlayer = MediaPlayer.create(this, R.raw.a1);
        raceMusicPlayer = MediaPlayer.create(this,R.raw.a2);
        loginMusicPlayer.setLooping(true); // Lặp lại nhạc khi kết thúc
        raceMusicPlayer.setLooping(true);
        startLoginMusic();
        raceMusicPlayer.start();
        raceMusicPlayer.setVolume(0f, 0f);
        // Khởi tạo các view
        seekBarPet1 = findViewById(R.id.seekBarPet1);
        seekBarPet2 = findViewById(R.id.seekBarPet2);
        seekBarPet3 = findViewById(R.id.seekBarPet3);

        SeekBar seekBarPet1 = findViewById(R.id.seekBarPet1);
        SeekBar seekBarPet2 = findViewById(R.id.seekBarPet2);
        SeekBar seekBarPet3 = findViewById(R.id.seekBarPet3);
        radioButtonPet1 = findViewById(R.id.radioButtonPet1);
        radioButtonPet2 = findViewById(R.id.radioButtonPet2);
        radioButtonPet3 = findViewById(R.id.radioButtonPet3);
        radioButtonPet1.setChecked(true);
        tienVit1 = findViewById(R.id.tienVit1);
        tienVit2 = findViewById(R.id.tienVit2);
        tienVit3 = findViewById(R.id.tienVit3);

        vit1 = findViewById(R.id.imageView8);
        vit2 = findViewById(R.id.imageView9);
        vit3 = findViewById(R.id.imageView10);
        editText = findViewById(R.id.editTextText2);
        startButton = findViewById(R.id.startButton);
        moneyTextView = findViewById(R.id.moneyTextView);
        resultTextView = findViewById(R.id.resultTextView);

        handler = new Handler();
//        khoong cho di chuyen vit cua toan ddau ca
        seekBarPet1.setEnabled(false);
        seekBarPet2.setEnabled(false);
        seekBarPet3.setEnabled(false);

        bets = new ArrayList<>();

        Button datcuocButton = findViewById(R.id.datcuoc);
        datcuocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDatCuoc();
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRace();
//                if (!raceStarted) {
//                    // Lấy ID của RadioButton được chọn
//                    RadioGroup radioGroup = findViewById(R.id.radioGroup);
//                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
//
//                    // Kiểm tra xem RadioButton ẩn đã được chọn chưa
//                    RadioButton radioButtonHidden = findViewById(R.id.radioButtonHidden);
//                    if (selectedRadioButtonId == -1 || selectedRadioButtonId == radioButtonHidden.getId()) {
//                        // Hiển thị thông báo lỗi nếu không có RadioButton nào được chọn
//                        resultTextView.setText("Vui lòng chọn một con vịt để bắt đầu đua");
//                        return;
//                    }
//                }
//
//                if (!raceStarted) {
//                    String betAmountStr = editText.getText().toString();
//                    if (betAmountStr.isEmpty()) {
//                        // Hiển thị thông báo lỗi nếu người dùng không nhập giá tiền cược
//                        resultTextView.setText("Vui lòng nhập số tiền cược");
//                        return;
//                    }
//
//                    long  betAmount = Long.parseLong(betAmountStr);
//                    if (betAmount > money) {
//                        resultTextView.setText("Số tiền không đủ");
//
//                        return;
//                    }
//                    if (betAmount <= money && betAmount > 0) {
//                        raceStarted = true;
//
//                        startRace();
//                    } else {
//                        // Hiển thị thông báo nếu số tiền cược không hợp lệ
//                        resultTextView.setText("Số tiền không hợp lệ");
//                    }
//                }
            }
        });

    }
    private void resetBetAmountsToZero() {
        initialBetAmountPet1 = 0;
        initialBetAmountPet2 = 0;
        initialBetAmountPet3 = 0;
    }


    class Bet {
        private int radioButtonId;
        private int betAmount;

        public Bet(int radioButtonId, int betAmount) {
            this.radioButtonId = radioButtonId;
            this.betAmount = betAmount;
        }

        public int getRadioButtonId() {
            return radioButtonId;
        }

        public int getBetAmount() {
            return betAmount;
        }
    }
    private void handleDatCuoc() {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        EditText editText = findViewById(R.id.editTextText2);

        // Kiểm tra xem có RadioButton nào được chọn không
        if (selectedRadioButtonId == -1) {
            // Hiển thị thông báo lỗi nếu không có RadioButton nào được chọn
            resultTextView.setText("Vui lòng chọn một con vịt để đặt cược");
            return;
        }

        // Lấy số tiền đã đặt cược từ EditText
        String betAmountStr = editText.getText().toString();

        if (betAmountStr.isEmpty()) {
            // Hiển thị thông báo lỗi nếu người chơi không nhập số tiền cược
            resultTextView.setText("Vui lòng nhập số tiền cược");
            return;
        }

        int betAmount = Integer.parseInt(betAmountStr);
        if (betAmount <1){
            resultTextView.setText("Số tiền không hợp lệ");
            return;
        }
        if (betAmount > money) {
            resultTextView.setText("Số tiền không đủ");
            return;
        }
        if (selectedRadioButtonId == R.id.radioButtonPet1) {

            money += initialBetAmountPet1;
            initialBetAmountPet1 = betAmount;
        } else if (selectedRadioButtonId == R.id.radioButtonPet2) {
            money += initialBetAmountPet2;
            initialBetAmountPet2 = betAmount;
        } else if (selectedRadioButtonId == R.id.radioButtonPet3) {
            money += initialBetAmountPet3;
            initialBetAmountPet3 = betAmount;
        }

//        if (betAmount <= money && betAmount > 1) {
//
//            initialBetAmountPet1 = (selectedRadioButtonId == R.id.radioButtonPet1) ? betAmount : 0;
//            initialBetAmountPet2 = (selectedRadioButtonId == R.id.radioButtonPet2) ? betAmount : 0;
//            initialBetAmountPet3 = (selectedRadioButtonId == R.id.radioButtonPet3) ? betAmount : 0;
//
//        } else {
//            // Hiển thị thông báo nếu số tiền cược không hợp lệ
//            resultTextView.setText("Số tiền không hợp lệ");
//        }
        // Kiểm tra xem người chơi có đủ tiền để đặt cược không



        // Thêm cược vào danh sách cược
        Bet bet = new Bet(selectedRadioButtonId, betAmount);
        bets.add(bet);

        // Cập nhật số tiền đã đặt cược cho từng con vịt trên giao diện
        updateBetAmountTextView(selectedRadioButtonId, betAmount);

        // Trừ số tiền đã đặt cược từ số tiền của người chơi
        money -= betAmount;
        moneyTextView.setText("Số Tiền: $" + money);
        resultTextView.setText(""); // Xóa thông báo kết quả trước đó
    }

    private void updateBetAmountTextView(int radioButtonId, int betAmount) {
        TextView textView;
        if (radioButtonId == R.id.radioButtonPet1) {
            textView = findViewById(R.id.tienVit1);
        } else if (radioButtonId == R.id.radioButtonPet2) {
            textView = findViewById(R.id.tienVit2);
        } else {
            textView = findViewById(R.id.tienVit3);
        }
        textView.setText(String.valueOf(betAmount));
    }

    private void startLoginMusic() {
            loginMusicPlayer.start();

    }

    private int getWinnerPetId(int progress1, int progress2, int progress3) {
        if (progress1 >= 100) {
            return R.id.radioButtonPet1;
        } else if (progress2 >= 100) {
            return R.id.radioButtonPet2;
        } else if (progress3 >= 100) {
            return R.id.radioButtonPet3;
        } else {
            // Nếu không có vịt nào về đích, trả về -1 hoặc giá trị không hợp lý khác
            return -1;
        }
    }
    private void startRace() {
        // Đặt vịt về vị trí ban đầu
        seekBarPet1.setProgress(0);
        seekBarPet2.setProgress(0);
        seekBarPet3.setProgress(0); // Lặp lại nhạc khi kết thúc
        radioButtonPet1.setEnabled(false);
        radioButtonPet2.setEnabled(false);
        radioButtonPet3.setEnabled(false);
        startButton.setEnabled(false);
        editText.setEnabled(false);
        Button datcuocButton = findViewById(R.id.datcuoc);
        datcuocButton.setEnabled(false);
        loginMusicPlayer.setVolume(0f, 0f);
        raceMusicPlayer.setVolume(1f, 1f);
        // Tạo và chạy luồng để di chuyển vịt
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                win=false;
                resultTextView.setText("");
                Random random = new Random();
                int progress1 = seekBarPet1.getProgress();
                int progress2 = seekBarPet2.getProgress();
                int progress3 = seekBarPet3.getProgress();
                int sum=0;
                // Di chuyển vịt 1, 2, 3 một cách ngẫu nhiên
                progress1 += random.nextInt(10);
                progress2 += random.nextInt(10);
                progress3 += random.nextInt(10);

                seekBarPet1.setProgress(progress1);
                seekBarPet2.setProgress(progress2);
                seekBarPet3.setProgress(progress3);

                // Kiểm tra xem có vịt nào về đích chưa
                if (progress1 >= 100 || progress2 >= 100 || progress3 >= 100) {
                    raceStarted = false;
                    int winnerPetId = getWinnerPetId(progress1, progress2, progress3);
                    for (Bet bet : bets) {
                        if (bet.getRadioButtonId() == winnerPetId) {
                            // Người chơi đã đặt cược trên con vịt đúng và vịt đó về đích

                            if (bet.getBetAmount()>0) {
                                sum = 3 * bet.getBetAmount();
                                resultTextView.setText("Chúc mừng! Bạn đã thắng!: $"+(3 * bet.getBetAmount()));

                            }
                            win=true;
                        }
                        radioButtonPet1.setEnabled(true);
                        radioButtonPet2.setEnabled(true);
                        radioButtonPet3.setEnabled(true);
                        startButton.setEnabled(true);
                        editText.setEnabled(true);
                        datcuocButton.setEnabled(true);
                    }
                    money+=sum;
                    // Lấy ID của RadioButton được chọn
                    RadioGroup radioGroup = findViewById(R.id.radioGroup);
                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                    // Kiểm tra xem RadioButton ẩn đã được chọn chưa
                    RadioButton radioButtonHidden = findViewById(R.id.radioButtonHidden);
                    if (selectedRadioButtonId == -1 || selectedRadioButtonId == radioButtonHidden.getId()) {
                        // Hiển thị thông báo lỗi nếu không có RadioButton nào được chọn
                        resultTextView.setText("Vui lòng chọn một con vịt để bắt đầu đua");
                    } else {
                        // Kiểm tra xem con vịt nào về đích
                        if ((progress1 >= 100 && selectedRadioButtonId == R.id.radioButtonPet1) ||
                                (progress2 >= 100 && selectedRadioButtonId == R.id.radioButtonPet2) ||
                                (progress3 >= 100 && selectedRadioButtonId == R.id.radioButtonPet3)) {
                            // Người dùng đã chọn con đúng và vịt đó về đích
//                            money += 3 * Integer.parseInt(editText.getText().toString());

                            if (!win){
                                resultTextView.setText("Chúc bạn may mắn lần sau");

                            }


                            loginMusicPlayer.setVolume(1f, 1f);
                            raceMusicPlayer.setVolume(0f, 0f);
                            radioButtonPet1.setEnabled(true);
                            radioButtonPet2.setEnabled(true);
                            radioButtonPet3.setEnabled(true);
                            startButton.setEnabled(true);
                            datcuocButton.setEnabled(true);
                            editText.setEnabled(true);

                        } else {
                            // Người dùng đã chọn con sai hoặc vịt không về đích
                            loginMusicPlayer.setVolume(1f, 1f);
                            raceMusicPlayer.setVolume(0f, 0f);
                            radioButtonPet1.setEnabled(true);
                            radioButtonPet2.setEnabled(true);
                            radioButtonPet3.setEnabled(true);
                            datcuocButton.setEnabled(true);
                            startButton.setEnabled(true);

                            if (!win){
                                resultTextView.setText("Chúc bạn may mắn lần sau");

                            }
                            editText.setEnabled(true);
                        }
                    }
                    // Hiển thị số tiền còn lại
                    moneyTextView.setText("Số Tiền: $" + money);
                    resetBetAmountsToZero();
                    tienVit1.setText("0");
                    tienVit2.setText("0");
                    tienVit3.setText("0");
                    bets = new ArrayList<>();

                    if (!win){
                        resultTextView.setText("Chúc bạn may mắn lần sau");

                    }

                } else {
                    // Chạy lại luồng sau một khoảng thời gian nhất định
                    handler.postDelayed(this, 500);
                }

            }
        };

        // Chạy luồng
        handler.postDelayed(runnable, 100);
    }
}