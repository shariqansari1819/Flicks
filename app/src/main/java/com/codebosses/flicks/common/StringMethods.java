package com.codebosses.flicks.common;

import com.codebosses.flicks.endpoints.EndpointKeys;

public class StringMethods {

    public static String getMovieGenreTypeById(int id) {
        switch (id) {
            case Constants.ACTION_ID:
                return EndpointKeys.ACTION_MOVIES;
            case Constants.ADVENTURE_ID:
                return EndpointKeys.ADVENTURE_MOVIES;
            case Constants.ANIMATED_ID:
                return EndpointKeys.ANIMATED_MOVIES;
            case Constants.COMEDY:
                return EndpointKeys.COMEDY_MOVIES;
            case Constants.CRIME_ID:
                return EndpointKeys.CRIME_MOVIES;
            case Constants.ROMANTIC_ID:
                return EndpointKeys.ROMANTIC_MOVOES;
            case Constants.FAMILY_ID:
                return EndpointKeys.FAMILY_MOVIES;
            case Constants.HORROR_ID:
                return EndpointKeys.HORROR_MOVIES;
            case Constants.SCIENCE_FICTION_ID:
                return EndpointKeys.SCIENCE_FICTION_MOVIES;
            case Constants.THRILLER_ID:
                return EndpointKeys.THRILLER_MOVIES;
        }
        return "";
    }

    public static String getTvShowGenreTypeById(int id) {
        switch (id) {
            case Constants.ACTION_ID:
                return EndpointKeys.ACTION_TV_SHOWS;
            case Constants.ADVENTURE_ID:
                return EndpointKeys.ADVENTURE_TV_SHOWS;
            case Constants.ANIMATED_ID:
                return EndpointKeys.ANIMATED_TV_SHOWS;
            case Constants.COMEDY:
                return EndpointKeys.COMEDY_TV_SHOWS;
            case Constants.CRIME_ID:
                return EndpointKeys.CRIME_TV_SHOWS;
            case Constants.ROMANTIC_ID:
                return EndpointKeys.ROMANTIC_TV_SHOWS;
//            case Constants.FAMILY_ID:
//                return EndpointKeys.FAMILY_Tv;
            case Constants.HORROR_ID:
                return EndpointKeys.HORROR_TV_SHOWS;
            case Constants.SCIENCE_FICTION_ID:
                return EndpointKeys.SCIENCE_FICTION_TV_SHOWS;
            case Constants.THRILLER_ID:
                return EndpointKeys.THRILLER_TV_SHOWS;
        }
        return "";
    }

}
