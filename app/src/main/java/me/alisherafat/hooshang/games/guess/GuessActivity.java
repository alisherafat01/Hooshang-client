package me.alisherafat.hooshang.games.guess;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.alisherafat.hooshang.R;
import me.alisherafat.hooshang.app.models.GameModel;

public class GuessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,
                            Guess3x3Fragment.newInstance((GameModel) getIntent().getExtras().getSerializable("game")))
                    .commit();
        }
    }
}
