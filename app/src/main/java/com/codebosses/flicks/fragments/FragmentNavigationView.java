package com.codebosses.flicks.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebosses.flicks.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNavigationView extends Fragment {


    public FragmentNavigationView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_view, container, false);
    }

//    private void funGetUserSites() {
//        mBind.mPb.setVisibility(View.VISIBLE);
//        mUserSitesCall = Api.WEB_SERVICE.getUserSites("55555", GlobalClass.getInstance().mPref.getString("id", ""));
//        mUserSitesCall.enqueue(new Callback<UserSitesModel>() {
//            @Override
//            public void onResponse(Call<UserSitesModel> call, retrofit2.Response<UserSitesModel> response) {
//                mBind.mPb.setVisibility(View.INVISIBLE);
//                if (response != null && response.isSuccessful()) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("mFlag", "email");
//                    switch (response.body().getStatus()) {
//                        case "0":
////                            ((MainActivity) getActivity()).fnLoadFragAdd("CREATE SITE", true, null);
//                            bundle.putBoolean("site_exist", false);
//                            break;
//                        case "1":
//                            bundle.putBoolean("site_exist", true);
//                            break;
//                    }
//                    GlobalClass.getInstance().snakeBar(((MainActivity) getActivity()).mBind.mRoot, getResources().getString(R.string.mail_sent), R.color.success);
//                    ((MainActivity) getActivity()).fnLoadFragAdd("SUCCESS EMAIL PHONE", false, bundle);
//                } else {
//                    GlobalClass.getInstance().snakeBar(((MainActivity) getActivity()).mBind.mRoot, getString(R.string.response_is_null), R.color.error);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserSitesModel> call, Throwable error) {
//                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
//                    return;
//                }
//                mBind.mPb.setVisibility(View.INVISIBLE);
//                if (error != null) {
//                    if (error.getMessage().contains("No address associated with hostname")) {
//                        GlobalClass.getInstance().snakeBar(((MainActivity) getActivity()).mBind.mRoot, getString(R.string.internet_or_server_error), R.color.error);
//                    } else {
//                        GlobalClass.getInstance().snakeBar(((MainActivity) getActivity()).mBind.mRoot, error.getMessage() + "", R.color.error);
//                    }
//                } else {
//                    GlobalClass.getInstance().snakeBar(((MainActivity) getActivity()).mBind.mRoot, getString(R.string.error_is_null), R.color.error);
//                }
//            }
//        });
//    }

}
