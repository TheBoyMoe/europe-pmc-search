package com.example.downloaderdemo.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.model.Article;

import java.util.List;

public class ArticleDetailFragment extends BaseFragment{

    public static final String ARTICLE_ITEM = "journal";

    protected Article mArticle;
    protected View mView;

    private TextView mArticleTitle;
    private TextView mAuthors;
    private TextView mJournalTitle;
    private TextView mYearOfPublication;
    private TextView mVolume;
    private TextView mIssue;
    private TextView mPage;
    private TextView mCited;
    private TextView mKeywords;
    private TextView mAbstract;

    public ArticleDetailFragment() {}


    protected Intent createShareArticleIntent() {

        String articleInfo = String.format("Article title:\n%s\n\nJournal: %s\nvol:%s/issue: %s/year: %s",
                mArticle.getTitle(), mArticle.getJournalInfo().getJournal().getTitle(),
                mArticle.getJournalInfo().getVolume(), mArticle.getJournalInfo().getIssue(),
                mArticle.getJournalInfo().getYearOfPublication());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, articleInfo);
        return intent;
    }

    protected void populateFragmentElements() {

        mArticleTitle.setText(mArticle.getTitle());
        mAuthors.setText(mArticle.getAuthorString());
        mPage.setText(mArticle.getPageInfo());
        mAbstract.setText(mArticle.getAbstractText());
        mCited.setText(String.valueOf(mArticle.getCitedByCount()));

        if(mArticle.getJournalInfo().getYearOfPublication() == 0) {
            mYearOfPublication.setText(getString(R.string.na_label));
        } else {
            mYearOfPublication.setText(String.valueOf(mArticle.getJournalInfo().getYearOfPublication()));
        }

        mVolume.setText(mArticle.getJournalInfo().getVolume());
        mIssue.setText(mArticle.getJournalInfo().getIssue());
        mJournalTitle.setText(mArticle.getJournalInfo().getJournal().getTitle());

        if(mArticle.getKeywordList().getKeyword() != null) {
            List<String> keywords = mArticle.getKeywordList().getKeyword();
            String list = keywords.toString().substring(2, keywords.toString().length() -2);
            mKeywords.setText(list);
        } else {
            mKeywords.setText(getString(R.string.na_label));
        }
    }

    protected void cacheFragmentElements() {
        mArticleTitle = (TextView) mView.findViewById(R.id.article_title);
        mAuthors = (TextView) mView.findViewById(R.id.article_authors);
        mJournalTitle = (TextView) mView.findViewById(R.id.journal_title);
        mYearOfPublication = (TextView) mView.findViewById(R.id.publication_year);
        mVolume = (TextView) mView.findViewById(R.id.journal_volume);
        mIssue = (TextView) mView.findViewById(R.id.journal_issue);
        mPage = (TextView) mView.findViewById(R.id.page_information);
        mCited = (TextView) mView.findViewById(R.id.cited_times);
        mKeywords = (TextView) mView.findViewById(R.id.article_keywords);
        mAbstract = (TextView) mView.findViewById(R.id.article_abstract);
    }


}
