package lab.mobileprogramming.cookaid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lab.mobileprogramming.cookaid.model.Recipe;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {

    private final List<String> instructions;

    public Context context;
    public InstructionAdapter(Context context, List<String> instructions) {
        this.context = context;
        this.instructions = instructions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView instructionView;
        public ProgressBar progressBarView;
        public TextView stepCounterView;
        public Button finishCookingBtnView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            instructionView = itemView.findViewById(R.id.instructionText);
            progressBarView = itemView.findViewById(R.id.progressBar);
            stepCounterView = itemView.findViewById(R.id.stepCounter);
            finishCookingBtnView = itemView.findViewById(R.id.finishCookingBtn);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recipeItemView = inflater.inflate(R.layout.instructions_item, parent, false);

        return new InstructionAdapter.ViewHolder(recipeItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionAdapter.ViewHolder holder, int position) {
        String instruction = instructions.get(position);

        TextView instructionText = holder.instructionView;
        ProgressBar progressBar = holder.progressBarView;
        TextView stepCounter = holder.stepCounterView;
        Button finishBtn = holder.finishCookingBtnView;

        instructionText.setText(instruction);
        progressBar.setMax(this.getItemCount());
        progressBar.setProgress(position + 1);
        stepCounter.setText("Step " + String.valueOf(position + 1) + "/" + String.valueOf(this.getItemCount()));
        if(position == (this.getItemCount() - 1)) {
            finishBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }
}
