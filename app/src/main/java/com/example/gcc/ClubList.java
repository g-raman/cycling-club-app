package com.example.gcc;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ClubList extends ArrayAdapter<Club> {

    private Activity context;

    List<Club> clubs;
    public ClubList(Activity context, List<Club> clubs) {
        super(context, R.layout.layout_user_club_search, clubs);
        this.context = context;
        this.clubs = clubs;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View clubListItem = inflater.inflate(R.layout.layout_user_club_search, null, true);

        TextView clubName = (TextView) clubListItem.findViewById(R.id.textViewClubName);
        TextView clubRating = (TextView) clubListItem.findViewById(R.id.textViewClubRating);

        Club newClub = clubs.get(position);

        clubName.setText(newClub.getName());
        String[] parts = newClub.getRating().split(" - ");
        if (parts.length == 2) {
            int rating = Integer.parseInt(parts[0]); // Extract the rating
            int numberOfRatings = Integer.parseInt(parts[1]);
            int sumOfRatings = rating * numberOfRatings; // Assuming the ratings are uniformly 4

            if (numberOfRatings > 0) {
                double averageRating = (double) sumOfRatings / numberOfRatings;
                clubRating.setText(String.valueOf(averageRating));
            }
            else {
                clubRating.setText(String.valueOf(0));
            }
        }
        return clubListItem;

    }
}
