package com.ft08.trailblazelearn.application;

import android.app.Application;
import com.ft08.trailblazelearn.models.Trainer;
import com.ft08.trailblazelearn.models.Participant;
import com.ft08.trailblazelearn.models.User;

/**
 * Created by keerthanadevi on 15/3/18.
 */

public class App extends Application {

    public static Trainer trainer;
    public static Participant participant;


    public App() {

    }
    public App(User user){
        if(user instanceof Trainer){
            trainer = (Trainer) user;
        }
        else {
            participant = (Participant)user;
        }
    }
}
