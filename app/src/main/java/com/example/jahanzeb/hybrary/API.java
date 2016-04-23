package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.util.Log;

import com.example.jahanzeb.hybrary.Datasources.RetrieveBookReviews;

/**
 * Created by jahanzeb on 24/04/2016.
 */
public class API {

    public void RetrieveBookReviews(Activity context, int BookId, RetrieveBookReviewsCallback callback){
        Log.e("RetrieveBookReviews", "Before");
        new RetrieveBookReviews(context, BookId, callback).execute();
    }

    public interface RetrieveBookReviewsCallback {
        void onSuccess(Feedback_Adapter adapter);
    }
}