package com.example.cwagt.taskapp345.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.cwagt.taskapp345.R;
import com.example.cwagt.taskapp345.helper.*;
import com.example.cwagt.taskapp345.object.Enums;
import com.example.cwagt.taskapp345.object.Task;
import com.example.cwagt.taskapp345.object.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.provider.BaseColumns._ID;

/**
 * User interacts with the avatar here.
 * Activities include switching cosmetics and starting animations.
 *
 * Authors: Josh April, Shaun Henderson, Craig Thomas
 */
public class AvatarHome extends AppCompatActivity {
    private RecyclerView bodyPartsRecyclerView;
    private AvatarEditor editor;
    private Avatar_Fragment avatar_fragment;
    private Context context;
    private User user;

	private final int BASE = 0;
	private final int HAT = 1;
	private final int LEFT_ARM = 2;
	private final int RIGHT_ARM = 3;
	private final int LEFT_LEG = 4;
	private final int RIGHT_LEG = 5;
	private final int BACKGROUND = 6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar_home);
        context = getApplicationContext();

        //Toolbar on the top of the screen
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        //display avatar fragment
        displayAvatar();

        //TODO HERE

        //avatar = new Avatar();
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        Long userID = preferences.getLong("currentUser", 0);
        ArrayList<User> avatars = DatabaseHelper.readUsers(context, _ID + " = ?", new String[]{Long.toString(userID)});
        if(avatars.isEmpty()){
            Log.d("AvatarHome", "Could not get any user with id " + userID);
		}else{
			user = avatars.get(0);
			editor = new AvatarEditor(avatar_fragment, user.getAvatar());
		}

        //great the list of categories
        List<String> categoriesList = getCategories();

        //populate categories
        RecyclerView categoryRecyclerView = findViewById(R.id.categoriesRecyclerView);
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this, categoriesList); //categories list
        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false);
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setAdapter(categoriesAdapter);

        //populate body parts
        ArrayList<String> bodyPartsList = getCategoryItems(0); //default is zero

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
                ArrayList<String> currentBodyPartList = getCategoryItems(position);
                setBodyPartList(bodyPartsRecyclerView, currentBodyPartList);
            }

            @Override
            public void onLongClick(View view, int position) {
                onClick(view, position);
            }
        }));

        //Define the behaviour of the body parts list when clicked
        bodyPartsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                bodyPartsRecyclerView, new RecyclerItemClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //make sure they have completed a task before changing body parts
                //get current user
                SharedPreferences preferences = getDefaultSharedPreferences(context);
                Long userID = preferences.getLong("currentUser", 0);
                final List<Task> taskList = DatabaseHelper.readAllTasks(context, userID);
                //Check the number of compelted tasks and update tasksCompleted
                int completed = 0;
                if(taskList.size() > 0) {
                    for (Task task : taskList) {
                        if (task.getStatus() == Enums.Status.COMPLETED) {
                            completed++;
                        }
                    }
                }
                if(completed < 0){
                    //NOT ENOUGH TASKS COMPLETED
                    //show the user a message to let them know they must complete tasks first
                    AlertDialog.Builder builder = new AlertDialog.Builder(AvatarHome.this);
                    builder.setMessage("You must complete a task before changing your avatar!")
                            .setTitle("Task Check");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    //get current list and item selected
                    Long categoryPreference = preferences.getLong("currentCategory", 0);//default zero
                    int currentCategory = categoryPreference.intValue();
                    ArrayList<String> items = getCategoryItems(currentCategory);
                    String itemSelected = items.get(position);
                    Log.d("AvatarHome", "Selected: " + itemSelected);

                    editor.setImage(currentCategory, itemSelected);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                onClick(view, position);
            }
        }));

    }

    public Context getContext(){
        return context;
    }

    /**
     * Display the avatar fragment within the main activity
     *
     * */
    private void displayAvatar(){

            //create instance of avatar fragment
            Avatar_Fragment avatar_home_avatar_fragment = Avatar_Fragment.newInstance();
            //add fragment to main activity
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.avatar_home_fragment_container,avatar_home_avatar_fragment,"avatar_home_avatar_fragment").commit();
            this.avatar_fragment = avatar_home_avatar_fragment;
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
        ArrayList<String> result = new ArrayList<>();

        switch (categoryID){
			case BASE:
				result.add("Silly");
				result.add("Surprised");
				break;
            case HAT:
                result.add("Crown");
                result.add("Pirate");
                break;
            case LEFT_ARM:
            case RIGHT_ARM:
			case LEFT_LEG:
			case RIGHT_LEG:
                result.add("Strong");
                result.add("Robot");
                result.add("Cartoon");
                break;
            case BACKGROUND:
                result.add("Beach");
                result.add("Desert");
                result.add("Jungle");
                result.add("White");
                break;
            default:
            	//TODO: What are these for?
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
        //TODO: Consider having these as images instead of text
		result.add("Head");
        result.add("Hat");
        result.add("Left arm");
        result.add("Right arm");
        result.add("Left leg");
        result.add("Right leg");
        result.add("Background");
        return result;
    }
    /**
     * Populates the options dropdown menu in the top right of the activity
     * @param menu the menu to display
     * @return true when created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Code that handles what happens when you click on one of the menu items
     * @param item the menu item clicked on
     * @return boolean clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Check if user clicked on the refresh button
            case R.id.menu_refresh:
                //go back to main activity
                Intent refreshIntent = new Intent(this, MainActivity.class);
                finish();
                startActivity(refreshIntent);
                break;

            //Got to select/change user activity
            case R.id.menu_user:
                Intent userIntent = new Intent(this, UserHome.class);
                finish();
                startActivity(userIntent);
                break;

            //Got to avatar screen
            case R.id.menu_avatar:
                Intent avatarIntent = new Intent(this, AvatarHome.class);
                finish();
                startActivity(avatarIntent);
                break;

            //Go to edit task screen
            case R.id.menu_tasks:
                Intent editTaskIntent = new Intent(this, EditTask.class);
                finish();
                startActivity(editTaskIntent);
                break;
                //Go to main Activity
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}


