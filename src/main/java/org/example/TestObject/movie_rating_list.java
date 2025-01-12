package org.example.TestObject;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class movie_rating_list {
    private ObjectId _id;
    public ObjectId get_id() {
        return this._id;
    }
    public void set_id(ObjectId _id) {
        this._id = _id;
    }
    private String userId;
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    private ArrayList ratings;
    public ArrayList getRatings() {
        return this.ratings;
    }
    public void setRatings(ArrayList ratings) {
        this.ratings = ratings;
    }
    private String _class;
    public String get_class() {
        return this._class;
    }
    public void set_class(String _class) {
        this._class = _class;
    }
    public movie_rating_list() {}
}
