package com.jishin.ankiji.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.CardItemsAdapter;
import com.jishin.ankiji.explores.TopicKanjiActivity;
import com.jishin.ankiji.learn.LearnActivity;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Set;
import com.jishin.ankiji.userlist.CreateVocabActivity;
import com.jishin.ankiji.utilities.Constants;
import com.jishin.ankiji.utilities.DatabaseService;

import java.util.ArrayList;

/**
 * Created by trungnguyeen on 12/27/17.
 */

@SuppressLint("ValidFragment")
public class KanjiFragment extends Fragment {

    private static final String TAG = KanjiFragment.class.getSimpleName();
    private ArrayList<Set> mKanjiSetList = new ArrayList<>();
    private RecyclerView rvRecentlyList;
    private CardItemsAdapter mItemsAdapter;
    public String FRAGMENT_TAG = "KANJI";
    private FloatingActionButton mFABtn, mFABCreate, mFABAdd;
    private boolean isStable = true;
    private DatabaseReference mKanjiSetRef;
    private DatabaseService mData = DatabaseService.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kanji, container, false);

        initParam();
        addControl(view);
        initRecycler(view);
        new LoadKanjiDataTask().execute();
        return view;
    }

    private void initParam() {
        mKanjiSetRef = mData.getDatabase()
                .child(Constants.KANJI_SET_NODE)
                .child(mData.getUserID());
        Log.d(TAG, "initParam: " + mKanjiSetList.toString());

    }
    private void initRecycler(View view) {
        rvRecentlyList = (RecyclerView) view.findViewById(R.id.recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvRecentlyList.setLayoutManager(layoutManager);
        mItemsAdapter = new CardItemsAdapter(FRAGMENT_TAG);
        mItemsAdapter.setSetList(this.mKanjiSetList);
        mItemsAdapter.setOnBoomMenuItemClick(new CardItemsAdapter.OnBoomMenuItemClicked() {
            @Override
            public void OnMenuItemClicked(int classIndex, DataTypeEnum dataTypeEnum, Set set) {
                switch (classIndex) {
                    case 0:
                        Intent intent = new Intent(getContext(), LearnActivity.class);
                        intent.putExtra(Constants.SET_BY_USER, set);
                        intent.putExtra(Constants.DATA_TYPE, dataTypeEnum);
                        startActivity(intent);
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
            }
        });
        rvRecentlyList.setItemAnimator(new DefaultItemAnimator());
        rvRecentlyList.setAdapter(mItemsAdapter);
        rvRecentlyList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mFABtn.show();
                    if(isStable == false && !mFABCreate.isShown()){
                        mFABtn.setRotation(mFABtn.getRotation()+45F);
                        isStable = true;
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if ((dy > 0 ||dy<0) && mFABtn.isShown()) {
                    mFABtn.hide();
                    if(mFABAdd.isShown()|| mFABCreate.isShown()){
                        mFABAdd.hide();
                        mFABCreate.hide();
                        isStable = true;
                    }
                }
            }
        });
    }
    private void addControl(View view){
        mFABtn = view.findViewById(R.id.fabKanji);
        mFABAdd = view.findViewById(R.id.fabAdd);
        mFABCreate = view.findViewById(R.id.fabCreate);
        mFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mFABCreate.isShown()){
                    mFABAdd.show();
                    mFABCreate.show();  //required
                    float deg = mFABtn.getRotation() + 45F;
                    mFABtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    isStable = false;
                }
                else{
                    mFABAdd.hide();
                    mFABCreate.hide();
                    float deg = mFABtn.getRotation() + 45F;
                    mFABtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    isStable = true;
                }
            }
        });
        mFABCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                final View dialogView = layoutInflater.inflate(R.layout.dialog_create_list, null);
                final TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
                final EditText edtSetName = dialogView.findViewById(R.id.edtSetName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String setName = edtSetName.getText().toString().trim();
                        if(!setName.isEmpty()){
                            Toast.makeText(getContext(), "Click click", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), CreateVocabActivity.class);
                            intent.putExtra("create", Constants.CREATE_KANJI);
                            intent.putExtra("name", setName);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getContext(), "Cannot create a new set.\nSet name field is required", Toast.LENGTH_SHORT).show();
                        }
                        mFABAdd.hide();
                        mFABCreate.hide();
                        mFABtn.setRotation(mFABtn.getRotation()+45F);
                        isStable = true;

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        mFABAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TopicKanjiActivity.class);
                startActivity(intent);
            }
        });
    }

    public class LoadKanjiDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getMojiSet();
            return null;
        }
    }

    private void getMojiSet() {
        mKanjiSetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        mKanjiSetList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            mKanjiSetList.add(ds.getValue(Set.class));
        }
        mItemsAdapter.notifyDataSetChanged();
    }
}

