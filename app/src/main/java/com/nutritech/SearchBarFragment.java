package com.nutritech;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nutritech.models.FoodSuggestor;
import com.wanderingcan.persistentsearch.PersistentSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class SearchBarFragment extends Fragment {

    public static final int VOICE_RECOGNITION_CODE = 9999;
    private final Object attachingActivityLock = new Object();
    private boolean mMicEnabled;
    private PersistentSearchView mSearchView;
    private boolean syncVariable = false;

    public SearchBarFragment() {
        Thread processImage = new Thread(() -> mMicEnabled = isIntentAvailable(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)));
        processImage.start();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        synchronized (attachingActivityLock) {
            syncVariable = true;
            attachingActivityLock.notifyAll();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_bar, container, false);
    }

    public PersistentSearchView getmSearchView() {
        return mSearchView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mSearchView = Objects.requireNonNull(getView()).findViewById(R.id.search_bar);
        initBasicSearchView();
    }

    private void initBasicSearchView() {
        mSearchView.setOnSearchListener(new PersistentSearchView.OnSearchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSearchOpened() {
                mSearchView.getSearchMenu().addSearchMenuItem(2, "Suggestion").setIcon(R.drawable.ic_globe);
            }

            @Override
            public void onSearchClosed() {
            }

            @Override
            public void onSearchCleared() {
                mSearchView.getSearchMenu().removeSearchMenuItem(mSearchView.getSearchMenu().getSearchMenuItem(1));

            }

            @Override
            public void onSearchTermChanged(CharSequence term) {
//                if (mSearchView.getSearchMenu().getSearchMenuItem(1) != null) {
//                    mSearchView.getSearchMenu().getSearchMenuItem(1).setTitle(term.toString());
//                } else {
//                    mSearchView.getSearchMenu().addSearchMenuItem(1, term.toString(), 1).setIcon(R.drawable.ic_history);
//                }

                FoodSuggestor foodSuggestor = new FoodSuggestor();
                //TODO : Ici on addSearchItem
            }

            @Override
            public void onSearch(CharSequence text) {
                Snackbar.make(mSearchView, "Searched for \'" + text + "\'", Snackbar.LENGTH_LONG)
                        .show();
            }

        });

        mSearchView.setOnIconClickListener(new PersistentSearchView.OnIconClickListener() {
            @Override
            public void OnNavigationIconClick() {
            }

            @Override
            public void OnEndIconClick() {
                startVoiceRecognition();
            }
        });

        mSearchView.setOnMenuItemClickListener(item -> {
            //Called when an Item in the SearchMenu is clicked, it passes in the
            //SearchMenuItem that was clicked
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mSearchView.populateSearchText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isIntentAvailable(Intent intent) {

        synchronized (attachingActivityLock) {
            while (!syncVariable) {
                try {
                    attachingActivityLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        PackageManager mgr = getActivity().getPackageManager();
        if (mgr != null) {
            List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }
        return false;
    }

    private void startVoiceRecognition() {

        if (mMicEnabled) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Prononcez le nom de votre aliment");
            startActivityForResult(intent, VOICE_RECOGNITION_CODE);
        }
    }

}
