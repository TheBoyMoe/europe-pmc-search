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

import timber.log.Timber;

public class DetailFragment extends BaseFragment{

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


    public DetailFragment() {}

    public static DetailFragment newInstance(Article article) {
        DetailFragment fragment = new DetailFragment();
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

        mArticleTitle.setText(validateStringValue(mArticle.getTitle()));
        mAuthors.setText(validateStringValue(mArticle.getAuthorString()));
        mPage.setText(validateStringValue(mArticle.getPageInfo()));
        mCited.setText(validateLongValue(mArticle.getCitedByCount()));
        mAbstract.setText(validateStringValue(mArticle.getAbstractText()));

        if(mArticle.getJournalInfo() != null) {
            mYearOfPublication.setText(validateLongValue(mArticle.getJournalInfo().getYearOfPublication()));
            mVolume.setText(validateStringValue(mArticle.getJournalInfo().getVolume()));
            mIssue.setText(validateStringValue(mArticle.getJournalInfo().getIssue()));

            if(mArticle.getJournalInfo().getJournal() != null) {
                mJournalTitle.setText(validateStringValue(mArticle.getJournalInfo().getJournal().getTitle()));
            } else {
                mJournalTitle.setText(R.string.na_label);
            }
        } else {
            mYearOfPublication.setText(R.string.na_label);
            mVolume.setText(R.string.na_label);
            mIssue.setText(R.string.na_label);
            mJournalTitle.setText(R.string.na_label);
        }


        if(mArticle.getKeywordList() != null && mArticle.getKeywordList().getKeyword() != null) {
            ArrayList<String> keywords = (ArrayList<String>) mArticle.getKeywordList().getKeyword();
            String str = keywords.toString();
            mKeywords.setText(str.substring(1, str.length() - 1));
        } else {
            mKeywords.setText(R.string.na_label);
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

    private String validateStringValue(String value) {
        if(value != null && !value.equals("")){
            return value;
        } else {
            return getString(R.string.na_label);
        }
    }

    private String validateLongValue(Long value) {
        if(value != null)
            return String.valueOf(value);
        else
            return getString(R.string.na_label);
    }


}
