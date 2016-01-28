package com.example.downloaderdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.model.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailFragment extends BaseFragment{

    public static final String ARTICLE_ITEM = "journal";
    private Article mArticle;
    private View mView;
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

    public static ArticleDetailFragment newInstance(Article article) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARTICLE_ITEM, article);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticle = getArguments().getParcelable(ARTICLE_ITEM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_detail, container, false);
        cacheFragmentElements();
        populateFragmentElements();
        return mView;
    }

    private void populateFragmentElements() {

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

    private void cacheFragmentElements() {
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
