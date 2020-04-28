package sg.edu.np.WhackAMole;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Whack-A-Mole";

    private TextView resultTextView;
    private Button holeButton1;
    private Button holeButton2;
    private Button holeButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        holeButton1 = findViewById(R.id.holeButton1);
        holeButton2 = findViewById(R.id.holeButton2);
        holeButton3 = findViewById(R.id.holeButton3);
        Log.v(TAG, "Finished Pre-initialisation!\n");
    }

    @Override
    protected void onStart() {
        super.onStart();

        final List<Button> holeButtons = new ArrayList<>();
        holeButtons.add(holeButton1);
        holeButtons.add(holeButton2);
        holeButtons.add(holeButton3);

        final List<Integer> holeButtonIDs = new ArrayList<>();
        holeButtonIDs.add(R.id.holeButton1);
        holeButtonIDs.add(R.id.holeButton2);
        holeButtonIDs.add(R.id.holeButton3);

        final Model.Game currentGame = new Model.Game(holeButtons);
        currentGame.LinkTextViewAsResultView(resultTextView);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int holeClickedIndex = holeButtonIDs.indexOf(v.getId());
                currentGame.HandleHoleHit(holeClickedIndex);
            }
        };

        holeButton1.setOnClickListener(listener);
        holeButton2.setOnClickListener(listener);
        holeButton3.setOnClickListener(listener);

        Log.v(TAG, "Starting GUI!");
    }
}

class Model {

    private static final String TAG = "Whack-A-Mole";

    public static class Game {

        public class MoleHole {
            private Boolean hasMole;
            private Button linkedButton;

            public MoleHole(Button b) {
                this.hasMole = false;
                this.linkedButton = b;
            }

            public Boolean getHasMole() {
                return hasMole;
            }

            public void setHasMole(Boolean value) {
                this.hasMole = value;
                UpdateLinkedButtonToReflectModel();
            }

            public void SpawnMole() {
                setHasMole(true);
            }

            public void UpdateLinkedButtonToReflectModel() {
                if (hasMole) {
                    linkedButton.setText(R.string.notempty);
                } else {
                    linkedButton.setText(R.string.empty);
                }
            }
        }

        public List<MoleHole> moleHoleList = new ArrayList<>();
        private TextView resultView;
        private Integer numberOfHoles;
        private Integer score;

        public Game(List<Button> holeButtonList) {
            this.score = 0;
            this.numberOfHoles = holeButtonList.size();
            for(int i=0; i<numberOfHoles; i++) {
                this.moleHoleList.add(new MoleHole(holeButtonList.get(i)));
            }
            ResetAndRespawnMole();
        }

        private void setScore(int value) {
            this.score = value;
            UpdateResultViewToMatchModel();
        }

        public void ResetAndRespawnMole() {
            int randomHoleIndex = (int)(Math.random() * numberOfHoles);
            for (MoleHole hole: moleHoleList) {
                hole.setHasMole(false);
            }
            moleHoleList.get(randomHoleIndex).SpawnMole();
        }

        public void HandleHoleHit(int holeIndex) {
            boolean hitMole = moleHoleList.get(holeIndex).getHasMole();
            ResetAndRespawnMole();
            System.out.print("Whack-A-Mole: ");
            switch (holeIndex) {
                case 0:
                    Log.v(TAG, "Button Left Clicked!");
                    break;
                case 1:
                    Log.v(TAG, "Button Middle Clicked!");
                    break;
                case 2:
                    Log.v(TAG, "Button Right Clicked!");
                    break;
                default:
                    Log.v(TAG, "Unknown Button Clicked! Did you forget to add cases for new buttons?");
            }
            if (hitMole) {
                setScore(score+1);
                Log.v(TAG, "Hit, score added!\n");
            } else {
                setScore(score-1);
                Log.v(TAG, "Missed, score deducted!\n");
            }
        }

        public void LinkTextViewAsResultView(TextView t) {
            this.resultView = t;
        }

        public void UpdateResultViewToMatchModel() {
            resultView.setText(score.toString());
        }

    }
}
