package me.alisherafat.hooshang.games.pictures;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.alisherafat.hooshang.R;
import me.alisherafat.hooshang.app.models.GameModel;

public class PicturesGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures_game);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,
                            PicturesGameFragment.newInstance((GameModel) getIntent().getSerializableExtra("game")))
                    .commit();
        }
    }
}
