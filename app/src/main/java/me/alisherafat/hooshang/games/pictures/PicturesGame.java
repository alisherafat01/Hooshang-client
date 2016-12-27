package me.alisherafat.hooshang.games.pictures;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.alisherafat.hooshang.R;
import me.alisherafat.hooshang.app.models.GameModel;
import me.alisherafat.hooshang.games.PictureMove;

public class PicturesGame extends GameModel {
    public static int[] icons = new int[]{
            R.drawable.ic_action_bike, R.drawable.ic_action_bulb, R.drawable.ic_action_camera,
            R.drawable.ic_action_car, R.drawable.ic_action_coffee, R.drawable.ic_action_emo_basic,
            R.drawable.ic_action_grow, R.drawable.ic_action_guitar, R.drawable.ic_action_plane,
            R.drawable.ic_action_star_0, R.drawable.ic_action_tshirt, R.drawable.ic_action_twitter};

    public List<PicturePlace> places;

    public int getImageResource(int position) {
        for (PicturePlace place : places) {
            if (place.firstPlace == position || place.secondPlace == position) {
                return icons[place.imageId];
            }
        }
        return -1;
    }

    public PicturePlace getPicturePlace(int imagePosition) {
        for (PicturePlace place : places) {
            if (place.firstPlace == imagePosition || place.secondPlace == imagePosition) {
                return place;
            }
        }
        return null;
    }

    public JSONObject getNewMoveData(PictureMove move) {
        JSONObject object = new JSONObject();
        try {
            object.put("room", room);
            object.put("right", move.righChoose);
            object.put("index", move.index);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean rightChoose(int first, int second) {
        for (PicturePlace place : places) {
            if ((place.firstPlace == first || place.firstPlace == second) &&
                    (place.secondPlace == first || place.secondPlace == second)) {
                return true;
            }
        }
        return false;
    }
}
