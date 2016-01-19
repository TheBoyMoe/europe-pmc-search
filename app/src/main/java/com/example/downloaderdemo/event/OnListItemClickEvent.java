package com.example.downloaderdemo.event;

import com.example.downloaderdemo.model.Article;

public class OnListItemClickEvent extends BaseEvent{

    private Article mArticle;

    public OnListItemClickEvent(Article article) {
        mArticle = article;
    }

    public Article getArticle() {
        return mArticle;
    }
}
