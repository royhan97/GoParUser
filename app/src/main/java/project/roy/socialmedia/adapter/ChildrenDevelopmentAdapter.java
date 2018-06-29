package project.roy.socialmedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.data.model.DDTK;

public class ChildrenDevelopmentAdapter extends RecyclerView.Adapter<ChildrenDevelopmentAdapter.ChildrenDevelopmentViewHolder> {

    private Context context;
    private List<DDTK> ddtkList;
    private OnDetailListener onDetailListener;

    public ChildrenDevelopmentAdapter(Context context) {
        this.context = context;
    }

    public ChildrenDevelopmentAdapter() {

    }
    public void setData(List<DDTK> ddtkList){
        this.ddtkList = ddtkList;
        notifyDataSetChanged();
    }

    @Override
    public ChildrenDevelopmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item__card_view_children_development, parent, false);
        return new ChildrenDevelopmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildrenDevelopmentViewHolder holder, int position) {
        if(position % 5 == 0){
            holder.tvAge.setVisibility(View.VISIBLE);
            holder.tvAge.setText(ddtkList.get(position).getUsia());
            holder.line.setVisibility(View.VISIBLE);
        }else{
            holder.tvAge.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
        }
        holder.tvddtkDescription.setText(ddtkList.get(position).getDdtkDescription());
        if(ddtkList.get(position).getStatus() == 1){
            holder.cbDDTK.setChecked(true);
            holder.cbDDTK.setEnabled(false);
        }else {
            holder.cbDDTK.setChecked(false);
        }

        if(position > 0){
            if(ddtkList.get(position-1).getStatus() == 0){
                holder.cbDDTK.setEnabled(false);
            }
            if(ddtkList.get(position-1).getStatus() == 1) {
                holder.cbDDTK.setEnabled(true);
            }


        }
        if(position == 0 ){
            holder.cbDDTK.setEnabled(true);
        }
        if(ddtkList.get(position).getStatus() == 1){
            holder.cbDDTK.setChecked(true);
            holder.cbDDTK.setEnabled(false);
        }


        holder.cbDDTK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b && holder.cbDDTK.isEnabled() ){
                    onDetailListener.onItemDetailClicked(ddtkList.get(position).getId(), true);
                }
            }
        });

    }

    public void setOnClickDetailListener(OnDetailListener onDetailListener){
        this.onDetailListener = onDetailListener;
    }

    @Override
    public boolean onFailedToRecycleView(ChildrenDevelopmentViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public int getItemCount() {
        if(ddtkList == null) return 0;
        return ddtkList.size();
    }

    public class ChildrenDevelopmentViewHolder extends RecyclerView.ViewHolder{
        TextView tvddtkDescription;
        CheckBox cbDDTK;
        TextView tvAge;
        View line;

        public ChildrenDevelopmentViewHolder(final View itemView) {
            super(itemView);
            tvAge = itemView.findViewById(R.id.tv_age);
            line = itemView.findViewById(R.id.line);
            tvddtkDescription = itemView.findViewById(R.id.tv_ddtk_description);
            cbDDTK = itemView.findViewById(R.id.cb_ddtk);
        }
    }

    public interface OnDetailListener{
        void onItemDetailClicked(int tipsId, boolean b);
        void onImgDeleteCommentarSelected(int timelineId);

    }
}