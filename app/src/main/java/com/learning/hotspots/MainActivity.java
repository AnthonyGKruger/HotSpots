package com.learning.hotspots;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //declaring variables to be used throughout the program
    DBHelper myDBHelper;
    String locationName, locationAddress;
    double beerRating, wineRating, musicRating, average;
    EditText editTextLocationName, editTextLocationAddress;
    Button buttonSave, buttonRate, buttonDoRating;
    RatingBar beerRatingBar,  wineRatingBar, musicRatingBar;
    TextView averageRatingOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapWidgetsMainActivity();

    }

    //helper function that makes sure store information was entered correctly and that it is in
    // lowercase so that all text in the db is uniform. returns true if details were satisfactory
    public boolean getLocationDetails(EditText editTextLocationName, EditText editTextLocationAddressAddress){

        if (!editTextLocationName.getText().toString().equals("") &&
                !editTextLocationAddressAddress.getText().toString().equals("") ){

            locationName = editTextLocationName.getText().toString().toLowerCase();
            locationAddress = editTextLocationAddressAddress.getText().toString().toLowerCase();
            return true;
        }
        return false;
    }

    public void mapWidgetsMainActivity(){

        //mapping variables to widgets in main activity
        editTextLocationName = findViewById(R.id.editTextLocationName);
        editTextLocationAddress = findViewById(R.id.editTextLocationAddress);
        buttonRate = findViewById(R.id.buttonRate);
        buttonSave = findViewById(R.id.buttonSaveLocationDetails);
        //initializing dbhelper variable
        myDBHelper = new DBHelper(this, null,null, 1);

    }

    public void mapWidgetsRatingLayout(){

        //mapping variables to widgets in rating activity
        wineRatingBar = findViewById(R.id.ratingBarWine);
        musicRatingBar = findViewById(R.id.ratingBarMusic);
        beerRatingBar = findViewById(R.id.ratingBarBeer);
        buttonDoRating = findViewById(R.id.buttonDoRating);

    }

    public void changeLayoutToRatingLayout(View view){
        //changing the content view to the rating layout.

        setContentView(R.layout.rating_layout);

        //mapping variables to the widgets in the rating layout.
        mapWidgetsRatingLayout();
    }


    public void doRating(View view){
        //checking if store details were entered correctly.
        if (getLocationDetails(editTextLocationName, editTextLocationAddress)){

            //storing the ratings in variables
            musicRating = musicRatingBar.getRating();
            wineRating = wineRatingBar.getRating();
            beerRating = beerRatingBar.getRating();

            //using the variables to calulate the average of the ratings
            average = (beerRating + wineRating + musicRating) / 3;

            //using our db handler to update the database.
            myDBHelper.updateHandler(locationName, locationAddress, beerRating,
                    wineRating, musicRating, average);

            setContentView(R.layout.activity_main);

            mapWidgetsMainActivity();

            //outputting the average onto the relevant textview.
            averageRatingOutput = findViewById(R.id.textViewTheAverage);
            averageRatingOutput.setText(String.format("%s", average));

            //outputting a message to let the user know it was a success
            Toast.makeText(getApplicationContext(),
                    "Your ratings have been saved to the database",
                    Toast.LENGTH_LONG).show();
        }

        //location details werent set so the user needs to let us know which store to rate.
         else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter valid location information",
                            Toast.LENGTH_LONG).show();
                }
        }


        public void saveLocation(View view){

            //calling a helper which checks if store details have been entered correctly
            if(getLocationDetails(editTextLocationName, editTextLocationAddress)){

                //store details entered correct, now checking if the store is actually in the db
                if(myDBHelper.findHandler(locationName, locationAddress)) {

                    //case details are in the db, update the db using update helper function
                    myDBHelper.updateHandler(locationName, locationAddress, 0, 0,
                            0, 0 );
                    //store details are not in the db. Add to the db
                } else {

                    //using db helper class method to add to the db
                    myDBHelper.addToDB(locationName, locationAddress, 0,0,
                            0,0);

                }

                //db has been altered, letting the user know details are saved.
                Toast.makeText(getApplicationContext(),
                        "You have successfully saved your location details",
                        Toast.LENGTH_LONG).show();

                //store details have been entered incorrectly, notifying the user to try again.
            } else {
                //showing user a message.
                Toast.makeText(getApplicationContext(),
                        "You have not yet entered any location details, please try again",
                        Toast.LENGTH_LONG).show();

            }

        }


}


