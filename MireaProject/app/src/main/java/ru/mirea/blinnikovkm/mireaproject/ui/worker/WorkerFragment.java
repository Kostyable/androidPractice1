package ru.mirea.blinnikovkm.mireaproject.ui.worker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import ru.mirea.blinnikovkm.mireaproject.R;
import ru.mirea.blinnikovkm.mireaproject.ForumWorker;

public class WorkerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayout comments;
    private ProgressBar progressBar;

    public static WorkerFragment newInstance(String param1, String param2) {
        WorkerFragment fragment = new WorkerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_worker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        comments = view.findViewById(R.id.commentsLayout);
        progressBar = view.findViewById(R.id.commentsProgressBar);

        comments.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        WorkRequest loadingRequest =
                new OneTimeWorkRequest.Builder(ForumWorker.class).build();

        WorkManager workManager = WorkManager.getInstance(view.getContext());
        workManager.enqueue(loadingRequest);

        workManager.getWorkInfoByIdLiveData(loadingRequest.getId()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                progressBar.setVisibility(View.GONE);
                comments.setVisibility(View.VISIBLE);
            }
        });
    }
}