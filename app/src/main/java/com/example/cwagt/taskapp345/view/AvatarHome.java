/**
 * User interacts with the avatar here.
 * Activities include switching cosmetics and starting animations.
 *
 * Authors: Josh April, Shaun Henderson, Craig Thomas
 */

package com.example.cwagt.taskapp345.view;
/**
 * Created by cwagt on 15/07/2018.
 *
 * Activity which holds the Avatar fragment and recycler view
 */

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.cwagt.taskapp345.R;
import com.example.cwagt.taskapp345.helper.BodyPartsAdapter;
import com.example.cwagt.taskapp345.helper.CategoriesAdapter;
import com.example.cwagt.taskapp345.helper.DatabaseHelper;
import com.example.cwagt.taskapp345.helper.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AvatarHome extends AppCompatActivity {
    private List<String> categoriesList;
    private RecyclerView categoryRecyclerView;
    private RecyclerView bodyPartsRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar_home);

        //great the list of categories
        categoriesList = getCategories();

        //populate categories
        categoryRecyclerView = findViewById(R.id.categoriesRecyclerView);
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this, categoriesList); //categories list
        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false);
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setAdapter(categoriesAdapter);

        //populate body parts
        ArrayList<String> bodyPartsList = new ArrayList<>();
        bodyPartsList.add("bodypart1");
        bodyPartsList.add("bodypart2"); // dont forget we have left arm, right arm
        bodyPartsList.add("bodypart3"); // left leg, right leg
        bodyPartsList.add("bodypart4");
        bodyPartsList.add("bodypart5");

        //create the bodyparts recyclerview
        bodyPartsRecyclerView = findViewById(R.id.bodyPartsRecyclerView);
        BodyPartsAdapter bodyPartsAdapter = new BodyPartsAdapter(this, bodyPartsList); //body part list list
        RecyclerView.LayoutManager bodyPartsLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false);
        bodyPartsRecyclerView.setLayoutManager(bodyPartsLayoutManager);
        bodyPartsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bodyPartsRecyclerView.setAdapter(bodyPartsAdapter);

        //define the behavior of the two recycler view on click events
        categoryRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                categoryRecyclerView, new RecyclerItemClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //display selected category debug
                Log.d("CategoriesAdapter", "Clicked on: " + position);

                //remember the selected list
                SharedPreferences preferences = getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong("currentCategory", position);
                editor.apply();

                //change the bodyPartsRecyclerView list
                int category = position;
                ArrayList<String> currentBodyPartList = getCategoryItems(category);
                setBodyPartList(bodyPartsRecyclerView, currentBodyPartList);
            }

            @Override
            public void onLongClick(View view, int position) {
                onClick(view, position);
            }
        }));
    }

    /**
     * This method updates the bodyPartsRecyclerView after the user clicks on a category,
     * allowing them to change what items are displayed in the list
     * @param bodyPartsRecyclerView the recycler view to change the items of
     * @param bodyPartsList a list of strings to populate the view with
     */
    private void setBodyPartList(RecyclerView bodyPartsRecyclerView, ArrayList<String> bodyPartsList){
        BodyPartsAdapter bodyPartsAdapter = new BodyPartsAdapter(this, bodyPartsList);
        bodyPartsRecyclerView.setAdapter(bodyPartsAdapter);
    }

    /**
     * Returns a list of items in the category with the given ID
     * DUMMY METHOD should be replaced so these values are read from the database
     * instead of being hardcoded here
     * @param categoryID the id of the list
     * @return a list of strings to display
     */
    private ArrayList<String> getCategoryItems(int categoryID){
        ArrayList<String> result = new ArrayList<String>();

        switch (categoryID){
            case 0: //HEAD
                result.add("Head 1");
                result.add("Head 2");
                result.add("Head 3");
                result.add("Head 4");
                result.add("Head 5");
                result.add("Head 6");
                break;
            case 1: //LEFT ARM
            case 2: //RIGHT ARM
                result.add("Arm 1");
                result.add("Arm 2");
                result.add("Arm 3");
                result.add("Arm 4");
                result.add("Arm 5");
                result.add("Arm 6");
                break;
            case 3:
            case 4:
                result.add("Leg 1");
                result.add("Leg 2");
                result.add("Leg 3");
                result.add("Leg 4");
                result.add("Leg 5");
                result.add("Leg 6");
                break;
            case 5:
                result.add("Torso 1");
                result.add("Torso 2");
                result.add("Torso 3");
                result.add("Torso 4");
                result.add("Torso 5");
                result.add("Torso 6");
                break;
            case 6:
                result.add("Background 1");
                result.add("Background 2");
                result.add("Background 3");
                result.add("Background 4");
                result.add("Background 5");
                result.add("Background 6");
                break;
            default:
                result.add("Head 1");
                result.add("Head 2");
                result.add("Head 3");
                result.add("Head 4");
                result.add("Head 5");
                result.add("Head 6");
                break;
        }
        return result;
    }

    /**
     * Creates a list of categories to display on the screen
     * DUMMY METHOD should get categories from database
     * @return a list of categories
     */
    public ArrayList<String> getCategories(){
        ArrayList<String> result = new ArrayList<>();
        categoriesList.add("HEAD");
        categoriesList.add("LEFT ARM"); // dont forget we have left arm, right arm
        categoriesList.add("RIGHT ARM");
        categoriesList.add("LEFT LEG"); // left leg, right leg
        categoriesList.add("RIGHT LEG");
        categoriesList.add("TORSO");
        categoriesList.add("BACKGROUND");
        return result;
    }
}


