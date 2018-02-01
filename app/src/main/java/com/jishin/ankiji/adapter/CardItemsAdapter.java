package com.jishin.ankiji.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jishin.ankiji.R;
import com.jishin.ankiji.animation.BuilderManager;
import com.jishin.ankiji.interfaces.RemoveDataCommunicator;
import com.jishin.ankiji.model.DataTypeEnum;
import com.jishin.ankiji.model.Set;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class CardItemsAdapter extends RecyclerView.Adapter<CardItemsAdapter.ItemViewHolder> {

    private final static String TAG = CardFragmentPagerAdapter.class.getSimpleName();
    private ArrayList<Set> mSetList = new ArrayList<>();
    private String FRAGMENT_TAG;
    private OnBoomMenuItemClicked mListener;
    private Context mContext;
    private RemoveDataCommunicator communicator;

    public CardItemsAdapter(String FRAGMENT_TAG, Context context) {
        this.FRAGMENT_TAG = FRAGMENT_TAG;
        this.mContext = context;
        this.communicator = communicator;
    }

    public void setOnBoomMenuItemClick(OnBoomMenuItemClicked mListener) {
        this.mListener = mListener;
    }

    public void setSetList(ArrayList<Set> mSetList) {
        this.mSetList = mSetList;
        notifyDataSetChanged();
    }

    @Override
    public CardItemsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.custom_recycler_item,
                parent,
                false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CardItemsAdapter.ItemViewHolder holder, final int position) {

        if (FRAGMENT_TAG.equals("RECENTLY")) {
            if(mSetList.size() != 0 && position < mSetList.size()){
                holder.set = this.mSetList.get(position);
                holder.tvTitle.setText(holder.set.getName());
                holder.tvItemCount.setText(convertDatetime(holder.set.getDatetime()));
                holder.dataType = DataTypeEnum.Moji;
//                holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showRemoveDialog(FRAGMENT_TAG, position);
//                    }
//                });
            }
        }

        if (FRAGMENT_TAG.equals("MOJI")) {
            if (this.mSetList.size() != 0) {
                holder.set = this.mSetList.get(position);
                holder.tvTitle.setText(holder.set.getName());
                holder.tvItemCount.setText(convertDatetime(holder.set.getDatetime()));
                holder.dataType = DataTypeEnum.Moji;
//                holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showRemoveDialog(FRAGMENT_TAG, position);
//                    }
//                });
            }
        }

        if (FRAGMENT_TAG.equals("KANJI")) {
            if (this.mSetList.size() != 0) {
                holder.set = this.mSetList.get(position);
                holder.tvTitle.setText(holder.set.getName());
                holder.tvItemCount.setText(convertDatetime(holder.set.getDatetime()));
                holder.dataType = DataTypeEnum.Kanji;
//                holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showRemoveDialog(FRAGMENT_TAG, position);
//                    }
//                });
            }
        }

        holder.bmb.clearBuilders();
        int stringIndex = 0;
        for (int i = 0; i < holder.bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            switch (i) {
                case 0:
                    stringIndex = R.string.text_outside_button_buider_learn;
                    break;
                case 1:
                    stringIndex = R.string.text_outside_button_buider_test;
                    break;
                case 2:
                    stringIndex = R.string.text_outside_button_buider_chart;
                    break;
                case 3:
                    stringIndex = R.string.text_outside_button_buider_edit;
                    break;
                case 4:
                    stringIndex = R.string.text_outside_button_buider_delete;
                    break;
            }
            addBuilder(holder, stringIndex, position);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.bmb.boom();
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(mContext, "Long clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    void showRemoveDialog(final String fragmentTag, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.remove_warning);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (fragmentTag.equals("KANJI")) {
                    communicator.removeData(mSetList.get(position).getId(), position);
                }
                if (fragmentTag.equals("MOJI")) {
                    communicator.removeData(mSetList.get(position).getId(), position);
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    void addBuilder(final ItemViewHolder viewHolder, int stringIndex, final int position) {
        viewHolder.bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(BuilderManager.getImageResource())
                .normalTextRes(stringIndex)
                .textSize(14)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        mListener.OnMenuItemClicked(index, viewHolder.dataType, viewHolder.set, position);
                    }
                }));
    }


    @Override
    public int getItemCount() {
        if (FRAGMENT_TAG.equalsIgnoreCase("RECENTLY")) {
            return 10;
        }
        return this.mSetList.size();

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        DataTypeEnum dataType;
        TextView tvTitle;
        TextView tvItemCount;
//        ImageButton btnDeleteItem;
        BoomMenuButton bmb;
        CardView cardView;
        Set set;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvItemCount = itemView.findViewById(R.id.tv_item_count);
//            btnDeleteItem = itemView.findViewById(R.id.btn_delete_item);
            bmb = itemView.findViewById(R.id.bmb);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    public interface OnBoomMenuItemClicked {
        void OnMenuItemClicked(int classIndex, DataTypeEnum dataTypeEnum, Set set, int position);
    }
    private String convertDatetime(String datetime){
        String result = datetime;
        String gmt = result.substring(20, 29);
        String year = result.substring(30);
        result = result.replace(gmt, "").replace(year, "");
        String part1 = result.substring(0, 10);
        String part2 = result.substring(11,19);
        result = part1+" "+year+" "+part2;
        return result;
    }
}
