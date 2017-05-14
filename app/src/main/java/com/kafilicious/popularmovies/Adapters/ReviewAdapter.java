package com.kafilicious.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kafilicious.popularmovies.Models.ReviewResults;
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

    Context context;
    private List<ReviewResults> review_results;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    public ReviewAdapter(List<ReviewResults> results){
        this.review_results = results;
    }

    public void setData(List<ReviewResults> reviewData) {
        this.review_results = reviewData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.review_content, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = String.format(context.getResources().getString(R.string.review_content_text),
                review_results.get(position).url);
        holder.authorTextView.setText(review_results.get(position).author);
        if (review_results.get(position).content.length() > 200) {
            holder.reviewTextView.setText(review_results.get(position).content + text);
        } else {
            holder.reviewTextView.setText(review_results.get(position).content);
        }

    }

    @Override
    public int getItemCount() {
        if (review_results != null) {
            return review_results.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView authorTextView, reviewTextView;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
            authorTextView = (TextView) view.findViewById(R.id.review_author);
            reviewTextView = (TextView) view.findViewById(R.id.review_text);
            reviewTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @Override
        public void onClick(View v) {

        }
    }
}
