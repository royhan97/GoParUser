package project.roy.socialmedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.data.model.DDTK;

public class ChildrenDevelopmentResultAdapter extends RecyclerView.Adapter<ChildrenDevelopmentResultAdapter.ChildrenDevelopmentResultViewHolder> {

    private Context context;
    private List<DDTK> ddtkList;
    private OnDetailListener onDetailListener;

    public ChildrenDevelopmentResultAdapter(Context context) {
        this.context = context;
    }

    public ChildrenDevelopmentResultAdapter() {

    }
    public void setData(List<DDTK> ddtkList){
        this.ddtkList = ddtkList;
        notifyDataSetChanged();
    }

    @Override
    public ChildrenDevelopmentResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view_children_development_result, parent, false);
        return new ChildrenDevelopmentResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildrenDevelopmentResultViewHolder holder, int position) {
        if(position % 5 == 0){
            holder.tvAge.setVisibility(View.VISIBLE);
            holder.tvAge.setText(ddtkList.get(position).getUsia());
            holder.line.setVisibility(View.VISIBLE);
        }else{
            holder.tvAge.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
        }
        holder.tvddtkDescription.setText(ddtkList.get(position).getAspekPerkembangan());
        if(ddtkList.get(position).getStatus() == 1){
            holder.imgCheck.setVisibility(View.VISIBLE);
        }else {
            holder.imgCheck.setVisibility(View.INVISIBLE);
        }
    }

    public void setOnClickDetailListener(OnDetailListener onDetailListener){
        this.onDetailListener = onDetailListener;
    }

    @Override
    public boolean onFailedToRecycleView(ChildrenDevelopmentResultViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public int getItemCount() {
        if(ddtkList == null) return 0;
        return ddtkList.size();
    }

    public class ChildrenDevelopmentResultViewHolder extends RecyclerView.ViewHolder{
        TextView tvddtkDescription, tvAge;
        View line;
        ImageView imgCheck;

        public ChildrenDevelopmentResultViewHolder(final View itemView) {
            super(itemView);
            tvAge = itemView.findViewById(R.id.tv_age);
            line = itemView.findViewById(R.id.line);
            tvddtkDescription = itemView.findViewById(R.id.tv_ddtk_description);
            imgCheck = itemView.findViewById(R.id.img_check);
        }
    }

    public interface OnDetailListener{
        void onItemDetailClicked(int tipsId, boolean b);
        void onImgDeleteCommentarSelected(int timelineId);

    }
}