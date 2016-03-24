package com.example.downloaderdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.model.Article;

import java.util.List;

public class ArticleDetailFragmentPhone extends BaseFragment{

    public static final String ARTICLE_ITEM = "journal";
    private ShareActionProvider mShareActionProvider;
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

    public ArticleDetailFragmentPhone() {}

    public static ArticleDetailFragmentPhone newInstance(Article article) {
        ArticleDetailFragmentPhone fragment = new ArticleDetailFragmentPhone();
        Bundle args = new Bundle();
        args.putParcelable(ARTICLE_ITEM, article);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mArticle = getArguments().getParcelable(ARTICLE_ITEM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_detail_phone_container, container, false);
        cacheFragmentElements();
        populateFragmentElements();

        // set toolbar
        Toolbar toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_action_back_black);
        }

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share_phone, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareArticleIntent());
    }

    private Intent createShareArticleIntent() {

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
