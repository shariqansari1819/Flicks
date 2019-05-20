package com.codebosses.flicks.utils;

import android.annotation.SuppressLint;

import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesResult;
import com.codebosses.flicks.pojo.celebritiespojo.celebmovies.CelebMoviesData;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortingUtils {

    public static void sortMovieByDate(List<MoviesResult> moviesResultArrayList) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Collections.sort(moviesResultArrayList, new Comparator<MoviesResult>() {
            @Override
            public int compare(MoviesResult o1, MoviesResult o2) {
                try {
                    return simpleDateFormat.parse(o2.getRelease_date()).compareTo(simpleDateFormat.parse(o1.getRelease_date()));
                } catch (Exception e) {
                    return 0;
                }
            }
        });
    }

    public static void sortCelebritiesByRating(List<CelebritiesResult> celebritiesResultList) {
        Collections.sort(celebritiesResultList, new Comparator<CelebritiesResult>() {
            @Override
            public int compare(CelebritiesResult o1, CelebritiesResult o2) {
                if (o1.getPopularity() > o2.getPopularity())
                    return 0;
                else
                    return 1;
            }
        });
    }

    public static void sortCelebMoviesByDate(List<CelebMoviesData> moviesResultList) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Collections.sort(moviesResultList, new Comparator<CelebMoviesData>() {
                @Override
                public int compare(CelebMoviesData o1, CelebMoviesData o2) {
                    try {
                        return simpleDateFormat.parse(o2.getRelease_date()).compareTo(simpleDateFormat.parse(o1.getRelease_date()));
                    } catch (Exception e) {
                        return 0;
                    }
                }
            });
        }catch (Exception e){

        }
    }

}