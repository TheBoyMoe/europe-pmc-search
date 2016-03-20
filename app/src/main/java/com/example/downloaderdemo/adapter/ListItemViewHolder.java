package com.example.downloaderdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.downloaderdemo.EuroPMCApplication;
import com.example.downloaderdemo.R;
import com.example.downloaderdemo.event.OnListItemClickEvent;
import com.example.downloaderdemo.model.Article;

public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView mArticleTitle;
    private TextView mJournalTitle;
    private TextView mArticleAuthors;
    private TextView mPageInformation;
    private TextView mJournalIssue;
    private TextView mJournalVolume;
    private TextView mYearOfPublication;
    private int mPosition;

    public ListItemViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);

        mArticleTitle = (TextView) view.findViewById(R.id.article_title);
        mJournalTitle = (TextView) view.findViewById(R.id.journal_title);
        mArticleAuthors = (TextView) view.findViewById(R.id.article_authors);
        mPageInformation = (TextView) view.findViewById(R.id.page_information);
        mJournalIssue = (TextView) view.findViewById(R.id.journal_issue);
        mJournalVolume = (TextView) view.findViewById(R.id.journal_volume);
        mYearOfPublication = (TextView) view.findViewById(R.id.publication_year);
    }


    public void bindArticleItem(Article article, int position) {
        mPosition = position;

        mArticleTitle.setText(article.getTitle());
        mArticleAuthors.setText(article.getAuthorString());
        mPageInformation.setText(article.getPageInfo());
        mJournalTitle.setText(article.getJournalInfo().getJournal().getTitle());
        mJournalIssue.setText(article.getJournalInfo().getIssue());
        mJournalVolume.setText(article.getJournalInfo().getVolume());
        if (article.getJournalInfo().getYearOfPublication() == 0)
            mYearOfPublication.setText(R.string.na_label);
        else
            mYearOfPublication.setText(String.valueOf(article.getJournalInfo().getYearOfPublication()));

    }

    @Override
    public void onClick(View v) {
        // post event
        EuroPMCApplication.postToBus(new OnListItemClickEvent(mPosition));
    }
}
