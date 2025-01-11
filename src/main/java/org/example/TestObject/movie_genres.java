package org.example.TestObject;

import org.bson.types.ObjectId;

public class movie_genres {
	private ObjectId _id;
	public ObjectId get_id() {
		return this._id;
	}
	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	private Integer tmdb_id;
	public Integer getTmdb_id() {
		return this.tmdb_id;
	}
	public void setTmdb_id(Integer tmdb_id) {
		this.tmdb_id = tmdb_id;
	}
	private Integer id;
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	private String name;
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public movie_genres() {}
}
