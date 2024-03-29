package com.example.pcdashboard.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pcdashboard.Manager.SharedPreferencesUtils;
import com.example.pcdashboard.Model.ClassPost;
import com.example.pcdashboard.R;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ClassPost> classPosts;
    private OnItemClickListener listener;

    public ClassAdapter(Context context, ArrayList<ClassPost> classPosts, OnItemClickListener listener) {
        this.context = context;
        this.classPosts = classPosts;
        this.listener = listener;
    }

    public void update(ArrayList<ClassPost> classPosts) {
        this.classPosts = classPosts;
    }

    public interface OnItemClickListener {
        void onCommentClick(ClassPost classPost);

        void onEditClick(ClassPost classPost);

        void onDeleteClick(ClassPost classPost);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            final ClassPost classPost = classPosts.get(position);
            holder.tvName.setText(classPost.getUserName());
            holder.tvTime.setText(classPost.getTime());
            holder.tvContent.setText(classPost.getContent());
            Glide.with(context).load(Uri.parse(classPost.getUserAvatar())).centerCrop().override(40, 40).into(holder.ivAvatar);
            if (classPost.getImage() != null) {
                holder.ivImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(Uri.parse(classPost.getImage())).into(holder.ivImage);
            } else holder.ivImage.setVisibility(View.GONE);
            holder.tvComment.setOnClickListener(v -> {
                holder.tvComment.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
                listener.onCommentClick(classPost);
            });
            if (classPost.getUserId().equals(SharedPreferencesUtils.loadSelf(context).getId())) {
                holder.ibEdit.setVisibility(View.VISIBLE);
                holder.ibDelete.setVisibility(View.VISIBLE);
                holder.ibEdit.setOnClickListener(v -> {
                    holder.ibEdit.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
                    listener.onEditClick(classPost);
                });
                holder.ibDelete.setOnClickListener(v -> {
                    holder.ibDelete.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
                    listener.onDeleteClick(classPost);
                });
            } else if (SharedPreferencesUtils.loadSelf(context).getRole().equals("ROLE_MONITOR") && classPost.getUserRole() != null) {
                if (classPost.getUserRole().equals("ROLE_TEACHER")) {
                    holder.ibEdit.setVisibility(View.GONE);
                    holder.ibDelete.setVisibility(View.GONE);
                } else {
                    holder.ibEdit.setVisibility(View.GONE);
                    holder.ibDelete.setVisibility(View.VISIBLE);
                    holder.ibDelete.setOnClickListener(v -> {
                                holder.ibDelete.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
                                listener.onDeleteClick(classPost);
                            }
                    );
                }
            } else if (SharedPreferencesUtils.loadSelf(context).getRole().equals("ROLE_TEACHER") && !classPost.getUserRole().equals("ROLE_TEACHER")) {
                holder.ibEdit.setVisibility(View.GONE);
                holder.ibDelete.setVisibility(View.VISIBLE);
                holder.ibDelete.setOnClickListener(v -> {
                    holder.ibDelete.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
                    listener.onDeleteClick(classPost);
                });
            } else {
                holder.ibEdit.setVisibility(View.GONE);
                holder.ibDelete.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){
            Log.i("tag", "NULL ClassAdapter "+e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return classPosts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvContent, tvComment;
        ImageView ivAvatar, ivImage;
        ImageButton ibEdit, ibDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_class_item);
            tvTime = itemView.findViewById(R.id.tv_time_class_item);
            tvContent = itemView.findViewById(R.id.tv_content_class_item);
            tvComment = itemView.findViewById(R.id.tv_comment_class_item);
            ivAvatar = itemView.findViewById(R.id.iv_avatar_class_item);
            ivImage = itemView.findViewById(R.id.iv_image_class_item);
            ibEdit = itemView.findViewById(R.id.ib_edit_class_item);
            ibDelete = itemView.findViewById(R.id.ib_delete_class_item);
        }
    }
}
