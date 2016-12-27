package me.alisherafat.hooshang.games.guess;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.alisherafat.hooshang.R;
import me.alisherafat.hooshang.app.models.GameModel;
import me.alisherafat.hooshang.app.preferences.UserPrefs;
import me.alisherafat.hooshang.app.utils.Utils;
import me.alisherafat.hooshang.games.GameInteractions;
import me.alisherafat.hooshang.games.GamesSocket;
import me.alisherafat.hooshang.games.GuessMove;

public class Guess3x3Fragment extends Fragment implements GameInteractions {

    @BindView(R.id.txtName1) TextView txtName1;
    @BindView(R.id.txtName2) TextView txtName2;
    @BindViews({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                       R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9})
    public List<Button> buttons;
    private GamesSocket gameSocket;
    private GameModel game;
    private Utils utils;
    private Unbinder unbinder;
    private boolean turn;
    private int answer;
    private JSONObject data;
    private UserPrefs userPrefs;



    public Guess3x3Fragment() {
    }

    public static Guess3x3Fragment newInstance(GameModel game) {
        Guess3x3Fragment fragment = new Guess3x3Fragment();
        Bundle args = new Bundle();
        args.putSerializable("game", game);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPrefs = UserPrefs.getInstance(getContext());
        utils = Utils.getInstance(getContext());
        gameSocket = GamesSocket.getInstance(getActivity());

        if (getArguments() == null) {
            return;
        }

        game = (GameModel) getArguments().getSerializable("game");
        turn = game.players[0].name.equals(userPrefs.getUsername());
        try {
            data = new JSONObject(game.jsonData);
            answer = data.getInt("answer");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gameSocket.setListener(this);
    }


    private void hanleBackPress() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed();
                    return true;
                }
                return false;
            }


        });
    }

    private void onBackPressed() {
        new MaterialDialog.Builder(getActivity())
                .content("Are you sure you wanna exit?")
                .positiveText("yes")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(
                            @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        gameSocket.resign(game.room);
                        getActivity().finish();
                    }
                })
                .negativeText("nope")
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guess3x3, container, false);
        unbinder = ButterKnife.bind(this, view);
        txtName1.setText(game.players[0].name);
        txtName2.setText(game.players[1].name);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hanleBackPress();
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                     R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9})
    public void onClick(Button button) {
        if (!turn) {
            utils.toast("not your turn");
            return;
        }
        turn = false;
        buttonClick(Integer.parseInt(button.getText().toString()));
    }

    private void buttonClick(int number) {
        if (number == answer) {
            utils.toast("Hooray! you won");
            gameSocket.win(game.room);
            getActivity().finish();
            return;
        }
        GuessMove move = new GuessMove();
        move.number = number;
        gameSocket.newMove(GuessGame.getNewMoveData(game.room,move));
        buttons.get(number - 1).setEnabled(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onOpponentResigned() {
        utils.log("resigned");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                utils.toast("opponent resigned");
                getActivity().finish();
            }
        });
    }

    @Override
    public void onNewMove(JSONObject object) {
        utils.log("asdfasfdsf");
        final GuessMove move = GuessMove.from(object);
        turn = true;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttons.get(move.number - 1).setEnabled(false);
            }
        });
    }


    @Override
    public void onOpponentWon() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                utils.toast("you lose!");
                getActivity().finish();
            }
        });
    }
}
