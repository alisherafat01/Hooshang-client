package me.alisherafat.hooshang.games.pictures;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.alisherafat.hooshang.R;
import me.alisherafat.hooshang.app.models.GameModel;
import me.alisherafat.hooshang.app.preferences.UserPrefs;
import me.alisherafat.hooshang.app.utils.Utils;
import me.alisherafat.hooshang.games.GameInteractions;
import me.alisherafat.hooshang.games.GamesSocket;
import me.alisherafat.hooshang.games.PictureMove;

public class PicturesGameFragment extends Fragment implements GameInteractions {

    @BindViews({R.id.img0, R.id.img1, R.id.img2, R.id.img3, R.id.img4,
                       R.id.img5, R.id.img6, R.id.img7, R.id.img8, R.id.img9,
                       R.id.img10, R.id.img11, R.id.img12, R.id.img13, R.id.img14, R.id.img15,
                       R.id.img16, R.id.img17, R.id.img18, R.id.img19, R.id.img20, R.id.img21,
                       R.id.img22, R.id.img23})
    public List<FloatingActionButton> images;

    @BindView(R.id.txtName1) TextView txtName1;
    @BindView(R.id.txtPoint1) TextView txtPoint1;
    @BindView(R.id.txtName2) TextView txtName2;
    @BindView(R.id.txtPoint2) TextView txtPoint2;
    private UserPrefs userPrefs;
    private PicturesGame game;
    private boolean turn, firstChoice = true;
    private Utils utils;
    private GamesSocket gameSocket;
    private int lastChoose = -1, points = 0, opponentPoints = 0;

    public PicturesGameFragment() {
        // Required empty public constructor
    }


    public static PicturesGameFragment newInstance(GameModel game) {
        PicturesGameFragment fragment = new PicturesGameFragment();
        Bundle args = new Bundle();
        args.putSerializable("game", game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameSocket = GamesSocket.getInstance(getActivity());
        utils = Utils.getInstance(getContext());
        userPrefs = UserPrefs.getInstance(getContext());

        if (getArguments() == null) {
            return;
        }

        game = (PicturesGame) getArguments().getSerializable("game");
        game.places = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(game.jsonData);
            JSONArray array = object.getJSONArray("answer");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                PicturePlace place = new PicturePlace();
                place.imageId = obj.getInt("a");
                place.firstPlace = obj.getInt("b");
                place.secondPlace = obj.getInt("c");
                utils.log(place.toString());
                game.places.add(place);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        turn = game.players[0].name.equals(userPrefs.getUsername());
        gameSocket.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pictures_game, container, false);
        ButterKnife.bind(this, view);
        txtName1.setText(userPrefs.getUsername());
        if (userPrefs.getUsername().equals(game.players[0].name)) {
            txtName2.setText(game.players[1].name);
        } else {
            txtName2.setText(game.players[0].name);
        }
        return view;
    }

    @OnClick({R.id.img0, R.id.img1, R.id.img2, R.id.img3, R.id.img4,
                     R.id.img5, R.id.img6, R.id.img7, R.id.img8, R.id.img9,
                     R.id.img10, R.id.img11, R.id.img12, R.id.img13, R.id.img14, R.id.img15,
                     R.id.img16, R.id.img17, R.id.img18, R.id.img19, R.id.img20, R.id.img21,
                     R.id.img22, R.id.img23})
    public void onClick(FloatingActionButton view) {
        onImageClicked(Integer.parseInt(view.getTag().toString()));
    }

    private void onImageClicked(final int imageNumber) {
        if (!turn) {
            utils.toast("not your turn");
            return;
        }

        images.get(imageNumber).setImageResource(game.getImageResource(imageNumber));
        if (firstChoice) {
            lastChoose = imageNumber;
        } else {
            PictureMove move = new PictureMove();
            move.index = imageNumber;
            move.righChoose = game.rightChoose(lastChoose, imageNumber);
            gameSocket.newMove(game.getNewMoveData(move));
            if (move.righChoose) {
                utils.log("self right");
                points++;
                txtPoint1.setText(String.valueOf(points));
                images.get(imageNumber).setEnabled(false);
                images.get(lastChoose).setEnabled(false);
                checkWin();
            } else {
                turn = false;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        images.get(imageNumber).setImageResource(android.R.color.transparent);
                        images.get(lastChoose).setImageResource(android.R.color.transparent);
                    }
                }, 1000);
            }

        }
        firstChoice = !firstChoice;
    }

    private void checkWin() {
        if ((points + opponentPoints) == 12) {
            gameSocket.win(game.room);
            utils.toast("Congrats!");
            getActivity().finish();
        }
    }


    private Handler handler = new Handler();

    @Override
    public void onOpponentResigned() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                utils.toast("opponent resigned");
                getActivity().finish();
            }
        });
    }

    @Override
    public void onNewMove(final JSONObject object) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PictureMove move = PictureMove.from(object);
                if (move.righChoose) {
                    utils.log("opp right");
                    opponentPoints++;
                    txtPoint2.setText(String.valueOf(opponentPoints));

                    PicturePlace place = game.getPicturePlace(move.index);

                    images.get(place.firstPlace).setImageResource(game.getImageResource(place.firstPlace));
                    images.get(place.firstPlace).setEnabled(false);

                    images.get(place.secondPlace).setImageResource(game.getImageResource(place.secondPlace));
                    images.get(place.secondPlace).setEnabled(false);
                    return;
                }
                turn = true;
                utils.toast("your turn");
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
