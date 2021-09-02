package lab.mobileprogramming.cookaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lab.mobileprogramming.cookaid.model.Recipe;

public class CookingProcedureActivity extends AppCompatActivity {

    Recipe recipe;
    private RecyclerView rvInstructionList;
    private RecyclerView.Adapter adapter;

    private List<String> instructions;

    TextView preText;
    TextView nameText;
    TextView postText;
    Button startBtn;

    public SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_procedure);

        preText = (TextView) findViewById(R.id.cookingPreText);
        nameText = (TextView) findViewById(R.id.cookingNameText);
        postText = (TextView) findViewById(R.id.cookingPostText);
        startBtn = (Button) findViewById(R.id.startCookingBtn);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        TextView nameText = findViewById(R.id.cookingNameText);

        nameText.setText(recipe.getName());

        class SnapHelperOneByOne extends LinearSnapHelper{

            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY){

                if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
                    return RecyclerView.NO_POSITION;
                }

                final View currentView = findSnapView(layoutManager);

                if( currentView == null ){
                    return RecyclerView.NO_POSITION;
                }

                final int currentPosition = layoutManager.getPosition(currentView);

                if (currentPosition == RecyclerView.NO_POSITION) {
                    return RecyclerView.NO_POSITION;
                }

                return currentPosition;
            }
        }

        rvInstructionList = (RecyclerView) findViewById(R.id.instructionList);
        SnapHelperOneByOne instructionListHelper = new SnapHelperOneByOne();
        instructionListHelper.attachToRecyclerView(rvInstructionList);
        rvInstructionList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        instructions = new ArrayList<>(Arrays.asList(recipe.getInstructions().split("\n")));
        instructions.removeIf(instr -> instr.length() <= 10);
        Log.d("INSTRUCTIONS", instructions.toString());

        adapter = new InstructionAdapter(getApplicationContext(), instructions);
        rvInstructionList.setAdapter(adapter);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());

    }

    public void startCooking(View view) {
        preText.setVisibility(View.GONE);
        nameText.setVisibility(View.GONE);
        postText.setVisibility(View.GONE);
        startBtn.setVisibility(View.GONE);
        rvInstructionList.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), "NOT Ready for speech recognition", Toast.LENGTH_LONG).show();

        if(SpeechRecognizer.isRecognitionAvailable(getApplicationContext())) {
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Toast.makeText(getApplicationContext(), "Ready for speech recognition", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onBeginningOfSpeech() {
                    Toast.makeText(getApplicationContext(), "Beginning of recognition", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    Toast.makeText(getApplicationContext(), results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    Toast.makeText(getApplicationContext(), partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });

            Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_VOICE_SEARCH_HANDS_FREE);
            speechRecognizer.startListening(recognizerIntent);
        }




    }

    public void finishCooking(View view) {
        finish();
    }
}