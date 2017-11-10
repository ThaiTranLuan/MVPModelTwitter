package com.example.giangtran.mvpmodeltwitter.main.timeline;

import com.example.giangtran.mvpmodeltwitter.base.model.Tweet;

import java.util.List;

/**
 * Created by giangtran on 25/10/2017.
 */

public interface ITimeLineView {

    void setItems(List<Tweet> tweet);

    void setItemsError();

    void updateItem();
}
