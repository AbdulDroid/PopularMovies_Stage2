package com.kafilicious.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kafilicious.popularmovies.data.models.db.Review;
import com.kafilicious.popularmovies.R;

import java.util.List;

/*
 * Copyright (C) 2017 Popular Movies, Stage 2
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context mContext;
    private List<Review> review_results;


    public ReviewAdapter(Context context, List<Review> results) {
        this.mContext = context;
        this.review_results = results;
    }

    public void setData(List<Review> reviewData) {
        this.review_results = reviewData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.review_content, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = "<a href='" + review_results.get(position).getUrl() + "'>Full Review link</a>";
        text = String.format(mContext.getResources().getString(R.string.review_content_text),
                Html.fromHtml(text));
        Log.i("ReviewAdapter: ", "built url for review " + text);
        holder.authorTextView.setText(review_results.get(position).getAuthor());
        holder.reviewTextView.setText(review_results.get(position).getContent());
        holder.reviewLinkTextView.setText(text);
        holder.reviewLinkTextView.setClickable(true);

    }

    @Override
    public int getItemCount() {
        if (review_results != null) {
            return review_results.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView authorTextView, reviewTextView, reviewLinkTextView;

        ViewHolder(View view) {
            super(view);
            authorTextView = view.findViewById(R.id.review_author);
            reviewTextView = view.findViewById(R.id.review_text);
            reviewLinkTextView = view.findViewById(R.id.complete_review_link);
            reviewLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }
}
